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
import org.openqa.selenium.support.ui.Select;

/**
 *
 * @author eduardoamaral
 */
public class CadastrarQuestaoDiscursivaSteps {

    @E("^preencher o enunciado da questão discursiva com um valor já cadastrado$")
    public void preencherEnunciadoDuplicado() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:txtEnunciado")).sendKeys("O que é um diagrama de classe?");
    }
    
    @Quando("^o professor selecionar o tipo discursiva$")
    public void selecionarTipoDiscursiva() throws Throwable {
        Select selectTipo = new Select(BrowserManager.getDriver().findElement(By.id("form:selTipo_input")));
        selectTipo.selectByValue("DISCURSIVA");
    }
    
    @Entao("^uma nova questão discursiva será cadastrada$")
    public void questaoCadastrada() {
        int questoes = BrowserManager.getDriver().findElements(By.xpath("//td/span[contains(text(), 'Discursiva')]")).size();
        assertTrue(questoes > 0);
        LoginSteps.logout();
    }
    
}
