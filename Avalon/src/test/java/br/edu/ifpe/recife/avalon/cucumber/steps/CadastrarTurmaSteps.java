/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.cucumber.steps;

import br.edu.ifpe.recife.avalon.cucumber.util.BrowserManager;
import br.edu.ifpe.recife.avalon.cucumber.util.DataSetEnum;
import br.edu.ifpe.recife.avalon.cucumber.util.DbUnitUtil;
import cucumber.api.java.pt.E;
import cucumber.api.java.pt.Entao;
import cucumber.api.java.pt.Quando;
import static org.junit.Assert.assertTrue;
import org.openqa.selenium.By;

/**
 *
 * @author eduardoamaral
 */
public class CadastrarTurmaSteps {

    public CadastrarTurmaSteps() {
        DbUnitUtil.setDataSet(DataSetEnum.PROVAS_SIMULADOS);
        DbUnitUtil.inserirDados();
    }
    
    @E("^esteja na página minhas turmas$")
    public void irParaMinhasTurmas() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("menu:menuTurmas")).click();
        BrowserManager.waitTime(3000);
    }
    
    @Quando("^o professor clicar no botão cadastrar turma$")
    public void irParaCadastrarTurma() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:btnCadastrarTurma")).click();
        BrowserManager.waitTime(1000);
    }
    
    @E("^preencher o nome da turma$")
    public void preencherNomeTurma() {
        BrowserManager.getDriver().findElement(By.id("form:txtNome")).sendKeys("Turma");
        BrowserManager.waitTime(1000);
    }
    
    @E("^não preencher o nome da turma$")
    public void naoPreencherNomeTurma() {
        BrowserManager.getDriver().findElement(By.id("form:txtNome")).clear();
        BrowserManager.waitTime(1000);
    }
    
    @E("^preencher o semestre e ano da turma$")
    public void preencherSemestreAno() {
        BrowserManager.getDriver().findElement(By.id("form:txtSemestreAno")).click();
        BrowserManager.getDriver().findElement(By.id("form:txtSemestreAno")).sendKeys("012018");
        BrowserManager.waitTime(1000);
    }
    
    @E("^não preencher o semestre e ano da turma$")
    public void naoPreencherSemestreAno() {
        BrowserManager.getDriver().findElement(By.id("form:txtSemestreAno")).clear();
        BrowserManager.waitTime(1000);
    }
    
    @E("^selecionar os alunos para a turma$")
    public void selecionarAlunos() {
        BrowserManager.getDriver().findElement(By.id("form:table:chkSelecionarTodos")).click();
        BrowserManager.waitTime(1000);
    }
    
    @E("^não selecionar os alunos para a turma$")
    public void naoSelecionarAlunos() {
    }
    
    @E("^clicar no botão salvar turma$")
    public void salvarTurma() {
        BrowserManager.getDriver().findElement(By.id("form:btnSalvar")).click();
        BrowserManager.waitTime(2000);
    }
    
    @E("^preencher o nome da turma com um valor já utilizado$")
    public void preencherTurmaDuplicada() {
        BrowserManager.getDriver().findElement(By.id("form:txtNome")).sendKeys("Turma 2");
        BrowserManager.waitTime(1000);
    }
    
    
    @Entao("^uma nova turma será cadastrada$")
    public void exibirMensagemComponenteObrigatorio() throws Throwable {
        int count = BrowserManager.getDriver().findElements(By.xpath("//td[contains(text(), 'Turma')]")).size();
        assertTrue(count > 0);
        LoginSteps.logout();
    }
    
}
