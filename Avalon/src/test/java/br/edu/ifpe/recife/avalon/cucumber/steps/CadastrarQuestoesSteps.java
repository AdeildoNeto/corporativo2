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
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

/**
 *
 * @author eduardoamaral
 */
public class CadastrarQuestoesSteps {

    @Dado("^que o usuario esta logado como professor$")
    public void logarComoProfessor() throws Throwable {
        LoginSteps.logar("efsa@a.recife.ifpe.edu.br", "Duh/132869");
    }

    @E("^esteja na pagina minhas questoes$")
    public void irParaMinhasQuestoes() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("menu:menuQuestoes")).click();
    }

    @E("^deseje cadastrar uma nova questao$")
    public void irParaCadastrarQuestao() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:btnAdicionarQuestao")).click();
    }

    @Quando("^o professor selecionar o tipo discursiva$")
    public void selecionarTipoDiscursiva() throws Throwable {
        Select selectTipo = new Select(BrowserManager.getDriver().findElement(By.id("form:selTipo_input")));
        selectTipo.selectByValue("DISCURSIVA");
    }
    
    @E("^selecionar o componente curricular$")
    public void selecionarComponenteCurricular() throws Throwable {
        Select selectComponente = new Select(BrowserManager.getDriver().findElement(By.id("form:selComponenteCurricular_input")));
        selectComponente.selectByIndex(0);
    }
    
    @E("^informar o enunciado da questao$")
    public void preencherEnunciado() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:txtEnunciado")).sendKeys("Teste");
    }
    
    @E("^clicar no botao salvar$")
    public void salvarQuestao() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:btnSalvar")).click();
    }
    
    @E("^confirmar cadastro da questao$")
    public void confirmarCadastro() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:btnSimAdicionar")).click();
    }
    
}
