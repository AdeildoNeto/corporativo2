/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.junit;

import br.edu.ifpe.recife.avalon.cucumber.util.DbUnitUtil;
import br.edu.ifpe.recife.avalon.excecao.ValidacaoException;
import br.edu.ifpe.recife.avalon.model.turma.Turma;
import br.edu.ifpe.recife.avalon.model.usuario.GrupoEnum;
import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
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
public class UsuarioTest {

    private static final String EMAIL_TESTE = "usuario@usuario.com";

    private static EJBContainer container;

    @EJB
    private UsuarioServico usuarioServico;

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
        usuarioServico = (UsuarioServico) container.getContext().lookup("java:global/classes/UsuarioServico");
    }

    @After
    public void tearDown() {
    }

    @Test
    public void t01_inserirUsuario() throws ValidacaoException {
        logger.info("Executando t01_inserirUsuario");
        Usuario usuario = getUsuarioPreenchido();

        usuarioServico.salvar(usuario);
        assertNotNull(usuario.getId());
    }

    @Test(expected = EJBException.class)
    public void t02_criticarUsuarioSemNome() throws ValidacaoException {
        logger.info("Executando t02_criticarUsuarioSemNome");
        Usuario usuario = getUsuarioPreenchido();
        usuario.setNome(null);
        
        usuarioServico.salvar(usuario);
    }
    
    @Test(expected = EJBException.class)
    public void t03_criticarUsuarioSemSobrenome() throws ValidacaoException {
        logger.info("Executando t03_criticarUsuarioSemSobrenome");
        Usuario usuario = getUsuarioPreenchido();
        usuario.setSobrenome(null);
        
        usuarioServico.salvar(usuario);
    }
    
    @Test(expected = EJBException.class)
    public void t04_criticarUsuarioSemEmail() throws ValidacaoException {
        logger.info("Executando t04_criticarUsuarioSemEmail");
        Usuario usuario = getUsuarioPreenchido();
        usuario.setEmail(null);
        
        usuarioServico.salvar(usuario);
    }
    
    @Test(expected = EJBException.class)
    public void t05_criticarUsuarioSemGrupo() throws ValidacaoException {
        logger.info("Executando t05_criticarUsuarioSemGrupo");
        Usuario usuario = getUsuarioPreenchido();
        usuario.setGrupo(null);
        
        usuarioServico.salvar(usuario);
    }
    
    @Test
    public void t06_consultarUsuarioPorEmail() throws ValidacaoException {
        logger.info("Executando t06_consultarUsuarioPorEmail");
        Usuario usuario = usuarioServico.buscarUsuarioPorEmail("teste@gmail.com");
        
        assertNotNull(usuario.getId());
    }
    
    private Usuario getUsuarioPreenchido(){
        Usuario usuario = new Usuario();
        usuario.setEmail(EMAIL_TESTE);
        usuario.setGrupo(GrupoEnum.ALUNO);
        usuario.setNome("Teste");
        usuario.setSobrenome("Usuario");
        
        return usuario;
    }
}
