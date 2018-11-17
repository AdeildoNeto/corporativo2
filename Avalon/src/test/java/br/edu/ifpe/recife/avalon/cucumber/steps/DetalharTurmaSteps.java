/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.cucumber.steps;

import br.edu.ifpe.recife.avalon.cucumber.util.BrowserManager;
import cucumber.api.java.pt.Entao;
import cucumber.api.java.pt.Quando;
import static org.junit.Assert.assertTrue;
import org.openqa.selenium.By;

/**
 *
 * @author eduardoamaral
 */
public class DetalharTurmaSteps {
    
    @Quando("^o professor clicar no botão detalhar turma$")
    public void detalharTurma() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:tbTurmas:0:btnDetalharTurma")).click();
        BrowserManager.waitTime(1000);
    }
    
    @Entao("^será exibido a lista de alunos que pertecem a turma selecionada$")
    public void exibirDetalheTurma() throws Throwable {
        int count = BrowserManager.getDriver().findElements(By.xpath("//*[contains(text(), 'Eduardo')]")).size();
        assertTrue(count > 0);
        LoginSteps.logout();
    }
    
}
