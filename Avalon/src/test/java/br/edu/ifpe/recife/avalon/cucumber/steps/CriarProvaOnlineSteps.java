/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.cucumber.steps;

import br.edu.ifpe.recife.avalon.cucumber.util.BrowserManager;
import cucumber.api.java.pt.E;
import cucumber.api.java.pt.Entao;
import cucumber.api.java.pt.Quando;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import static org.junit.Assert.assertTrue;
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

    @E("^preencher o título da prova$")
    public void preencherTituloProva() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:txtTitulo")).sendKeys("Prova");
    }

    @E("^preencher a data de início da prova$")
    public void preencherDataInicio() throws Throwable {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 10);
        String dataInicio = formatarData(calendar);
        BrowserManager.getDriver().findElement(By.id("form:calDataHoraInicio_input")).click();
        BrowserManager.getDriver().findElement(By.id("form:calDataHoraInicio_input")).sendKeys(dataInicio);
    }

    @E("^preencher a data de término da prova$")
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

    @E("^não preencher o título da prova$")
    public void naoPreencherTituloProva() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:txtTitulo")).clear();
    }

    @E("^não preencher a data de início da prova$")
    public void naoPreencherDataInicioProva() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:calDataHoraInicio_input")).clear();
    }

    @E("^não preencher a data de término da prova$")
    public void naoPreencherDataTerminioProva() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:calDataHoraFim_input")).clear();
    }

    @E("^preencher a data de início da prova com uma data menor que a atual$")
    public void preencherDataInicioMenorDataAtual() throws Throwable {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -10);
        String dataInicio = formatarData(calendar);
        BrowserManager.getDriver().findElement(By.id("form:calDataHoraInicio_input")).click();
        BrowserManager.getDriver().findElement(By.id("form:calDataHoraInicio_input")).sendKeys(dataInicio);
    }

    @E("^preencher a data de início da prova com uma data maior que a data de termino$")
    public void preencherDataInicioMaiorDataTermino() throws Throwable {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 360);
        String dataInicio = formatarData(calendar);
        BrowserManager.getDriver().findElement(By.id("form:calDataHoraInicio_input")).click();
        BrowserManager.getDriver().findElement(By.id("form:calDataHoraInicio_input")).sendKeys(dataInicio);
    }
    
    @E("^preencher a data de término da prova com um intervalo menor que trinta minutos$")
    public void preencherIntervaloMenorMinimo() throws Throwable {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 10);
        String dataTermino = formatarData(calendar);
        BrowserManager.getDriver().findElement(By.id("form:calDataHoraFim_input")).click();
        BrowserManager.getDriver().findElement(By.id("form:calDataHoraFim_input")).sendKeys(dataTermino);
    }
    
    @E("^preencher a data de término da prova com um intervalo maior que cinco horas$")
    public void preencherIntervaloMaiorMaximo() throws Throwable {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 500);
        String dataTermino = formatarData(calendar);
        BrowserManager.getDriver().findElement(By.id("form:calDataHoraFim_input")).click();
        BrowserManager.getDriver().findElement(By.id("form:calDataHoraFim_input")).sendKeys(dataTermino);
    }
    
    @E("^não preencher o peso de uma questão$")
    public void naoPreencherPesoQuestao() throws Throwable {
        BrowserManager. waitTime(2000);
        BrowserManager.getDriver().findElement(By.id("form:table:0:txtPeso")).click();
        BrowserManager.getDriver().findElement(By.id("form:table:0:txtPeso")).clear();
    }
    
    @E("^não preencher a nota máxima da prova$")
    public void naoPreencherNotaMaxima() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:txtNotaMaxima")).clear();
    }
    
    @E("^preencher a nota máxima da prova com (\\d+)$")
    public void preencherNotaMaxima(int nota) throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:txtNotaMaxima")).clear();
        BrowserManager.getDriver().findElement(By.id("form:txtNotaMaxima")).sendKeys(String.valueOf(nota));
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

    private int obterListaProvas() {
        return BrowserManager.getDriver().findElements(By.xpath("//td/span[contains(text(), 'Prova')]")).size();
    }

    private String formatarData(Calendar calendar) {
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmm");
        return formatter.format(calendar.getTime());
    }

}
