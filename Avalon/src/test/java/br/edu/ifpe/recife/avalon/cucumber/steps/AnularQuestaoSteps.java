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
public class AnularQuestaoSteps {

    @Quando("^o professor clicar no botão anular questão$")
    public void anularQuestao() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:tbMinhasQuestoes:0:btnAnularQuestao")).click();
        BrowserManager.waitTime(1000);
    }
    
    @E("^confirmar a anulação da questão$")
    public void confirmarAnulacao() {
        BrowserManager.getDriver().findElement(By.id("formModalAnular:btnConfirmarAnulacao")).click();
        BrowserManager.waitTime(1000);
    }
    
    @Entao("^a questão será anulada e a nota de todas as provas em que foi utilizada será recalculada$")
    public void verificarQuestaoAnulada() throws Throwable {
        int count = BrowserManager.getDriver().findElements(By.xpath("//*[contains(text(), 'Anulada')]")).size();
        assertTrue(count > 0);
        LoginSteps.logout();
    }
    
}
