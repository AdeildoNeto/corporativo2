package br.edu.ifpe.recife.avalon.testes;

import br.edu.ifpe.recife.avalon.model.QuestaoTest;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({QuestaoTest.class})
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
