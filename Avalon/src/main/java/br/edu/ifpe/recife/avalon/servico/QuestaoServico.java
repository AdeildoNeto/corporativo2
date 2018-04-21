/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.servico;

import br.edu.ifpe.recife.avalon.model.questao.Questao;
import br.edu.ifpe.recife.avalon.model.questao.TipoQuestaoEnum;
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
 * @author eduardo.f.amaral
 */
@Stateless
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class QuestaoServico {

    @Resource
    private SessionContext sessao;

    @PersistenceContext(name = "jdbc/avalonDataSource", type = TRANSACTION)
    private EntityManager entityManager;

    /**
     * Método para salvar um Questão
     *
     * @param questao
     */
    public void salvar(@Valid Questao questao) {
        questao.setEnunciado(questao.getEnunciado().trim());
        entityManager.persist(questao);
    }

    /**
     * Método para alterar uma Questão
     *
     * @param questao
     */
    public void alterar(@Valid Questao questao) {
        entityManager.merge(questao);
    }

    /**
     * Método para remover uma Questão
     *
     * @param questao
     */
    public void remover(@Valid Questao questao) {
        if (!entityManager.contains(questao)) {
            questao.setAtiva(false);
            entityManager.merge(questao);
        }
    }

    /**
     * Método para consultar Questões por Tipo e Criador
     *
     * @param emailCriador
     * @param tipo
     * @return lista de questões
     */
    public List<Questao> buscarQuestoesPorCriadorTipo(@NotNull String emailCriador, TipoQuestaoEnum tipo) {
        TypedQuery<Questao> query = entityManager.createNamedQuery("Questao.PorCriadorTipo", Questao.class);
        query.setParameter("emailCriador", emailCriador);
        query.setParameter("tipo", tipo);

        return query.getResultList();
    }

    /**
     * Método para consultar Questões por Criador
     *
     * @param emailCriador
     * @return lista de questões
     */
    public List<Questao> buscarQuestoesPorCriador(@NotNull String emailCriador) {
        TypedQuery<Questao> query = entityManager.createNamedQuery("Questao.PorCriador", Questao.class);
        query.setParameter("emailCriador", emailCriador);

        return query.getResultList();
    }

    /**
     * Método para validar o enunciado da Questão por Tipo
     *
     * @param questao
     * @return isQuestaoValida
     */
    public boolean isEnunciadoPorTipoValido(@Valid Questao questao) {
        TypedQuery<Questao> query = entityManager.createNamedQuery("Questao.PorEnunciadoTipo", Questao.class);
        query.setParameter("enunciado", questao.getEnunciado().trim());
        query.setParameter("tipo", questao.getTipo());

        if (TipoQuestaoEnum.MULTIPLA_ESCOLHA.equals(questao.getTipo())) {
            return true;
        }

        return query.getResultList().isEmpty();
    }

}
