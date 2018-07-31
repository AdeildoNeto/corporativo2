/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.cucumber.steps;

import br.edu.ifpe.recife.avalon.cucumber.util.BrowserManager;
import cucumber.api.java.pt.E;
import cucumber.api.java.pt.Entao;
import static org.junit.Assert.assertTrue;
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
    
    @E("^informar o enunciado da questao$")
    public void preencherEnunciado() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:txtEnunciado")).sendKeys("Teste");
    }
    
    @E("^clicar no botao salvar questao$")
    public void salvarQuestao() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:btnSalvar")).click();
        BrowserManager.waitTime(1000);
    }
    
    @E("^confirmar cadastro da questao$")
    public void confirmarCadastro() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:btnSimAdicionar")).click();
        BrowserManager.waitTime(1000);
    }
    
    @Entao("^uma nova questao sera cadastrada$")
    public void questaoCadastrada(){
        int questoes = BrowserManager.getDriver().findElements(By.xpath("//*[@id='form:tbMinhasQuestoes']/tbody/tr")).size();
        assertTrue(questoes > 0);
        LoginSteps.logout();
    }
    
    @E("^nao informar o enunciado da questao$")
    public void naoPreencherEnunciado() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:txtEnunciado")).sendKeys("");
    }
    
    @Entao("^sera exibido mensagem para enunciado obrigatorio$")
    public void exibirMensagemEnunciadoObrigatorio() throws Throwable {
        String mensagem = getMensagemValidacao();
        assertTrue(mensagem.equals("O enunciado da questão é obrigatório."));
        LoginSteps.logout();
    }
    
    @Entao("^sera exibido mensagem para questao duplicada$")
    public void exibirMensagemQuestaoDuplicada() throws Throwable {
        String mensagem = getMensagemValidacao();
        assertTrue(mensagem.equals("Já existe uma questão com este enunciado."));
        LoginSteps.logout();
    }
    
    private String getMensagemValidacao(){
        return BrowserManager.getDriver().findElement(By.xpath("//*[@id='form:msgPrincipal']/div/ul/li/span")).getText();
    }
    
}
