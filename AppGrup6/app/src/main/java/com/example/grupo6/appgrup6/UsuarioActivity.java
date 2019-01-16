package com.example.grupo6.appgrup6;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class UsuarioActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);
    }

    public void cerrarSesion(View view) {
        Intent i = new Intent(this, LogoutActivity.class);
        startActivity(i);
    }

    public void lanzarAcercaDe(View view){
        Intent i = new Intent(this, AcercaDeActivity.class);
        startActivity(i);
    }

    public void lanzarPreferencias(View view) {
        Intent i = new Intent(this, PreferenciasActivity.class);
        startActivity(i);
    }

    public void lanzarContacto(View view) {
        Intent i = new Intent(this, ContactoActivity.class);
        startActivity(i);
    }

    public void lanzarEditar(View view) {
        Intent i = new Intent(this, EditarPerfilActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.salir:
                atras(null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void atras(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}