/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.model.questao;

import br.edu.ifpe.recife.avalon.util.AvalonUtil;
import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 *
 * @author aldenio
 */
@Entity
@Table(name = "TB_MULTIPLA_ESCOLHA")
@DiscriminatorValue("M")
@PrimaryKeyJoinColumn(name = "ID_MULTIPLA_ESCOLHA", referencedColumnName = "ID_QUESTAO")
public class MultiplaEscolha extends Questao implements Serializable{
    
    @OneToMany(mappedBy = "questao", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Alternativa> alternativas;
    
    @NotNull(message = "{questao.reposta.obrigatoria}")
    @Column(name = "OP_CORRETA")
    private Integer opcaoCorreta;
            
    @Transient
    private Integer respostaUsuario;

    @Override
    public String formatarQuestao(){
        StringBuilder sb = new StringBuilder();
        
        for(int i = 0; i < this.alternativas.size(); i++){
            char opcao = (char) (i+97);
            sb.append(this.alternativas.get(i).formatarAlternativa(opcao));
            sb.append(AvalonUtil.quebrarLinha());
        }
        
        return sb.toString();
    }
    
    @Override
    public String formatarResposta(){
        Character alternativaCorreta = (char) (opcaoCorreta + 97);
        return alternativaCorreta.toString();
    }
    
    public List<Alternativa> getAlternativas() {
        return alternativas;
    }

    public void setAlternativas(List<Alternativa> alternativas) {
        this.alternativas = alternativas;
    }

    public Integer getOpcaoCorreta() {
        return opcaoCorreta;
    }

    public void setOpcaoCorreta(Integer opcaoCorreta) {
        this.opcaoCorreta = opcaoCorreta;
    }

    public Integer getRespostaUsuario() {
        return respostaUsuario;
    }

    public void setRespostaUsuario(Integer respostaUsuario) {
        this.respostaUsuario = respostaUsuario;
    }
    
}
