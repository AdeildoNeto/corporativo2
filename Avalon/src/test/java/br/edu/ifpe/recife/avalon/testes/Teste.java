package br.edu.ifpe.recife.avalon.testes;

import br.ifpe.avalon.testesEjb.DbUnitUtil;
import javax.ejb.embeddable.EJBContainer;
import org.junit.AfterClass;
import static org.junit.Assert.assertNotNull;
import org.junit.BeforeClass;

public class Teste {

    protected static EJBContainer container;

    @BeforeClass
    public static void setUpClass() {
        container = EJBContainer.createEJBContainer();
        assertNotNull(container);
        //DbUnitUtil.inserirDados();
    }

    @AfterClass
    public static void tearDownClass() {

        container.close();
    }

}
