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
public class CadastrarComponenteCurricularSteps {

    @E("^deseje cadastrar um novo componente curricular$")
    public void irParaCadastrarComponente() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:btnAdicionarQuestao")).click();
        BrowserManager.waitTime(1000);
    }
    
    @Quando("^o professor clicar no botao adicionar componente$")
    public void clicarBotaoAdicionarComponente() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:btnAdicionarComponente")).click();
        BrowserManager.waitTime(1000);
    }
    
    @E("^preencher o nome do componente curricular$")
    public void preencherNomeComponente(){
        BrowserManager.getDriver().findElement(By.id("formComponente:txtNomeComponente")).sendKeys("Teste");
    }
    
    @E("^clicar no botao salvar componente$")
    public void salvarComponente(){
        BrowserManager.getDriver().findElement(By.id("formComponente:btnSalvarComponenteCurricular")).click();
        BrowserManager.waitTime(1000);
    }
    
    @Entao("^um novo componente sera cadastrado$")
    public void componenteCadastrado(){
        Select selComponente = new Select(BrowserManager.getDriver().findElement(By.id("form:selComponenteCurricular_input")));
        assertTrue(!selComponente.getOptions().isEmpty());
    }
    
    @Entao("^sera exibido mensagem para componente duplicado$")
    public void exibirMensagemComponenteDuplicado() throws Throwable {
        String mensagem = getMensagemValidacao();
        assertTrue(mensagem.equals("O componente curricular já existe."));
        fecharModalComponente();
        LoginSteps.logout();
    }
    
    @E("^nao preencher o nome do componente curricular$")
    public void naoPreencherNomeComponente(){
        BrowserManager.getDriver().findElement(By.id("formComponente:txtNomeComponente")).sendKeys("");
    }
    
    @Entao("^sera exibido mensagem para nome do componente curricular obrigatorio$")
    public void exibirMensagemNomeComponenteObrigatorio() throws Throwable {
        String mensagem = getMensagemValidacao();
        assertTrue(mensagem.equals("O nome do componente curricular é obrigatório."));
        fecharModalComponente();
        LoginSteps.logout();
    }
    
    @E("^preencher o nome do componente curricular com mais caracteres do que o permitido$")
    public void preencherNomeComponenteMaxCaracteres(){
        BrowserManager.getDriver().findElement(By.id("formComponente:txtNomeComponente")).sendKeys("1234567890"
                + "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"
                + "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"
                + "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
    }
    
    @Entao("^sera exibido mensagem para nome do componente excedeu limite de caracteres$")
    public void exibirMensagemNomeComponenteExcedeuLimite() throws Throwable {
        String mensagem = getMensagemValidacao();
        assertTrue(mensagem.equals("O tamanho do nome é maior do que o máximo permitido."));
        fecharModalComponente();
        LoginSteps.logout();
    }
    
    private String getMensagemValidacao(){
        return BrowserManager.getDriver().findElement(By.xpath("//*[@id='formComponente:msgComponente']/div/ul/li/span")).getText();
    }
    
    private void fecharModalComponente(){
        BrowserManager.getDriver().findElement(By.id("formComponente:btnCancelarComponenteCurricular")).click();
    }
}
