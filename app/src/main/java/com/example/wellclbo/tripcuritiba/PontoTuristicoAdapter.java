package com.example.wellclbo.tripcuritiba;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wellclbo on 17/04/2017.
 */

public class PontoTuristicoAdapter extends ArrayAdapter<PontoTuristico> {
    private List<PontoTuristico> pontoTuristicos;
    private int layout;
    private ImageView imageViewThis;
    public static Bundle bundle = new Bundle();
    private static String caminhoImagem;
    public static List<String> arrayString = new ArrayList<>();


    static List<Bitmap> lista = new ArrayList<Bitmap>();

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

            ImageView imageView;
            imageView = (ImageView) localView.findViewById(R.id.imagenPonto);

            if(textNome != null){

                textNome.setText(pontoTuristico.getNome_ponto());
            }
            // imageView.setImageResource(R.drawable.ponto1);
            int i=-1;
            String caminhoImagem;
            for (Bitmap bitmap:lista) {
                i = i+1;
                caminhoImagem = arrayString.get(i);
                if( caminhoImagem.equals(pontoTuristico.getImagem_historia().toString())) {
                    imageView.setImageBitmap(bitmap);
                }
            }

        }

        return localView;
    }
    public static Bitmap loadImage (URL url) throws IOException {


        InputStream inputStream;
        Bitmap myBitMap;

        inputStream = url.openStream();
        myBitMap = BitmapFactory.decodeStream(inputStream);

        inputStream.close();
        return myBitMap;

    }
    //Carrega imagens para o app
    public static class DownloadImageAsync extends AsyncTask<URL,Integer,Bitmap> {
        ProgressDialog progress;
        @Override
        protected Bitmap doInBackground(URL... params) {
            Bitmap myBitmap = null;
            try{
                myBitmap = Util.loadImage(params[0]);
            }catch(IOException e){

            }
            return myBitmap;
        }
        @Override
        protected void onPreExecute(){
        }
        @Override
        protected void onPostExecute(Bitmap bitmap){
            if(bitmap != null){
                lista.add(bitmap);

            }
        }
    }
}

