package com.pack.red_social;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Inicio extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    Button CerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        ActionBar actionBar = getSupportActionBar();
        assert  actionBar != null;
        actionBar.setTitle("Inicio");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        CerrarSesion = findViewById(R.id.CerrarSesion);

        CerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cerrarsesion();
            }
        });
    }

    private void Cerrarsesion() {
        firebaseAuth.signOut();
        Toast.makeText(this, "Ha cerrado la sesión", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Inicio.this, MainActivity.class));
    }

    @Override
    protected void onStart(){
        VerificacionInicioSesion();
        super.onStart();
    }

    private void VerificacionInicioSesion(){
        if (firebaseUser != null){
            Toast.makeText(this, "Se ha iniciado sesión", Toast.LENGTH_SHORT).show();
        }

        else {
            startActivity(new Intent(Inicio.this, MainActivity.class));
            finish();
        }
    }
}