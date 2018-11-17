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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.openqa.selenium.By;

/**
 *
 * @author eduardoamaral
 */
public class RealizarProvaSteps {
    
    private int qtdeProvasDisponiveisAnterior = 0;

    @Quando("^o aluno iniciar uma prova de verdadeiro ou falso$")
    public void iniciarProvaVF() throws Throwable {
        qtdeProvasDisponiveisAnterior = BrowserManager.getDriver().findElements(By.xpath("//tbody/tr")).size();
        BrowserManager.getDriver().findElement(By.id("form:table:0:btnIniciar")).click();
        BrowserManager.waitTime(1000);
    }
    
    @Quando("^o aluno iniciar uma prova de múltipla escolha$")
    public void iniciarProvaME() throws Throwable {
        qtdeProvasDisponiveisAnterior = BrowserManager.getDriver().findElements(By.xpath("//tbody/tr")).size();
        BrowserManager.getDriver().findElement(By.id("form:table:1:btnIniciar")).click();
        BrowserManager.waitTime(1000);
    }
    
    @Quando("^o aluno iniciar uma prova$")
    public void iniciarProva() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:table:0:btnIniciar")).click();
        BrowserManager.waitTime(1000);
    }
    
    @E("^clicar no botão iniciar do modal de instruções da prova$")
    public void clicarIniciarProvaModal() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("formModalIniciar:btnIniciarProva")).click();
        BrowserManager.waitTime(2000);
    }
    
    @E("^responder a primeira questão como verdadeiro$")
    public void responderVerdadeiro() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:cslVerdadeiroFalso:0:rbRespostaVerdadeiroFalso:0_clone")).click();
        BrowserManager.waitTime(1000);
    }
    
    @E("^passar para a próxima questão de verdadeiro ou falso$")
    public void proximaQuestaoVF() throws Throwable {
        BrowserManager.getDriver().findElement(By.xpath("//*[@id='form:cslVerdadeiroFalso']/div[1]/span[1]")).click();
        BrowserManager.waitTime(1000);
    }
    
    @E("^responder a segunda questão como falso$")
    public void responderFalso() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:cslVerdadeiroFalso:1:rbRespostaVerdadeiroFalso:3_clone")).click();
        BrowserManager.waitTime(1000);
    }
    
    @E("^clicar no botão finalizar prova$")
    public void finalizarProva() throws Throwable {
        finalizar();
    }
    
    @E("^confirmar o fim da prova$")
    public void confirmarFinalizarProva() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:btnConfirmar")).click();
        BrowserManager.waitTime(1000);
    }
    
    @E("^responder a primeira questão com a terceira alternativa$")
    public void responderAlternativaUm() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:cslMultiplaEscolha:0:rbRespostaMultiplaEscolha:2_clone")).click();
        BrowserManager.waitTime(1000);
    }
    
    @E("^passar para a próxima questão de múltipla escolha$")
    public void proximaQuestaoME() throws Throwable {
        BrowserManager.getDriver().findElement(By.xpath("//*[@id='form:cslMultiplaEscolha']/div[1]/span[1]")).click();
        BrowserManager.waitTime(1000);
    }
    
    @E("^responder a segunda questão com a quinta alternativa$")
    public void responderAlternativaDois() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:cslMultiplaEscolha:1:rbRespostaMultiplaEscolha:9_clone")).click();
        BrowserManager.waitTime(1000);
    }
    
    @E("^clicar no botão finalizar prova sem responder todas as questões$")
    public void finalizarProvaQuestoesAbertas() throws Throwable {
        finalizar();
    }
    
    @Entao("^a prova será finalizada$")
    public void verificarProvaFinalizada() throws Throwable {
        int qtdeProvasDisponiveis = BrowserManager.getDriver().findElements(By.xpath("//tbody/tr")).size();
        assertEquals((qtdeProvasDisponiveisAnterior - 1), qtdeProvasDisponiveis);
        LoginSteps.logout();
    }
    
    @Entao("^será exibido mensagem para prova sem todas as questões respondidas$")
    public void exibirMensagemProvaQuestoesAbertas() throws Throwable {
        String mensagem = BrowserManager.getDriver().findElement(By.id("lbMensagemFinalizacao")).getText();
        
        assertEquals("Ainda há questões não respondidas, deseja finalizar a prova?", mensagem);
        BrowserManager.getDriver().findElement(By.id("form:btnCancelar")).click();
        BrowserManager.waitTime(1000);
        BrowserManager.getDriver().close();
    }
    
    @Entao("^o nome e o email do aluno serão exibidos na prova$")
    public void exibirInformacoesAluno() throws Throwable {
        String nomeAluno = BrowserManager.getDriver().findElement(By.id("lbNomeAluno")).getText();
        String emailAluno = BrowserManager.getDriver().findElement(By.id("lbEmailAluno")).getText();
        assertTrue(!nomeAluno.isEmpty() && !emailAluno.isEmpty());
        BrowserManager.getDriver().close();
    }
    
    private void finalizar(){
        BrowserManager.getDriver().findElement(By.id("form:btnFinalizar")).click();
        BrowserManager.waitTime(1000);
    }
    
}
