/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.model;

import br.edu.ifpe.recife.avalon.model.questao.Questao;
import br.edu.ifpe.recife.avalon.model.questao.TipoQuestaoEnum;
import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

/**
 *
 * @author eduardo.f.amaral
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class QuestaoTest {

    /*private static final int QTDE_QUESTOES_DISCURSIVAS = 1;
    
    private static EntityManagerFactory emf;
    private static Logger logger;
    private EntityManager em;
    private EntityTransaction et;
    private static long idQuestao;
    private static long idAutor;

    public QuestaoTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        logger = Logger.getGlobal();
        logger.setLevel(Level.INFO);
        emf = Persistence.createEntityManagerFactory("avalondb");
    }

    @AfterClass
    public static void tearDownClass() {
        if (emf != null) {
            emf.close();
        }
    }

    @Before
    public void setUp() {
        em = emf.createEntityManager();
        et = em.getTransaction();
        et.begin();
    }

    @After
    public void tearDown() {
        try {
            et.commit();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage());

            if (et.isActive()) {
                et.rollback();
            }
        } finally {
            em.close();
            em = null;
            et = null;
        }
    }

    @Test
    public void t01_inserirQuestaoDiscursiva() {
        logger.info("Executando t01: inserirQuestaoDiscursiva");

        Usuario usuario = new Usuario();

        usuario.setEmail("email@email.com");
        usuario.setNome("TESTE");
        usuario.setSenha("TESTE");
        usuario.setSobrenome("TESTE");

        em.persist(usuario);
        em.flush();

        Questao questao = new Questao();

        questao.setEnunciado("Teste?");
        questao.setAutor(usuario);
        questao.setTipo(TipoQuestaoEnum.DISCURSIVA);
        questao.setDataCriacao(Calendar.getInstance().getTime());

        em.persist(questao);
        em.flush();

        assertNotNull(questao.getId());
        idAutor = usuario.getId();
        idQuestao = questao.getId();
        logger.log(Level.INFO, "Questão {0} incluída com sucesso.", questao.getId());

    }

    @Test
    public void t02_buscarQuestaoPorId() {
        logger.info("Executando t02: buscarQuestaoPorId");

        TypedQuery<Questao> query = em.createNamedQuery("Questao.PorId", Questao.class);
        query.setParameter("id", idQuestao);

        assertEquals(1, query.getResultList().size());
    }
    
    @Test
    public void t03_buscarQuestoesDiscursivas() {
        logger.info("Executando t03: buscarQuestoesDiscursivas");

        TypedQuery<Questao> query = em.createNamedQuery("Questao.PorTipo", Questao.class);
        query.setParameter("tipo", TipoQuestaoEnum.DISCURSIVA);

        assertEquals(QTDE_QUESTOES_DISCURSIVAS, query.getResultList().size());
    }
    
    @Test
    public void t04_buscarQuestoesPorAutor() {
        logger.info("Executando t04: buscarQuestoesPorAutor");

        TypedQuery<Questao> query = em.createNamedQuery("Questao.PorAutor", Questao.class);
        query.setParameter("idAutor", idAutor);

        assertEquals(QTDE_QUESTOES_DISCURSIVAS, query.getResultList().size());        
    }
    
    @Test
    public void t05_buscarQuestoesDiscursivasPorAutor() {
        logger.info("Executando t05: buscarQuestoesDiscursivasPorAutor");

        TypedQuery<Questao> query = em.createNamedQuery("Questao.PorAutorTipo", Questao.class);
        query.setParameter("idAutor", idAutor);
        query.setParameter("tipo", TipoQuestaoEnum.DISCURSIVA);

        assertEquals(QTDE_QUESTOES_DISCURSIVAS, query.getResultList().size());        
    }

    @Test
    public void t06_atualizarQuestaoValida() {
        logger.info("Executando t06: atualizarQuestaoValida");

        TypedQuery<Questao> query = em.createNamedQuery("Questao.PorId", Questao.class);
        query.setParameter("id", idQuestao);

        Questao questao = (Questao) query.getSingleResult();
        assertNotNull(questao);

        questao.setEnunciado("Teste 2?");
        em.merge(questao);
        assertEquals(1, query.getResultList().size());
    }

    @Test
    public void t07_removerQuestaoValida() {
        logger.info("Executando t07: removerQuestaoValida");

        TypedQuery<Questao> query = em.createNamedQuery("Questao.PorId", Questao.class);
        query.setParameter("id", idQuestao);

        Questao questao = (Questao) query.getSingleResult();

        assertNotNull(questao);

        em.remove(questao);
        em.flush();
        em.clear();

        assertEquals(0, query.getResultList().size());
    }
*/
}
