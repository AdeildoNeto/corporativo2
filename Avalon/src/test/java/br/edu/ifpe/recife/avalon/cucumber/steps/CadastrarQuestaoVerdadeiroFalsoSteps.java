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
public class CadastrarQuestaoVerdadeiroFalsoSteps {
    
    @E("^preencher o enunciado da questão de verdadeiro ou falso com um valor já cadastrado$")
    public void preencherEnunciadoDuplicado() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:txtEnunciado")).sendKeys("O diagrama de atividade é composto pelos diagramas de estado e de sequência.");
    }

    @Quando("^o professor selecionar o tipo verdadeiro ou falso$")
    public void selecionarTipoVerdadeiroFalso() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:selTipo_label")).click();
        BrowserManager.getDriver().findElement(By.xpath("//*[@data-label='Verdadeiro ou Falso']")).click();
    }
    
    @Entao("^uma nova questão de verdadeiro ou falso será cadastrada$")
    public void questaoCadastrada() {
        int questoes = BrowserManager.getDriver().findElements(By.xpath("//td/span[contains(text(), 'Verdadeiro ou Falso')]")).size();
        assertTrue(questoes > 0);
        LoginSteps.logout();
    }
        
}
