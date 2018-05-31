/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.testes;

import br.edu.ifpe.recife.avalon.excecao.ValidacaoException;
import br.edu.ifpe.recife.avalon.model.prova.Prova;
import br.edu.ifpe.recife.avalon.model.questao.FiltroQuestao;
import br.edu.ifpe.recife.avalon.model.questao.Questao;
import br.edu.ifpe.recife.avalon.model.questao.TipoQuestaoEnum;
import br.edu.ifpe.recife.avalon.model.simulado.FiltroSimulado;
import br.edu.ifpe.recife.avalon.model.simulado.Simulado;
import br.edu.ifpe.recife.avalon.servico.ComponenteCurricularServico;
import br.edu.ifpe.recife.avalon.servico.ProvaServico;
import br.edu.ifpe.recife.avalon.servico.QuestaoServico;
import br.edu.ifpe.recife.avalon.servico.UsuarioServico;
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
public class ProvaTest {
    
    private static EJBContainer container;
    
    @EJB
    private ProvaServico provaServico;
    
    @EJB
    private QuestaoServico questaoServico;

    @EJB
    private UsuarioServico usuarioServico;

    @EJB
    private ComponenteCurricularServico ccurricularServico;

    private static Logger logger;
    
    public ProvaTest() {
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
        provaServico = (ProvaServico) container.getContext().lookup("java:global/classes/ProvaServico");
        questaoServico = (QuestaoServico) container.getContext().lookup("java:global/classes/QuestaoServico");
        usuarioServico = (UsuarioServico) container.getContext().lookup("java:global/classes/UsuarioServico");
        ccurricularServico = (ComponenteCurricularServico) container.getContext().lookup("java:global/classes/ComponenteCurricularServico");
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void t01_inserirProva() throws ValidacaoException{
        logger.info("Executando t01: inserirProva");
        Prova prova = new Prova();
        preencherNovaProva(prova);
        
        provaServico.salvar(prova);
        
        assertNotNull(prova.getId());
    }
    
    @Test
    public void t02_buscarPorDisponibilidade(){
        logger.info("Executando t02: buscarPorDisponibilidade");
        FiltroSimulado filtro = new FiltroSimulado();
        
        filtro.setIdComponenteCurricular(1l);
        
        List<Prova> lista = provaServico.buscarProvasDisponiveis();
        
        assertTrue(!lista.isEmpty());
    }
    
    @Test(expected = ValidacaoException.class)
    public void t03_criticarProvaSemQuestoes() throws ValidacaoException{
        logger.info("Executando t03: criticarProvaSemQuestoes");
        Prova prova = new Prova();
        preencherNovaProva(prova);
        prova.setQuestoes(null);
        
        provaServico.salvar(prova);
    }
    
    @Test(expected = EJBException.class)
    public void t04_criticarProvaSemTitulo() throws ValidacaoException{
        logger.info("Executando t04: criticarProvaSemTitulo");
        Prova prova = new Prova();
        
        prova = preencherNovaProva(prova);
        prova.setTitulo(null);
        
        provaServico.salvar(prova);
    }
    
    @Test(expected = EJBException.class)
    public void t05_criticarProvaSemProfessor() throws ValidacaoException{
        logger.info("Executando t05: criticarProvaSemProfessor");
        Prova prova = new Prova();
        prova = preencherNovaProva(prova);
        prova.setProfessor(null);
        
        provaServico.salvar(prova);
    }
    
    @Test(expected = EJBException.class)
    public void t06_criticarProvaSemComponenteCurricular() throws ValidacaoException{
        logger.info("Executando t06: criticarProvaSemComponenteCurricular");
        Prova prova = new Prova();
        prova = preencherNovaProva(prova);
        prova.setComponenteCurricular(null);
        
        provaServico.salvar(prova);
    }
    
    @Test(expected = EJBException.class)
    public void t07_criticarProvaSemDataInicio() throws ValidacaoException{
        logger.info("Executando t07: criticarProvaSemDataInicio");
        Prova prova = new Prova();
        prova = preencherNovaProva(prova);
        prova.setDataHoraInicio(null);
        
        provaServico.salvar(prova);
    }
    
    @Test(expected = EJBException.class)
    public void t08_criticarProvaSemDataFim() throws ValidacaoException{
        logger.info("Executando t08: criticarProvaSemDataFim");
        Prova prova = new Prova();
        prova = preencherNovaProva(prova);
        prova.setDataHoraFim(null);
        
        provaServico.salvar(prova);
    }
    
    private Prova preencherNovaProva(Prova prova){
        Calendar calendar = Calendar.getInstance();
        
        prova.setComponenteCurricular(ccurricularServico.buscarComponentePorNome("Teste"));
        prova.setProfessor(usuarioServico.buscarUsuarioPorEmail("teste@gmail.com"));
        prova.setTitulo("Teste Simulado.");
        prova.setDataCriacao(calendar.getTime());
        prova.setDataHoraInicio(calendar.getTime());
        calendar.add(Calendar.HOUR, 1);
        prova.setDataHoraFim(calendar.getTime());
        prova.setTempoMaximo(60l);
        
        FiltroQuestao filtro = new FiltroQuestao();
        
        filtro.setIdComponenteCurricular(1l);
        filtro.setTipo(TipoQuestaoEnum.VERDADEIRO_FALSO);
        
        List<Questao> questoes = questaoServico.buscarQuestoesPorFiltro(filtro);
        
        prova.setQuestoes(questoes);
        
        return prova;
    }
    
}
