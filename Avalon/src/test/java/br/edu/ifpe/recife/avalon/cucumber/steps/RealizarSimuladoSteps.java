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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.openqa.selenium.By;

/**
 *
 * @author eduardoamaral
 */
public class RealizarSimuladoSteps {
    
    @Quando("^o aluno pesquisar os simulados disponíveis$")
    public void pesquisarSimuladosDisponiveis() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:pesquisar")).click();
        BrowserManager.waitTime(1000);
    }
    
    @E("^iniciar um simulado de verdadeiro ou falso$")
    public void iniciarSimuladoVF() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:table:0:btnIniciar")).click();
        BrowserManager.waitTime(1000);
    }
    
    @E("^responder todas as questões de verdadeiro ou falso$")
    public void responderSimuladoVF() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:rpQuestoesVF:0:rbRespostaVerdadeiroFalso:0_clone")).click();
        BrowserManager.waitTime(1000);
        
        BrowserManager.getDriver().findElement(By.id("form:rpQuestoesVF:1:rbRespostaVerdadeiroFalso:3_clone")).click();
        BrowserManager.waitTime(1000);
    }
    
    @E("^finalizar o simulado$")
    public void finalizarSimulado() throws Throwable {
        finalizar();
    }
    
    @E("^iniciar um simulado de múltipla escolha$")
    public void iniciarSimuladoME() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:table:1:btnIniciar")).click();
        BrowserManager.waitTime(1000);
    }
    
    @E("^responder todas as questões de múltipla escolha$")
    public void responderSimuladoME() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:rpQuestoesME:0:rbRespostaMultiplaEscolha:2_clone")).click();
        BrowserManager.waitTime(1000);
    
        BrowserManager.getDriver().findElement(By.id("form:rpQuestoesME:1:rbRespostaMultiplaEscolha:8_clone")).click();
        BrowserManager.waitTime(1000);
    }
    
    @E("^iniciar um simulado$")
    public void iniciarSimulado() throws Throwable {
        iniciarSimuladoVF();
    }
    
    @E("^clicar no botão finalizar simulado sem responder todas as questões$")
    public void finalizarSimuladoQuestoesAbertas() throws Throwable {
        finalizar();
    }
    
    @Entao("^será exibido o resultado do aluno no simulado$")
    public void exibirResultadoAluno() throws Throwable {
        int resultados = BrowserManager.getDriver().findElements(By.xpath("//span[contains(text(), 'A nota obtida no simulado foi:')]")).size();
        assertTrue(resultados > 0);
        BrowserManager.getDriver().findElement(By.id("formModalResultado:btnFecharModalResultado")).click();
        BrowserManager.waitTime(1000);
        LoginSteps.logout();
    }
    
    @Entao("^será exibido mensagem para simulado sem todas as questões respondidas$")
    public void exibirMensagemProvaQuestoesAbertas() throws Throwable {
        String mensagem = TestUtil.obterMensagemValidacao();
        
        assertEquals("Responda todas as questões antes de finalizar o simulado.", mensagem);
        LoginSteps.logout();
    }
    
    private void finalizar(){
        BrowserManager.getDriver().findElement(By.id("form:btnFinalizar")).click();
        BrowserManager.waitTime(1000);
    }
    
}
