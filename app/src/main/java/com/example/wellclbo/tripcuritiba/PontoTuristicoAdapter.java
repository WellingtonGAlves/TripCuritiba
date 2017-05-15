package com.example.wellclbo.tripcuritiba;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by wellclbo on 17/04/2017.
 */

public class PontoTuristicoAdapter extends ArrayAdapter<PontoTuristico> {
    private List<PontoTuristico> pontoTuristicos;
    private int layout;

    public PontoTuristicoAdapter(Context context, int resource, List<PontoTuristico> pontoTuristicos){
        super(context,resource,pontoTuristicos);
        this.pontoTuristicos = pontoTuristicos;
        layout = resource;
    }


    @Override
    public View getView(int position, View contentView, ViewGroup parent){
        View localView = contentView;

        if(localView == null){
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            localView = inflater.inflate(layout,null);
        }

        PontoTuristico pontoTuristico = pontoTuristicos.get(position);

        if(pontoTuristico != null){

            TextView textNome = (TextView) localView.findViewById(R.id.lblNomePonto);
            ImageView imageView = (ImageView) localView.findViewById(R.id.imagenPonto);

            if(textNome != null){

                textNome.setText(pontoTuristico.getNome_ponto());
            }
            imageView.setImageResource(R.drawable.ponto1);

        }
        return localView;
    }
}

