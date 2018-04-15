/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.bean;

import br.edu.ifpe.recife.avalon.model.questao.Questao;
import br.edu.ifpe.recife.avalon.model.questao.TipoQuestaoEnum;
import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
import br.edu.ifpe.recife.avalon.servico.QuestaoServico;
import br.edu.ifpe.recife.avalon.servico.UsuarioServico;
import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.validation.Valid;

/**
 *
 * @author eduardo.f.amaral
 */
@Named(value = "questaoBean")
@SessionScoped
public class QuestaoBean extends BaseBean implements Serializable{

    @EJB
    private QuestaoServico questaoServico;
    
    @EJB
    private UsuarioServico usuarioServico;
    
    private List<TipoQuestaoEnum> tipoQuestoes = new ArrayList<>();
    
    private TipoQuestaoEnum tipoSelecionado = TipoQuestaoEnum.DISCURSIVA;
    
    @Valid
    private Questao novaQuestao;
    
    private static final String MSG_QUESTAO_UNICA = "questao.enunciado.repetido";
    
    public QuestaoBean() {
        this.novaQuestao = new Questao();
        this.carregarTiposQuestao();
    }
    
    private void carregarTiposQuestao(){
        this.tipoQuestoes.add(TipoQuestaoEnum.DISCURSIVA);
        this.tipoQuestoes.add(TipoQuestaoEnum.MULTIPLA_ESCOLHA);
        this.tipoQuestoes.add(TipoQuestaoEnum.VERDADEIRO_FALSO);
    }
    
    public String salvar(){
        Usuario usuario = new Usuario();
        usuario.setEmail("teste@gmail.com");
        usuario.setSenha("teste");
        usuario = usuarioServico.buscarUsuarioPorLogin(usuario);
        
        novaQuestao.setTipo(tipoSelecionado);
        novaQuestao.setCriador(usuario);
        novaQuestao.setDataCriacao(Calendar.getInstance().getTime());
        
        if(questaoServico.isEnunciadoPorTipoValido(novaQuestao)){
            questaoServico.salvar(novaQuestao);
            limparTela();
        }else{
            FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMensagemValidacao(MSG_QUESTAO_UNICA), null);
            FacesContext.getCurrentInstance().addMessage(null, mensagem);
        }
        
        return "";
    }
    
    private void limparTela(){
        tipoSelecionado = TipoQuestaoEnum.DISCURSIVA;
        novaQuestao = new Questao();
    }
    
    public QuestaoServico getQuestaoServico() {
        return questaoServico;
    }

    public void setQuestaoServico(QuestaoServico questaoServico) {
        this.questaoServico = questaoServico;
    }

    public List<TipoQuestaoEnum> getTipoQuestoes() {
        return tipoQuestoes;
    }

    public void setTipoQuestoes(List<TipoQuestaoEnum> tipoQuestoes) {
        this.tipoQuestoes = tipoQuestoes;
    }

    public Questao getNovaQuestao() {
        return novaQuestao;
    }

    public void setNovaQuestao(Questao novaQuestao) {
        this.novaQuestao = novaQuestao;
    }

    public TipoQuestaoEnum getTipoSelecionado() {
        return tipoSelecionado;
    }

    public void setTipoSelecionado(TipoQuestaoEnum tipoSelecionado) {
        this.tipoSelecionado = tipoSelecionado;
    }
    
}
