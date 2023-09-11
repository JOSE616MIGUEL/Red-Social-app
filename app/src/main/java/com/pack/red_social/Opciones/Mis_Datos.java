package com.pack.red_social.Opciones;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pack.red_social.R;
import com.squareup.picasso.Picasso;

public class Mis_Datos extends AppCompatActivity {

    ImageView ImagenDato;
    TextView UidDato, NombresDatos, ApellidosDato, CorreoDato, ContraseniaDato, EdadDato, DireccionDato, TelefonoDato;
    Button Actualizar, ActualizarContrasenia;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    DatabaseReference BASE_DE_DATOS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_datos);

        ActionBar actionBar = getSupportActionBar();
        assert  actionBar != null;
        actionBar.setTitle("Mis datos");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        UidDato = findViewById(R.id.UidDato);
        NombresDatos = findViewById(R.id.NombresDato);
        ApellidosDato = findViewById(R.id.ApellidosDato);
        CorreoDato = findViewById(R.id.CorreoDato);
        ContraseniaDato = findViewById(R.id.ContraseniaDato);
        EdadDato = findViewById(R.id.EdadDato);
        DireccionDato = findViewById(R.id.DireccionDato);
        TelefonoDato = findViewById(R.id.TelefonoDato);
        Actualizar = findViewById(R.id.Actualizar);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        BASE_DE_DATOS = FirebaseDatabase.getInstance().getReference("USUARIOS");
        BASE_DE_DATOS.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String uid = ""+snapshot.child("uid").getValue();
                    String nombres = ""+snapshot.child("nombres").getValue();
                    String apellidos = ""+snapshot.child("apellidos").getValue();
                    String correo = ""+snapshot.child("correo").getValue();
                    String contrasenia = ""+snapshot.child("pass").getValue();
                    String direccion = ""+snapshot.child("direccion").getValue();
                    String edad = ""+snapshot.child("edad").getValue();
                    String telefono = ""+snapshot.child("telefono").getValue();

                    UidDato.setText(uid);
                    NombresDatos.setText(nombres);
                    ApellidosDato.setText(apellidos);
                    CorreoDato.setText(correo);
                    ContraseniaDato.setText(contrasenia);
                    DireccionDato.setText(direccion);
                    EdadDato.setText(edad);
                    TelefonoDato.setText(telefono);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}