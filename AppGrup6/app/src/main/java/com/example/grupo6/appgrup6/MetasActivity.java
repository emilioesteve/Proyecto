package com.example.grupo6.appgrup6;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class MetasActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    TextView obj;
    TextView pisomet;
    TextView dateini;
    TextView datefin;
    TextView pesact;
    TextView calculo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metas);

        obj = (TextView) findViewById(R.id.objetivo);
        pisomet = (TextView) findViewById(R.id.pesmet);
        dateini = (TextView) findViewById(R.id.inicio);
        datefin = (TextView) findViewById(R.id.fin);
        pesact = (TextView) findViewById(R.id.pesoAho);
        calculo = (TextView) findViewById(R.id.calc);

        Bundle extras = getIntent().getExtras();
        String obje = extras.getString("Objetivo");
        String pemeta = extras.getString("PesoMeta");
        String fini = extras.getString("FechaIni");
        String ffin = extras.getString("FechaFin");
        String pesoactual = extras.getString("PesoAct");
        obj.setText(obje);
        pisomet.setText(pemeta);
        datefin.setText(ffin);
        dateini.setText(fini);
        pesact.setText(pesoactual);
        /*Map<String, Object> meti = new HashMap<>();
        meti.put("objetivo", obje);
        meti.put("pesometa", pemeta);
        meti.put("fechaini", fini);
        meti.put("fechafin", ffin);
        db.collection("usuarios").document(user.getUid()).collection("metas")
                .add(meti)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });*/

        calculo.setText("0.3");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.vista_edicion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.accion_editar:
                lanzarEdit(null);
                return true;
            case R.id.accion_salir:
                atras(null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public  void lanzarEdit(View view){
        Intent intent = new Intent(this, MetasEdicionActivity.class);
        startActivity(intent);
    }

    public void atras(View view){
        Intent i = new Intent(this, UsuarioActivity.class);
        startActivity(i);

    }


}