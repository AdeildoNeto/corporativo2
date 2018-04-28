/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.servico;

import br.edu.ifpe.recife.avalon.excecao.ValidacaoException;
import br.edu.ifpe.recife.avalon.model.questao.ComponenteCurricular;
import br.edu.ifpe.recife.avalon.util.AvalonUtil;
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

/**
 *
 * @author eduardo.f.amaral
 */
@Stateless
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ComponenteCurricularServico {

    @Resource
    private SessionContext sessao;

    @PersistenceContext(name = "jdbc/avalonDataSource", type = TRANSACTION)
    private EntityManager entityManager;
    
    public void salvar(@Valid ComponenteCurricular componenteCurricular) throws ValidacaoException{
        validarComponenteCurricular(componenteCurricular.getNome());
        entityManager.persist(componenteCurricular);
    }

    private void validarComponenteCurricular(String nome) throws ValidacaoException {
        ComponenteCurricular componente = buscarComponentePorNome(nome);
        
        if(componente != null){
            throw new ValidacaoException(AvalonUtil.getInstance().getMensagemValidacao("componente.curricular.nome.duplicado"));
        }
        
    }
    
    public ComponenteCurricular buscarComponentePorNome(String nome){
        TypedQuery<ComponenteCurricular> query = entityManager.createNamedQuery("ComponenteCurricular.PorNome",
                ComponenteCurricular.class);
        
        query.setParameter("nomeComponente", nome);
        
        if(query.getResultList().isEmpty()){
            return null;
        }
        
        return query.getSingleResult();
        
    }
    
    public List<ComponenteCurricular> buscarTodosComponentes(){
        TypedQuery<ComponenteCurricular> query = entityManager.createNamedQuery("ComponenteCurricular.Todos",
                ComponenteCurricular.class);
        
        return query.getResultList();
    }

}
