package com.example.wellclbo.tripcuritiba;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by wellclbo on 09/04/2017.
 */

public class Pessoa implements Serializable
{
    private String nome;
    private String email;
    private boolean aceito;
    private Date dataCadastro;
    private List<Rota> Rotas;

    public List<Rota> getRotas() {
        return Rotas;
    }

    public void setRotas(List<Rota> rotas) {
        Rotas = rotas;
    }

    public Pessoa(){}


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAceito() {
        return aceito;
    }

    public void setAceito(boolean aceito) {
        this.aceito = aceito;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
}
