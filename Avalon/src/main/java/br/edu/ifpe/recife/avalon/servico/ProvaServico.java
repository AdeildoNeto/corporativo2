/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.servico;

import br.edu.ifpe.recife.avalon.excecao.ValidacaoException;
import br.edu.ifpe.recife.avalon.model.prova.Prova;
import br.edu.ifpe.recife.avalon.model.simulado.FiltroSimulado;
import br.edu.ifpe.recife.avalon.model.simulado.Simulado;
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
    private static final String DATA_INICIO_MAIOR_DATA_FIM = "prova.data.inicio.maior.data.fim";
    private static final String DURACAO_MINIMA = "prova.duracao.minima";
    private static final String DISPONIBILIDADE_MINIMA = "prova.disponibilidade.minima";

    /**
     * Salva uma prova.
     *
     * @param prova
     * @throws ValidacaoException - quando o título já está em uso.
     */
    public void salvar(@Valid Prova prova) throws ValidacaoException {
        validarPeriodoDuracao(prova);
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
     * Valida se ao menos duas questões adicionadas a prova.
     *
     * @param prova
     * @throws ValidacaoException - lançada caso o número de questões
     * selecionadas sejam menor que 2.
     */
    private void validarQtdeQuestoesSelecionadas(@Valid Prova prova) throws ValidacaoException {
        if (prova.getQuestoes() == null
                || prova.getQuestoes().isEmpty()
                || prova.getQuestoes().size() < QTDE_MIN_QUESTOES) {
            throw new ValidacaoException(AvalonUtil.getInstance()
                    .getMensagemValidacao(QUESTOES_MINIMAS));
        }
    }

    private void validarPeriodoDuracao(@Valid Prova prova) throws ValidacaoException {
        Calendar calendarInicio = Calendar.getInstance();
        Calendar calendarFim = Calendar.getInstance();

        if (prova.getDataHoraInicio().after(prova.getDataHoraFim())) {
            throw new ValidacaoException(DATA_INICIO_MAIOR_DATA_FIM);
        }

        calendarInicio.setTime(prova.getDataHoraInicio());
        calendarFim.setTime(prova.getDataHoraFim());
        calendarInicio.add(Calendar.MINUTE, 30);

        if (calendarInicio.after(calendarFim)) {
            throw new ValidacaoException(DISPONIBILIDADE_MINIMA);
        }

        if (prova.getDuracao() < 30) {
            throw new ValidacaoException(DURACAO_MINIMA);
        }
    }

    /**
     * Consulta todas as provas disponíveis.
     *
     * São provas disponíveis todas as provas que tenham data/hora início maior
     * ou igual a data/hora atual e data/hora fim menor que a data/hora atual.
     *
     * @return provas
     */
    public List<Prova> buscarProvasDisponiveis() {
        TypedQuery<Prova> query = entityManager.createNamedQuery("Prova.PorDisponibilidade",
                Prova.class);

        query.setParameter("dataHoraAtual", Calendar.getInstance().getTime());

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

}
