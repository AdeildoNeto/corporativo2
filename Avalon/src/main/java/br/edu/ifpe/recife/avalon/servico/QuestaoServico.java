/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.servico;

import br.edu.ifpe.recife.avalon.model.questao.Questao;
import br.edu.ifpe.recife.avalon.model.questao.TipoQuestaoEnum;
import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
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
    public void salvar(Questao questao) {
        questao.setEnunciado(questao.getEnunciado().trim());
        entityManager.persist(questao);
    }

    /**
     * Método para alterar uma Questão
     *
     * @param questao
     */
    public void alterar(Questao questao) {
        entityManager.merge(questao);
    }

    /**
     * Método para remover uma Questão
     *
     * @param questao
     */
    public void remover(Questao questao) {
        if (!entityManager.contains(questao)) {
            questao.setAtiva(false);
            entityManager.merge(questao);
        }
    }

    /**
     * Método para consultar Questões por Tipo e Criador
     *
     * @param idCriador
     * @param tipo
     * @return lista de questões
     */
    public List<Questao> buscarQuestoesPorCriadorTipo(Long idCriador, TipoQuestaoEnum tipo) {
        TypedQuery<Questao> query = entityManager.createNamedQuery("Questao.PorCriadorTipo", Questao.class);
        query.setParameter("idCriador", idCriador);
        query.setParameter("tipo", tipo);

        return query.getResultList();
    }

    /**
     * Método para consultar Questões por Criador
     *
     * @param id
     * @return lista de questões
     */
    public List<Questao> buscarQuestoesPorCriador(Long id) {
        TypedQuery<Questao> query = entityManager.createNamedQuery("Questao.PorCriador", Questao.class);
        query.setParameter("idCriador", id);

        return query.getResultList();
    }

    public List<Questao> buscarQuestoesPorEmail(String email) {
        TypedQuery<Usuario> query = entityManager.createNamedQuery("Usuario.PorEmail", Usuario.class);
        query.setParameter("email", email);

        Usuario usuario = query.getSingleResult();

        TypedQuery<Questao> query2 = entityManager.createNamedQuery("Questao.PorCriador", Questao.class);
        query2.setParameter("idCriador", usuario.getId());

        return query2.getResultList();
    }

    /**
     * Método para validar o enunciado da Questão por Tipo
     *
     * @param questao
     * @return isQuestaoValida
     */
    public boolean isEnunciadoPorTipoValido(Questao questao) {
        TypedQuery<Questao> query = entityManager.createNamedQuery("Questao.PorEnunciadoTipo", Questao.class);
        query.setParameter("enunciado", questao.getEnunciado().trim());
        query.setParameter("tipo", questao.getTipo());

        if (TipoQuestaoEnum.MULTIPLA_ESCOLHA.equals(questao.getTipo())) {
            return true;
        }

        return query.getResultList().isEmpty();
    }

}
