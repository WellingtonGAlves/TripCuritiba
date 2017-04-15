package com.example.wellclbo.tripcuritiba;

import java.util.Date;

/**
 * Created by wellclbo on 15/04/2017.
 */

public class Rota {
    public int Id ;
    public int PessoaId ;
    public String Nome ;
    public Boolean Status ;
    public Date Data ;


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


