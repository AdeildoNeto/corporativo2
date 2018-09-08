/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.cucumber.steps;

import br.edu.ifpe.recife.avalon.cucumber.util.BrowserManager;
import cucumber.api.java.pt.E;
import cucumber.api.java.pt.Entao;
import static org.junit.Assert.assertEquals;
import org.openqa.selenium.By;

/**
 *
 * @author eduardoamaral
 */
public class DetalharQuestaoSteps {

    @E("^clicar no ícone de informação da questão$")
    public void detalharQuestao() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:table:0:btnDetalhar")).click();
        BrowserManager.waitTime(1000);
    }

    @Entao("^será exibido um modal com as informações da questão discursiva$")
    public void detalharQuestaoDiscursiva() {
        String tipo = obterTipoQuestao();
        assertEquals("Discursiva", tipo);
        fecharModalDetalhe();
        LoginSteps.logout();
    }
    
    @Entao("^será exibido um modal com as informações da questão de verdadeiro ou falso$")
    public void detalharQuestaoVF() {
        String tipo = obterTipoQuestao();
        assertEquals("Verdadeiro ou Falso", tipo);
        fecharModalDetalhe();
        LoginSteps.logout();
    }
    
    @Entao("^será exibido um modal com as informações da questão de múltipla escolha$")
    public void detalharQuestaoMultiplaEscolha() {
        String tipo = obterTipoQuestao();
        assertEquals("Múltipla Escolha", tipo);
        fecharModalDetalhe();
        LoginSteps.logout();
    }
    
    private void fecharModalDetalhe(){
        BrowserManager.getDriver().findElement(By.id("formDetalhes:btnVotarDetalhar")).click();
        BrowserManager.waitTime(1000);
    }
    
    private String obterTipoQuestao(){
        return BrowserManager.getDriver().findElement(By.id("formDetalhes:lbDetalheTipoQuestao")).getText();
    }

}
