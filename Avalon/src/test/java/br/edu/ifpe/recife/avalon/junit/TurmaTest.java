/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.junit;

import br.edu.ifpe.recife.avalon.cucumber.util.DbUnitUtil;
import br.edu.ifpe.recife.avalon.excecao.ValidacaoException;
import br.edu.ifpe.recife.avalon.model.turma.Turma;
import br.edu.ifpe.recife.avalon.servico.TurmaServico;
import br.edu.ifpe.recife.avalon.servico.UsuarioServico;
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
public class TurmaTest {

    private static final String EMAIL_TESTE = "teste@gmail.com";

    private static EJBContainer container;

    @EJB
    private UsuarioServico usuarioServico;

    @EJB
    private TurmaServico turmaServico;

    private static Logger logger;

    public TurmaTest() {
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
        turmaServico = (TurmaServico) container.getContext().lookup("java:global/classes/TurmaServico");
        usuarioServico = (UsuarioServico) container.getContext().lookup("java:global/classes/UsuarioServico");
    }

    @After
    public void tearDown() {
    }

    @Test
    public void t01_inserirTurma() throws ValidacaoException {
        logger.info("Executando t01_inserirTurma");
        Turma turma = preencherTurma();

        turmaServico.salvar(turma);
        assertNotNull(turma.getId());
    }

    @Test(expected = EJBException.class)
    public void t02_criticarTurmaSemNome() throws ValidacaoException {
        logger.info("Executando t02_criticarTurmaSemNome");
        Turma turma = preencherTurma();
        turma.setNome(null);

        turmaServico.salvar(turma);
    }
    
    @Test(expected = EJBException.class)
    public void t03_criticarTurmaSemSemestreAno() throws ValidacaoException {
        logger.info("Executando t03_criticarTurmaSemSemestreAno");
        Turma turma = preencherTurma();
        turma.setSemestreAno(null);

        turmaServico.salvar(turma);
    }
    
    @Test(expected = EJBException.class)
    public void t04_criticarTurmaSemProfessor() throws ValidacaoException {
        logger.info("Executando t04_criticarTurmaSemProfessor");
        Turma turma = preencherTurma();
        turma.setProfessor(null);

        turmaServico.salvar(turma);
    }
    
    @Test(expected = ValidacaoException.class)
    public void t05_criticarTurmaSemAlunos() throws ValidacaoException {
        logger.info("Executando t05_criticarTurmaSemAlunos");
        Turma turma = preencherTurma();
        turma.setAlunos(null);

        turmaServico.salvar(turma);
    }

    private Turma preencherTurma(){
        Turma turma = new Turma();
        turma.setNome("Turma 1");
        turma.setSemestreAno("01/2001");
        turma.setProfessor(usuarioServico.buscarUsuarioPorEmail(EMAIL_TESTE));
        turma.setAlunos(usuarioServico.buscarAlunos());
        
        return turma;
    }
}
