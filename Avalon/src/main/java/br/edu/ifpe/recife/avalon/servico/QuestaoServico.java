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

    @PersistenceContext(name = "avalon", type = TRANSACTION)
    private EntityManager entityManager;

    public void salvar(Questao questao) {
        entityManager.persist(questao);
    }

    public void alterar(Questao questao) {
        entityManager.merge(questao);
    }

    public void remover(Questao questao) {
        if (!entityManager.contains(questao)) {
            questao = entityManager.merge(questao);
        }
        entityManager.remove(questao);
    }

    public List<Questao> buscarQuestoesPorAutorTipo(Usuario autor, TipoQuestaoEnum tipo) {
        TypedQuery<Questao> query = entityManager.createNamedQuery("Questao.PorAutorTipo", Questao.class);
        query.setParameter("idAutor", autor.getId());
        query.setParameter("tipo", tipo);

        return query.getResultList();
    }
    
    public List<Questao> buscarQuestoesPorAutor(Usuario autor){
        TypedQuery<Questao> query = entityManager.createNamedQuery("Questao.PorAutor", Questao.class);
        query.setParameter("idAutor", autor.getId());
        
        return query.getResultList();
    }

}
