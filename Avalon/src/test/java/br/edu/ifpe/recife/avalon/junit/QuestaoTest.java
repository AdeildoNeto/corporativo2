/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.junit;

import br.edu.ifpe.recife.avalon.cucumber.util.DbUnitUtil;
import br.edu.ifpe.recife.avalon.excecao.ValidacaoException;
import br.edu.ifpe.recife.avalon.model.questao.Alternativa;
import br.edu.ifpe.recife.avalon.model.questao.componente.ComponenteCurricular;
import br.edu.ifpe.recife.avalon.model.filtro.FiltroQuestao;
import br.edu.ifpe.recife.avalon.model.questao.MultiplaEscolha;
import br.edu.ifpe.recife.avalon.model.questao.Questao;
import br.edu.ifpe.recife.avalon.model.questao.enums.TipoQuestaoEnum;
import br.edu.ifpe.recife.avalon.model.questao.VerdadeiroFalso;
import br.edu.ifpe.recife.avalon.model.usuario.GrupoEnum;
import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
import br.edu.ifpe.recife.avalon.servico.ComponenteCurricularServico;
import br.edu.ifpe.recife.avalon.servico.QuestaoServico;
import br.edu.ifpe.recife.avalon.servico.UsuarioServico;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 *
 * @author eduardoamaral
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class QuestaoTest {

    private static EJBContainer container;

    @EJB
    private QuestaoServico questaoServico;

    @EJB
    private UsuarioServico usuarioServico;

    @EJB
    private ComponenteCurricularServico ccurricularServico;

    private static Logger logger;

    @BeforeClass
    public static void setUpClass() {
        container = EJBContainer.createEJBContainer();
        logger = Logger.getGlobal();
        logger.setLevel(Level.INFO);
        DbUnitUtil.inserirDados();

    }

    @AfterClass
    public static void tearDownClass() {
        container.close();
    }

    @Before
    public void setUp() throws NamingException {
        questaoServico = (QuestaoServico) container.getContext().lookup("java:global/classes/QuestaoServico");
        usuarioServico = (UsuarioServico) container.getContext().lookup("java:global/classes/UsuarioServico");
        ccurricularServico = (ComponenteCurricularServico) container.getContext().lookup("java:global/classes/ComponenteCurricularServico");
    }

    @After
    public void tearDown() {
    }

    @Test
    public void t01_inserirQuestaoDiscursiva() throws ValidacaoException {
        logger.info("Executando t01_inserirQuestaoDiscursiva");
        Questao questao = getQuestaoPreenchida();
        questaoServico.salvar(questao);

        assertNotNull(questao.getId());
    }

    @Test
    public void t02_buscarQuestaoProfessor() {
        logger.info("Executando t02_buscarQuestaoProfessor");
        List<Questao> questoes = questaoServico.buscarQuestoesProfessor(getProfessor());

        assertTrue(!questoes.isEmpty());
    }

    @Test
    public void t03_alterarQuestao() throws ValidacaoException {
        logger.info("Executando t03: alterarQuestao");
        Usuario professor = getProfessor();
        List<Questao> resultado = questaoServico.buscarQuestoesProfessor(professor);

        Questao questao = resultado.get(0);

        questao.setEnunciado("Enunciado alterado");

        questaoServico.alterar(questao);

        resultado = questaoServico.buscarQuestoesProfessor(professor);
        Questao questao2 = resultado.get(0);

        assertEquals("Enunciado alterado", questao2.getEnunciado());
    }

    @Test
    public void t04_anularQuestao() {
        logger.info("Executando t04: anularQuestao");
        FiltroQuestao filtro = new FiltroQuestao();
        filtro.setEnunciado("O que é um diagrama de atividades?");
        filtro.setEmailUsuario(getProfessor().getEmail());
        List<Questao> resultado = questaoServico.buscarQuestoesPorFiltro(filtro);
        
        questaoServico.anular(resultado.get(0));
        
        resultado = questaoServico.buscarQuestoesPorFiltro(filtro);

        assertTrue(resultado.isEmpty());
    }

    @Test
    public void t05_inserirQuestaoMultiplaEscolha() throws ValidacaoException {
        logger.info("Executando t05: InserirQuestaoMultiplaEscolha");
        MultiplaEscolha questao = getQuestaoMEPreenchida();
        questaoServico.salvar(questao);

        assertNotNull(questao.getId());

    }

    @Test
    public void t06_cadastrarQuestaoVF() throws ValidacaoException {
        logger.info("Executando t06_cadastrarQuestaoVF");
        VerdadeiroFalso questao = getQuestaoVFPreenchida();

        questaoServico.salvar(questao);

        assertNotNull(questao.getId());
    }
    
    @Test(expected = EJBException.class)
    public void t07_criticarQuestaoSemProfessor() throws ValidacaoException {
        logger.info("Executando t07_criticarQuestaoSemProfessor");
        Questao questao = getQuestaoPreenchida();
        questao.setProfessor(null);
        
        questaoServico.salvar(questao);
    }

    @Test(expected = EJBException.class)
    public void t08_criticarQuestaoSemComponenteCurricular() throws ValidacaoException {
        logger.info("Executando t08_criticarQuestaoSemComponenteCurricular");
        Questao questao = getQuestaoPreenchida();
        questao.setComponenteCurricular(null);
        
        questaoServico.salvar(questao);
    }
    
    @Test(expected = EJBException.class)
    public void t09_criticarQuestaoSemEnunciado() throws ValidacaoException {
        logger.info("Executando t09_criticarQuestaoSemEnunciado");
        Questao questao = getQuestaoPreenchida();
        questao.setEnunciado(null);
        
        questaoServico.salvar(questao);
    }
    
    @Test(expected = EJBException.class)
    public void t10_criticarQuestaoSemTipo() throws ValidacaoException {
        logger.info("Executando t10_criticarQuestaoSemTipo");
        Questao questao = getQuestaoPreenchida();
        questao.setTipo(null);
        
        questaoServico.salvar(questao);
    }
    
    @Test(expected = EJBException.class)
    public void t11_criticarQuestaoSemDataCriacao() throws ValidacaoException {
        logger.info("Executando t11_criticarQuestaoSemDataCriacao");
        Questao questao = getQuestaoPreenchida();
        questao.setDataCriacao(null);
        
        questaoServico.salvar(questao);
    }

    @Test(expected = ValidacaoException.class)
    public void t12_criticarQuestaoRepetida() throws ValidacaoException {
        logger.info("Executando t12_criticarQuestaoRepetida");
        Questao questao = getQuestaoPreenchida();
        questao.setEnunciado("O que é um diagrama de sequência?");
        
        questaoServico.salvar(questao);
    }

    @Test(expected = ValidacaoException.class)
    public void t13_criticarAlternativasRepetidas() throws ValidacaoException {
        logger.info("Executando t13_criticarAlternativasRepetidas");

        MultiplaEscolha questao = getQuestaoMEPreenchida();

        questao.getAlternativas().get(0).setDescricao("Teste");
        questao.getAlternativas().get(1).setDescricao("Teste");
        
        questaoServico.salvar(questao);

    }

    @Test
    public void t14_buscarQuestaoPorFiltroEnunciado() {
        logger.info("Executando t14_buscarQuestaoPorFiltroEnunciado");
        FiltroQuestao filtro = new FiltroQuestao();
        List<Questao> resultado;
        
        filtro.setTipo(TipoQuestaoEnum.DISCURSIVA);
        filtro.setEnunciado("O que é");
        filtro.setEmailUsuario(getProfessor().getEmail());

        resultado = questaoServico.buscarQuestoesPorFiltro(filtro);

        assertTrue(!resultado.isEmpty());
    }

    @Test
    public void t21_buscarQuestaoPorFiltroProfessor() {
        logger.info("Executando t21: buscarQuestaoPorFiltroProfessor");
        FiltroQuestao filtro = new FiltroQuestao();
        List<Questao> resultado;
        filtro.setTipo(TipoQuestaoEnum.DISCURSIVA);
        filtro.setEmailUsuario(getProfessor().getEmail());
        filtro.setNomeProfessor(getProfessor().getNome());

        resultado = questaoServico.buscarQuestoesPorFiltro(filtro);

        assertTrue(!resultado.isEmpty());
    }

    @Test
    public void t22_buscarQuestaoPorFiltroComponenteCurricular() {
        logger.info("Executando t22: buscarQuestaoPorFiltroComponenteCurricular");
        FiltroQuestao filtro = new FiltroQuestao();
        List<Questao> resultado;
        filtro.setTipo(TipoQuestaoEnum.DISCURSIVA);
        filtro.setIdComponenteCurricular(getComponenteCurricular().getId());
        filtro.setEmailUsuario(getProfessor().getEmail());

        resultado = questaoServico.buscarQuestoesPorFiltro(filtro);

        assertTrue(!resultado.isEmpty());
    }

    @Test(expected = EJBException.class)
    public void t23_criticarVFSemRespostaDefinida() throws ValidacaoException {
        logger.info("Executando t23_criticarVFSemRespostaDefinida");
        VerdadeiroFalso questao = getQuestaoVFPreenchida();
        questao.setResposta(null);

        questaoServico.salvar(questao);
    }
    
    @Test(expected = EJBException.class)
    public void t23_criticarMESemRespostaDefinida() throws ValidacaoException {
        logger.info("Executando t23_criticarMESemRespostaDefinida");
        MultiplaEscolha questao = getQuestaoMEPreenchida();
        questao.setAlternativaCorreta(null);

        questaoServico.salvar(questao);
    }

    private Questao getQuestaoPreenchida() {
        Questao questao = new Questao();
        ComponenteCurricular componente = getComponenteCurricular();
        Usuario professor = getProfessor();
        
        questao.setComponenteCurricular(componente);
        questao.setEnunciado("Questao sem professor?");
        questao.setTipo(TipoQuestaoEnum.DISCURSIVA);
        questao.setProfessor(professor);
        questao.setSolucao("Teste Solução");
        questao.setDataCriacao(Calendar.getInstance().getTime());
        
        return questao;
    }
    
    private VerdadeiroFalso getQuestaoVFPreenchida(){
        VerdadeiroFalso questao = new VerdadeiroFalso();

        questao.setProfessor(getProfessor());
        questao.setComponenteCurricular(getComponenteCurricular());
        questao.setEnunciado("Questao V/F teste");
        questao.setTipo(TipoQuestaoEnum.VERDADEIRO_FALSO);
        questao.setDataCriacao(Calendar.getInstance().getTime());
        questao.setResposta(true);
        
        return questao;
    }
    
    private MultiplaEscolha getQuestaoMEPreenchida(){
        MultiplaEscolha questao = new MultiplaEscolha();
        questao.setComponenteCurricular(getComponenteCurricular());
        questao.setEnunciado("Teste?");
        questao.setProfessor(getProfessor());
        questao.setTipo(TipoQuestaoEnum.MULTIPLA_ESCOLHA);
        questao.setDataCriacao(Calendar.getInstance().getTime());
        questao.setAlternativas(getAlternativasPreenchidas(questao));
        questao.setAlternativaCorreta(0);
        
        return questao;
    }
    
    private List<Alternativa> getAlternativasPreenchidas(MultiplaEscolha questao){
        List<Alternativa> alternativas = new ArrayList<>();
        Alternativa alternativa;
        for(int i = 1; i < 6; i++){
            alternativa = new Alternativa();
            alternativa.setDescricao("Alternativa "+ i);
            alternativa.setQuestao(questao);
            alternativas.add(alternativa);
        }
        
        return alternativas;
    }
    
    private Usuario getProfessor(){
        return usuarioServico.buscarUsuarioPorEmail("teste@gmail.com");
    }
    
    private ComponenteCurricular getComponenteCurricular(){
        return ccurricularServico.buscarComponentePorNome("Engenharia de Software");
    }

}

