package com.example.grupo6.appgrup6;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
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

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class EditarPerfilActivity extends AppCompatActivity {

    FirebaseFirestore db =FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    TextView sexo;
    TextView nacimiento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        sexo = (TextView) findViewById(R.id.sexo);
        nacimiento = (TextView) findViewById(R.id.nacimiento);

        Bundle extras = getIntent().getExtras();
        String sex = extras.getString("Genero");
        String date = extras.getString("Nacimiento");
        Map<String, Object> info = new HashMap<>();
        info.put("Sexo", sex);
        info.put("Nacimiento", date);
        db.collection("usuarios").document(user.getUid()).collection("infor")
                .add(info)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

        loadLastDataFirestore();

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
        Intent intent = new Intent(this, EdicionActivity.class);
        startActivity(intent);
    }

    public void atras(View view){
        Intent i = new Intent(this, UsuarioActivity.class);
        startActivity(i);

    }

    private void loadLastDataFirestore(){
        final CollectionReference medidasInfo = db.collection("usuarios").document(user.getUid()).collection("infor");

        medidasInfo.orderBy("Nacimiento", Query.Direction.DESCENDING).limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                            Log.d(TAG, documentSnapshot.getId() + " => " + documentSnapshot.getData());

                            String se = documentSnapshot.getString("Sexo");
                            sexo.setText(se);
                            String naci = documentSnapshot.getString("Nacimiento");
                            nacimiento.setText(naci);
                        }
                    }
                });
    }

}

