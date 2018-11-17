/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.model.turma;

import br.edu.ifpe.recife.avalon.model.avaliacao.prova.Prova;
import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author eduardoamaral
 */
@Entity
@Table(name = "TB_TURMA")
@NamedQueries(
        {
            @NamedQuery(
                    name = "Turma.PorProfessor",
                    query = "SELECT t FROM Turma t WHERE t.professor = :professor "
                    + " AND t.ativa = true"
            )
            ,
            @NamedQuery(
                    name = "Turma.PorNomeProfessor",
                    query = "SELECT t FROM Turma t WHERE t.ativa = true "
                    + " AND t.professor = :professor"
                    + " AND t.nome = :nome"
                    + " AND (:idTurma IS NULL OR t.id <> :idTurma)"
            )
        }
)
public class Turma implements Serializable, Comparable<Turma> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_TURMA")
    private Long id;

    @Size(max = 40, message = "{turma.nome.tamanho.maximo}")
    @NotBlank(message = "{turma.nome.obrigatorio}")
    @Column(name = "TXT_NOME", columnDefinition = "varchar(40)")
    private String nome;

    @NotBlank(message = "{turma.semestre.ano.obrigatorio}")
    @Column(name = "TXT_SEMESTRE_ANO")
    private String semestreAno;

    @NotNull(message = "{turma.professor.obrigatorio}")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_PROFESSOR", referencedColumnName = "ID")
    private Usuario professor;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "TB_TURMA_ALUNO", joinColumns = {
        @JoinColumn(name = "ID_TURMA")},
            inverseJoinColumns = {
                @JoinColumn(name = "ID_USUARIO")
            })
    private List<Usuario> alunos;

    @OneToMany(mappedBy = "turma", fetch = FetchType.LAZY, orphanRemoval = false)
    private List<Prova> provas;

    @Column(name = "SN_ATIVA", nullable = false)
    private boolean ativa = true;

    public Turma() {
        super();
    }

    public Turma(String nome) {
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSemestreAno() {
        return semestreAno;
    }

    public void setSemestreAno(String semestreAno) {
        this.semestreAno = semestreAno;
    }

    public Usuario getProfessor() {
        return professor;
    }

    public void setProfessor(Usuario professor) {
        this.professor = professor;
    }

    public List<Usuario> getAlunos() {
        return alunos;
    }

    public void setAlunos(List<Usuario> alunos) {
        this.alunos = alunos;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }

    public List<Prova> getProvas() {
        return provas;
    }

    public void setProvas(List<Prova> provas) {
        this.provas = provas;
    }

    @Override
    public int compareTo(Turma o) {
        return this.nome.compareTo(o.getNome());
    }

}
