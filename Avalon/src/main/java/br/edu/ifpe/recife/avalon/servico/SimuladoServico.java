/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.servico;

import br.edu.ifpe.recife.avalon.excecao.ValidacaoException;
import br.edu.ifpe.recife.avalon.model.filtro.FiltroSimulado;
import br.edu.ifpe.recife.avalon.model.simulado.Simulado;
import br.edu.ifpe.recife.avalon.model.simulado.SimuladoAluno;
import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
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
import javax.validation.constraints.NotNull;

/**
 *
 * @author eduardoamaral
 */
@Stateless
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class SimuladoServico {

    @Resource
    private SessionContext sessao;

    @PersistenceContext(name = "jdbc/avalonDataSource", type = TRANSACTION)
    private EntityManager entityManager;
    private static final String PERCENT = "%";

    /**
     * Salva um simulado.
     *
     * @param simulado
     * @throws ValidacaoException - quando o título já está em uso.
     */
    public void salvar(@Valid Simulado simulado) throws ValidacaoException {
        validarTitulo(simulado);
        validarQuestoesSelecionadas(simulado);
        entityManager.persist(simulado);
    }

    /**
     * Remove um Simulado.
     *
     * @param simulado
     */
    public void remover(@Valid Simulado simulado) {
        if (!entityManager.contains(simulado)) {
            simulado.setAtivo(false);
            entityManager.merge(simulado);
        }
    }

    /**
     * Valida o título de um novo simulado.
     *
     * @param simulado
     * @throws ValidacaoException - quando o título já está em uso.
     */
    private void validarTitulo(@NotNull Simulado simulado) throws ValidacaoException {
        TypedQuery<Simulado> query = entityManager.createNamedQuery("Simulado.PorTituloValido",
                Simulado.class);

        query.setParameter("titulo", simulado.getTitulo());
        query.setParameter("idComponenteCurricular", simulado.getComponenteCurricular().getId());
        query.setParameter("emailProfessor", simulado.getProfessor().getEmail());

        if (!query.getResultList().isEmpty()) {
            throw new ValidacaoException(AvalonUtil.getInstance()
                    .getMensagemValidacao("simulado.titulo.duplicado"));
        }

    }

    /**
     * Valida se ao menos uma questão foi selecionada para o simulado.
     *
     * @param simulado
     * @throws ValidacaoException
     */
    private void validarQuestoesSelecionadas(Simulado simulado) throws ValidacaoException {
        if (simulado.getQuestoes() == null || simulado.getQuestoes().isEmpty()) {
            throw new ValidacaoException(AvalonUtil.getInstance()
                    .getMensagemValidacao("selecione.uma.questao"));
        }
    }

    /**
     * Consulta simulados por professor.
     *
     * @param emailProfessor - email do professor.
     * @return lista de simulados.
     */
    public List<Simulado> buscarSimuladosPorProfessor(@NotNull String emailProfessor) {
        TypedQuery<Simulado> query = entityManager.createNamedQuery("Simulado.PorProfessor",
                Simulado.class);

        query.setParameter("emailProfessor", emailProfessor);

        return query.getResultList();
    }

    /**
     * Consulta simulados por filtro.
     *
     * @param filtro
     * @return lista de simulados
     */
    public List<Simulado> buscarSimuladoPorFiltro(FiltroSimulado filtro) {
        TypedQuery<Simulado> query = entityManager.createNamedQuery("Simulado.PorFiltro",
                Simulado.class);

        query.setParameter("titulo", PERCENT.concat(filtro.getTitulo()).concat(PERCENT));
        query.setParameter("idComponenteCurricular", filtro.getIdComponenteCurricular());
        query.setParameter("nomeProfessor", PERCENT.concat(filtro.getnomeProfessor()).concat(PERCENT));

        return query.getResultList();
    }

    /**
     * Registra o simulado realizado pelo aluno.
     *
     * @param simuladoAluno
     */
    public void salvarSimuladoAluno(@Valid SimuladoAluno simuladoAluno) {
        entityManager.persist(simuladoAluno);
    }

    /**
     * Consulta todos os simulados realizados por um aluno.
     *
     * @param aluno
     * @return lista de simulados realizados pelo aluno.
     */
    public List<SimuladoAluno> buscarResultadosSimuladoAluno(Usuario aluno) {
        TypedQuery<SimuladoAluno> query = entityManager.createNamedQuery("SimuladoAluno.PorAluno",
                SimuladoAluno.class);

        query.setParameter("idAluno", aluno.getId());

        return query.getResultList();
    }

    /**
     * Consulta todos os resultados de um simulado.
     *
     * @param simulado
     * @return simulados.
     */
    public List<SimuladoAluno> buscarResultadosSimulado(Simulado simulado) {
        TypedQuery<SimuladoAluno> query = entityManager.createNamedQuery("SimuladoAluno.PorSimulado",
                SimuladoAluno.class);

        query.setParameter("idSimulado", simulado.getId());

        return query.getResultList();
    }

    /**
     * Recupera um simulado por ID.
     * 
     * @param id
     * @return 
     */
    public Simulado buscarSimuladoPorId(@NotNull Long id) {
        TypedQuery<Simulado> query = entityManager.createNamedQuery("Simulado.PorId", Simulado.class);
        query.setParameter("idSimulado", id);

        if (!query.getResultList().isEmpty()) {
            return query.getSingleResult();
        }
        return null;
    }

}
