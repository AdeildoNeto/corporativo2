/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.testes;

import br.edu.ifpe.recife.avalon.excecao.ValidacaoException;
import br.edu.ifpe.recife.avalon.model.questao.Alternativa;
import br.edu.ifpe.recife.avalon.model.questao.ComponenteCurricular;
import br.edu.ifpe.recife.avalon.model.questao.FiltroQuestao;
import br.edu.ifpe.recife.avalon.model.questao.MultiplaEscolha;
import br.edu.ifpe.recife.avalon.model.questao.Questao;
import br.edu.ifpe.recife.avalon.model.questao.TipoQuestaoEnum;
import br.edu.ifpe.recife.avalon.model.questao.VerdadeiroFalso;
import br.edu.ifpe.recife.avalon.model.usuario.GrupoEnum;
import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
import br.edu.ifpe.recife.avalon.servico.ComponenteCurricularServico;
import br.edu.ifpe.recife.avalon.servico.QuestaoServico;
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
public class QuestaoTest {

    private static EJBContainer container;

    @EJB
    private QuestaoServico questaoServico;

    @EJB
    private UsuarioServico usuarioServico;

    @EJB
    private ComponenteCurricularServico ccurricularServico;

    private static Logger logger;

    private static final String EMAIL_TESTE = "teste@gmail.com";
    private static final String EMAIL2 = "email2@email.com";

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
        questaoServico = (QuestaoServico) container.getContext().lookup("java:global/classes/QuestaoServico");
        usuarioServico = (UsuarioServico) container.getContext().lookup("java:global/classes/UsuarioServico");
        ccurricularServico = (ComponenteCurricularServico) container.getContext().lookup("java:global/classes/ComponenteCurricularServico");
    }

    @After
    public void tearDown() {
    }

    @Test
    public void t01_inserirQuestaoDiscursiva() throws ValidacaoException {

        logger.info("Executando t01: InserirQuestaoDiscursiva");

        Usuario usuario = new Usuario();

        usuario.setEmail(EMAIL2);
        usuario.setNome("TESTE2");
        usuario.setSobrenome("TESTE2");
        usuario.setGrupo(GrupoEnum.PROFESSOR);

        usuarioServico.salvar(usuario);

        ComponenteCurricular componente = ccurricularServico.buscarComponentePorNome("Teste");

        Questao questao = new Questao();

        questao.setComponenteCurricular(componente);
        questao.setEnunciado("Teste?");
        questao.setProfessor(usuario);
        questao.setTipo(TipoQuestaoEnum.DISCURSIVA);
        questao.setDataCriacao(Calendar.getInstance().getTime());

        questaoServico.salvar(questao);

        assertNotNull(questao.getId());

    }

    @Test
    public void t02_buscarQuestaoPorProfessor() {
        logger.info("Executando t02: buscarQuestaoPorProfessor");

        List<Questao> questao = questaoServico.buscarQuestoesPorProfessor(EMAIL2);

        assertNotNull(questao.get(0));

    }

    @Test
    public void t03_alterarQuestao() throws ValidacaoException {
        logger.info("Executando t03: alterarQuestao");
        List<Questao> questaoBuscada = questaoServico.buscarQuestoesPorProfessor(EMAIL2);

        Questao questao = questaoBuscada.get(0);

        questao.setEnunciado("Enunciado alterado");

        questaoServico.alterar(questao);

        questaoBuscada = questaoServico.buscarQuestoesPorProfessor(EMAIL2);
        Questao questao2 = questaoBuscada.get(0);

        assertEquals("Enunciado alterado", questao2.getEnunciado());

    }

    @Test
    public void t04_excluirQuestao() {
        logger.info("Executando t04: excluirQuestao");
        FiltroQuestao filtro = new FiltroQuestao();
        filtro.setNomeProfessor(usuarioServico.buscarUsuarioPorEmail(EMAIL2).getNome());
        filtro.setEmailUsuario(EMAIL_TESTE);
        List<Questao> questaoBuscada = questaoServico.buscarQuestoesPorProfessor(EMAIL2);
        
        
        questaoServico.anular(questaoBuscada.get(0));
        
        
        List<Questao> questaoRemovida = questaoServico.buscarQuestoesPorFiltro(filtro);

        assertEquals(0, questaoRemovida.size());

    }

    @Test(expected = EJBException.class)
    public void t05_criticarExclusaoUsuarioComQuestoes() {
        logger.info("Executando: t05 criticarExclusaoUsuarioComQuestoes");

        Usuario usuario = usuarioServico.buscarUsuarioPorEmail(EMAIL2);

        usuarioServico.remover(usuario);

    }

    @Test
    public void t06_inserirQuestaoMultiplaEscolha() throws ValidacaoException {

        logger.info("Executando t05: InserirQuestaoMultiplaEscolha");

        Usuario usuario = usuarioServico.buscarUsuarioPorEmail(EMAIL_TESTE);

        ComponenteCurricular componente = ccurricularServico.buscarComponentePorNome("Teste");

        MultiplaEscolha questao = new MultiplaEscolha();

        questao.setComponenteCurricular(componente);
        questao.setEnunciado("Teste?");
        questao.setProfessor(usuario);
        questao.setTipo(TipoQuestaoEnum.MULTIPLA_ESCOLHA);
        questao.setDataCriacao(Calendar.getInstance().getTime());
        List alternativas = new ArrayList();

        Alternativa alternativaA = new Alternativa();
        alternativaA.setDescricao("alternativa a");
        alternativaA.setQuestao(questao);
        alternativas.add(alternativaA);

        Alternativa alternativaB = new Alternativa();
        alternativaB.setDescricao("alternativa b");
        alternativaB.setQuestao(questao);
        alternativas.add(alternativaB);

        Alternativa alternativaC = new Alternativa();
        alternativaC.setDescricao("alternativa c");
        alternativaC.setQuestao(questao);
        alternativas.add(alternativaC);

        Alternativa alternativaD = new Alternativa();
        alternativaD.setDescricao("alternativa d");
        alternativaD.setQuestao(questao);
        alternativas.add(alternativaD);

        Alternativa alternativaE = new Alternativa();
        alternativaE.setDescricao("alternativa e");
        alternativaE.setQuestao(questao);
        alternativas.add(alternativaE);
        
        questao.setOpcaoCorreta(1);
        questao.setAlternativas(alternativas);
        questaoServico.salvar(questao);

        assertNotNull(questao.getId());

    }

    @Test
    public void t07_salvarQuestaoVF() throws ValidacaoException {
        logger.info("Executando t07: salvarQuestaoVF");

        VerdadeiroFalso questao = new VerdadeiroFalso();

        ComponenteCurricular componente = ccurricularServico.buscarComponentePorNome("Teste");

        Usuario usuario = usuarioServico.buscarUsuarioPorEmail(EMAIL_TESTE);

        questao.setProfessor(usuario);
        questao.setComponenteCurricular(componente);
        questao.setEnunciado("Questao V/F teste");
        questao.setTipo(TipoQuestaoEnum.VERDADEIRO_FALSO);
        questao.setDataCriacao(Calendar.getInstance().getTime());
        questao.setResposta(true);

        questaoServico.salvar(questao);

        assertNotNull(questao.getId());
    }

    @Test
    public void t08_selecionarQuestaoVF() {
        logger.info("Executando t08: selecionarQuestaoVF");
        FiltroQuestao filtro = new FiltroQuestao();
        filtro.setTipo(TipoQuestaoEnum.VERDADEIRO_FALSO);
        List<Questao> questoes = questaoServico.buscarQuestoesPorFiltro(filtro);

        assertTrue(questoes.size() > 1);
    }

    @Test
    public void t09_excluirQuestaoVF() {
        logger.info("Executando t09: excluirQuestaoVF");
        FiltroQuestao filtro = new FiltroQuestao();
        filtro.setTipo(TipoQuestaoEnum.VERDADEIRO_FALSO);
        List<Questao> questoes = questaoServico.buscarQuestoesPorFiltro(filtro);
        
        Questao questao = questoes.get(0);
        int sizeAnterior = questoes.size();
        System.out.println(sizeAnterior);
        
        questaoServico.anular(questao);

        questoes = questaoServico.buscarQuestoesPorFiltro(filtro);

        System.out.println(questoes.size());
        assertTrue(sizeAnterior - 1 == questoes.size());
    }

    @Test
    public void t10_editarQuestaoMultiplaEscolha() throws ValidacaoException {
        logger.info("Executando t10: editarQuestaoMultiplaEscolha");
        List<Questao> questoes = questaoServico.buscarQuestoesPorProfessor(EMAIL_TESTE);
        MultiplaEscolha questaoMp = new MultiplaEscolha();

        for (Questao questao : questoes) {
            if (TipoQuestaoEnum.MULTIPLA_ESCOLHA.equals(questao.getTipo())) {
                questaoMp = (MultiplaEscolha) questao;
            }
        }

        List<Alternativa> alternativas = questaoMp.getAlternativas();

        Alternativa alternativa = alternativas.get(1);

        alternativa.setDescricao("Alternativa alterada");

        alternativas.set(1, alternativa);

        questaoMp.setAlternativas(alternativas);

        questaoServico.alterar(questaoMp);

        for (Questao questao : questoes) {
            if (TipoQuestaoEnum.MULTIPLA_ESCOLHA.equals(questao.getTipo())) {
                questaoMp = (MultiplaEscolha) questao;
            }
        }

        alternativas = questaoMp.getAlternativas();
        alternativa = alternativas.get(1);

        assertEquals("Alternativa alterada", alternativa.getDescricao());
    }

    @Test(expected = EJBException.class)
    public void t11_criticarQuestaoSemProfessor() throws ValidacaoException {
        logger.info("Executando t11: criticarQuestaoSemProfessor");

        Questao questao = new Questao();
        ComponenteCurricular componente = ccurricularServico.buscarComponentePorNome("TESTE");

        questao.setComponenteCurricular(componente);
        questao.setEnunciado("Questao sem professor?");
        questao.setTipo(TipoQuestaoEnum.DISCURSIVA);

        questaoServico.salvar(questao);
    }

    @Test(expected = EJBException.class)
    public void t12_criticarQuestaoSemComponenteCurricular() throws ValidacaoException {
        logger.info("Executando t12: criticarQuestaoSemComponenteCurricular");

        Questao questao = new Questao();
        Usuario professor = usuarioServico.buscarUsuarioPorEmail(EMAIL_TESTE);

        questao.setProfessor(professor);
        questao.setEnunciado("Questao sem componente?");
        questao.setTipo(TipoQuestaoEnum.DISCURSIVA);

        questaoServico.salvar(questao);
    }

    @Test(expected = ValidacaoException.class)
    public void t13_criticarQuestaoRepetida() throws ValidacaoException {
        logger.info("Executando t13: criticarQuestaoRepetida");

        Usuario professor = usuarioServico.buscarUsuarioPorEmail(EMAIL_TESTE);
        ComponenteCurricular componente = ccurricularServico.buscarComponentePorNome("TESTE");
        Questao questao = new Questao();
        String enunciado = "Questao repetida?";

        questao.setProfessor(professor);
        questao.setComponenteCurricular(componente);
        questao.setEnunciado(enunciado);
        questao.setDataCriacao(Calendar.getInstance().getTime());
        questao.setTipo(TipoQuestaoEnum.DISCURSIVA);

        questaoServico.salvar(questao);

        Questao questao2 = new Questao();

        questao2.setProfessor(professor);
        questao2.setComponenteCurricular(componente);
        questao2.setEnunciado(enunciado);
        questao2.setDataCriacao(Calendar.getInstance().getTime());
        questao2.setTipo(TipoQuestaoEnum.DISCURSIVA);

        questaoServico.salvar(questao2);
    }

    @Test(expected = ValidacaoException.class)
    public void t14_criticarQuestaoRepetidaEdicao() throws ValidacaoException {
        logger.info("Executando t14: criticarQuestaoRepetidaEdicao");

        Usuario professor = usuarioServico.buscarUsuarioPorEmail(EMAIL_TESTE);
        ComponenteCurricular componente = ccurricularServico.buscarComponentePorNome("TESTE");
        String enunciado = "Questao repetida?";

        Questao questao = new Questao();

        questao.setProfessor(professor);
        questao.setComponenteCurricular(componente);
        questao.setEnunciado("questao x");
        questao.setDataCriacao(Calendar.getInstance().getTime());
        questao.setTipo(TipoQuestaoEnum.DISCURSIVA);

        questaoServico.salvar(questao);

        List<Questao> lista = questaoServico.buscarQuestoesPorProfessor(professor.getEmail());

        questao = lista.get(lista.size() - 1);

        questao.setEnunciado(enunciado);

        questaoServico.salvar(questao);
    }

    @Test(expected = ValidacaoException.class)
    public void t15_criticarAlternativasRepetidas() throws ValidacaoException {
        logger.info("Executando t15: criticarAlternativasRepetidas");

        MultiplaEscolha questao = new MultiplaEscolha();

        questao.setComponenteCurricular(ccurricularServico.buscarComponentePorNome("TESTE"));
        questao.setEnunciado("Teste?");
        questao.setProfessor(usuarioServico.buscarUsuarioPorEmail(EMAIL_TESTE));
        questao.setTipo(TipoQuestaoEnum.MULTIPLA_ESCOLHA);
        questao.setDataCriacao(Calendar.getInstance().getTime());
        List<Alternativa> alternativas = new ArrayList<>();

        alternativas.add(new Alternativa());
        alternativas.get(0).setDescricao("Teste");
        alternativas.get(0).setQuestao(questao);

        alternativas.add(new Alternativa());
        alternativas.get(1).setDescricao("Teste");
        alternativas.get(1).setQuestao(questao);

        questao.setAlternativas(alternativas);
        questao.setOpcaoCorreta(1);
        
        questaoServico.salvar(questao);

    }

    @Test
    public void t16_buscarQuestaoCompartilhadaProfessor() throws ValidacaoException {
        logger.info("Executando t16: buscarQuestaoCompartilhadaProfessor");
        FiltroQuestao filtro = new FiltroQuestao();
        Questao questao = new Questao();
        List<Questao> lista;
        Usuario professor = usuarioServico.buscarUsuarioPorEmail("user1@gmail.com");
        ComponenteCurricular componente = ccurricularServico.buscarComponentePorNome("TESTE");

        questao.setProfessor(professor);
        questao.setComponenteCurricular(componente);
        questao.setEnunciado("questao compartilhada?");
        questao.setDataCriacao(Calendar.getInstance().getTime());
        questao.setTipo(TipoQuestaoEnum.DISCURSIVA);

        questaoServico.salvar(questao);

        filtro.setTipo(TipoQuestaoEnum.DISCURSIVA);
        filtro.setEmailUsuario(professor.getEmail());
        filtro.setNomeProfessor(professor.getNome());

        lista = questaoServico.buscarQuestoesPorFiltro(filtro);

        assertEquals(1, lista.size());
    }

    @Test
    public void t17_buscarQuestaoCompartilhadaOutros() {
        logger.info("Executando t17: buscarQuestaoCompartilhadaOutros");
        FiltroQuestao filtro = new FiltroQuestao();
        List<Questao> lista;
        Usuario professor = usuarioServico.buscarUsuarioPorEmail("user1@gmail.com");

        filtro.setTipo(TipoQuestaoEnum.DISCURSIVA);
        filtro.setEmailUsuario("user2@gmail.com");
        filtro.setNomeProfessor(professor.getNome());

        lista = questaoServico.buscarQuestoesPorFiltro(filtro);

        assertEquals(1, lista.size());

    }

    @Test
    public void t18_buscarQuestaoNaoCompartilhadaProfessor() throws ValidacaoException {
        logger.info("Executando t18: buscarQuestaoNaoCompartilhadaProfessor");
        FiltroQuestao filtro = new FiltroQuestao();
        Questao questao = new Questao();
        List<Questao> lista;
        Usuario professor = usuarioServico.buscarUsuarioPorEmail("user1@gmail.com");
        ComponenteCurricular componente = ccurricularServico.buscarComponentePorNome("TESTE");

        questao.setProfessor(professor);
        questao.setComponenteCurricular(componente);
        questao.setEnunciado("questao não compartilhada?");
        questao.setDataCriacao(Calendar.getInstance().getTime());
        questao.setTipo(TipoQuestaoEnum.DISCURSIVA);
        questao.setCompartilhada(Boolean.FALSE);

        questaoServico.salvar(questao);

        filtro.setTipo(TipoQuestaoEnum.DISCURSIVA);
        filtro.setNomeProfessor(professor.getNome());
        filtro.setEmailUsuario(professor.getEmail());

        lista = questaoServico.buscarQuestoesPorFiltro(filtro);

        assertEquals(2, lista.size());
    }

    @Test
    public void t19_buscarQuestaoNaoCompartilhadaOutros() {
        logger.info("Executando t19: buscarQuestaoNaoCompartilhadaOutros");
        FiltroQuestao filtro = new FiltroQuestao();
        List<Questao> lista;
        Usuario professor = usuarioServico.buscarUsuarioPorEmail("user1@gmail.com");

        filtro.setTipo(TipoQuestaoEnum.DISCURSIVA);
        filtro.setEmailUsuario("user2@gmail.com");
        filtro.setNomeProfessor(professor.getNome());

        lista = questaoServico.buscarQuestoesPorFiltro(filtro);

        assertEquals(1, lista.size());
    }

    @Test
    public void t20_buscarQuestaoPorFiltroEnunciado() {
        logger.info("Executando t20: buscarQuestaoPorFiltroEnunciado");
        FiltroQuestao filtro = new FiltroQuestao();
        List<Questao> lista;

        filtro.setTipo(TipoQuestaoEnum.DISCURSIVA);
        filtro.setEnunciado("questao");
        filtro.setEmailUsuario("user2@gmail.com");

        lista = questaoServico.buscarQuestoesPorFiltro(filtro);

        assertEquals(3, lista.size());
    }

    @Test
    public void t21_buscarQuestaoPorFiltroProfessor() {
        logger.info("Executando t21: buscarQuestaoPorFiltroProfessor");
        FiltroQuestao filtro = new FiltroQuestao();
        List<Questao> lista;
        Usuario professor = usuarioServico.buscarUsuarioPorEmail("user1@gmail.com");

        filtro.setTipo(TipoQuestaoEnum.DISCURSIVA);
        filtro.setEmailUsuario("user2@gmail.com");
        filtro.setNomeProfessor(professor.getNome());

        lista = questaoServico.buscarQuestoesPorFiltro(filtro);

        assertEquals(1, lista.size());
    }

    @Test
    public void t22_buscarQuestaoPorFiltroComponenteCurricular() {
        logger.info("Executando t22: buscarQuestaoPorFiltroComponenteCurricular");
        FiltroQuestao filtro = new FiltroQuestao();
        List<Questao> lista;
        ComponenteCurricular componente = ccurricularServico.buscarComponentePorNome("TESTE");

        filtro.setTipo(TipoQuestaoEnum.DISCURSIVA);
        filtro.setIdComponenteCurricular(componente.getId());
        filtro.setEmailUsuario("user2@gmail.com");

        lista = questaoServico.buscarQuestoesPorFiltro(filtro);

        assertEquals(3, lista.size());
    }

    @Test(expected = EJBException.class)
    public void t23_criticarVFSemRespostaDefinida() throws ValidacaoException {
        logger.info("Executando t23: criticarVFSemRespostaDefinida");
        VerdadeiroFalso questao = new VerdadeiroFalso();

        ComponenteCurricular componente = ccurricularServico.buscarComponentePorNome("Teste");

        Usuario usuario = usuarioServico.buscarUsuarioPorEmail(EMAIL_TESTE);

        questao.setProfessor(usuario);
        questao.setComponenteCurricular(componente);
        questao.setEnunciado("Questao V/F teste");
        questao.setTipo(TipoQuestaoEnum.VERDADEIRO_FALSO);
        questao.setDataCriacao(Calendar.getInstance().getTime());

        questaoServico.salvar(questao);
    }

}
