/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.cucumber.steps;

import br.edu.ifpe.recife.avalon.cucumber.util.BrowserManager;
import cucumber.api.java.pt.E;
import cucumber.api.java.pt.Entao;
import static org.junit.Assert.assertTrue;
import org.openqa.selenium.By;

/**
 *
 * @author eduardoamaral
 */
public class ImprimirProvaSteps {
    
    private String abaPrincipal;

    @E("^selecionar uma questao$")
    public void selecionarQuestao() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:table:0:chkSelecionarQuestao")).click();
        BrowserManager.waitTime(1000);
    }

    @E("^clicar no botao imprimir$")
    public void imprimirProva() throws Throwable {
        abaPrincipal = BrowserManager.getDriver().getWindowHandle();
        BrowserManager.getDriver().findElement(By.id("form:btn")).click();
        BrowserManager.waitTime(1000);
    }

    @Entao("^sera exibido em uma nova aba a prova com as questoes discursivas selecionadas$")
    public void imprimirProvaDiscursiva() {
        mudarAbaImpressao();
        int questoes = BrowserManager.getDriver().findElements(By.xpath("//div[contains(@class, 'prova-alternativa')]")).size();
        assertTrue(questoes == 0);
        fecharAbaImpressao();
        LoginSteps.logout();
    }

    @Entao("^sera exibido em uma nova aba a prova com as questoes de verdadeiro ou falso selecionadas$")
    public void imprimirProvaVF() {
        mudarAbaImpressao();
        int questoes = BrowserManager.getDriver().findElements(By.xpath("//div[contains(@class, 'VERDADEIRO_FALSO')]")).size();
        assertTrue(questoes > 0);
        fecharAbaImpressao();
        LoginSteps.logout();
    }

    @Entao("^sera exibido em uma nova aba a prova com as questoes de multipla escolha selecionadas$")
    public void imprimirProvaMultiplaEscolha() throws Throwable {
        mudarAbaImpressao();
        int questoes = BrowserManager.getDriver().findElements(By.xpath("//div[contains(@class, 'MULTIPLA_ESCOLHA')]")).size();
        assertTrue(questoes > 0);
        fecharAbaImpressao();
        LoginSteps.logout();
    }
    
    private void mudarAbaImpressao(){
        for (String curWindow : BrowserManager.getDriver().getWindowHandles()) {
            BrowserManager.getDriver().switchTo().window(curWindow);
        }
    }

    private void fecharAbaImpressao() {
        BrowserManager.getDriver().close();
        BrowserManager.getDriver().switchTo().window(abaPrincipal);
        BrowserManager.waitTime(1000);
    }

}
