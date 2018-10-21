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

    @Quando("^o professor clicar no botão nova prova$")
    public void clicarBotaoNovaProva() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:btnGerarProva")).click();
        BrowserManager.waitTime(1000);
    }

    @E("^preencher o titulo da prova$")
    public void preencherTituloProva() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:txtTitulo")).sendKeys("Prova");
    }

    @E("^preencher a data de inicio da prova$")
    public void preencherDataInicio() throws Throwable {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 10);
        String dataInicio = formatarData(calendar);
        BrowserManager.getDriver().findElement(By.id("form:calDataHoraInicio_input")).click();
        BrowserManager.getDriver().findElement(By.id("form:calDataHoraInicio_input")).sendKeys(dataInicio);
    }

    @E("^preencher a data de termino da prova$")
    public void preencherDataTermino() throws Throwable {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 120);
        String dataTermino = formatarData(calendar);
        BrowserManager.getDriver().findElement(By.id("form:calDataHoraFim_input")).click();
        BrowserManager.getDriver().findElement(By.id("form:calDataHoraFim_input")).sendKeys(dataTermino);
    }

    @E("^selecionar o tipo verdadeiro ou falso no filtro de questões$")
    public void selecionarTipoQuestaoVF() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:selTipoQuestao_label")).click();
        BrowserManager.getDriver().findElement(By.xpath("//*[@data-label='Verdadeiro ou Falso']")).click();
    }

    @E("^selecionar o tipo múltipla escolha no filtro de questões$")
    public void selecionarTipoQuestaoMultiplaEscolha() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:selTipoQuestao_label")).click();
        BrowserManager.getDriver().findElement(By.xpath("//*[@data-label='Múltipla Escolha']")).click();
    }

    @E("^selecionar questões para prova$")
    public void selecionarTodasQuestoes() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:table:chkSelecionarTodas")).click();
        BrowserManager.waitTime(2000);
    }

    @E("^clicar no botão salvar nova prova$")
    public void salvarNovaProva() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:btn")).click();
        BrowserManager.waitTime(3000);
    }

    @E("^nao preencher o titulo da prova$")
    public void naoPreencherTituloProva() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:txtTitulo")).clear();
    }

    @E("^nao preencher a data de inicio da prova$")
    public void naoPreencherDataInicioProva() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:calDataHoraInicio_input")).clear();
    }

    @E("^nao preencher a data de termino da prova$")
    public void naoPreencherDataTerminioProva() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:calDataHoraFim_input")).clear();
    }

    @E("^preencher a data de inicio da prova com uma data menor que a atual$")
    public void preencherDataInicioMenorDataAtual() throws Throwable {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -10);
        String dataInicio = formatarData(calendar);
        BrowserManager.getDriver().findElement(By.id("form:calDataHoraInicio_input")).click();
        BrowserManager.getDriver().findElement(By.id("form:calDataHoraInicio_input")).sendKeys(dataInicio);
    }

    @E("^preencher a data de inicio da prova com uma data maior que a data de termino$")
    public void preencherDataInicioMaiorDataTermino() throws Throwable {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 360);
        String dataInicio = formatarData(calendar);
        BrowserManager.getDriver().findElement(By.id("form:calDataHoraInicio_input")).click();
        BrowserManager.getDriver().findElement(By.id("form:calDataHoraInicio_input")).sendKeys(dataInicio);
    }
    
    @E("^preencher a data de termino da prova com um intervalo menor que trinta minutos$")
    public void preencherIntervaloMenorMinimo() throws Throwable {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 10);
        String dataTermino = formatarData(calendar);
        BrowserManager.getDriver().findElement(By.id("form:calDataHoraFim_input")).click();
        BrowserManager.getDriver().findElement(By.id("form:calDataHoraFim_input")).sendKeys(dataTermino);
    }
    
    @E("^preencher a data de termino da prova com um intervalo maior que cinco horas$")
    public void preencherIntervaloMaiorMaximo() throws Throwable {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 500);
        String dataTermino = formatarData(calendar);
        BrowserManager.getDriver().findElement(By.id("form:calDataHoraFim_input")).click();
        BrowserManager.getDriver().findElement(By.id("form:calDataHoraFim_input")).sendKeys(dataTermino);
    }

    @Entao("^uma nova prova do tipo verdadeiro ou falso sera criada$")
    public void salvarProvaVF() {
        int resultado = obterListaProvas();
        assertTrue(resultado > 0);
        LoginSteps.logout();
    }

    @Entao("^uma nova prova do tipo múltipla escolha sera criada$")
    public void salvarProvaMultiplaEscolha() {
        int resultado = obterListaProvas();
        assertTrue(resultado > 0);
        LoginSteps.logout();
    }

    @Entao("^será exibido mensagem para titulo da prova obrigatório$")
    public void criticarTituloObrigatorio() {
        String mensagem = TestUtil.obterMensagemValidacao();
        assertEquals("O título da prova é obrigatório.", mensagem);
        BrowserManager.waitTime(1000);
        LoginSteps.logout();
    }

    @Entao("^será exibido mensagem para data de inicio da prova obrigatório$")
    public void criticarDataInicioObrigatoria() {
        String mensagem = TestUtil.obterMensagemValidacao();
        assertEquals("A data e hora de início é obrigatória.", mensagem);
        LoginSteps.logout();
    }

    @Entao("^será exibido mensagem para data de termino da prova obrigatório$")
    public void criticarDataTerminioObrigatoria() {
        String mensagem = TestUtil.obterMensagemValidacao();
        assertEquals("A data e hora de término é obrigatória.", mensagem);
        LoginSteps.logout();
    }

    @Entao("^será exibido mensagem para data de inicio da prova menor que a data atual$")
    public void criticarDataInicioMenorAtual() {
        String mensagem = TestUtil.obterMensagemValidacao();
        assertEquals("A data de início da prova não pode ser menor que a data e hora atual.", mensagem);
        LoginSteps.logout();
    }

    @Entao("^será exibido mensagem para data de inicio da prova maior que a data de termino$")
    public void criticarDataInicioMaiorDataTerminio() throws Throwable {
        String mensagem = TestUtil.obterMensagemValidacao();
        assertEquals("A data de início da prova não pode ser maior que a data de término.", mensagem);
        LoginSteps.logout();
    }

    @Entao("^será exibido mensagem para disponibilidade minima da prova$")
    public void criticarDisponibilidadeMinima() throws Throwable {
        String mensagem = TestUtil.obterMensagemValidacao();
        assertEquals("A prova deve ficar disponível por ao menos 30 minutos.", mensagem);
        LoginSteps.logout();
    }

    @Entao("^será exibido mensagem para disponibilidade maxima da prova$")
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
