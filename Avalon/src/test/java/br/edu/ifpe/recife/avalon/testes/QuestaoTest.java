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

        usuario.setEmail("email2@email.com");
        usuario.setNome("TESTE2");
        usuario.setSobrenome("TESTE2");
        usuario.setGrupo(GrupoEnum.PROFESSOR);

        usuarioServico.salvar(usuario);

        ComponenteCurricular componente = ccurricularServico.buscarComponentePorNome("Teste");

        Questao questao = new Questao();

        questao.setComponenteCurricular(componente);
        questao.setEnunciado("Teste?");
        questao.setCriador(usuario);
        questao.setTipo(TipoQuestaoEnum.DISCURSIVA);
        questao.setDataCriacao(Calendar.getInstance().getTime());

        questaoServico.salvar(questao);

        assertNotNull(questao.getId());

    }

    @Test
    public void t02_buscarQuestaoPorCriador() {
        logger.info("Executando t02: buscarQuestaoPorCriador");

        List<Questao> questao = questaoServico.buscarQuestoesPorCriador("email2@email.com");

        assertNotNull(questao.get(0));

    }

    @Test
    public void t03_alterarQuestao() throws ValidacaoException {
        logger.info("Executando t03: AlterarQuestão");
        List<Questao> questaoBuscada = questaoServico.buscarQuestoesPorCriador("email2@email.com");

        Questao questao = questaoBuscada.get(0);

        questao.setEnunciado("Enunciado alterado");

        questaoServico.alterar(questao);

        questaoBuscada = questaoServico.buscarQuestoesPorCriador("email2@email.com");
        Questao questao2 = questaoBuscada.get(0);

        assertEquals("Enunciado alterado", questao2.getEnunciado());

    }

    @Test
    public void t04_excluirQuestao() {
        logger.info("Executando t03: ExcluirQuestão");

        List<Questao> questaoBuscada = questaoServico.buscarQuestoesPorCriador("email2@email.com");

        questaoServico.remover(questaoBuscada.get(0));

        List<Questao> questaoRemovida = questaoServico.buscarQuestoesPorCriador("email2@email.com");

        assertEquals(0, questaoRemovida.size());

    }

    @Test(expected = EJBException.class)
    public void t05_criticarExclusaoUsuarioComQuestoes() {
        logger.info("Executando: T04 Excluir usuário");

        Usuario usuario = usuarioServico.buscarUsuarioPorEmail("email2@email.com");

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
        questao.setCriador(usuario);
        questao.setTipo(TipoQuestaoEnum.MULTIPLA_ESCOLHA);
        questao.setDataCriacao(Calendar.getInstance().getTime());
        List alternativas = new ArrayList();

        Alternativa alternativaA = new Alternativa();
        alternativaA.setAlternativa("alternativa a");
        alternativaA.setQuestao(questao);
        alternativas.add(alternativaA);

        Alternativa alternativaB = new Alternativa();
        alternativaB.setAlternativa("alternativa b");
        alternativaB.setQuestao(questao);
        alternativas.add(alternativaB);

        Alternativa alternativaC = new Alternativa();
        alternativaC.setAlternativa("alternativa c");
        alternativaC.setQuestao(questao);
        alternativas.add(alternativaC);

        Alternativa alternativaD = new Alternativa();
        alternativaD.setAlternativa("alternativa d");
        alternativaD.setQuestao(questao);
        alternativas.add(alternativaD);

        Alternativa alternativaE = new Alternativa();
        alternativaE.setAlternativa("alternativa e");
        alternativaE.setQuestao(questao);
        alternativas.add(alternativaE);

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

        questao.setCriador(usuario);
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
        List<Questao> questoes = questaoServico.buscarQuestoesPorCriadorTipo(EMAIL_TESTE, TipoQuestaoEnum.VERDADEIRO_FALSO);

        assertEquals(1, questoes.size());
    }

    @Test
    public void t09_excluirQuestaoVF() {
        logger.info("Executando t09: excluirQuestaoVF");
        List<Questao> questoes = questaoServico.buscarQuestoesPorCriadorTipo(EMAIL_TESTE, TipoQuestaoEnum.VERDADEIRO_FALSO);

        Questao questao = questoes.get(0);

        questaoServico.remover(questao);

        questoes = questaoServico.buscarQuestoesPorCriadorTipo(EMAIL_TESTE, TipoQuestaoEnum.VERDADEIRO_FALSO);

        assertEquals(0, questoes.size());
    }

    @Test
    public void t10_editarQuestaoMultiplaEscolha() throws ValidacaoException {
        logger.info("Executando t10: editarQuestaoMultiplaEscolha");
        List<Questao> questoes = questaoServico.buscarQuestoesPorCriador(EMAIL_TESTE);
        MultiplaEscolha questaoMp = new MultiplaEscolha();

        for (Questao questao : questoes) {
            if (TipoQuestaoEnum.MULTIPLA_ESCOLHA.equals(questao.getTipo())) {
                questaoMp = (MultiplaEscolha) questao;
            }
        }

        List<Alternativa> alternativas = questaoMp.getAlternativas();

        Alternativa alternativa = alternativas.get(1);

        alternativa.setAlternativa("Alternativa alterada");

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

        assertEquals("Alternativa alterada", alternativa.getAlternativa());
    }

    @Test(expected = EJBException.class)
    public void t11_criticarQuestaoSemCriador() throws ValidacaoException {
        logger.info("Executando t11: criticarQuestaoSemCriador");

        Questao questao = new Questao();
        ComponenteCurricular componente = ccurricularServico.buscarComponentePorNome("TESTE");

        questao.setComponenteCurricular(componente);
        questao.setEnunciado("Questao sem criador?");
        questao.setTipo(TipoQuestaoEnum.DISCURSIVA);

        questaoServico.salvar(questao);
    }

    @Test(expected = EJBException.class)
    public void t12_criticarQuestaoSemComponenteCurricular() throws ValidacaoException {
        logger.info("Executando t12: criticarQuestaoSemComponenteCurricular");

        Questao questao = new Questao();
        Usuario criador = usuarioServico.buscarUsuarioPorEmail(EMAIL_TESTE);

        questao.setCriador(criador);
        questao.setEnunciado("Questao sem componente?");
        questao.setTipo(TipoQuestaoEnum.DISCURSIVA);

        questaoServico.salvar(questao);
    }

    @Test(expected = ValidacaoException.class)
    public void t13_criticarQuestaoRepetida() throws ValidacaoException {
        logger.info("Executando t13: criticarQuestaoRepetida");

        Usuario criador = usuarioServico.buscarUsuarioPorEmail(EMAIL_TESTE);
        ComponenteCurricular componente = ccurricularServico.buscarComponentePorNome("TESTE");
        Questao questao = new Questao();
        String enunciado = "Questao repetida?";

        questao.setCriador(criador);
        questao.setComponenteCurricular(componente);
        questao.setEnunciado(enunciado);
        questao.setDataCriacao(Calendar.getInstance().getTime());
        questao.setTipo(TipoQuestaoEnum.DISCURSIVA);

        questaoServico.salvar(questao);

        Questao questao2 = new Questao();

        questao2.setCriador(criador);
        questao2.setComponenteCurricular(componente);
        questao2.setEnunciado(enunciado);
        questao2.setDataCriacao(Calendar.getInstance().getTime());
        questao2.setTipo(TipoQuestaoEnum.DISCURSIVA);

        questaoServico.salvar(questao2);
    }

    @Test(expected = ValidacaoException.class)
    public void t14_criticarQuestaoRepetidaEdicao() throws ValidacaoException {
        logger.info("Executando t14: criticarQuestaoRepetidaEdicao");

        Usuario criador = usuarioServico.buscarUsuarioPorEmail(EMAIL_TESTE);
        ComponenteCurricular componente = ccurricularServico.buscarComponentePorNome("TESTE");
        String enunciado = "Questao repetida?";

        Questao questao = new Questao();

        questao.setCriador(criador);
        questao.setComponenteCurricular(componente);
        questao.setEnunciado("questao x");
        questao.setDataCriacao(Calendar.getInstance().getTime());
        questao.setTipo(TipoQuestaoEnum.DISCURSIVA);

        questaoServico.salvar(questao);

        List<Questao> lista = questaoServico.buscarQuestoesPorCriador(criador.getEmail());

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
        questao.setCriador(usuarioServico.buscarUsuarioPorEmail(EMAIL_TESTE));
        questao.setTipo(TipoQuestaoEnum.MULTIPLA_ESCOLHA);
        questao.setDataCriacao(Calendar.getInstance().getTime());
        List<Alternativa> alternativas = new ArrayList<>();

        alternativas.add(new Alternativa());
        alternativas.get(0).setAlternativa("Teste");
        alternativas.get(0).setQuestao(questao);

        alternativas.add(new Alternativa());
        alternativas.get(1).setAlternativa("Teste");
        alternativas.get(1).setQuestao(questao);

        questao.setAlternativas(alternativas);
        
        questaoServico.salvar(questao);

    }

    @Test
    public void t16_buscarQuestaoCompartilhadaCriador() throws ValidacaoException {
        logger.info("Executando t16: buscarQuestaoCompartilhadaCriador");
        FiltroQuestao filtro = new FiltroQuestao();
        Questao questao = new Questao();
        List<Questao> lista;
        Usuario criador = usuarioServico.buscarUsuarioPorEmail("user1@gmail.com");
        ComponenteCurricular componente = ccurricularServico.buscarComponentePorNome("TESTE");

        questao.setCriador(criador);
        questao.setComponenteCurricular(componente);
        questao.setEnunciado("questao compartilhada?");
        questao.setDataCriacao(Calendar.getInstance().getTime());
        questao.setTipo(TipoQuestaoEnum.DISCURSIVA);

        questaoServico.salvar(questao);

        filtro.setTipo(TipoQuestaoEnum.DISCURSIVA);
        filtro.setEmailUsuario(criador.getEmail());
        filtro.setNomeCriador(criador.getNome());

        lista = questaoServico.buscarQuestoesPorFiltro(filtro);

        assertEquals(1, lista.size());
    }

    @Test
    public void t17_buscarQuestaoCompartilhadaOutros() {
        logger.info("Executando t17: buscarQuestaoCompartilhadaOutros");
        FiltroQuestao filtro = new FiltroQuestao();
        List<Questao> lista;
        Usuario criador = usuarioServico.buscarUsuarioPorEmail("user1@gmail.com");

        filtro.setTipo(TipoQuestaoEnum.DISCURSIVA);
        filtro.setEmailUsuario("user2@gmail.com");
        filtro.setNomeCriador(criador.getNome());

        lista = questaoServico.buscarQuestoesPorFiltro(filtro);

        assertEquals(1, lista.size());

    }

    @Test
    public void t18_buscarQuestaoNaoCompartilhadaCriador() throws ValidacaoException {
        logger.info("Executando t18: buscarQuestaoNaoCompartilhadaCriador");
        FiltroQuestao filtro = new FiltroQuestao();
        Questao questao = new Questao();
        List<Questao> lista;
        Usuario criador = usuarioServico.buscarUsuarioPorEmail("user1@gmail.com");
        ComponenteCurricular componente = ccurricularServico.buscarComponentePorNome("TESTE");

        questao.setCriador(criador);
        questao.setComponenteCurricular(componente);
        questao.setEnunciado("questao não compartilhada?");
        questao.setDataCriacao(Calendar.getInstance().getTime());
        questao.setTipo(TipoQuestaoEnum.DISCURSIVA);
        questao.setCompartilhada(Boolean.FALSE);
        
        questaoServico.salvar(questao);

        filtro.setTipo(TipoQuestaoEnum.DISCURSIVA);
        filtro.setNomeCriador(criador.getNome());
        filtro.setEmailUsuario(criador.getEmail());

        lista = questaoServico.buscarQuestoesPorFiltro(filtro);

        assertEquals(2, lista.size());
    }

    @Test
    public void t19_buscarQuestaoNaoCompartilhadaOutros() {
        logger.info("Executando t19: buscarQuestaoNaoCompartilhadaOutros");
        FiltroQuestao filtro = new FiltroQuestao();
        List<Questao> lista;
        Usuario criador = usuarioServico.buscarUsuarioPorEmail("user1@gmail.com");

        filtro.setTipo(TipoQuestaoEnum.DISCURSIVA); 
        filtro.setEmailUsuario("user2@gmail.com");
        filtro.setNomeCriador(criador.getNome());

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
    public void t21_buscarQuestaoPorFiltroCriador() {
        logger.info("Executando t21: buscarQuestaoPorFiltroCriador");
        FiltroQuestao filtro = new FiltroQuestao();
        List<Questao> lista;
        Usuario criador = usuarioServico.buscarUsuarioPorEmail("user1@gmail.com");

        filtro.setTipo(TipoQuestaoEnum.DISCURSIVA);
        filtro.setEmailUsuario("user2@gmail.com");
        filtro.setNomeCriador(criador.getNome());

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

}
