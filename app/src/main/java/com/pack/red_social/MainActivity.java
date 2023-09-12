package com.pack.red_social;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.pack.red_social.Opciones.Mis_Datos;

public class MainActivity extends AppCompatActivity {

    Button LoginBTN, RegistrarBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoginBTN = findViewById(R.id.LoginBTN);
        RegistrarBTN = findViewById(R.id.RegistrarBTN);

        CambioDeletra();

        LoginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Login.class));
            }
        });

        RegistrarBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Registro.class));
            }
        });


    }

    private void CambioDeletra(){
        String link = "Fuentes/static/Inconsolata-Black.ttf";
        Typeface Tf = Typeface.createFromAsset(MainActivity.this.getAssets(), link);

        LoginBTN.setTypeface(Tf);
        RegistrarBTN.setTypeface(Tf);
    }
}