package com.example.grupo6.appgrup6;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MetasEdicionActivity extends AppCompatActivity {
    private static final String TAG = "MetasEdicionActivity";
    FirebaseFirestore db =FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private TextView btnIni;
    private DatePickerDialog.OnDateSetListener mDateSetListenerIni;
    String dateini;
    private TextView btnFin;
    private DatePickerDialog.OnDateSetListener mDateSetListenerFin;
    String datefin;
    String cc;
    String pes;
    Spinner spinner;
    RadioButton perder;
    RadioButton ganar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metas_edicion);

        spinner = (Spinner) findViewById(R.id.spinner);
        String[] letra = {"60","65","70","75","80", "85", "90"};
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, letra));

        pes = "70";

        perder = (RadioButton) findViewById(R.id.radio_perder);
        ganar = (RadioButton) findViewById(R.id.radio_ganar);

        if( perder.isChecked() == true ){
            cc = "Perder peso";
        } else{
            cc = "Femenino";
        }

        btnIni = (TextView) findViewById(R.id.fechaini);


        btnIni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        MetasEdicionActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListenerIni,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListenerIni = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month +1;
                Log.d(TAG, "onDataSet: date: " + dayOfMonth + "/" + month + "/" + year);

                dateini = dayOfMonth + "/" + month + "/" + year;
                btnIni.setText(dateini);
            }
        };

        btnFin = (TextView) findViewById(R.id.fechafin);


        btnFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        MetasEdicionActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListenerFin,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListenerFin = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month +1;
                Log.d(TAG, "onDataSet: date: " + dayOfMonth + "/" + month + "/" + year);

                datefin = dayOfMonth + "/" + month + "/" + year;
                btnFin.setText(datefin);
            }
        };

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edicion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.accion_guardar:
                lanzarEditarMetas(null);
                return true;
            case R.id.accion_cancelar:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void lanzarEditarMetas(View view){



        Intent intent = new Intent(this, MetasActivity.class);
        intent.putExtra("Objetivo", cc);
        intent.putExtra("PesoMeta", pes);
        intent.putExtra("FechaIni", dateini);
        intent.putExtra("FechaFin", datefin);
        intent.putExtra("PesoAct", "80.9");
        startActivity(intent);
    }

    public void escribirBaseDatos(){
        Map<String, Object> met = new HashMap<>();
        met.put("objetivo", cc);
        met.put("pesometa", pes);
        met.put("fechaini", dateini);
        met.put("fechafin", datefin);
        db.collection("usuarios").document(user.getUid()).collection("metas")
                .add(met)
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

    }

}
