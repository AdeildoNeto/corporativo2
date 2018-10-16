/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.junit;

import br.edu.ifpe.recife.avalon.cucumber.util.DbUnitUtil;
import br.edu.ifpe.recife.avalon.excecao.ValidacaoException;
import br.edu.ifpe.recife.avalon.model.questao.FiltroQuestao;
import br.edu.ifpe.recife.avalon.model.questao.Questao;
import br.edu.ifpe.recife.avalon.model.questao.TipoQuestaoEnum;
import br.edu.ifpe.recife.avalon.model.simulado.FiltroSimulado;
import br.edu.ifpe.recife.avalon.model.simulado.Simulado;
import br.edu.ifpe.recife.avalon.model.simulado.SimuladoAluno;
import br.edu.ifpe.recife.avalon.model.simulado.SimuladoAlunoQuestao;
import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
import br.edu.ifpe.recife.avalon.servico.ComponenteCurricularServico;
import br.edu.ifpe.recife.avalon.servico.QuestaoServico;
import br.edu.ifpe.recife.avalon.servico.SimuladoServico;
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
public class SimuladoTest {
    
    private static final String EMAIL_TESTE = "teste@gmail.com";
    
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
    public void t03_buscarPorFiltroComponenteCurricular(){
        logger.info("Executando t03: buscarPorFiltroComponenteCurricular");
        FiltroSimulado filtro = new FiltroSimulado();
        
        filtro.setIdComponenteCurricular(1l);
        
        List<Simulado> lista = simuladoServico.buscarSimuladoPorFiltro(filtro);
        
        assertTrue(!lista.isEmpty());
    }
    
    @Test
    public void t04_buscarPorFiltroTitulo(){
        logger.info("Executando t04: buscarPorComponenteCurricular");
        FiltroSimulado filtro = new FiltroSimulado();
        
        filtro.setTitulo("Teste");
        
        List<Simulado> lista = simuladoServico.buscarSimuladoPorFiltro(filtro);
        
        assertTrue(!lista.isEmpty());
    }
    
    @Test
    public void t05_buscarPorFiltroProfessor(){
        logger.info("Executando t05: buscarPorFiltroProfessor");
        FiltroSimulado filtro = new FiltroSimulado();
        
        filtro.setTitulo("Teste");
        
        List<Simulado> lista = simuladoServico.buscarSimuladoPorFiltro(filtro);
        
        assertTrue(!lista.isEmpty());
    }
    
    @Test(expected = EJBException.class)
    public void t06_criticarSimuladoSemTitulo() throws ValidacaoException{
        logger.info("Executando t06: criticarSimuladoSemTitulo");
        Simulado simulado = new Simulado();
        simulado.setComponenteCurricular(ccurricularServico.buscarComponentePorNome("Teste"));
        simulado.setProfessor(usuarioServico.buscarUsuarioPorEmail(EMAIL_TESTE));
        simulado.setDataCriacao(Calendar.getInstance().getTime());
        
        FiltroQuestao filtro = new FiltroQuestao();
        
        filtro.setIdComponenteCurricular(1l);
        filtro.setTipo(TipoQuestaoEnum.VERDADEIRO_FALSO);
        
        List<Questao> questoes = questaoServico.buscarQuestoesPorFiltro(filtro);
        
        simulado.setQuestoes(questoes);
        
        simuladoServico.salvar(simulado);
    }
    
    @Test(expected = EJBException.class)
    public void t07_criticarSimuladoSemProfessor() throws ValidacaoException{
        logger.info("Executando t07: criticarSimuladoSemProfessor");
        Simulado simulado = new Simulado();
        simulado.setComponenteCurricular(ccurricularServico.buscarComponentePorNome("Teste"));
        simulado.setTitulo("Teste Simulado 7.");
        simulado.setDataCriacao(Calendar.getInstance().getTime());
        
        FiltroQuestao filtro = new FiltroQuestao();
        
        filtro.setIdComponenteCurricular(1l);
        filtro.setTipo(TipoQuestaoEnum.VERDADEIRO_FALSO);
        
        List<Questao> questoes = questaoServico.buscarQuestoesPorFiltro(filtro);
        
        simulado.setQuestoes(questoes);
        
        simuladoServico.salvar(simulado);
    }
    
    @Test(expected = EJBException.class)
    public void t08_criticarSimuladoSemComponenteCurricular() throws ValidacaoException{
        logger.info("Executando t08: criticarSimuladoSemComponenteCurricular");
        Simulado simulado = new Simulado();
        simulado.setTitulo("Teste Simulado 8.");
        simulado.setDataCriacao(Calendar.getInstance().getTime());
        simulado.setProfessor(usuarioServico.buscarUsuarioPorEmail("teste@gmail.com"));
        
        FiltroQuestao filtro = new FiltroQuestao();
        
        filtro.setIdComponenteCurricular(1l);
        filtro.setTipo(TipoQuestaoEnum.VERDADEIRO_FALSO);
        
        List<Questao> questoes = questaoServico.buscarQuestoesPorFiltro(filtro);
        
        simulado.setQuestoes(questoes);
        
        simuladoServico.salvar(simulado);
    }
    
    @Test(expected = ValidacaoException.class)
    public void t09_criticarSimuladoSemQuestoes() throws ValidacaoException{
        logger.info("Executando t09: criticarSimuladoSemQuestoes");
        Simulado simulado = new Simulado();
        simulado.setComponenteCurricular(ccurricularServico.buscarComponentePorNome("Teste"));
        simulado.setProfessor(usuarioServico.buscarUsuarioPorEmail("teste@gmail.com"));
        simulado.setTitulo("Teste Simulado 9.");
        simulado.setDataCriacao(Calendar.getInstance().getTime());
        simulado.setQuestoes(null);
        
        simuladoServico.salvar(simulado);
    }
    
    @Test(expected = EJBException.class)
    public void t10_salvarSimuladoAlunoSemSimulado() throws ValidacaoException {
        logger.info("Executando t10: salvarSimuladoAlunoSemProva");
        SimuladoAluno simuladoAluno = new SimuladoAluno();
        preencherSimuladoAluno(simuladoAluno);
        simuladoAluno.setSimulado(null);

        simuladoServico.salvarSimuladoAluno(simuladoAluno);
    }

    @Test(expected = EJBException.class)
    public void t11_salvarSimuladoAlunoSemAluno() throws ValidacaoException {
        logger.info("Executando t11: salvarSimuladoAlunoSemAluno");
        SimuladoAluno simuladoAluno = new SimuladoAluno();
        preencherSimuladoAluno(simuladoAluno);
        simuladoAluno.setAluno(null);

        simuladoServico.salvarSimuladoAluno(simuladoAluno);
    }

    @Test(expected = EJBException.class)
    public void t12_salvarSimuladoAlunoSemDHInicio() throws ValidacaoException {
        logger.info("Executando t12: salvarSimuladoAlunoSemDHInicio");
        SimuladoAluno simuladoAluno = new SimuladoAluno();
        preencherSimuladoAluno(simuladoAluno);
        simuladoAluno.setDataHoraInicio(null);

        simuladoServico.salvarSimuladoAluno(simuladoAluno);
    }
    
    @Test
    public void t13_salvarSimuladoAluno() throws ValidacaoException, InterruptedException {
        logger.info("Executando t13: salvarSimuladoAluno");
        SimuladoAluno simuladoAluno = new SimuladoAluno();
        preencherSimuladoAluno(simuladoAluno);

        simuladoServico.salvarSimuladoAluno(simuladoAluno);
        
        assertTrue(simuladoAluno.getId() > 0);
    }
    
    @Test
    public void t14_listarResultadosSimuladoAluno() {
        logger.info("Executando t14: listarResultadosSimuladoAluno");
        Simulado simulado = simuladoServico.buscarSimuladoPorId(1l);
        List<SimuladoAluno> resultados = simuladoServico.buscarResultadosSimulado(simulado);

        assertTrue(!resultados.isEmpty());
    }
    
    private Simulado preencherNovoSimulado(Simulado simulado){
        simulado.setComponenteCurricular(ccurricularServico.buscarComponentePorNome("Teste"));
        simulado.setProfessor(usuarioServico.buscarUsuarioPorEmail("teste@gmail.com"));
        simulado.setTitulo("Teste Simulado.");
        simulado.setDataCriacao(Calendar.getInstance().getTime());
        
        FiltroQuestao filtro = new FiltroQuestao();
        
        filtro.setIdComponenteCurricular(1l);
        filtro.setTipo(TipoQuestaoEnum.VERDADEIRO_FALSO);
        
        List<Questao> questoes = questaoServico.buscarQuestoesPorFiltro(filtro);
        
        simulado.setQuestoes(questoes);
        
        return simulado;
    }
    
    private void preencherSimuladoAluno(SimuladoAluno simuladoAluno) {
        Usuario aluno = usuarioServico.buscarUsuarioPorEmail(EMAIL_TESTE);
        FiltroSimulado filtro = new FiltroSimulado();
        filtro.setIdComponenteCurricular(ccurricularServico.buscarComponentePorNome("Teste").getId());
        Simulado simulado = simuladoServico.buscarSimuladoPorFiltro(filtro).get(0);
        simuladoAluno.setAluno(aluno);
        simuladoAluno.setSimulado(simulado);
        simuladoAluno.setDataHoraInicio(Calendar.getInstance().getTime());
        simuladoAluno.setDataHoraFim(Calendar.getInstance().getTime());
        simuladoAluno.setQuestoesAluno(new ArrayList<SimuladoAlunoQuestao>());

        for (Questao questao : simulado.getQuestoes()) {
            SimuladoAlunoQuestao simuladoAlunoQuestao = new SimuladoAlunoQuestao();
            simuladoAlunoQuestao.setSimuladoAluno(simuladoAluno);
            simuladoAlunoQuestao.setQuestao(questao);
            simuladoAlunoQuestao.setRespostaVF(Boolean.TRUE);
            simuladoAluno.getQuestoesAluno().add(simuladoAlunoQuestao);
        }

    }
    
}
