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
public class VisualizarNotasProvaSteps {

    @Quando("^o professor clicar no botão notas da prova selecionada$")
    public void preencherFiltroEnunciado() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:txtEnunciado")).sendKeys("Teste");
    }
    
    @Entao("^será exibido a lista de notas dos alunos que realizaram esta prova$")
    public void exibirMensagemComponenteobrigatório() throws Throwable {
        String mensagem = TestUtil.obterMensagemValidacao();
        assertEquals("Sem resultados para o filtro informado.", mensagem);
        LoginSteps.logout();
    }
    
    private int obterResultado(){
        return BrowserManager.getDriver().findElements(By.xpath("//td/span[contains(text(), 'Teste')]")).size();
    }

}
