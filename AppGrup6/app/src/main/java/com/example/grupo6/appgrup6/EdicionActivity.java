package com.example.grupo6.appgrup6;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;

import static android.support.v4.content.FileProvider.getUriForFile;

public class EdicionActivity extends AppCompatActivity {

    private static final String TAG = "EdicionActivity";

    private TextView btn;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    String date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edicion);



        btn = (TextView) findViewById(R.id.nacimientos);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        EdicionActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month +1;
                Log.d(TAG, "onDataSet: date: " + dayOfMonth + "/" + month + "/" + year);

                date = dayOfMonth + "/" + month + "/" + year;
                btn.setText(date);
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
                lanzarEditarPerfilF(null);
                return true;
            case R.id.accion_cancelar:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void lanzarEditarPerfilF(View view){

        String c;
        String d;
        RadioButton masc = (RadioButton) findViewById(R.id.radio_masculino);
        RadioButton fem = (RadioButton) findViewById(R.id.radio_femenino);

        if( masc.isChecked() == true ){
             c = "Masculino";
        } else{
             c = "Femenino";
        }

        Intent intent = new Intent(this, EditarPerfilActivity.class);
        intent.putExtra("Genero", c);
        intent.putExtra("Nacimiento", date);
        startActivity(intent);

    }
   

}
