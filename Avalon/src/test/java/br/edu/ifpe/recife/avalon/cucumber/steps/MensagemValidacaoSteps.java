/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.cucumber.steps;

import br.edu.ifpe.recife.avalon.cucumber.util.BrowserManager;
import br.edu.ifpe.recife.avalon.cucumber.util.TestUtil;
import cucumber.api.java.pt.Entao;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author eduardoamaral
 */
public class MensagemValidacaoSteps {
    
    @Entao("^será exibida a mensagem \"(.*)\"$")
    public void exibirMensagemValidacao(String mensagemEsperada) {
        String mensagem = TestUtil.obterMensagemValidacao();
        assertEquals(mensagemEsperada, mensagem);
        BrowserManager.waitTime(1000);
        LoginSteps.logout();
    }
    
}
