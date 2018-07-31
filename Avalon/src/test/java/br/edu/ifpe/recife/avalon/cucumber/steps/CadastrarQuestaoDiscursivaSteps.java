/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.cucumber.steps;

import br.edu.ifpe.recife.avalon.cucumber.util.BrowserManager;
import cucumber.api.java.pt.Quando;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

/**
 *
 * @author eduardoamaral
 */
public class CadastrarQuestaoDiscursivaSteps {

    @Quando("^o professor selecionar o tipo discursiva$")
    public void selecionarTipoDiscursiva() throws Throwable {
        Select selectTipo = new Select(BrowserManager.getDriver().findElement(By.id("form:selTipo_input")));
        selectTipo.selectByValue("DISCURSIVA");
    }
    
}
