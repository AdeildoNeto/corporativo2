/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.testes;

import br.edu.ifpe.recife.avalon.excecao.ValidacaoException;
import br.edu.ifpe.recife.avalon.model.questao.FiltroQuestao;
import br.edu.ifpe.recife.avalon.model.questao.Questao;
import br.edu.ifpe.recife.avalon.model.questao.TipoQuestaoEnum;
import br.edu.ifpe.recife.avalon.model.simulado.Simulado;
import br.edu.ifpe.recife.avalon.servico.ComponenteCurricularServico;
import br.edu.ifpe.recife.avalon.servico.QuestaoServico;
import br.edu.ifpe.recife.avalon.servico.SimuladoServico;
import br.edu.ifpe.recife.avalon.servico.UsuarioServico;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
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
public class SimuladoTest {
    
    private static EJBContainer container;
    
    @EJB
    private SimuladoServico simuladoServico;
    
    @EJB
    private QuestaoServico questaoServico;

    @EJB
    private UsuarioServico usuarioServico;

    @EJB
    private ComponenteCurricularServico ccurricularServico;

    private static Logger logger;
    
    public SimuladoTest() {
    }

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
        simuladoServico = (SimuladoServico) container.getContext().lookup("java:global/classes/SimuladoServico");
        questaoServico = (QuestaoServico) container.getContext().lookup("java:global/classes/QuestaoServico");
        usuarioServico = (UsuarioServico) container.getContext().lookup("java:global/classes/UsuarioServico");
        ccurricularServico = (ComponenteCurricularServico) container.getContext().lookup("java:global/classes/ComponenteCurricularServico");
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void t01_inserirSimulado() throws ValidacaoException{
        logger.info("Executando t01: inserirSimulado");
        Simulado simulado = new Simulado();
        preencherNovoSimulado(simulado);
        
        simuladoServico.salvar(simulado);
        
        assertNotNull(simulado.getId());
    }
    
    @Test(expected = ValidacaoException.class)
    public void t02_criticarSimuladoComTituloRepetido() throws ValidacaoException{
        logger.info("Executando t02: criticarSimuladoComTituloRepetido");
        Simulado simulado = new Simulado();
        preencherNovoSimulado(simulado);
        
        simuladoServico.salvar(simulado);
    }
    
    @Test
    public void t03_buscarPorComponenteCurricular(){
        logger.info("Executando t03: buscarPorComponenteCurricular");
        List<Simulado> lista = simuladoServico.buscarPorComponenteCurricular(1l);
        
        assertTrue(!lista.isEmpty());
    }
    
    private Simulado preencherNovoSimulado(Simulado simulado){
        simulado.setComponenteCurricular(ccurricularServico.buscarComponentePorNome("Teste"));
        simulado.setCriador(usuarioServico.buscarUsuarioPorEmail("teste@gmail.com"));
        simulado.setTitulo("Teste Simulado.");
        simulado.setDataCriacao(Calendar.getInstance().getTime());
        
        FiltroQuestao filtro = new FiltroQuestao();
        
        filtro.setIdComponenteCurricular(1l);
        filtro.setTipo(TipoQuestaoEnum.VERDADEIRO_FALSO);
        
        List<Questao> questoes = questaoServico.buscarQuestoesPorFiltro(filtro);
        
        logger.info("Nr. questões: "+ questoes.size());
        
        simulado.setQuestoes(questoes);
        
        return simulado;
    }
    
}
