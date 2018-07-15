/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.model.questao;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author aldenio
 */
@Embeddable
@XmlAccessorType(XmlAccessType.FIELD)
public class Imagem implements Serializable {

    @XmlAttribute
    @NotNull
    @Lob
    @Basic(fetch = FetchType.LAZY, optional = true)
    @Column(name = "BLOB_IMAGEM", nullable = true)
    private byte[] imagem;

    @XmlAttribute(required = true)
    @NotNull
    @Column(name = "TXT_EXTENSAO_ARQUIVO", nullable = true)
    private String extensao;

    @XmlAttribute(required = true)
    @NotBlank
    @Column(name = "TXT_NOME_ARQUIVO", nullable = true)
    private String nome;

    public byte[] getArquivo() {
        return imagem;
    }

    public void setArquivo(byte[] imagem) {
        this.imagem = imagem;
    }

    public String getExtensao() {
        return extensao;
    }

    public void setExtensao(String extensao) {
        this.extensao = extensao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
