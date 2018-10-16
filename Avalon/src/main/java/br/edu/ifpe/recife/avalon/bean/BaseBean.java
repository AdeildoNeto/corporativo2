/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.bean;

import br.edu.ifpe.recife.avalon.model.questao.Questao;
import br.edu.ifpe.recife.avalon.servico.QuestaoServico;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.inject.Named;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author eduardoamaral
 */
@Named(value = BaseBean.BASE_BEAN)
@SessionScoped
public class BaseBean implements Serializable {
    
    public static final String BASE_BEAN = "baseBean";
    
    private String gmt;
   
    @EJB
    private QuestaoServico questaoServico;
    
    public BaseBean() {
        Locale locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
        TimeZone timeZone = Calendar.getInstance(locale).getTimeZone();
        gmt = timeZone.getID();
    }

    /**
     * Recupera a imagem de uma questão.
     *
     * @return
     * @throws IOException
     */
    public StreamedContent getImagem() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            return new DefaultStreamedContent();
        } else {
            String questaoId = context.getExternalContext().getRequestParameterMap().get("questaoId");
            Questao questao = questaoServico.buscarQuestaoPorId(Long.valueOf(questaoId));
            return new DefaultStreamedContent(new ByteArrayInputStream(questao.getImagem().getArquivo()));
        }
    }
    
    public String getGmt() {
        return gmt;
    }
    
    
}
