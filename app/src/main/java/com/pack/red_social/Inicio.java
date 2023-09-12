package com.pack.red_social;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pack.red_social.Opciones.Mis_Datos;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Inicio extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference BASE_DE_DATOS;

    Button MisDatosOpcion, CrearPublicacionOpcion, PublicacionesOpcion, UsuarioOpcion, ChatsOpcion;

    ImageView foto_perfil;

    TextView fecha;
    TextView uidPerfil, correoPerfil, nombrePerfil;

    Button CerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        ActionBar actionBar = getSupportActionBar();
        assert  actionBar != null;
        actionBar.setTitle("Inicio");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        BASE_DE_DATOS = firebaseDatabase.getReference("USUARIOS");

        fecha = findViewById(R.id.fecha);
        foto_perfil = findViewById(R.id.foto_perfil);
        uidPerfil = findViewById(R.id.uidPerfil);
        correoPerfil = findViewById(R.id.correoPerfil);
        nombrePerfil = findViewById(R.id.nombresPerfil);

        MisDatosOpcion = findViewById(R.id.MisDatosOpcion);
        CrearPublicacionOpcion = findViewById(R.id.CrearPublicacionOpcion);
        PublicacionesOpcion = findViewById(R.id.PublicacionesOpcion);
        UsuarioOpcion = findViewById(R.id.UsuarioOpcion);
        ChatsOpcion = findViewById(R.id.ChatOpcion);

        CerrarSesion = findViewById(R.id.CerrarSesion);

        CambioLetra();

        Date date = new Date();
        SimpleDateFormat fechaC = new SimpleDateFormat("d'de'MMM'del'yyyy");
        String sFecha = fechaC.format(date);
        fecha.setText(sFecha);

        MisDatosOpcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Inicio.this, Mis_Datos.class);
                startActivity(intent);
                Toast.makeText(Inicio.this, "Mis datos", Toast.LENGTH_SHORT).show();
            }
        });

        CerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cerrarsesion();
            }
        });
    }

    private void CargarDatos(){
        Query query = BASE_DE_DATOS.orderByChild("correo").equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    String uid = ""+ds.child("uid").getValue();
                    String correo = ""+ds.child("correo").getValue();
                    String nombres = ""+ds.child("nombres").getValue();
                    String image = ""+ds.child("imagen").getValue();

                    uidPerfil.setText(uid);
                    correoPerfil.setText(correo);
                    nombrePerfil.setText(nombres);

                    try {

                        Picasso.get().load(image).placeholder(R.drawable.baseline_person_24).into(foto_perfil);

                    }catch (Exception e){
                        Picasso.get().load(R.drawable.baseline_person_24).into(foto_perfil);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void CambioLetra(){
        String link = "Fuentes/static/Inconsolata-Black.ttf";
        Typeface Tf = Typeface.createFromAsset(Inicio.this.getAssets(), link);

        fecha.setTypeface(Tf);
        correoPerfil.setTypeface(Tf);
        nombrePerfil.setTypeface(Tf);

        MisDatosOpcion.setTypeface(Tf);
        CrearPublicacionOpcion.setTypeface(Tf);
        PublicacionesOpcion.setTypeface(Tf);
        UsuarioOpcion.setTypeface(Tf);
        ChatsOpcion.setTypeface(Tf);
        CerrarSesion.setTypeface(Tf);
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
            CargarDatos();
            Toast.makeText(this, "Se ha iniciado sesión", Toast.LENGTH_SHORT).show();
        }

        else {
            startActivity(new Intent(Inicio.this, MainActivity.class));
            finish();
        }
    }
}