package com.pack.red_social.CambiarContrasenia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pack.red_social.Login;
import com.pack.red_social.R;

import java.util.HashMap;

public class CambiarContrasenia extends AppCompatActivity {

    TextView MisCredencialesTXT, CorreoActualTXT, CorreoActual, ContraseniaActualTXT, ContraseniaActual;
    EditText ActualContraseniaET, NuevoContraseniaEt;
    Button CAMBIARCONTRASENIABTN;
    DatabaseReference USUARIOS;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_contrasenia);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Cambiar Contraseña");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        MisCredencialesTXT = findViewById(R.id.MisCredencialesTXT);
        CorreoActualTXT = findViewById(R.id.CorreoActualTXT);
        CorreoActual = findViewById(R.id.CorreoActual);
        ContraseniaActualTXT = findViewById(R.id.CorreoActualTXT);
        ContraseniaActual = findViewById(R.id.ContraseniaActual);
        ActualContraseniaET = findViewById(R.id.ActualContraseniaET);
        NuevoContraseniaEt = findViewById(R.id.NuevoContraseniaET);
        CAMBIARCONTRASENIABTN = findViewById(R.id.CAMBIARCONTRASENIABTN);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        USUARIOS = FirebaseDatabase.getInstance().getReference("USUARIOS");

        progressDialog = new ProgressDialog(CambiarContrasenia.this);

        Cambiar_de_letra();

        Query query = USUARIOS.orderByChild("correo").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    String correo = ""+ds.child("correo").getValue();
                    String contrasenia = ""+ds.child("pass").getValue();

                    CorreoActual.setText(correo);
                    ContraseniaActual.setText(contrasenia);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        CAMBIARCONTRASENIABTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String CONTRASENIA_ANTERIOR = ActualContraseniaET.getText().toString().trim();
                String NUEVO_CONTRASENIA = NuevoContraseniaEt.getText().toString().trim();

                if (TextUtils.isEmpty(CONTRASENIA_ANTERIOR)){
                    Toast.makeText(CambiarContrasenia.this, "El campo contraseña actual esta vacío", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(NUEVO_CONTRASENIA)){
                    Toast.makeText(CambiarContrasenia.this, "El campo nueva contraseña está vacía", Toast.LENGTH_SHORT).show();
                }
                if (!NUEVO_CONTRASENIA.equals("") && NUEVO_CONTRASENIA.length() >= 6){
                    Cambio_de_contrasenia(CONTRASENIA_ANTERIOR, NUEVO_CONTRASENIA);
                }else {
                    NuevoContraseniaEt.setError("La contraseña debe ser mayor a 6 caracteres");
                    NuevoContraseniaEt.setFocusable(true);
                }
            }
        });
    }

    private void Cambio_de_contrasenia(String contrasenia_anterior, String nuevo_contrasenia) {
        progressDialog.show();
        progressDialog.setTitle("Actualizado");
        progressDialog.setMessage("Espere Por Favor");
        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), contrasenia_anterior);
        user.reauthenticate(authCredential)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        user.updatePassword(nuevo_contrasenia)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        progressDialog.dismiss();
                                        String value = NuevoContraseniaEt.getText().toString().trim();
                                        HashMap<String, Object> result = new HashMap<>();
                                        result.put("pass", value);
                                        USUARIOS.child(user.getUid()).updateChildren(result)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(CambiarContrasenia.this, "Contraseña cambiada", Toast.LENGTH_SHORT).show();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        progressDialog.dismiss();
                                                    }
                                                });
                                        firebaseAuth.signOut();
                                        startActivity(new Intent(CambiarContrasenia.this, Login.class));
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(CambiarContrasenia.this, "La contraseña actual no es correcta", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void Cambiar_de_letra(){
        String link = "Fuentes/static/Inconsolata-Black.ttf";
        Typeface Tf = Typeface.createFromAsset(CambiarContrasenia.this.getAssets(), link);

        MisCredencialesTXT.setTypeface(Tf);
        CorreoActualTXT.setTypeface(Tf);
        CorreoActual.setTypeface(Tf);
        ContraseniaActualTXT.setTypeface(Tf);
        ContraseniaActual.setTypeface(Tf);
        ActualContraseniaET.setTypeface(Tf);
        NuevoContraseniaEt.setTypeface(Tf);
    }
    @Override
    public boolean onNavigateUp(){
        onBackPressed();
        return super.onNavigateUp();
    }
}