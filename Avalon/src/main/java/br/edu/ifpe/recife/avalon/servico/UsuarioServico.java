/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.servico;

import br.edu.ifpe.recife.avalon.model.usuario.GrupoEnum;
import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
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
public class UsuarioServico {

    @Resource
    private SessionContext sessao;

    @PersistenceContext(name = "jdbc/avalonDataSource", type = TRANSACTION)
    private EntityManager entityManager;

    /**
     * Método para salvar um usuário.
     *
     * @param usuario
     */
    public void salvar(@Valid Usuario usuario) {
        TypedQuery<Usuario> query = entityManager.createNamedQuery("Usuario.PorEmail", Usuario.class);
        query.setParameter("email", usuario.getEmail());

        if (query.getResultList().isEmpty()) {
            entityManager.persist(usuario);
        } else {
            throw new Error("USUARIO CADASTRADO");
        }

    }

    /**
     * Método para alterar um usuário.
     *
     * @param usuario
     */
    public void alterar(@Valid Usuario usuario) {
        entityManager.merge(usuario);
    }

    /**
     * Método para remover um usuário.
     *
     * @param usuario
     */
    public void remover(@Valid Usuario usuario) {
        if (!entityManager.contains(usuario)) {
            usuario = entityManager.merge(usuario);
        }
        entityManager.remove(usuario);
    }

    /**
     * Método para consultar um usuário por Email.
     * 
     * @param email
     * @return usuario.
     */
    public Usuario buscarUsuarioPorEmail(@NotNull String email) {
        TypedQuery<Usuario> query = entityManager.createNamedQuery("Usuario.PorEmail", Usuario.class);
        query.setParameter("email", email);

        if (!query.getResultList().isEmpty()) {
            return query.getSingleResult();
        }
        return null;
    }
    
}
