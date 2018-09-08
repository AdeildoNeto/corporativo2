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
        BrowserManager.getDriver().findElement(By.id("form:btnSimAdicionar")).click();
        BrowserManager.waitTime(1000);
    }

    @E("^nao preencher o enunciado da questão$")
    public void naoPreencherEnunciado() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:txtEnunciado")).sendKeys("");
    }

    @E("^preencher o enunciado da questão com um valor já cadastrado$")
    public void preencherEnunciadoDuplicado() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:txtEnunciado")).sendKeys("Teste 2");
    }
    
    @Entao("^será exibido mensagem para enunciado obrigatório$")
    public void exibirMensagemEnunciadoobrigatório() throws Throwable {
        String mensagem = TestUtil.obterMensagemValidacao();
        assertEquals("O enunciado da questão é obrigatório.", mensagem);
        LoginSteps.logout();
    }

    @Entao("^será exibido mensagem para questão duplicada$")
    public void exibirMensagemQuestaoDuplicada() throws Throwable {
        String mensagem = TestUtil.obterMensagemValidacao();
        assertEquals("Já existe uma questão com este enunciado.", mensagem);
        LoginSteps.logout();
    }

    @E("^preencher o enunciado da questão com mais caracteres do que o permitido$")
    public void preencherEnunciadoMaiorPermitido() throws Throwable {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < 30; i++) {
            builder.append("0123456789012345678901234567890123456789012345678901234567890123456789");
        }

        BrowserManager.getDriver().findElement(By.id("form:txtEnunciado"))
                .sendKeys(builder.toString());
    }

    @Entao("^será exibido mensagem para enunciado da questão maior que o permitido$")
    public void exibirMensagemEnunciadoLimite() throws Throwable {
        String mensagem = TestUtil.obterMensagemValidacao();
        assertEquals("O tamanho do enunciado é maior do que o máximo permitido.", mensagem);
        LoginSteps.logout();
    }

}
