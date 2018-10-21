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
public class VisualizarNotasSimuladoSteps {

    @Quando("^o professor clicar no botão notas do simulado selecionado$")
    public void clicarBotaoResultadosProva() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:tableSimulados:0:btnResultadosSimulado")).click();
        BrowserManager.waitTime(2000);
    }
    
    @Entao("^será exibido a lista de notas dos alunos que realizaram este simulado")
    public void exibirListaNotasProva() throws Throwable {
        int resultados = BrowserManager.getDriver().findElements(By.xpath("//td/span[contains(text(), 'ifpe')]")).size();
        assertTrue(resultados > 0);
        LoginSteps.logout();
    }
    
}
