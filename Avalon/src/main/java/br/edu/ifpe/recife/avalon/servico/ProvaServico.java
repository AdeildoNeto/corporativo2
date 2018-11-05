/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.servico;

import br.edu.ifpe.recife.avalon.excecao.ValidacaoException;
import br.edu.ifpe.recife.avalon.model.avaliacao.QuestaoAvaliacao;
import br.edu.ifpe.recife.avalon.model.avaliacao.prova.Prova;
import br.edu.ifpe.recife.avalon.model.avaliacao.prova.ProvaAluno;
import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
import br.edu.ifpe.recife.avalon.util.AvalonUtil;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import static javax.persistence.PersistenceContextType.TRANSACTION;
import javax.persistence.TypedQuery;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 *
 * @author eduardoamaral
 */
@Stateless
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ProvaServico {

    @PersistenceContext(name = "jdbc/avalonDataSource", type = TRANSACTION)
    private EntityManager entityManager;

    private static final String PERCENT = "%";
    private static final int QTDE_MIN_QUESTOES = 2;
    private static final String QUESTOES_MINIMAS = "prova.questoes.minimas";
    private static final String DATA_INICIO_ANTERIOR_DATA_ATUAL = "prova.data.inicia.anterior.data.atual";
    private static final String DATA_INICIO_MAIOR_DATA_FIM = "prova.data.inicio.maior.data.fim";
    private static final String DISPONIBILIDADE_MINIMA = "prova.disponibilidade.minima";
    private static final String DISPONIBILIDADE_MAXIMA = "prova.disponibilidade.maxima";
    private static final int PARAM_MIN_DISPONIBILIDADE_MINUTOS = 30;
    private static final int PARAM_MAX_DISPONIBILIDADE_HORAS = 5;
    private static final double LIMITE_INFERIOR_NOTA_MAXIMA = 1.0;
    private static final double LIMITE_SUPERIOR_NOTA_MAXIMA = 10.0;

    /**
     * Salva uma prova.
     *
     * @param prova
     * @throws ValidacaoException - quando o título já está em uso.
     */
    public void salvar(@Valid Prova prova) throws ValidacaoException {
        validarDisponibilidade(prova);
        validarQtdeQuestoesSelecionadas(prova);
        validarPesoQuestoes(prova);
        validarNotaMaxima(prova);
        entityManager.persist(prova);
    }

    /**
     * Remove uma prova.
     *
     * @param prova
     */
    public void excluir(@Valid Prova prova) {
        if (!entityManager.contains(prova)) {
            prova.setAtiva(false);
            entityManager.merge(prova);
        }
    }

    /**
     * Valida se ao menos duas questões foram associadas à prova.
     *
     * @param prova
     * @throws ValidacaoException - lançada caso o número de questões
     * selecionadas sejam menor que 2.
     */
    private void validarQtdeQuestoesSelecionadas(@Valid Prova prova) throws ValidacaoException {
        if (prova.getQuestoes() == null
                || prova.getQuestoes().isEmpty()
                || prova.getQuestoes().size() < QTDE_MIN_QUESTOES) {
            throw new ValidacaoException(getMensagemValidacao(QUESTOES_MINIMAS));
        }
    }

    /**
     * Valida se o período de disponibilidade da prova.
     *
     * @param prova
     * @throws ValidacaoException - Lançada quando: A data de início da
     * disponibilidade for maior que a data de fim. A disponiblidade for menor
     * que 30 minutos. A dispobilidade for maior que 5 horas.
     */
    private void validarDisponibilidade(@Valid Prova prova) throws ValidacaoException, ValidacaoException, ValidacaoException {
        if (prova.getDataHoraInicio().before(Calendar.getInstance().getTime())) {
            throw new ValidacaoException(getMensagemValidacao(DATA_INICIO_ANTERIOR_DATA_ATUAL));
        }

        validarDataHoraProva(prova);
    }

    /**
     * Valida se as datas da prova são validas.
     *
     * @param prova
     * @throws ValidacaoException
     */
    private void validarDataHoraProva(Prova prova) throws ValidacaoException {
        if (prova.getDataHoraInicio().after(prova.getDataHoraFim())) {
            throw new ValidacaoException(getMensagemValidacao(DATA_INICIO_MAIOR_DATA_FIM));
        }

        validarLimiteDisponibilidade(prova);
    }

    /**
     * Valida se a disponibilidade da prova é válida.
     *
     * @param prova
     * @throws ValidacaoException
     */
    private void validarLimiteDisponibilidade(Prova prova) throws ValidacaoException {
        Calendar calendarInicio = Calendar.getInstance();
        Calendar calendarFim = Calendar.getInstance();

        calendarInicio.setTime(prova.getDataHoraInicio());
        calendarFim.setTime(prova.getDataHoraFim());
        calendarInicio.add(Calendar.MINUTE, PARAM_MIN_DISPONIBILIDADE_MINUTOS);

        if (calendarInicio.after(calendarFim)) {
            throw new ValidacaoException(getMensagemValidacao(DISPONIBILIDADE_MINIMA));
        }

        calendarInicio.setTime(prova.getDataHoraInicio());
        calendarInicio.add(Calendar.HOUR, PARAM_MAX_DISPONIBILIDADE_HORAS);

        if (calendarInicio.before(calendarFim)) {
            throw new ValidacaoException(getMensagemValidacao(DISPONIBILIDADE_MAXIMA));
        }
    }

    /**
     * Recupera uma mensagem de validação.
     *
     * @param key - chave definida para a mensagem.
     * @return mensagem.
     */
    private String getMensagemValidacao(String key) {
        return AvalonUtil.getInstance().getMensagemValidacao(key);
    }

    /**
     * Consulta todas as provas disponíveis.
     *
     * São provas disponíveis todas as provas que tenham data/hora início maior
     * ou igual a data/hora atual e data/hora fim menor que a data/hora atual.
     *
     * @param aluno
     * @return provas
     */
    public List<Prova> buscarProvasDisponiveis(@NotNull Usuario aluno) {
        TypedQuery<Prova> query = entityManager.createNamedQuery("Prova.PorDisponibilidade",
                Prova.class);

        query.setParameter("dataHoraAtual", Calendar.getInstance().getTime());
        query.setParameter("idAluno", aluno.getId());

        return query.getResultList();
    }

    /**
     * Consulta todas as provas criadas por um Professor.
     *
     * @param email - email do professor.
     * @return provas
     */
    public List<Prova> buscarProvasPorProfessor(@NotNull String email) {
        TypedQuery<Prova> query = entityManager.createNamedQuery("Prova.PorProfessor",
                Prova.class);

        query.setParameter("emailProfessor", email);

        return query.getResultList();
    }

    /**
     * Registra a prova realizada pelo aluno.
     *
     * @param provaAluno
     * @return a prova do aluno
     */
    public ProvaAluno salvarProvaAluno(@Valid ProvaAluno provaAluno) {
        if (provaAluno.getId() != null) {
            provaAluno = entityManager.merge(provaAluno);
        } else {
            entityManager.persist(provaAluno);
        }

        return provaAluno;
    }

    /**
     * Consulta todos as provas realizadas por um aluno.
     *
     * @param aluno
     * @return lista de provas realizada pelo aluno.
     */
    public List<ProvaAluno> buscarResultadosProvasAluno(Usuario aluno) {
        TypedQuery<ProvaAluno> query = entityManager.createNamedQuery("ProvaAluno.PorResultadoAluno",
                ProvaAluno.class);
        query.setParameter("idAluno", aluno.getId());
        query.setParameter("dataAtual", new Date());

        return query.getResultList();
    }

    /**
     * Consulta todos os resultados de uma prova.
     *
     * @param prova
     * @return provas.
     */
    public List<ProvaAluno> buscarResultadosProva(Prova prova) {
        TypedQuery<ProvaAluno> query = entityManager.createNamedQuery("ProvaAluno.PorProva",
                ProvaAluno.class);

        query.setParameter("idProva", prova.getId());

        return query.getResultList();
    }

    /**
     * Recupera uma prova por ID.
     *
     * @param id
     * @return
     */
    public Prova buscarProvaPorId(@NotNull Long id) {
        TypedQuery<Prova> query = entityManager.createNamedQuery("Prova.PorId", Prova.class);
        query.setParameter("idProva", id);

        if (!query.getResultList().isEmpty()) {
            return query.getSingleResult();
        }
        return null;
    }

    /**
     * Recupera o histórico de uma prova de um aluno.
     *
     * @param aluno
     * @param prova
     * @return
     */
    public ProvaAluno buscarProvaAluno(Usuario aluno, Prova prova) {
        TypedQuery<ProvaAluno> query = entityManager.createNamedQuery("ProvaAluno.PorAlunoProva",
                ProvaAluno.class);
        ProvaAluno provaAluno = new ProvaAluno();

        query.setParameter("idAluno", aluno.getId());
        query.setParameter("idProva", prova.getId());

        if (!query.getResultList().isEmpty()) {
            provaAluno = query.getSingleResult();
        }

        return provaAluno;
    }

    /**
     * Reagenda uma prova alterando sua disponibilidade.
     *
     * @param prova
     * @throws ValidacaoException
     */
    public void reagendarProva(Prova prova) throws ValidacaoException {
        validarDataHoraProva(prova);
        entityManager.merge(prova);
    }

    /**
     * Valida se todas as questões da prova possuem peso definido.
     * 
     * @param prova
     * @throws ValidacaoException 
     */
    private void validarPesoQuestoes(Prova prova) throws ValidacaoException {
        for(QuestaoAvaliacao questao : prova.getQuestoes()){
            if(questao.getPeso() == null || questao.getPeso() <= 0){
                throw new ValidacaoException(AvalonUtil.getInstance().getMensagemValidacao("questao.peso.obrigatorio"));
            }
        }
    }

    private void validarNotaMaxima(Prova prova) throws ValidacaoException {
        if(prova.getNotaMaxima().compareTo(LIMITE_INFERIOR_NOTA_MAXIMA) < 0){
            throw new ValidacaoException(AvalonUtil.getInstance().getMensagemValidacao("limite.nota.maxima.inferior"));
        }
        
        if(prova.getNotaMaxima().compareTo(LIMITE_SUPERIOR_NOTA_MAXIMA) > 0){
            throw new ValidacaoException(AvalonUtil.getInstance().getMensagemValidacao("limite.nota.maxima.superior"));
        }
    }

}
