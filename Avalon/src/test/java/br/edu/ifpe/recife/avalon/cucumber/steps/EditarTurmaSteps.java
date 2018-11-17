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
public class EditarTurmaSteps {
    
    @Quando("^o professor clicar no botão editar turma$")
    public void editarTurma() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:tbTurmas:0:btnEditarTurma")).click();
        BrowserManager.waitTime(1000);
    }
    
    @E("^alterar o nome da turma$")
    public void alterarNome() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:txtNome")).clear();
        BrowserManager.getDriver().findElement(By.id("form:txtNome")).sendKeys("Turma 2019");
        BrowserManager.waitTime(1000);
    }
    
    @E("^clicar no botão salvar edição da turma$")
    public void salvarEdicao() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:btnSalvar")).click();
        BrowserManager.waitTime(1000);
    }
    
    @Entao("^as alterações da turma serão efetivadas$")
    public void verificarTurmaAlterada() throws Throwable {
        int count = BrowserManager.getDriver().findElements(By.xpath("//*[contains(text(), 'Turma 2019')]")).size();
        assertTrue(count > 0);
        LoginSteps.logout();
    }
    
}
