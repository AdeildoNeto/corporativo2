/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.bean;

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
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 *
 * @author eduardo.f.amaral
 */
@ManagedBean(name = "loginBean")
@SessionScoped
public class LoginBean implements Serializable {

    private static final String NAV_INICIAL = "goListarQuestao";
    private static final String LOGIN_FALHA_GERAL = "login.falha.geral";
    private static final String LOGIN_FALHA_TOKEN = "login.falha.token";

    @EJB
    private UsuarioServico usuarioServico;

    @Valid
    private Usuario usuario = new Usuario();

    @ManagedProperty("#{param.token}")
    private String token;

    private FacesContext contexto = FacesContext.getCurrentInstance();

    private Payload payload;

    /**
     * Método para realizar login via conta Google.
     */
    public void googleLogin() {

        if (token != null) {

            verificarToken();

            if (isNotUsuarioCadastrado()) {
                salvarNovoUsuario();
            }

            realizarLogin();

        }else{
            contexto.addMessage(null, new FacesMessage(AvalonUtil.getInstance().getMensagem(LOGIN_FALHA_GERAL)));
        }

    }

    /**
     * Método para verificar o token enviado.
     */
    private void verificarToken() {
        GoogleIdTokenVerifier verificador = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                .setAudience(Collections.singletonList("131562098478-bvjjnubvmsauka865rsd8rrdol9flj9n.apps.googleusercontent.com"))
                .build();

        payload = null;

        try {

            GoogleIdToken googleIdToken = verificador.verify(token);

            if (googleIdToken != null) {
                MessageDigest digest = MessageDigest.getInstance("MD5");

                payload = googleIdToken.getPayload();

                digest.update(payload.getUserId().getBytes(), 0, payload.getUserId().length());

                usuario.setEmail(payload.getEmail());
                usuario.setSenha(new BigInteger(1, digest.digest()).toString(16));
            } else {
                contexto.addMessage(null, new FacesMessage(AvalonUtil.getInstance().getMensagem(LOGIN_FALHA_TOKEN)));
            }

        } catch (IOException | GeneralSecurityException e) {
            Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, e);
            contexto.addMessage(null, new FacesMessage(AvalonUtil.getInstance().getMensagem(LOGIN_FALHA_GERAL)));
        }

    }

    /**
     * Método para salvar um novo usuário.
     */
    private void salvarNovoUsuario() {
        if (payload != null) {

            usuario.setNome(payload.get("given_name").toString());
            usuario.setSobrenome(payload.get("family_name").toString());

            //if("recife.ifpe.edu.br".equalsIgnoreCase(payload.getHostedDomain())){
            usuarioServico.salvar(usuario);
            //}else{
            //SalvarAluno
            //}

        }
    }

    /**
     * Método para verificar se o usuário já existe.
     *
     * @return boolean
     */
    private boolean isNotUsuarioCadastrado() {
        Usuario usuarioAplicao = usuarioServico.buscarUsuarioPorEmail(payload.getEmail());
        return usuarioAplicao == null;
    }

    /**
     * Método para realizar Login.
     */
    private void realizarLogin() {
        HttpSession sessao = (HttpSession) contexto.getExternalContext().getSession(true);

        usuario = usuarioServico.buscarUsuarioPorLogin(usuario.getEmail(), usuario.getSenha());

        if (usuario != null) {
            sessao.setAttribute("usuario", usuario);
            sessao.setAttribute("emailUsuario", usuario.getEmail());

            NavigationHandler navigationHandler = contexto.getApplication().getNavigationHandler();
            navigationHandler.handleNavigation(contexto, null, NAV_INICIAL);
        } else {
            contexto.addMessage(null, new FacesMessage(AvalonUtil.getInstance().getMensagem(LOGIN_FALHA_GERAL)));
        }

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
