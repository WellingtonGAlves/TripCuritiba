package com.example.wellclbo.tripcuritiba;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by wellclbo on 15/04/2017.
 */

public class RotaComPonto implements Serializable {
    private int Id ;
    private int PontoTuristicoId ;
    private int RotaId ;
    private PontoTuristico PontoTuristico ;

    public com.example.wellclbo.tripcuritiba.PontoTuristico getPontoTuristico() {
        return PontoTuristico;
    }

    public void setPontoTuristico(com.example.wellclbo.tripcuritiba.PontoTuristico pontoTuristico) {
        PontoTuristico = pontoTuristico;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getPontoTuristicoId() {
        return PontoTuristicoId;
    }

    public void setPontoTuristicoId(int pontoTuristicoId) {
        PontoTuristicoId = pontoTuristicoId;
    }

    public int getRotaId() {
        return RotaId;
    }

    public void setRotaId(int rotaId) {
        RotaId = rotaId;
    }

    public RotaComPonto(){}
}


