/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.servico;

import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
import java.util.logging.Logger;
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
public class UsuarioServico {

    @Resource
    private SessionContext sessao;

    @PersistenceContext(name = "jdbc/avalonDataSource", type = TRANSACTION)
    private EntityManager entityManager;
    private Logger log;
 
    
    
    /**
     * Método para salvar um usuário.
     *
     * @param usuario
     */
    public void salvar(Usuario usuario) {
        TypedQuery<Usuario> query = entityManager.createNamedQuery("Usuario.PorLogin", Usuario.class);
        query.setParameter("email", usuario.getEmail());
        if (query.getResultList().isEmpty()) {
            entityManager.persist(usuario);
        }else{
            System.out.println("USUÁRIO JÁ CADASTRADO");
        }

    }

    /**
     * Método para alterar um usuário.
     *
     * @param usuario
     */
    public void alterar(Usuario usuario) {
        entityManager.merge(usuario);
    }

    /**
     * Método para remover um usuário.
     *
     * @param usuario
     */
    public void remover(Usuario usuario) {
        if (!entityManager.contains(usuario)) {
            usuario = entityManager.merge(usuario);
        }
        entityManager.remove(usuario);
    }

    /**
     * Método para consultar um usuário por Email e Senha.
     *
     * @param usuario
     * @return usuário
     */
    public Usuario buscarUsuarioPorLogin(Usuario usuario) {
        TypedQuery<Usuario> query = entityManager.createNamedQuery("Usuario.PorLogin", Usuario.class);
        query.setParameter("email", usuario.getEmail());
        query.setParameter("senha", usuario.getSenha());

        if (!query.getResultList().isEmpty()) {
            return query.getSingleResult();
        }

        return null;
    }    
    /**
     * Método para buscar um usuário por id
     * @param id
     * @return usuario
     */
    public Usuario buscarUsuarioPorId(long id){
        TypedQuery<Usuario> query = entityManager.createNamedQuery("Usuario.PorId", Usuario.class);
        query.setParameter("id", id);

        if (!query.getResultList().isEmpty()) {
            return query.getSingleResult();
        }

        return null;
    }

}
