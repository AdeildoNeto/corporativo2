/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.servico;

import br.edu.ifpe.recife.avalon.excecao.ValidacaoException;
import br.edu.ifpe.recife.avalon.model.avaliacao.simulado.Simulado;
import br.edu.ifpe.recife.avalon.model.avaliacao.simulado.SimuladoAluno;
import br.edu.ifpe.recife.avalon.model.turma.Turma;
import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
import br.edu.ifpe.recife.avalon.util.AvalonUtil;
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
public class TurmaServico {

    @PersistenceContext(name = "jdbc/avalonDataSource", type = TRANSACTION)
    private EntityManager entityManager;

    
    /**
     * Cadastra uma nova turma.
     * 
     * @param turma
     * @throws ValidacaoException 
     */
    public void salvar(@Valid Turma turma) throws ValidacaoException {
        validarAlunos(turma);
        entityManager.persist(turma);
    }
    
    private void validarAlunos(Turma turma) throws ValidacaoException {
        if(turma.getAlunos() == null || turma.getAlunos().isEmpty()){
            throw new ValidacaoException(AvalonUtil.getInstance().getMensagemValidacao("turma.alunos.obrigatorio"));
        }
    }

    /**
     * Remove uma turma.
     *
     * @param turma
     */
    public void remover(@Valid Turma turma) {
        if (!entityManager.contains(turma)) {
            turma.setAtiva(false);
            entityManager.merge(turma);
        }
    }
    
    /**
     * Consulta todas as turmas de um professor.
     * 
     * @param emailProfessor
     * @return 
     */
    public List<Turma> buscarTurmas(@NotNull String emailProfessor) {
        TypedQuery<Turma> query = entityManager.createNamedQuery("Turma.PorProfessor",
                Turma.class);

        query.setParameter("emailProfessor", emailProfessor);

        return query.getResultList();
    }

}
