/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.viewhelper;

import br.edu.ifpe.recife.avalon.model.questao.MultiplaEscolha;
import br.edu.ifpe.recife.avalon.model.questao.Questao;
import br.edu.ifpe.recife.avalon.model.questao.VerdadeiroFalso;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author eduardoamaral
 */
public class PdfGeneratorViewHelper implements Serializable {
    
    Document document;
    
    public void gerarArquivo(OutputStream path, List<Questao> questoes){
        try {
            Rectangle pagesize = new Rectangle(794f, 1123f);
            document = new Document(pagesize, 72f, 48f, 72f, 36f);
            PdfWriter.getInstance(document, path);
            
            document.open();
            
            imprimirCabecalho();
            imprimirQuestoes(questoes);
            
            document.close();
            
        } catch (DocumentException ex) {
            Logger.getLogger(PdfGeneratorViewHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PdfGeneratorViewHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void imprimirCabecalho() throws DocumentException, IOException{
        Image cabecalho = Image.getInstance("https://localhost:8181/Avalon/resources/img/cabecalho.png");
            
        cabecalho.scalePercent(50.0f, 50.0f);
        
        document.add(cabecalho);
    }
    
    private void imprimirQuestoes(List<Questao> questoes) throws DocumentException, IOException{
        Questao questao;
        for (int i = 0; i < questoes.size(); i++) {
            questao = questoes.get(i);
            document.add(new Paragraph(questao.formatarEnunciado(i + 1)));
            imprimirAlternativas(questao);
            document.add(Chunk.NEWLINE);
        }
        
    }
    
    

    private void imprimirAlternativas(Questao questao) throws DocumentException, IOException {
        if(questao instanceof VerdadeiroFalso || questao instanceof MultiplaEscolha){
            float indentation = BaseFont.createFont().getWidthPoint(questao.formatarQuestao(), 1);
            Paragraph alternativas = new Paragraph(questao.formatarQuestao());
            alternativas.setIndentationLeft(indentation);
            
            document.add(alternativas);
        }
    }
    
}
