package com.pack.red_social;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Registro extends AppCompatActivity {

    EditText Correo, Contrasenia, Nombres, Apellidos, Edad, Telefono, Direccion;
    Button REGISTRARUSUARIO;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        ActionBar actionBar = getSupportActionBar();
        assert  actionBar != null;
        actionBar.setTitle("Registro");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Correo = findViewById(R.id.Correo);
        Contrasenia = findViewById(R.id.Contrasenia);
        Nombres = findViewById(R.id.Nombres);
        Apellidos = findViewById(R.id.Apellidos);
        Edad = findViewById(R.id.Edad);
        Telefono = findViewById(R.id.Telefono);
        Direccion = findViewById(R.id.Direccion);
        REGISTRARUSUARIO = findViewById(R.id.REGISTRARUSUARIO);

        firebaseAuth = FirebaseAuth.getInstance();

        REGISTRARUSUARIO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo = Correo.getText().toString();
                String pass = Contrasenia.getText().toString();

                if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
                    Correo.setError("Correo no es válido");
                    Correo.setFocusable(true);
                }else if (pass.length() <6){
                    Contrasenia.setError("Contraseña debe ser mayor a 6");
                    Contrasenia.setFocusable(true);
                }else {
                    REGISTRAR(correo,pass);
                }
            }
        });
    }

    private void REGISTRAR(String correo, String pass) {
        firebaseAuth.createUserWithEmailAndPassword(correo, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            assert user != null;
                            String uid = user.getUid();
                            String correo = Correo.getText().toString();
                            String pass = Contrasenia.getText().toString();
                            String nombres = Nombres.getText().toString();
                            String apellidos = Apellidos.getText().toString();
                            String edad = Edad.getText().toString();
                            String telefono = Telefono.getText().toString();
                            String direccion = Direccion.getText().toString();

                            HashMap <Object,String> DatoUsuario = new HashMap<>();

                            DatoUsuario.put("uid", uid);
                            DatoUsuario.put("correo", correo);
                            DatoUsuario.put("pass", pass);
                            DatoUsuario.put("nombres", nombres);
                            DatoUsuario.put("apellidos", apellidos);
                            DatoUsuario.put("edad", edad);
                            DatoUsuario.put("telefono", telefono);
                            DatoUsuario.put("direccion", direccion);
                            
                            DatoUsuario.put("imagen","");

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            
                            DatabaseReference reference = database.getReference("USUARIOS");
                            
                            reference.child(uid).setValue(DatoUsuario);
                            Toast.makeText(Registro.this, "Usuario registrado", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(Registro.this, Inicio.class));

                        }else {
                            Toast.makeText(Registro.this, "Algo salio mal", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Registro.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}