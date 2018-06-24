/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.servico;

import br.edu.ifpe.recife.avalon.excecao.ValidacaoException;
import br.edu.ifpe.recife.avalon.model.prova.Prova;
import br.edu.ifpe.recife.avalon.model.prova.ProvaAluno;
import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
import br.edu.ifpe.recife.avalon.util.AvalonUtil;
import java.util.Calendar;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
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

    @Resource
    private SessionContext sessao;

    @PersistenceContext(name = "jdbc/avalonDataSource", type = TRANSACTION)
    private EntityManager entityManager;

    private static final String PERCENT = "%";
    private static final int QTDE_MIN_QUESTOES = 2;
    private static final String QUESTOES_MINIMAS = "prova.questoes.minimas";
    private static final String DATA_INICIO_ANTERIOR_DATA_ATUAL = "prova.data.inicia.anterior.data.atual";
    private static final String DATA_INICIO_MAIOR_DATA_FIM = "prova.data.inicio.maior.data.fim";
    private static final String DURACAO_MINIMA = "prova.duracao.minima";
    private static final String DURACAO_MAXIMA = "prova.duracao.maxima";
    private static final String DISPONIBILIDADE_MINIMA = "prova.disponibilidade.minima";
    private static final String DISPONIBILIDADE_MAXIMA = "prova.disponibilidade.maxima";
    private static final int PARAM_MIN_DISPONIBILIDADE_MINUTOS = 30;
    private static final int PARAM_MAX_DISPONIBILIDADE_HORAS = 5;
    private static final int PARAM_MIN_DURACAO = 30;
    private static final int PARAM_MAX_DURACAO = 300;

    /**
     * Salva uma prova.
     *
     * @param prova
     * @throws ValidacaoException - quando o título já está em uso.
     */
    public void salvar(@Valid Prova prova) throws ValidacaoException {
        validarDisponibilidade(prova);
        validarDuracao(prova);
        validarQtdeQuestoesSelecionadas(prova);
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
     * @throws ValidacaoException - Lançada quando:
     * A data de início da disponibilidade for maior que a data de fim.
     * A disponiblidade for menor que 30 minutos.
     * A dispobilidade for maior que 5 horas.
     */
    private void validarDisponibilidade(@Valid Prova prova) throws ValidacaoException {
        Calendar calendarInicio = Calendar.getInstance();
        Calendar calendarFim = Calendar.getInstance();
        
        if(prova.getDataHoraInicio().before(Calendar.getInstance().getTime())){
            throw new ValidacaoException(getMensagemValidacao(DATA_INICIO_ANTERIOR_DATA_ATUAL));
        }

        if (prova.getDataHoraInicio().after(prova.getDataHoraFim())) {
            throw new ValidacaoException(getMensagemValidacao(DATA_INICIO_MAIOR_DATA_FIM));
        }

        calendarInicio.setTime(prova.getDataHoraInicio());
        calendarFim.setTime(prova.getDataHoraFim());
        calendarInicio.add(Calendar.MINUTE, PARAM_MIN_DISPONIBILIDADE_MINUTOS);

        if (calendarInicio.after(calendarFim)) {
            throw new ValidacaoException(getMensagemValidacao(DISPONIBILIDADE_MINIMA));
        }
        
        calendarFim.setTime(prova.getDataHoraFim());
        calendarInicio.setTime(prova.getDataHoraInicio());
        calendarInicio.add(Calendar.HOUR, PARAM_MAX_DISPONIBILIDADE_HORAS);
        
        if(calendarInicio.before(calendarFim)){
            throw new ValidacaoException(getMensagemValidacao(DISPONIBILIDADE_MAXIMA));
        }
    }
    
    /**
     * Valida a duração da prova.
     * 
     * @param prova
     * @throws ValidacaoException - Lançada quando:
     * A duração definida for menor que 30 minutos.
     * A duração definida for maior que 300 minutos.
     */
    private void validarDuracao(@Valid Prova prova) throws ValidacaoException {
        if (prova.getDuracao() < PARAM_MIN_DURACAO) {
            throw new ValidacaoException(getMensagemValidacao(DURACAO_MINIMA));
        }
        
        if(prova.getDuracao() > PARAM_MAX_DURACAO){
            throw new ValidacaoException(getMensagemValidacao(DURACAO_MAXIMA));
        }
    }
    
    /**
     * Recupera uma mensagem de validação.
     * 
     * @param key - chave definida para a mensagem.
     * @return mensagem.
     */
    private String getMensagemValidacao(String key){
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
     * @param email - email do criador.
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
     */
    public void salvarProvaAluno(@Valid ProvaAluno provaAluno){
        entityManager.persist(provaAluno);
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
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);

        query.setParameter("idAluno", aluno.getId());
        query.setParameter("dhAtual", calendar.getTime());

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
}
