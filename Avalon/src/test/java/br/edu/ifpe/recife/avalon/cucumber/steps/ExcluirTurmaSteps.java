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
public class ExcluirTurmaSteps {

    @Quando("^o professor clicar no botão excluir turma$")
    public void excluirTurma() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:tbTurmas:0:btnExcluirTurma")).click();
        BrowserManager.waitTime(1000);
    }
    
    @E("^confirmar a exclusão da turma$")
    public void confirmarExclusaoTurma() {
        BrowserManager.getDriver().findElement(By.id("formModalExcluir:btnConfirmarExclusaoTurma")).click();
        BrowserManager.waitTime(1000);
    }
    
    @Entao("^a turma será desativada e não poderá mais ser utilizada$")
    public void turmaRemovidaSucesso() throws Throwable {
        int count = BrowserManager.getDriver().findElements(By.xpath("//td[contains(text(), 'Turma 2')]")).size();
        assertTrue(count == 0);
        LoginSteps.logout();
    }
    
}
