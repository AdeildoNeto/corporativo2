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
import org.openqa.selenium.support.ui.Select;

/**
 *
 * @author eduardoamaral
 */
public class PesquisarQuestoesProvaSteps {

    @Quando("^o professor preencher o filtro enunciado$")
    public void preencherFiltroEnunciado() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:txtEnunciado")).sendKeys("Teste");
    }
    
    @Quando("^o professor preencher o filtro nome do professor$")
    public void preencherFiltroProfessor() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:txtProfessor")).sendKeys("Silva");
    }
    
    @Quando("^o professor selecionar um componente curricular no filtro$")
    public void selecionarComponenteCurricular() throws Throwable {
        Select selectComponente = new Select(BrowserManager.getDriver().findElement(By.id("form:selComponenteCurricular_input")));
        selectComponente.selectByIndex(0);
    }

    @Quando("^o professor selecionar o tipo discursiva no filtro$")
    public void selecionarTipoQuestaoDiscursiva() throws Throwable {
        Select selectComponente = new Select(BrowserManager.getDriver().findElement(By.id("form:selTipoQuestao_input")));
        selectComponente.selectByIndex(0);
    }
    
    @Quando("^o professor optar pelo tipo verdadeiro ou falso no filtro de pesquisa$")
    public void selecionarTipoQuestaoVF() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:selTipoQuestao_label")).click();
        BrowserManager.getDriver().findElement(By.xpath("//*[@data-label='Verdadeiro ou Falso']")).click();
    }
    
    @Quando("^o professor optar pelo tipo múltipla escolha no filtro de pesquisa$")
    public void selecionarTipoQuestaoMultiplaEscolha() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:selTipoQuestao_label")).click();
        BrowserManager.getDriver().findElement(By.xpath("//*[@data-label='Múltipla Escolha']")).click();
    }
    
    @Quando("^o professor preencher o filtro enunciado com um enunciado não existente$")
    public void preencherFiltroEnunciadoSemDados() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:txtEnunciado")).sendKeys("Sed ut perspiciatis unde omnis iste natus error");
    }

    @E("^clicar no botão pesquisar$")
    public void pesquisarQuestoes() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:btnPesquisar")).click();
        BrowserManager.waitTime(1000);
    }

    @Entao("^será exibido a lista de questões onde o enunciado contenha o filtro informado$")
    public void pesquisaPorEnunciado() {
        int resultado = obterResultado();
        assertTrue(resultado > 0);
        LoginSteps.logout();
    }
    
    @Entao("^será exibido a lista de questões onde o nome do autor contenha o filtro informado$")
    public void pesquisaPorProfessor() {
        int resultado = BrowserManager.getDriver().findElements(By.xpath("//td/span[contains(text(), 'Silva')]")).size();
        assertTrue(resultado > 0);
        LoginSteps.logout();
    }
    
    @Entao("^será exibido a lista de questões do componente curricular informado$")
    public void pesquisaPorComponente() {
        int resultado = BrowserManager.getDriver().findElements(By.xpath("//td/span[contains(text(), 'Engenharia')]")).size();
        assertTrue(resultado > 0);
        LoginSteps.logout();
    }
    
    @Entao("^será exibido a lista de questões do tipo discursiva$")
    public void pesquisaPorTipoDiscursiva() {
        int resultado = obterResultado();
        assertTrue(resultado > 0);
        LoginSteps.logout();
    }
    
    @Entao("^será exibido a lista de questões do tipo verdadeiro ou falso$")
    public void pesquisaPorTipoVF() {
        int resultado = obterResultado();
        assertTrue(resultado > 0);
        LoginSteps.logout();
    }
    
    @Entao("^será exibido a lista de questões do tipo múltipla escolha$")
    public void pesquisaPorTipoMultiplaEscolha() {
        int resultado = obterResultado();
        assertTrue(resultado > 0);
        LoginSteps.logout();
    }
    
    @Entao("^será exibido a mensagem sem resultados para o filtro informado$")
    public void exibirMensagemPesquisaSemResultados() throws Throwable {
        String mensagem = TestUtil.obterMensagemValidacao();
        assertEquals("Sem resultados para o filtro informado.", mensagem);
        LoginSteps.logout();
    }
    
    @Quando("^o professor preencher o filtro com o enunciado de uma questão compartilhada$")
    public void preencherFiltroCompartilhado() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:txtEnunciado")).sendKeys("Compartilhada");
    }
    
    @Entao("^será exibido a lista de questões compartilhadas em que o enunciado contenha o filtro informado$")
    public void exibirQuestoesCompartilhadas() throws Throwable {
        int resultados = BrowserManager.getDriver().findElements(By.xpath("//td/span[contains(text(), 'Compartilhada')]")).size();
        assertTrue(resultados > 0);
        LoginSteps.logout();
    }
    
    @Quando("^o professor preencher o filtro com o enunciado de uma questão não compartilhada$")
    public void preencherFiltroNaoCompartilhado() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:txtEnunciado")).sendKeys("Não");
    }
    
    private int obterResultado(){
        return BrowserManager.getDriver().findElements(By.xpath("//td/span[contains(text(), 'Teste')]")).size();
    }

}
