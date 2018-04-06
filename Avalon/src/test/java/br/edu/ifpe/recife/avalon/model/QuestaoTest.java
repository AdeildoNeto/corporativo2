/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.model;

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

/**
 *
 * @author eduardo.f.amaral
 */
public class QuestaoTest {
    
    private static EntityManagerFactory emf;
    private static Logger logger;
    private EntityManager em;
    private EntityTransaction et;
    private static long idQuestao;
    
    
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
        if(emf != null){
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
    public void t01_inserirQuestaoValida() {
        logger.info("Executando t01: inserirQuestaoValida");

        Questao questao = new Questao();
        
        questao.setEnunciado("Teste?");

        em.persist(questao);
        em.flush();

        assertNotNull(questao.getId());
        idQuestao = questao.getId();
        logger.log(Level.INFO, "Questão {0} incluída com sucesso.", questao.getId());

    }

    @Test
    public void t02_atualizarQuestaoValida() {
        logger.info("Executando t02: atualizarQuestaoValida");

        TypedQuery<Questao> query = em.createNamedQuery("Questao.PorId", Questao.class);
        query.setParameter("id", idQuestao);

        Questao questao = (Questao) query.getSingleResult();
        assertNotNull(query);

        questao.setEnunciado("Teste 2?");
        em.flush();
        assertEquals(0, query.getResultList().size());
    }

    @Test
    public void t03_selecionarQuestaoPorId() {
        logger.info("Executando t03: selecionarQuestaoPorId");

        TypedQuery<Questao> query = em.createNamedQuery("Questao.PorId", Questao.class);
        query.setParameter("id", idQuestao);

        assertEquals(1, query.getResultList().size());
    }

    @Test
    public void t04_removerQuestaoValida() {
        logger.info("Executando t04: removerQuestaoValida");

        TypedQuery<Questao> query = em.createNamedQuery("Questao.PorId", Questao.class);
        query.setParameter("id", idQuestao);

        Questao questao = (Questao) query.getSingleResult();
        
        em.remove(questao);
        em.flush();
        assertEquals(0, query.getResultList().size());
    }

}
