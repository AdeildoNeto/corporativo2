/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.cucumber.steps;

import br.edu.ifpe.recife.avalon.cucumber.util.BrowserManager;
import br.edu.ifpe.recife.avalon.cucumber.util.TestUtil;
import cucumber.api.java.pt.E;
import cucumber.api.java.pt.Entao;
import cucumber.api.java.pt.Quando;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import org.openqa.selenium.By;

/**
 *
 * @author eduardoamaral
 */
public class CriarProvaOnlineSteps {

    @Quando("^o professor clicar no botao nova prova$")
    public void clicarBotaoNovaProva() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:btnGerarProva")).click();
        BrowserManager.waitTime(1000);
    }

    @E("^preencher o titulo da prova$")
    public void preencherTituloProva() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("formTitulo:titulo")).sendKeys("Prova");
    }

    @E("^clicar no botao gerar$")
    public void clicarBotaoGerar() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("formTitulo:btnGerar")).click();
        BrowserManager.waitTime(1000);
    }

    @E("^preencher a data de inicio da prova$")
    public void preencherDataInicio() throws Throwable {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 10);
        String dataInicio = formatarData(calendar);
        BrowserManager.getDriver().findElement(By.id("form:dataHoraInicio_input")).click();
        BrowserManager.getDriver().findElement(By.id("form:dataHoraInicio_input")).sendKeys(dataInicio);
    }

    @E("^preencher a data de termino da prova$")
    public void preencherDataTermino() throws Throwable {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 120);
        String dataTermino = formatarData(calendar);
        BrowserManager.getDriver().findElement(By.id("form:dataHoraFim_input")).click();
        BrowserManager.getDriver().findElement(By.id("form:dataHoraFim_input")).sendKeys(dataTermino);
    }

    @E("^selecionar o tipo verdadeiro ou falso no filtro de questoes$")
    public void selecionarTipoQuestaoVF() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:selTipoQuestao_label")).click();
        BrowserManager.getDriver().findElement(By.xpath("//*[@data-label='Verdadeiro ou Falso']")).click();
    }

    @E("^selecionar o tipo multipla escolha no filtro de questoes$")
    public void selecionarTipoQuestaoMultiplaEscolha() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:selTipoQuestao_label")).click();
        BrowserManager.getDriver().findElement(By.xpath("//*[@data-label='Múltipla Escolha']")).click();
    }

    @E("^selecionar questoes para prova$")
    public void selecionarTodasQuestoes() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:table:chkSelecionarTodas")).click();
        BrowserManager.waitTime(2000);
    }

    @E("^clicar no botao salvar nova prova$")
    public void salvarNovaProva() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:btn")).click();
        BrowserManager.waitTime(3000);
    }

    @E("^nao preencher o titulo da prova$")
    public void naoPreencherTituloProva() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("formTitulo:titulo")).clear();
    }

    @E("^nao preencher a data de inicio da prova$")
    public void naoPreencherDataInicioProva() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:dataHoraInicio_input")).clear();
    }

    @E("^nao preencher a data de termino da prova$")
    public void naoPreencherDataTerminioProva() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:dataHoraFim_input")).clear();
    }

    @E("^preencher a data de inicio da prova com uma data menor que a atual$")
    public void preencherDataInicioMenorDataAtual() throws Throwable {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -10);
        String dataInicio = formatarData(calendar);
        BrowserManager.getDriver().findElement(By.id("form:dataHoraInicio_input")).click();
        BrowserManager.getDriver().findElement(By.id("form:dataHoraInicio_input")).sendKeys(dataInicio);
    }

    @E("^preencher a data de inicio da prova com uma data maior que a data de termino$")
    public void preencherDataInicioMaiorDataTermino() throws Throwable {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 360);
        String dataInicio = formatarData(calendar);
        BrowserManager.getDriver().findElement(By.id("form:dataHoraInicio_input")).click();
        BrowserManager.getDriver().findElement(By.id("form:dataHoraInicio_input")).sendKeys(dataInicio);
    }
    
    @E("^preencher a data de termino da prova com um intervalo menor que trinta minutos$")
    public void preencherIntervaloMenorMinimo() throws Throwable {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 10);
        String dataTermino = formatarData(calendar);
        BrowserManager.getDriver().findElement(By.id("form:dataHoraFim_input")).click();
        BrowserManager.getDriver().findElement(By.id("form:dataHoraFim_input")).sendKeys(dataTermino);
    }
    
    @E("^preencher a data de termino da prova com um intervalo maior que cinco horas$")
    public void preencherIntervaloMaiorMaximo() throws Throwable {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 500);
        String dataTermino = formatarData(calendar);
        BrowserManager.getDriver().findElement(By.id("form:dataHoraFim_input")).click();
        BrowserManager.getDriver().findElement(By.id("form:dataHoraFim_input")).sendKeys(dataTermino);
    }

    @Entao("^uma nova prova do tipo verdadeiro ou falso sera criada$")
    public void salvarProvaVF() {
        int resultado = obterListaProvas();
        assertTrue(resultado > 0);
        LoginSteps.logout();
    }

    @Entao("^uma nova prova do tipo multipla escolha sera criada$")
    public void salvarProvaMultiplaEscolha() {
        int resultado = obterListaProvas();
        assertTrue(resultado > 0);
        LoginSteps.logout();
    }

    @Entao("^sera exibido mensagem para titulo da prova obrigatorio$")
    public void criticarTituloObrigatorio() {
        String mensagem = BrowserManager.getDriver().findElement(By.xpath("//*[@id='formTitulo:msgTitulo']/div/ul/li/span")).getText();
        assertEquals("O título da prova é obrigatório.", mensagem);
        BrowserManager.getDriver().findElement(By.id("formTitulo:btnCancelar")).click();
        BrowserManager.waitTime(1000);
        LoginSteps.logout();
    }

    @Entao("^sera exibido mensagem para data de inicio da prova obrigatorio$")
    public void criticarDataInicioObrigatoria() {
        String mensagem = TestUtil.obterMensagemValidacao();
        assertEquals("A data e hora de início é obrigatória.", mensagem);
        LoginSteps.logout();
    }

    @Entao("^sera exibido mensagem para data de termino da prova obrigatorio$")
    public void criticarDataTerminioObrigatoria() {
        String mensagem = TestUtil.obterMensagemValidacao();
        assertEquals("A data e hora de término é obrigatória.", mensagem);
        LoginSteps.logout();
    }

    @Entao("^sera exibido mensagem para data de inicio da prova menor que a data atual$")
    public void criticarDataInicioMenorAtual() {
        String mensagem = TestUtil.obterMensagemValidacao();
        assertEquals("A data de início da prova não pode ser menor que a data e hora atual.", mensagem);
        LoginSteps.logout();
    }

    @Entao("^sera exibido mensagem para data de inicio da prova maior que a data de termino$")
    public void criticarDataInicioMaiorDataTerminio() throws Throwable {
        String mensagem = TestUtil.obterMensagemValidacao();
        assertEquals("A data de início da prova não pode ser maior que a data de término.", mensagem);
        LoginSteps.logout();
    }

    @Entao("^sera exibido mensagem para disponibilidade minima da prova$")
    public void criticarDisponibilidadeMinima() throws Throwable {
        String mensagem = TestUtil.obterMensagemValidacao();
        assertEquals("A prova deve ficar disponível por ao menos 30 minutos.", mensagem);
        LoginSteps.logout();
    }

    @Entao("^sera exibido mensagem para disponibilidade maxima da prova$")
    public void criticarDisponibilidadeMaxima() throws Throwable {
        String mensagem = TestUtil.obterMensagemValidacao();
        assertEquals("A prova deve ficar disponível por no máximo 5 horas.", mensagem);
        LoginSteps.logout();
    }

    private int obterListaProvas() {
        return BrowserManager.getDriver().findElements(By.xpath("//td/span[contains(text(), 'Prova')]")).size();
    }

    private String formatarData(Calendar calendar) {
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmm");
        return formatter.format(calendar.getTime());
    }

}
