package com.pack.red_social;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pack.red_social.Opciones.Mis_Datos;

import java.util.HashMap;

public class Login extends AppCompatActivity {

    EditText CorreoLogin, ContraseniaLogin;
    Button INGRESAR, INGRESARGOOGLE;

    TextView app_name;

    private GoogleSignInClient mGoogleSignClient;
    private final static int RC_SIGN_IN = 123;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        assert  actionBar != null;
        actionBar.setTitle("Login");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        app_name = findViewById(R.id.app_name);
        CorreoLogin = findViewById(R.id.CorreoLogin);
        ContraseniaLogin = findViewById(R.id.ContraseniaLogin);
        INGRESAR = findViewById(R.id.INGRESAR);
        INGRESARGOOGLE = findViewById(R.id.INGRESARGOOGLE);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(Login.this);
        dialog = new Dialog(Login.this);

        CambioDeLetra();

        crearSolicitud();

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
                    LOGINUSUARIO(correo, contrasenia);
                }
            }
        });
        INGRESARGOOGLE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void crearSolicitud() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignClient = GoogleSignIn.getClient(this,gso);
    }

    private void signIn(){
        Intent signIntent = mGoogleSignClient.getSignInIntent();
        startActivityForResult(signIntent, RC_SIGN_IN);
    }


    /*para crear está funcion excriba onA despues presion callsuper*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount cuenta = task.getResult(ApiException.class);
                AutenticacionFirebase(cuenta);
            }catch (ApiException e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void AutenticacionFirebase(GoogleSignInAccount cuenta) {
        AuthCredential credencial = GoogleAuthProvider.getCredential(cuenta.getIdToken(), null);
        firebaseAuth.signInWithCredential(credencial)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            if (task.getResult().getAdditionalUserInfo().isNewUser()){
                                String uid = user.getUid();
                                String correo = user.getEmail();
                                String nombre = user.getDisplayName();


                                HashMap<Object,String> DatoUsuario = new HashMap<>();

                                DatoUsuario.put("uid", uid);
                                DatoUsuario.put("correo", correo);
                                DatoUsuario.put("pass", "");
                                DatoUsuario.put("nombres", nombre);
                                DatoUsuario.put("apellidos", "");
                                DatoUsuario.put("edad", "");
                                DatoUsuario.put("telefono", "");
                                DatoUsuario.put("direccion", "");

                                DatoUsuario.put("imagen","");

                                FirebaseDatabase database = FirebaseDatabase.getInstance();

                                DatabaseReference reference = database.getReference("USUARIOS");

                            }
                            startActivity(new Intent(Login.this, Inicio.class));
                        }
                        else {
                            Dialog_No_Inicio();
                        }
                    }
                });
    }

    private void LOGINUSUARIO(String correo, String contrasenia) {
        progressDialog.setTitle("Ingresando");
        progressDialog.setMessage("Espere por favor");
        progressDialog.setCancelable(false);
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(correo, contrasenia)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            startActivity(new Intent(Login.this, Inicio.class));
                            assert user != null;
                            Toast.makeText(Login.this, "Hola ! Bienvenid@"+user.getEmail(), Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            progressDialog.dismiss();
                            Dialog_No_Inicio();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void CambioDeLetra(){
        String link = "Fuentes/static/Inconsolata-Black.ttf";
        Typeface Tf = Typeface.createFromAsset(Login.this.getAssets(), link);

        app_name.setTypeface(Tf);
        CorreoLogin.setTypeface(Tf);
        ContraseniaLogin.setTypeface(Tf);
        INGRESAR.setTypeface(Tf);
        INGRESARGOOGLE.setTypeface(Tf);
    }

    private void Dialog_No_Inicio(){
        Button ok_no_inicio;
        dialog.setContentView(R.layout.no_sesion);

        ok_no_inicio = dialog.findViewById(R.id.ok_no_inicio);

        ok_no_inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}