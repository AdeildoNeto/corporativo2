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
public class VisualizarHistoricoSimuladoSteps {

    @Quando("^o professor clicar no botão resultados do simulado selecionado$")
    public void clicarResultadosSimulado() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:tableSimulados:0:btnResultadosSimulado")).click();
        BrowserManager.waitTime(2000);
    }
    
    @Entao("^será exibido a lista de notas dos alunos que realizaram este simulado")
    public void exibirResultadosSimulado() throws Throwable {
        int resultados = BrowserManager.getDriver().findElements(By.xpath("//td/span[contains(text(), 'ifpe')]")).size();
        assertTrue(resultados > 0);
        LoginSteps.logout();
    }

    @E("^selecionar um resultado do simulado")
    public void selecionarHistorico() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:tableResultados:0:btnDetalharResultado")).click();
        BrowserManager.waitTime(2000);
    }
    
    @Entao("^será exibido o detalhe do simulado selecionado$")
    public void exibirResultadoSelecionado(){
        int resultados = BrowserManager.getDriver().findElements(By.xpath("//span[contains(text(), '1)')]")).size();
        assertTrue(resultados > 0);
        LoginSteps.logout();
    }
    
    
}
