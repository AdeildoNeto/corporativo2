/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.servico;

import br.edu.ifpe.recife.avalon.excecao.ValidacaoException;
import br.edu.ifpe.recife.avalon.model.questao.Alternativa;
import br.edu.ifpe.recife.avalon.model.questao.FiltroQuestao;
import br.edu.ifpe.recife.avalon.model.questao.MultiplaEscolha;
import br.edu.ifpe.recife.avalon.model.questao.Questao;
import br.edu.ifpe.recife.avalon.model.questao.TipoQuestaoEnum;
import br.edu.ifpe.recife.avalon.util.AvalonUtil;
import java.io.Serializable;
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
public class QuestaoServico implements Serializable{

    private static final long serialVersionUID = 2L;
    
    private static final String MSG_QUESTAO_UNICA = "questao.enunciado.repetido";
    private static final String MSG_ALTERNATIVAS_IGUAIS = "questao.alternativas.iguais";
    private static final String PERCENT = "%";

    @Resource
    private SessionContext sessao;

    @PersistenceContext(name = "jdbc/avalonDataSource", type = TRANSACTION)
    private EntityManager entityManager;

    /**
     * Salva uma questão.
     *
     * @param questao
     * @throws br.edu.ifpe.recife.avalon.excecao.ValidacaoException
     */
    public void salvar(@Valid Questao questao) throws ValidacaoException {
        questao.setEnunciado(questao.getEnunciado().trim());
        validarEnunciadoPorTipoValido(questao);
        entityManager.persist(questao);
    }
    
    /**
     * Salva uma questão de múltipla escolha.
     * 
     * @param questao
     * @throws ValidacaoException 
     */
    public void salvar(@Valid MultiplaEscolha questao) throws ValidacaoException{
        questao.setEnunciado(questao.getEnunciado().trim());
        validarAlternativasDiferentes(questao.getAlternativas());
        entityManager.persist(questao);
    }

    /**
     * Efetiva as alterações de uma questão.
     *
     * @param questao
     * @throws br.edu.ifpe.recife.avalon.excecao.ValidacaoException
     */
    public void alterar(@Valid Questao questao) throws ValidacaoException {
        validarEnunciadoPorTipoValido(questao);
        entityManager.merge(questao);
    }
    
    /**
     * Efetiva as alterações de uma questão de múltipla escolha.
     * 
     * @param questao
     * @throws ValidacaoException 
     */
    public void alterar(@Valid MultiplaEscolha questao) throws ValidacaoException {
        validarAlternativasDiferentes(questao.getAlternativas());
        entityManager.merge(questao);
    }

    /**
     * Remove uma questão.
     *
     * @param questao
     */
    public void anular(@Valid Questao questao) {
        if (!entityManager.contains(questao)) {
            questao.setAnulada(true);
            entityManager.merge(questao);
        }
    }

    /**
     * Consulta questões por criador.
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
     * Valida se o enunciado de uma nova questão é válido.
     *
     * @param questao
     * @throws ValidacaoException
     */
    private void validarEnunciadoPorTipoValido(@Valid Questao questao) throws ValidacaoException {
        TypedQuery<Questao> query = entityManager.createNamedQuery("Questao.PorTipoValido", Questao.class);
        query.setParameter("enunciado", questao.getEnunciado().trim());
        query.setParameter("tipo", questao.getTipo());
        query.setParameter("idCriador", questao.getCriador().getId());
        query.setParameter("idComponenteCurricular", questao.getComponenteCurricular().getId());
        query.setParameter("idQuestao", questao.getId());

        if (!TipoQuestaoEnum.MULTIPLA_ESCOLHA.equals(questao.getTipo()) && !query.getResultList().isEmpty()) {
            throw new ValidacaoException(AvalonUtil.getInstance().getMensagemValidacao(MSG_QUESTAO_UNICA));
        }
    }

    /**
     * Verifica se todas as alternativas são diferentes entre si.
     *
     * @param alternativas
     * @throws br.edu.ifpe.recife.avalon.excecao.ValidacaoException
     */
    private void validarAlternativasDiferentes(@NotNull List<Alternativa> alternativas) throws ValidacaoException {
        if (alternativas.isEmpty()) {
            throw new ValidacaoException();
        }

        int qtdeAlternativas = alternativas.size();

        for (int i = 0; i < qtdeAlternativas - 1; i++) {
            for (int j = i + 1; j < qtdeAlternativas; j++) {
                if (alternativas.get(i).getDescricao()
                        .equals(alternativas.get(j).getDescricao())) {
                    throw new ValidacaoException(AvalonUtil.getInstance().getMensagemValidacao(MSG_ALTERNATIVAS_IGUAIS));
                }
            }
        }
    }
    
    /**
     * Consulta questões a partir de um filtro.
     * 
     * @param filtro - filtro para buscar questoes
     * @return lista de questões.
     */
    public List<Questao> buscarQuestoesPorFiltro(@NotNull FiltroQuestao filtro){
        TypedQuery<Questao> query = entityManager.createNamedQuery("Questao.PorFiltroCompartilhada", Questao.class);
        
        query.setParameter("emailUsuario", filtro.getEmailUsuario());
        query.setParameter("tipo", filtro.getTipo());
        query.setParameter("enunciado", PERCENT.concat(filtro.getEnunciado()).concat(PERCENT));
        query.setParameter("idComponenteCurricular", filtro.getIdComponenteCurricular());
        query.setParameter("nomeCriador", PERCENT.concat(filtro.getNomeCriador()).concat(PERCENT));
        
        return query.getResultList();
    }
    
    /**
     * Método para consultar um usuário por Id.
     *
     * @param id
     * @return usuario.
     */
    public Questao buscarQuestaoPorId(@NotNull Long id) {
        TypedQuery<Questao> query = entityManager.createNamedQuery("Questao.PorId", Questao.class);
        query.setParameter("id", id);

        if (!query.getResultList().isEmpty()) {
            return query.getSingleResult();
        }
        return null;
    }

    /**
     * Consulta questões por simulado.
     * 
     * @param idSimulado
     * @return 
     */
    public List<Questao> buscarQuestoesPorSimulado(Long idSimulado) {
        TypedQuery<Questao> query = entityManager.createNamedQuery("Questao.PorSimulado", Questao.class);
        
        query.setParameter(1, idSimulado);
        
        return query.getResultList();
    }
    
    /**
     * Consulta questões por simulado.
     * 
     * @param idProva
     * @return 
     */
    public List<Questao> buscarQuestoesPorProva(Long idProva) {
        TypedQuery<Questao> query = entityManager.createNamedQuery("Questao.PorProva", Questao.class);
        
        query.setParameter(1, idProva);
        
        return query.getResultList();
    }
    
}
