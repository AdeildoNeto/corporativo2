/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.bean;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author eduardoamaral
 */
@Named(value = BaseBean.BASE_BEAN)
@SessionScoped
public class BaseBean implements Serializable {
    
    public static final String BASE_BEAN = "baseBean";
    
    private String gmt;
   
    public BaseBean() {
        Locale locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
        TimeZone timeZone = Calendar.getInstance(locale).getTimeZone();
        gmt = timeZone.getID();
    }

    public String getGmt() {
        return gmt;
    }
    
    
}
