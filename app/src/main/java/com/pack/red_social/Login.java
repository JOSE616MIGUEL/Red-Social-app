package com.pack.red_social;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText CorreoLogin, ContraseniaLogin;
    Button INGRESAR;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        assert  actionBar != null;
        actionBar.setTitle("Login");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        CorreoLogin = findViewById(R.id.CorreoLogin);
        ContraseniaLogin = findViewById(R.id.ContraseniaLogin);
        INGRESAR = findViewById(R.id.INGRESAR);

        firebaseAuth = FirebaseAuth.getInstance();

        INGRESAR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo = CorreoLogin.getText().toString();
                String contrasenia = ContraseniaLogin.getText().toString();

                if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
                    CorreoLogin.setError("Correo invalido");
                    CorreoLogin.setFocusable(true);
                } else if (contrasenia.length()<6) {
                    ContraseniaLogin.setError("La contraseña debe ser mayor a 6 caracteres");
                    ContraseniaLogin.setFocusable(true);
                }else {
                    LOGINUSUARIO();
                }
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}