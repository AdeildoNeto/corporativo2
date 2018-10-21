/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.cucumber.run;

import cucumber.api.CucumberOptions;
import cucumber.api.SnippetType;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 *
 * @author eduardoamaral
 */
@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features/visualizarResultadoProva.feature",
        glue = "br.edu.ifpe.recife.avalon.cucumber.steps",
        monochrome = false,
        snippets = SnippetType.CAMELCASE)
public class VisualizarResutaldoProvaRun {
    
}
