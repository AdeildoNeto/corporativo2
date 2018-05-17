/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.servico;

import br.edu.ifpe.recife.avalon.excecao.ValidacaoException;
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
 * @author eduardo.f.amaral
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

    public void salvar(@Valid Simulado simulado) throws ValidacaoException {
        validarTitulo(simulado);
        entityManager.persist(simulado);
    }

    private void validarTitulo(@NotNull Simulado simulado) throws ValidacaoException {
        TypedQuery<Simulado> query = entityManager.createNamedQuery("Simulado.PorTituloValido",
                Simulado.class);

        query.setParameter("titulo", simulado.getTitulo());
        query.setParameter("idComponenteCurricular", simulado.getComponenteCurricular().getId());
        query.setParameter("emailCriador", simulado.getCriador().getEmail());

        if (!query.getResultList().isEmpty()) {
            throw new ValidacaoException(AvalonUtil.getInstance()
                    .getMensagemValidacao("componente.curricular.nome.duplicado"));
        }

    }

    public List<Simulado> buscarPorComponenteCurricular(Long idComponenteCurricular) {
        TypedQuery<Simulado> query = entityManager.createNamedQuery("Simulado.PorDisciplina",
                Simulado.class);

        query.setParameter("idComponenteCurricular", idComponenteCurricular);

        return query.getResultList();
    }

}
