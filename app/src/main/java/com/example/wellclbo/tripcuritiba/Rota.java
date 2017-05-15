package com.example.wellclbo.tripcuritiba;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by wellclbo on 15/04/2017.
 */

public class Rota implements Serializable {
    private int Id ;
    private int PessoaId ;
    private String Nome ;
    private Boolean Status ;
    private Date Data ;
    private List<RotaComPonto> lRotaComPontos;

    public List<RotaComPonto> getlRotaComPontos() {
        return lRotaComPontos;
    }

    public void setlRotaComPontos(List<RotaComPonto> lRotaComPontos) {
        this.lRotaComPontos = lRotaComPontos;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getPessoaId() {
        return PessoaId;
    }

    public void setPessoaId(int pessoaId) {
        PessoaId = pessoaId;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public Boolean getStatus() {
        return Status;
    }

    public void setStatus(Boolean status) {
        Status = status;
    }

    public Date getData() {
        return Data;
    }

    public void setData(Date data) {
        Data = data;
    }

    public Rota(){}
}


