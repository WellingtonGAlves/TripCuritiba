package com.example.wellclbo.tripcuritiba;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by wellclbo on 17/04/2017.
 */

public class RotaAdapter extends ArrayAdapter<Rota> {
    private List<Rota> rotas;
    private int layout;

    public RotaAdapter(Context context, int resource, List<Rota> rotas){
        super(context,resource,rotas);
        this.rotas = rotas;
        layout = resource;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup parent){
        View localView = contentView;

        if(localView == null){
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            localView = inflater.inflate(layout,null);
        }

        Rota rota = rotas.get(position);

        if(rota != null){

            TextView textNome = (TextView) localView.findViewById(R.id.textNome);


            if(textNome != null){
                textNome.setText(rota.getNome());
            }

        }
        return localView;
    }
}

