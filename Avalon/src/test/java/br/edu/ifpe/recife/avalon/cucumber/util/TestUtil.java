/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.cucumber.util;

import org.openqa.selenium.By;

/**
 *
 * @author eduardoamaral
 */
public class TestUtil {
    
    public static String obterMensagemValidacao(){
        return BrowserManager.getDriver().findElement(By.xpath("//*[@id='form:msgPrincipal']/div/ul/li/span")).getText();
    }
    
}
