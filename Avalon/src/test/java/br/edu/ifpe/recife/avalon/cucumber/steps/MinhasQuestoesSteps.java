/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.cucumber.steps;

import br.edu.ifpe.recife.avalon.cucumber.util.BrowserManager;
import cucumber.api.java.pt.Dado;
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
public class MinhasQuestoesSteps {

    @E("^esteja na pagina minhas questoes$")
    public void irParaMinhasQuestoes() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("menu:menuQuestoes")).click();
    }

    @E("^deseje cadastrar uma nova questao$")
    public void irParaCadastrarQuestao() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:btnAdicionarQuestao")).click();
        BrowserManager.waitTime(3000);
    }
    
}
