/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.bean;

import br.edu.ifpe.recife.avalon.model.usuario.GrupoEnum;
import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
import br.edu.ifpe.recife.avalon.servico.UsuarioServico;
import br.edu.ifpe.recife.avalon.util.AvalonUtil;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import java.io.IOException;
import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.auth.login.LoginException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 *
 * @author eduardoamaral
 */
@Named(value = "loginBean")
@RequestScoped
public class LoginBean implements Serializable {

    @Inject
    private HttpServletRequest httpServletRequest;

    private static final String NAV_HOME_PROFESSOR = "goHomeProfessor";
    private static final String NAV_HOME_ALUNO = "goHomeAluno";
    private static final String NAV_LOGIN = "goLogin";
    private static final String LOGIN_FALHA_GERAL = "login.falha.geral";
    private static final String LOGIN_FALHA_TOKEN = "login.falha.token";
    private static final String DOMINIO_PROFESSOR = "dominioProfessor";
    private static final String DOMINIO_ALUNO = "dominioAluno";

    @EJB
    private UsuarioServico usuarioServico;

    @Valid
    private Usuario usuario = new Usuario();

    private String token;

    private Payload payload;

    private final FacesContext facesContext = FacesContext.getCurrentInstance();

    /**
     * Realiza login utilizando uma conta Google.
     */
    public void googleLogin() throws LoginException {

        Map<String, String> param = facesContext.getExternalContext().getRequestParameterMap();
        token = param.get("token");

        if (token != null) {

            verificarToken();

            if (isNotUsuarioCadastrado()) {
                salvarNovoUsuario();
            }

            realizarLogin();

        } else {
            exibirMensagemFalhaLogin();
        }

    }

    /**
     * Verifica a validade do token recebido.
     */
    private void verificarToken() throws LoginException {
        GoogleIdTokenVerifier verificador = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                .setAudience(Collections.singletonList("131562098478-bvjjnubvmsauka865rsd8rrdol9flj9n.apps.googleusercontent.com"))
                .build();

        payload = null;

        try {

            GoogleIdToken googleIdToken = verificador.verify(token);

            if (googleIdToken != null) {
                payload = googleIdToken.getPayload();

                Logger.getGlobal().log(Level.SEVERE, payload.toString());

                usuario.setEmail(payload.getEmail());
            } else {
                exibirMensagemFalhaLogin();
            }

        } catch (IOException | GeneralSecurityException e) {
            Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, e);
            exibirMensagemFalhaLogin();
        }

    }

    /**
     * Salva um novo usário.
     */
    private void salvarNovoUsuario() throws LoginException {
        if (payload != null) {

            usuario.setNome(payload.get("given_name").toString());
            usuario.setSobrenome(payload.get("family_name").toString());

            if (payload.getHostedDomain() != null) {
                if (verificarDominio(DOMINIO_ALUNO)) {
                    usuario.setGrupo(GrupoEnum.ALUNO);
                } else if (verificarDominio(DOMINIO_PROFESSOR)) {
                    usuario.setGrupo(GrupoEnum.PROFESSOR);
                }
            } else {
                exibirMensagemLoginInvalido();
            }

            usuarioServico.salvar(usuario);

        }
    }

    private boolean verificarDominio(String dominio) {
        String[] dominios = FacesContext.getCurrentInstance().getExternalContext().getInitParameter(dominio).split(",");
        Arrays.sort(dominios);
        int index = Arrays.binarySearch(dominios, payload.getHostedDomain());

        return index > -1;
    }

    /**
     * Verifica se o usuário ainda não possuí cadastro.
     *
     * @return true - quando o usuário não existe.
     */
    private boolean isNotUsuarioCadastrado() {
        Usuario usuarioAplicacao = usuarioServico.buscarUsuarioPorEmail(payload.getEmail());
        return usuarioAplicacao == null;
    }

    /**
     * Realiza o login do usuário.
     */
    private void realizarLogin() throws LoginException {
        usuario = usuarioServico.buscarUsuarioPorEmail(usuario.getEmail());

        if (usuario != null) {
            facesContext.getExternalContext().getSessionMap().put("usuario", usuario);
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

            try {
                NavigationHandler navigationHandler = facesContext.getApplication().getNavigationHandler();

                if (payload.getHostedDomain() != null) {
                    if (verificarDominio(DOMINIO_ALUNO)) {
                        request.login(usuario.getEmail(), usuario.getEmail());
                        navigationHandler.handleNavigation(facesContext, null, NAV_HOME_ALUNO);
                    } else if (verificarDominio(DOMINIO_PROFESSOR)) {
                        request.login(usuario.getEmail(), usuario.getEmail());
                        navigationHandler.handleNavigation(facesContext, null, NAV_HOME_PROFESSOR);
                    }
                }

                exibirMensagemLoginInvalido();

            } catch (ServletException ex) {
                Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, ex);
                exibirMensagemFalhaLogin();
            }

        } else {
            exibirMensagemLoginInvalido();
        }

    }

    private void exibirMensagemLoginInvalido() throws LoginException {
        exibirMensagemErro(AvalonUtil.getInstance().getMensagem("login.dominio.invalido"));
    }

    /**
     * Finaliza a sessão do usuário.
     *
     * @return retorna para a tela de Login.
     * @throws javax.servlet.ServletException
     */
    public String logout() throws ServletException {
        HttpSession sessao = httpServletRequest.getSession(false);

        if (sessao != null) {
            sessao.invalidate();
        }

        httpServletRequest.logout();

        return NAV_LOGIN;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private void exibirMensagemFalhaLogin() throws LoginException {
        exibirMensagemErro(AvalonUtil.getInstance().getMensagem(LOGIN_FALHA_TOKEN));
    }
    
    private void exibirMensagemErro(String mensagem) throws LoginException {
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, mensagem, mensagem));
        throw new LoginException(mensagem);
    }

}
