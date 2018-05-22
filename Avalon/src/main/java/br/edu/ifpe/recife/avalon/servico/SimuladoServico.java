/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.servico;

import br.edu.ifpe.recife.avalon.excecao.ValidacaoException;
import br.edu.ifpe.recife.avalon.model.simulado.FiltroSimulado;
import br.edu.ifpe.recife.avalon.model.simulado.Simulado;
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
     * Método para salvar um simulado.
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
     * Método para remover um Simulado.
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
     * Método para validar o título de um novo simulado.
     *
     * @param simulado
     * @throws ValidacaoException - quando o título já está em uso.
     */
    private void validarTitulo(@NotNull Simulado simulado) throws ValidacaoException {
        TypedQuery<Simulado> query = entityManager.createNamedQuery("Simulado.PorTituloValido",
                Simulado.class);

        query.setParameter("titulo", simulado.getTitulo());
        query.setParameter("idComponenteCurricular", simulado.getComponenteCurricular().getId());
        query.setParameter("emailCriador", simulado.getCriador().getEmail());

        if (!query.getResultList().isEmpty()) {
            throw new ValidacaoException(AvalonUtil.getInstance()
                    .getMensagemValidacao("simulado.titulo.duplicado"));
        }

    }

    /**
     * Método para validar se ao menos uma questão foi selecionada para o simulado.
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
     * Método para buscar simulados por criador.
     *
     * @param emailCriador - email do criador.
     * @return lista de simulados.
     */
    public List<Simulado> buscarSimuladosPorCriador(@NotNull String emailCriador) {
        TypedQuery<Simulado> query = entityManager.createNamedQuery("Simulado.PorCriador",
                Simulado.class);

        query.setParameter("emailCriador", emailCriador);

        return query.getResultList();
    }

    /**
     * Método para buscar questão por filtro.
     *
     * @param filtro
     * @return lista de simulados
     */
    public List<Simulado> buscarSimuladoPorFiltro(FiltroSimulado filtro) {
        TypedQuery<Simulado> query = entityManager.createNamedQuery("Simulado.PorFiltro",
                Simulado.class);

        query.setParameter("titulo", PERCENT.concat(filtro.getTitulo()).concat(PERCENT));
        query.setParameter("idComponenteCurricular", filtro.getIdComponenteCurricular());
        query.setParameter("nomeCriador", PERCENT.concat(filtro.getNomeCriador()).concat(PERCENT));

        return query.getResultList();
    }

}
