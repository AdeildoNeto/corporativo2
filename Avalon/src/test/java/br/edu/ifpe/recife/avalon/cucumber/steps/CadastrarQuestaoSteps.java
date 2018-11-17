/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.cucumber.steps;

import br.edu.ifpe.recife.avalon.cucumber.util.BrowserManager;
import br.edu.ifpe.recife.avalon.cucumber.util.TestUtil;
import cucumber.api.java.pt.E;
import cucumber.api.java.pt.Entao;
import static org.junit.Assert.assertEquals;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

/**
 *
 * @author eduardoamaral
 */
public class CadastrarQuestaoSteps {

    @E("^selecionar um componente curricular$")
    public void selecionarComponenteCurricular() throws Throwable {
        Select selectComponente = new Select(BrowserManager.getDriver().findElement(By.id("form:selComponenteCurricular_input")));
        selectComponente.selectByIndex(0);
    }

    @E("^preencher o enunciado da questão$")
    public void preencherEnunciado() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:txtEnunciado")).sendKeys("Teste");
    }

    @E("^clicar no botão salvar questão$")
    public void salvarQuestao() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:btnSalvar")).click();
        BrowserManager.waitTime(1000);
    }

    @E("^confirmar o cadastro da questão$")
    public void confirmarCadastro() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:btnConfirmar")).click();
        BrowserManager.waitTime(1000);
    }

    @E("^não preencher o enunciado da questão$")
    public void naoPreencherEnunciado() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:txtEnunciado")).clear();
    }
    
    @E("^preencher o enunciado da questão com mais caracteres que o permitido$")
    public void preencherEnunciadoMaiorPermitido() throws Throwable {
        String enunciado = TestUtil.obterTextoRepetido("ABCDEFGHIJKLMNOPQRSTUVWXYZ", 80);

        BrowserManager.getDriver().findElement(By.id("form:txtEnunciado"))
                .sendKeys(enunciado);
    }
    
    @E("^preencher a solução da questão com mais caracteres que o permitido$")
    public void preencherSolucaoMaiorPermitido() throws Throwable {
        String enunciado = TestUtil.obterTextoRepetido("ABCDEFGHIJKLMNOPQRSTUVWXYZ", 12);

        BrowserManager.getDriver().findElement(By.id("form:txtSolucao"))
                .sendKeys(enunciado);
    }
    
}
