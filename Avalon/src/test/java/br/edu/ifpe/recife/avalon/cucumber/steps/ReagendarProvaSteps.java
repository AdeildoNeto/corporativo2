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
import static org.junit.Assert.assertEquals;
import org.openqa.selenium.By;

/**
 *
 * @author eduardoamaral
 */
public class ReagendarProvaSteps {

    @Quando("^o professor selecionar uma prova para reagendamento$")
    public void selecionarProvaReagendar() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:tableProvas:2:btnReagendarProva")).click();
        BrowserManager.waitTime(2000);
    }
    
    @Quando("^o professor selecionar uma prova em andamento para reagendamento$")
    public void selecionarProvaAndamentoReagendar() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:tableProvas:3:btnReagendarProva")).click();
        BrowserManager.waitTime(2000);
    }
    
    @E("^alterar a data de início da prova$")
    public void alterarDataInicio() throws Throwable {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 30);
        String dataInicio = formatarData(calendar);
        BrowserManager.getDriver().findElement(By.id("formReagendamento:calDataHoraInicioReagendamento_input")).click();
        BrowserManager.getDriver().findElement(By.id("formReagendamento:calDataHoraInicioReagendamento_input")).sendKeys(dataInicio);
    }
    
    @E("^alterar a data fim da prova$")
    public void alterarDataFim() throws Throwable {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 120);
        String dataFim = formatarData(calendar);
        BrowserManager.getDriver().findElement(By.id("formReagendamento:calDataHoraFimReagendamento_input")).click();
        BrowserManager.getDriver().findElement(By.id("formReagendamento:calDataHoraFimReagendamento_input")).sendKeys(dataFim);
    }
    
    @E("^alterar apenas a data fim de reagendamento$")
    public void alterarApenasDataFim() throws Throwable {
        Calendar calendar = Calendar.getInstance();
        String dataFim = formatarData(calendar);
        BrowserManager.getDriver().findElement(By.id("formReagendamento:calDataHoraFimReagendamento_input")).click();
        BrowserManager.getDriver().findElement(By.id("formReagendamento:calDataHoraFimReagendamento_input")).sendKeys(dataFim);
    }

    @E("^clicar no botão reagendar$")
    public void reagendarProva() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("formReagendamento:btnConfirmarReagendamento")).click();
        BrowserManager.waitTime(1000);
    }

    @E("^não preencher a data de início do reagendamento$")
    public void naoPreencherDataInicio() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("formReagendamento:calDataHoraInicioReagendamento_input")).click();
        BrowserManager.getDriver().findElement(By.id("formReagendamento:calDataHoraInicioReagendamento_input")).clear();
    }

    @E("^não preencher a data fim de reagendamento$")
    public void naoPreencherDataFim() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("formReagendamento:calDataHoraFimReagendamento_input")).click();
        BrowserManager.getDriver().findElement(By.id("formReagendamento:calDataHoraFimReagendamento_input")).clear();
    }

    @E("^alterar a data fim da prova com um valor menor que a data de início$")
    public void preencherDataFimMenorDataInicio() throws Throwable {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -120);
        String dataFim = formatarData(calendar);
        BrowserManager.getDriver().findElement(By.id("formReagendamento:calDataHoraFimReagendamento_input")).click();
        BrowserManager.getDriver().findElement(By.id("formReagendamento:calDataHoraFimReagendamento_input")).sendKeys(dataFim);
    }

    @E("^alterar a data fim da prova com um intervalo menor do que trinta minutos$")
    public void preencharIntevaloMenorPermitido() throws Throwable {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 40);
        String dataFim = formatarData(calendar);
        BrowserManager.getDriver().findElement(By.id("formReagendamento:calDataHoraFimReagendamento_input")).click();
        BrowserManager.getDriver().findElement(By.id("formReagendamento:calDataHoraFimReagendamento_input")).sendKeys(dataFim);
    }

    @E("^alterar a data fim da prova com um intervalo maior que cinco horas$")
    public void preencherInvervaloMaiorPermitido() throws Throwable {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 600);
        String dataFim = formatarData(calendar);
        BrowserManager.getDriver().findElement(By.id("formReagendamento:calDataHoraFimReagendamento_input")).click();
        BrowserManager.getDriver().findElement(By.id("formReagendamento:calDataHoraFimReagendamento_input")).sendKeys(dataFim);
    }

    @Entao("^a disponibilidade da prova será alterada$")
    public void disponibilidadeProvaAlterada() throws Throwable {
        LoginSteps.logout();
    }
    
    @Entao("^será exibido mensagem para alterar data de início de uma prova em andamento$")
    public void critirAlterarDataInicioProvaAndamento() {
        String mensagem = obterMensagemValidacao();
        assertEquals("Não é permitido alterar o horário de início de uma prova em andamento.", mensagem);
        fecharModalReagendamento();
        LoginSteps.logout();
    }

    @Entao("^será exibido mensagem para data de início do reagendamento obrigatória$")
    public void criticarDataInicioObrigatoria() {
        String mensagem = obterMensagemValidacao();
        assertEquals("A data e hora de início é obrigatória.", mensagem);
        fecharModalReagendamento();
        LoginSteps.logout();
    }

    @Entao("^será exibido mensagem para data fim do reagendamento obrigatória$")
    public void criticarDataTerminioObrigatoria() {
        String mensagem = obterMensagemValidacao();
        assertEquals("A data e hora de término é obrigatória.", mensagem);
        fecharModalReagendamento();
        LoginSteps.logout();
    }

    @Entao("^será exibido mensagem para data de início do reagendamento maior que a data fim$")
    public void criticarDataInicioMaiorDataTerminio() throws Throwable {
        String mensagem = obterMensagemValidacao();
        assertEquals("A data de início da prova não pode ser maior que a data de término.", mensagem);
        fecharModalReagendamento();
        LoginSteps.logout();
    }

    @Entao("^será exibido mensagem para disponibilidade mínima para o reagendamento$")
    public void criticarDisponibilidadeMinima() throws Throwable {
        String mensagem = obterMensagemValidacao();
        assertEquals("A prova deve ficar disponível por ao menos 30 minutos.", mensagem);
        fecharModalReagendamento();
        LoginSteps.logout();
    }

    @Entao("será exibido mensagem para disponibilidade máxima para o reagendamento$")
    public void criticarDisponibilidadeMaxima() throws Throwable {
        String mensagem = obterMensagemValidacao();
        assertEquals("A prova deve ficar disponível por no máximo 5 horas.", mensagem);
        fecharModalReagendamento();
        LoginSteps.logout();
    }
    
    private void fecharModalReagendamento(){
        BrowserManager.getDriver().findElement(By.id("formReagendamento:btnCancelarReagendamento")).click();
        BrowserManager.waitTime(1000);
    }

    private int obterListaProvas() {
        return BrowserManager.getDriver().findElements(By.xpath("//td/span[contains(text(), 'Prova')]")).size();
    }

    private String formatarData(Calendar calendar) {
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmm");
        return formatter.format(calendar.getTime());
    }
    
    private String obterMensagemValidacao(){
        return BrowserManager.getDriver().findElement(By.xpath("//*[@id='formReagendamento:msgReagendamento']/div/ul/li/span")).getText();
    }

}
