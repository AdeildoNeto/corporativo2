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
public class QuestaoServico {

    private static final String MSG_QUESTAO_UNICA = "questao.enunciado.repetido";
    private static final String MSG_ALTERNATIVAS_IGUAIS = "questao.alternativas.iguais";
    private static final String PERCENT = "%";

    @Resource
    private SessionContext sessao;

    @PersistenceContext(name = "jdbc/avalonDataSource", type = TRANSACTION)
    private EntityManager entityManager;

    /**
     * Método para salvar um Questão
     *
     * @param questao
     * @throws br.edu.ifpe.recife.avalon.excecao.ValidacaoException
     */
    public void salvar(@Valid Questao questao) throws ValidacaoException {
        questao.setEnunciado(questao.getEnunciado().trim());
        validarEnunciadoPorTipoValido(questao);
        entityManager.persist(questao);
    }
    
    public void salvar(@Valid MultiplaEscolha questao) throws ValidacaoException{
        questao.setEnunciado(questao.getEnunciado().trim());
        validarAlternativasDiferentes(questao.getAlternativas());
        entityManager.persist(questao);
    }

    /**
     * Método para alterar uma Questão
     *
     * @param questao
     * @throws br.edu.ifpe.recife.avalon.excecao.ValidacaoException
     */
    public void alterar(@Valid Questao questao) throws ValidacaoException {
        validarEnunciadoPorTipoValidoEdicao(questao);
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
     * Método para validar se o enunciado de uma nova Questão é válido para um
     * determinado Tipo
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

        if (!TipoQuestaoEnum.MULTIPLA_ESCOLHA.equals(questao.getTipo()) && !query.getResultList().isEmpty()) {
            throw new ValidacaoException(AvalonUtil.getInstance().getMensagemValidacao(MSG_QUESTAO_UNICA));
        }
    }

    /**
     * Método para validar se o enunciado de uma Questão a ser alterada é válido
     * a depender do seu Tipo.
     *
     * @param questao
     * @throws br.edu.ifpe.recife.avalon.excecao.ValidacaoException
     */
    private void validarEnunciadoPorTipoValidoEdicao(@Valid Questao questao) throws ValidacaoException {
        TypedQuery<Questao> query = entityManager.createNamedQuery("Questao.PorEnunciadoTipoId", Questao.class);
        query.setParameter("enunciado", questao.getEnunciado().trim());
        query.setParameter("tipo", questao.getTipo());
        query.setParameter("id", questao.getId());

        if (!TipoQuestaoEnum.MULTIPLA_ESCOLHA.equals(questao.getTipo()) && !query.getResultList().isEmpty()) {
            throw new ValidacaoException(AvalonUtil.getInstance().getMensagemValidacao(MSG_QUESTAO_UNICA));
        }
    }

    /**
     * Método para verificar se todas as alternativas são diferentes entre si.
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
                if (alternativas.get(i).getAlternativa()
                        .equals(alternativas.get(j).getAlternativa())) {
                    throw new ValidacaoException(AvalonUtil.getInstance().getMensagemValidacao(MSG_ALTERNATIVAS_IGUAIS));
                }
            }
        }
    }
    
    /**
     * Método para pesquisar questões a partir de um filtro.
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
     * Método para pesquisar questões do tipo V/F e Múltipla Escolha a partir de um filtro.
     * 
     * @param filtro - filtro para buscar questoes
     * @return lista de questões.
     */
    public List<Questao> buscarQuestoesParaSimulado(@NotNull FiltroQuestao filtro){
        TypedQuery<Questao> query = entityManager.createNamedQuery("Questao.ParaSimulado", Questao.class);
        
        query.setParameter("emailUsuario", filtro.getEmailUsuario());
        query.setParameter("enunciado", PERCENT.concat(filtro.getEnunciado()).concat(PERCENT));
        query.setParameter("idComponenteCurricular", filtro.getIdComponenteCurricular());
        query.setParameter("nomeCriador", PERCENT.concat(filtro.getNomeCriador()).concat(PERCENT));
        
        return query.getResultList();
    }
    
}
