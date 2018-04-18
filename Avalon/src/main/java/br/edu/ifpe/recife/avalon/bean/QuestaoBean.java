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
import br.edu.ifpe.recife.avalon.util.AvalonUtil;
import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
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
public class QuestaoBean implements Serializable {

    @EJB
    private QuestaoServico questaoServico;

    @EJB
    private UsuarioServico usuarioServico;

    private List<TipoQuestaoEnum> tipoQuestoes = new ArrayList<>();

    private TipoQuestaoEnum tipoSelecionado = TipoQuestaoEnum.DISCURSIVA;

    private List<Questao> questoes = new ArrayList<>();

    @Valid
    private Questao novaQuestao = new Questao();
    
    private Questao questaoSelecionada;
    
    private boolean exibirModalConfirmarExclusao = false;

    private static final String MSG_QUESTAO_UNICA = "questao.enunciado.repetido";

    public String iniciarPagina(){
        buscarQuestoes();
        return "goListarQuestao";
    }
    
    public String iniciarPaginaInclusao(){
        this.limparTela();
        return "goAddQuestao";
    }
    
    public QuestaoBean() {
        this.novaQuestao = new Questao();
        this.carregarTiposQuestao();
    }

    /**
     * Método para inicializar a busca de questões. 
     */
    @PostConstruct
    private void init() {
        buscarQuestoes();
    }
    
    /**
     * Método para carregar as questões do usuário.
     */
    private void buscarQuestoes() {
        this.questoes = questaoServico.buscarQuestoesPorCriador(1l);
    }

    /**
     * Método responsável por carregar os tipos de questão disponíveis.
     */
    private void carregarTiposQuestao() {
        this.tipoQuestoes.add(TipoQuestaoEnum.DISCURSIVA);
        this.tipoQuestoes.add(TipoQuestaoEnum.MULTIPLA_ESCOLHA);
        this.tipoQuestoes.add(TipoQuestaoEnum.VERDADEIRO_FALSO);
    }

    /**
     * Método responsável por enviar ao servico a Questão à salvar.
     *
     * @return rota da próxima tela.
     */
    public String salvar() {
        Usuario usuario = usuarioServico.buscarUsuarioPorId(1l);

        novaQuestao.setTipo(tipoSelecionado);
        novaQuestao.setCriador(usuario);
        novaQuestao.setDataCriacao(Calendar.getInstance().getTime());

        if (questaoServico.isEnunciadoPorTipoValido(novaQuestao)) {
            questaoServico.salvar(novaQuestao);
            limparTela();
            buscarQuestoes();
        } else {
            FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_ERROR, AvalonUtil.getInstance().getMensagemValidacao(MSG_QUESTAO_UNICA), null);
            FacesContext.getCurrentInstance().addMessage(null, mensagem);
            return "";
        }

        return "goListarQuestao";
    }

    /**
     * Método para limpar os campos da tela.
     */
    private void limparTela() {
        tipoSelecionado = TipoQuestaoEnum.DISCURSIVA;
        novaQuestao = new Questao();
    }
    
    /**
     * Método para selecionar uma questão da lista de questões.
     * @param questao 
     */
    public void selecionarQuestao(Questao questao){
        questaoSelecionada = questao;
    }
    
    /**
     * Método para excluir uma questão
     */
    public void excluir(){
        questaoServico.remover(questaoSelecionada);
        questoes.remove(questaoSelecionada);
        questaoSelecionada = null;
    }
    
    /*
        GETTERS AND SETTERS
    */
    
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

    public List<Questao> getQuestoes() {
        return questoes;
    }

    public void setQuestoes(List<Questao> questoes) {
        this.questoes = questoes;
    }

    public boolean isExibirModalConfirmarExclusao() {
        return exibirModalConfirmarExclusao;
    }

    public void setExibirModalConfirmarExclusao(boolean exibirModalConfirmarExclusao) {
        this.exibirModalConfirmarExclusao = exibirModalConfirmarExclusao;
    }
    
}
