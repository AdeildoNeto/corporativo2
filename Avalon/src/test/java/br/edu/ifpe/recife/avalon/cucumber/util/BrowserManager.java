/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.cucumber.util;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 *
 * @author eduardoamaral
 */
public class BrowserManager {

    private static WebDriver driver;

    public static void openBrowser(String url) {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver");
        
        driver = new ChromeDriver();
        driver.manage().window();
        
        driver.get(url);
    }

    public static void waitTime(long millis) {
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException ex) {
            Logger.getLogger(BrowserManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static WebDriver getDriver() {
        return driver;
    }
    
}
