/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.bean;

import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
import br.edu.ifpe.recife.avalon.servico.UsuarioServico;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import java.io.Serializable;
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

    @EJB
    private UsuarioServico usuarioServico;

    @Valid
    private Usuario usuario = new Usuario();

    @ManagedProperty("#{param.token}")
    private String token;
    
    private FacesContext contexto = FacesContext.getCurrentInstance();

    private Payload payload;

    public void googleLogin() {
        verificarToken();
        preencherUsuario();
        verificarUsuario();
        realizarLogin();
    }

    private void verificarToken() {
        GoogleIdTokenVerifier verificador = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                .setAudience(Collections.singletonList("131562098478-bvjjnubvmsauka865rsd8rrdol9flj9n.apps.googleusercontent.com"))
                .build();

        payload = null;

        try {

            GoogleIdToken googleIdToken = verificador.verify(token);

            if (googleIdToken != null) {
                payload = googleIdToken.getPayload();
            } else {
                contexto.addMessage(null, new FacesMessage("Token inválido."));
            }

        } catch (Exception e) {
            Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, e);
            contexto.addMessage(null, new FacesMessage("Falha ao tentar realizar o acesso."));
        }

    }

    private void preencherUsuario() {
        if (payload != null) {

            /*if (payload.getHostedDomain().equalsIgnoreCase("recife.ifpe.edu.br")) {
            } else if (payload.getHostedDomain().equalsIgnoreCase("a.recife.ifpe.edu.br")) {
            } else {
                //Retorne erro.
            }*/

            usuario.setEmail(payload.getEmail());
            usuario.setSenha(payload.getUserId());
            usuario.setNome(payload.get("given_name").toString());
            usuario.setSobrenome(payload.get("family_name").toString());

        }
    }
    
    private void verificarUsuario(){
        Usuario usuarioAplicao = usuarioServico.buscarUsuarioPorEmail(usuario.getEmail());
        
        if(usuarioAplicao == null){
            usuarioServico.salvar(usuario);
        }
        
    }
    
    private void realizarLogin(){
        HttpSession sessao = (HttpSession) contexto.getExternalContext().getSession(true);
        
        usuario = usuarioServico.buscarUsuarioPorLogin(usuario.getEmail(), usuario.getSenha());
        
        sessao.setAttribute("usuario", usuario);
        sessao.setAttribute("emailUsuario", usuario.getEmail());
        
        NavigationHandler navigationHandler = contexto.getApplication().getNavigationHandler();
        navigationHandler.handleNavigation(contexto, null, "goListarQuestao");
        
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
