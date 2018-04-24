/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.servico;

import br.edu.ifpe.recife.avalon.model.questao.Alternativa;
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
     * Método para validar se o enunciado de uma nova Questão é válido para um determinado Tipo
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
    
    /**
     * Método para validar se o enunciado de uma Questão a ser alterada é válido a depender do seu Tipo.
     *
     * @param questao
     * @return isQuestaoValida
     */
    public boolean isEdicaoEnunciadoPorTipoValido(@Valid Questao questao) {
        TypedQuery<Questao> query = entityManager.createNamedQuery("Questao.PorEnunciadoTipoId", Questao.class);
        query.setParameter("enunciado", questao.getEnunciado().trim());
        query.setParameter("tipo", questao.getTipo());
        query.setParameter("id", questao.getId());

        if (TipoQuestaoEnum.MULTIPLA_ESCOLHA.equals(questao.getTipo())) {
            return true;
        }

        return query.getResultList().isEmpty();
    }
    
    /**
     * Método para verificar se todas as alternativas são diferentes entre si.
     * @param alternativas
     * @return alternativaValida
     */
    public boolean isAlternativasValidas(@NotNull List<Alternativa> alternativas){
        if(alternativas.isEmpty()){
            return false;
        }
        
        int qtdeAlternativas = alternativas.size();
        
        for (int i = 0; i < qtdeAlternativas - 1; i++){
            for(int j = i + 1; j < qtdeAlternativas; j++){
                if(alternativas.get(i).getAlternativa()
                        .equals(alternativas.get(j).getAlternativa())){
                    return false;
                }
            }
        }
        
        return true;
        
    }

}
