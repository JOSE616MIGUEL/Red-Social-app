package com.pack.red_social;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.pack.red_social.Opciones.Mis_Datos;

public class PantallaDeCarga extends AppCompatActivity {

    TextView app_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_de_carga);

        final int Duracion = 2500;
        app_name = findViewById(R.id.app_name);

        CambioDeLetra();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(PantallaDeCarga.this, Inicio.class);
                startActivity(intent);
                finish();
            }
        },Duracion);
    }

    private void CambioDeLetra(){
        String link = "Fuentes/static/Inconsolata-Black.ttf";
        Typeface Tf = Typeface.createFromAsset(PantallaDeCarga.this.getAssets(), link);

        app_name.setTypeface(Tf);
    }
}