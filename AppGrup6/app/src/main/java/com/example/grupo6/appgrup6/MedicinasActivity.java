package com.example.grupo6.appgrup6;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.data.Entry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class MedicinasActivity extends AppCompatActivity {

    ArrayList<Entry> entries = new ArrayList<>();
    FirebaseFirestore db =FirebaseFirestore.getInstance();
    RecyclerView mRecyclerView;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    CollectionReference userAltura = db.collection("usuarios").document(user.getUid()).collection("medicinas");
    ArrayList<Medicina> altArrayList;
    MediAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medicinas);

        altArrayList = new ArrayList<>();
        mRecyclerView = findViewById(R.id.recyclermedi);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        setUpDatos();
        loadDataFromFirestore();

    }

    private void loadDataFromFirestore() {

        if (altArrayList.size() > 0) {
            altArrayList.clear();
        }

        final CollectionReference medidasInfo = db.collection("usuarios").document(user.getUid()).collection("medicinas");

        medidasInfo.orderBy("nombre", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                            Log.d(TAG, documentSnapshot.getId() + " => " + documentSnapshot.getData());

                            Medicina mimedida = new Medicina(documentSnapshot.getString("nombre"),documentSnapshot.getDouble("tomas"),documentSnapshot.getString("tiempo"));
                            altArrayList.add(mimedida);

                        }

                        adapter = new MediAdapter(MedicinasActivity.this, altArrayList);
                        mRecyclerView.setAdapter(adapter);

                    }
                });

    }//loadDataFromFirestore()

    private void setUpDatos() {

        db = FirebaseFirestore.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.menu_usuario) {
            Intent intent = new Intent(this, UsuarioActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
