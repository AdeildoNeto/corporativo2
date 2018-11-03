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
public class CadastrarQuestaoMultiplaEscolhaSteps {

    @Quando("^o professor selecionar o tipo múltipla escolha$")
    public void selecionarTipoMultiplaEscolha() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:selTipo_label")).click();
        BrowserManager.getDriver().findElement(By.xpath("//*[@data-label='Múltipla Escolha']")).click();
        BrowserManager.waitTime(2000);
    }

    @E("^preencher todas as alternativas da questão$")
    public void preencherTodasAlternativas() {
        BrowserManager.getDriver().findElement(By.id("form:alternativas:0:alternativa"))
                .sendKeys("A");
        BrowserManager.getDriver().findElement(By.id("form:alternativas:1:alternativa"))
                .sendKeys("B");
        BrowserManager.getDriver().findElement(By.id("form:alternativas:2:alternativa"))
                .sendKeys("C");
        BrowserManager.getDriver().findElement(By.id("form:alternativas:3:alternativa"))
                .sendKeys("D");
        BrowserManager.getDriver().findElement(By.id("form:alternativas:4:alternativa"))
                .sendKeys("E");
    }

    @E("^definir a alternativa correta$")
    public void definirAlternativaCorreta() {
        BrowserManager.getDriver().findElement(By.xpath("//*[@id='form:radio:0_clone']")).click();
    }
    
    @Entao("^uma nova questão de múltipla escolha será cadastrada$")
    public void questaoCadastrada() {
        int questoes = BrowserManager.getDriver().findElements(By.xpath("//td/span[contains(text(), 'Múltipla Escolha')]")).size();
        assertTrue(questoes > 0);
        LoginSteps.logout();
    }

    @E("^não preencher um das alternativas da questão$")
    public void naoPreencherAlternativas() {
        BrowserManager.getDriver().findElement(By.id("form:alternativas:0:alternativa"))
                .sendKeys("");
    }

    @E("^preencher as alternativas iguais para a questão$")
    public void preencherAlternativasIguais() {
        BrowserManager.getDriver().findElement(By.id("form:alternativas:0:alternativa"))
                .sendKeys("A");
        BrowserManager.getDriver().findElement(By.id("form:alternativas:1:alternativa"))
                .sendKeys("A");
        BrowserManager.getDriver().findElement(By.id("form:alternativas:2:alternativa"))
                .sendKeys("a");
        BrowserManager.getDriver().findElement(By.id("form:alternativas:3:alternativa"))
                .sendKeys("B");
        BrowserManager.getDriver().findElement(By.id("form:alternativas:4:alternativa"))
                .sendKeys("C");
    }

    @E("^não definir a alternativa correta$")
    public void naoDefinirAlternativaCorreta() {
    }

}
