/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.junit;

import br.edu.ifpe.recife.avalon.cucumber.util.DbUnitUtil;
import br.edu.ifpe.recife.avalon.excecao.ValidacaoException;
import br.edu.ifpe.recife.avalon.model.questao.componente.ComponenteCurricular;
import br.edu.ifpe.recife.avalon.servico.ComponenteCurricularServico;
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
public class ComponenteCurricularTest {
    
    private static String NOME_COMPONENTE = "Engenharia de Software";
    
    private static EJBContainer container;
    
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
        ccurricularServico = (ComponenteCurricularServico) container.getContext().lookup("java:global/classes/ComponenteCurricularServico");
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void t01_inserirComponenteCurricular() throws ValidacaoException{
        logger.info("Executando t01: inserirComponenteCurricular");
        ComponenteCurricular componente = new ComponenteCurricular();
        
        componente.setNome("Teste");
        
        ccurricularServico.salvar(componente);
        
        assertNotNull(componente.getId());
    }
    
    @Test(expected = ValidacaoException.class)
    public void t02_criticarComponenteCurricularRepetido() throws ValidacaoException{
        logger.info("Executando t02: criticarComponenteCurricularRepetido");
        ComponenteCurricular componente =  new ComponenteCurricular();
        
        componente.setNome(NOME_COMPONENTE);
        
        ccurricularServico.salvar(componente);
    }
    
    @Test
    public void t03_buscarTodosComponentesCurricular(){
        logger.info("Executando t03: buscarTodosComponentesCurricular");
        List<ComponenteCurricular> lista = ccurricularServico.buscarTodosComponentes();
        
        assertTrue(!lista.isEmpty());
    }
    
    @Test
    public void t04_buscarComponentesCurricularPorNome(){
        logger.info("Executando t04: buscarComponentesCurricularPorNome");
        ComponenteCurricular componente = ccurricularServico.buscarComponentePorNome(NOME_COMPONENTE);
        
        assertNotNull(componente);
    }
    
}
