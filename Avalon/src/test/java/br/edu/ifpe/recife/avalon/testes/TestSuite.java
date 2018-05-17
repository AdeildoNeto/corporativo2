package br.edu.ifpe.recife.avalon.testes;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Suite de testes.
 * 
 * @author eduardoamaral
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    ComponenteCurricularTest.class,
    QuestaoTest.class,
    SimuladoTest.class}
)
public class TestSuite {

    @BeforeClass
    public static void setUpClass() throws Exception{
    
    }
    
    @AfterClass
    public static void tearDownClass() throws Exception{
        
    }
    
    @Before
    public void setUp() throws Exception{
    
    }
    
    @After
    public void tearDown() throws Exception{
    }
}
