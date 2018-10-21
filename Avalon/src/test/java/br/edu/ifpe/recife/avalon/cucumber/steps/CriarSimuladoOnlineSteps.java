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
import cucumber.api.java.pt.Quando;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import org.openqa.selenium.By;

/**
 *
 * @author eduardoamaral
 */
public class CriarSimuladoOnlineSteps {

    @Quando("^o professor preencher o título do simulado$")
    public void preencherTituloSimulado() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:txtTitulo")).sendKeys("Simulado");
    }
    
    @Quando("^o professor não preencher o título do simulado$")
    public void naoPreencherTituloSimulado() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:txtTitulo")).sendKeys("");
    }

    @E("^selecionar questões para simulado$")
    public void selecionarTodasQuestoes() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:table:chkSelecionarTodas")).click();
        BrowserManager.waitTime(2000);
    }

    @E("^clicar no botão salvar novo simulado$")
    public void salvarNovaProva() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:btn")).click();
        BrowserManager.waitTime(3000);
    }

    @Entao("^um novo simulado do tipo verdadeiro ou falso sera criada$")
    public void salvarSimuladoVF() {
        int resultado = obterListaSimulados();
        assertTrue(resultado > 0);
        LoginSteps.logout();
    }

    @Entao("^um novo simulado do tipo múltipla escolha sera criada$")
    public void salvarSimuladoMultiplaEscolha() {
        int resultado = obterListaSimulados();
        assertTrue(resultado > 0);
        LoginSteps.logout();
    }

    @Entao("^será exibido mensagem para titulo do simulado obrigatório$")
    public void criticarTituloObrigatorio() {
        String mensagem = TestUtil.obterMensagemValidacao();
        assertEquals("O título do simulado é obrigatório.", mensagem);
        BrowserManager.waitTime(1000);
        LoginSteps.logout();
    }
    
    private int obterListaSimulados() {
        return BrowserManager.getDriver().findElements(By.xpath("//td/span[contains(text(), 'Simulado')]")).size();
    }


}
