package com.example.grupo6.appgrup6;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.CalendarView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

public class CalendarioActivity extends AppCompatActivity {

    CalendarView mCalendarView;
    int count;
    FirebaseFirestore db =FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);

         mCalendarView = (CalendarView) findViewById(R.id.calendarView);

        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                String monthInText = "";

                switch (month){
                    case 0: monthInText = "enero"; break;
                    case 1: monthInText = "febrero"; break;
                    case 2: monthInText = "marzo"; break;
                    case 3: monthInText = "abril"; break;
                    case 4: monthInText = "mayo"; break;
                    case 5: monthInText = "junio"; break;
                    case 6: monthInText = "julio"; break;
                    case 7: monthInText = "agosto"; break;
                    case 8: monthInText = "septiembre"; break;
                    case 9: monthInText = "octubre"; break;
                    case 10: monthInText = "noviembre"; break;
                    case 11: monthInText = "diciembre"; break;
                }

                String date = dayOfMonth + " de " + monthInText + " de " + year;


                //COMPROBAR EN BASE DE DATOS


                final CollectionReference medidasInfo = db.collection("usuarios").document(user.getUid()).collection("bascula");

                medidasInfo.whereEqualTo("fecha", date)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {

                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                        count = 1;

                                        Peso mimedida = new Peso(documentSnapshot.getString("fecha"), documentSnapshot.getDouble("peso"));
                                        Toast toast = (Toast) Toast.makeText(CalendarioActivity.this, "Peso registrado: " + mimedida.getPeso() + "Kg", LENGTH_SHORT);
                                        toast.show();
                                    }

                                }


                                if (count == 0){
                                    Toast toast = (Toast) Toast.makeText(CalendarioActivity.this, "Ningún peso registrado", LENGTH_SHORT);
                                    toast.show();

                                } else {
                                    count = 0;
                                }

                            }
                        });

            }
        });
    }
}
