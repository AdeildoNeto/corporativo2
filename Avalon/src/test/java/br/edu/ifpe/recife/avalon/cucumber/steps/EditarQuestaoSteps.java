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
import static org.junit.Assert.assertTrue;
import org.openqa.selenium.By;

/**
 *
 * @author eduardoamaral
 */
public class EditarQuestaoSteps {

    @Quando("^o professor selecionar uma questão para edição$")
    public void selecionarQuestaoEdicao() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:tbMinhasQuestoes:0:btnEditarQuestao")).click();
        BrowserManager.waitTime(1000);
    }
    
    @Quando("^o professor selecionar uma questão de verdadeiro ou falso para edição$")
    public void selecionarQuestaoVFEdicao() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:tbMinhasQuestoes:2:btnEditarQuestao")).click();
        BrowserManager.waitTime(1000);
    }
    
    @Quando("^o professor selecionar uma questão de múltipla escolha para edição$")
    public void selecionarQuestaoMEEdicao() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:tbMinhasQuestoes:4:btnEditarQuestao")).click();
        BrowserManager.waitTime(1000);
    }
    
    @E("^alterar o enunciado da questão$")
    public void alterarEnunciado() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:txtEnunciado")).clear();
        BrowserManager.getDriver().findElement(By.id("form:txtEnunciado")).sendKeys("O que é um diagrama de classe alterado?");
    }

    @E("^confirmar a edição da questão$")
    public void confirmarEdicao() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:btnConfirmar")).click();
        BrowserManager.waitTime(1000);
    }

    @E("^alterar o enunciado com um valor já existente para o tipo selecionado$")
    public void preencherEnunciadoRepetidoEdicao() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:txtEnunciado")).clear();
        BrowserManager.getDriver().findElement(By.id("form:txtEnunciado")).sendKeys("O que é um diagrama de atividades?");
    }
    
    @E("^alterar a respota da questão$")
    public void alterarRespostaVF() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:tglRespostaVF")).click();
        BrowserManager.waitTime(1000);
    }
    
    @E("^alterar a alternativa correta da questão$")
    public void alterarRespostaME() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:radio:0_clone")).click();
        BrowserManager.waitTime(1000);
    }
    
    @Entao("^o enunciado da questão será alterado$")
    public void validarQuestaoAlterada() throws Throwable {
        int questoes = BrowserManager.getDriver().findElements(By.xpath("//*[contains(text(), 'alterado')]")).size();
        assertTrue(questoes > 0);
        LoginSteps.logout();
    }
    
    @Entao("^a respota da questão será alterada$")
    public void validarRespotaVFAlterada() throws Throwable {
        LoginSteps.logout();
    }
    
    @Entao("^a alternativa correta da questão será alterada$")
    public void validarRespotaMEAlterada() throws Throwable {
        LoginSteps.logout();
    }
    
}
