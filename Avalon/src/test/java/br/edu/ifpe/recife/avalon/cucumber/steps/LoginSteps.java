/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.cucumber.steps;

import br.edu.ifpe.recife.avalon.cucumber.util.BrowserManager;
import cucumber.api.java.pt.Dado;
import org.openqa.selenium.By;

/**
 *
 * @author eduardoamaral
 */
public class LoginSteps {

    @Dado("^que o usuário está logado como professor$")
    public void logarComoProfessor() throws Throwable {
        LoginSteps.logar("avalonifpe@gmail.com", "Mantra1994");
    }

    public static void logar(String login, String senha) {

        BrowserManager.openBrowser("https://localhost:8181/Avalon/index.xhtml");
        BrowserManager.waitTime(5000);

        BrowserManager.getDriver().findElement(By.id("loginForm:btnGoogleLogin")).click();
        BrowserManager.waitTime(5000);

        String mainHandle = BrowserManager.getDriver().getWindowHandle();
        String[] handles = BrowserManager.getDriver().getWindowHandles().toArray(new String[0]);
        BrowserManager.getDriver().switchTo().window(handles[handles.length - 1]);

        BrowserManager.getDriver().findElement(By.id("identifierId")).sendKeys(login);
        BrowserManager.getDriver().findElement(By.id("identifierNext")).click();
        BrowserManager.waitTime(5000);

        BrowserManager.getDriver().findElement(By.name("password")).sendKeys(senha);
        BrowserManager.getDriver().findElement(By.id("passwordNext")).click();
        BrowserManager.waitTime(5000);

        BrowserManager.getDriver().switchTo().window(mainHandle);
        BrowserManager.waitTime(5000);

    }

    public static void logout() {
        BrowserManager.getDriver().findElement(By.id("menu:logout")).click();
        BrowserManager.getDriver().close();
    }

}
