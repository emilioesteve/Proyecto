package com.example.grupo6.appgrup6;

import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.ArrayList;

import static com.example.grupo6.appgrup6.Mqtt.TAG;
import static com.example.grupo6.appgrup6.Mqtt.broker;
import static com.example.grupo6.appgrup6.Mqtt.clientId;
import static com.example.grupo6.appgrup6.Mqtt.qos;
import static com.example.grupo6.appgrup6.Mqtt.topicRoot;

public class TemperaturaHumedad extends AppCompatActivity implements MqttCallback {

    ArrayList<Temperatura> tempArrayList0 = new ArrayList<>();
    ArrayList<Humedad> humeArrayList0 = new ArrayList<>();
    ArrayList<Temperatura> tempArrayList1 = new ArrayList<>();
    ArrayList<Humedad> humeArrayList1 = new ArrayList<>();
    ArrayList<Temperatura> tempArrayList2 = new ArrayList<>();
    ArrayList<Humedad> humeArrayList2 = new ArrayList<>();
    ArrayList<Temperatura> tempArrayList3 = new ArrayList<>();
    ArrayList<Humedad> humeArrayList3 = new ArrayList<>();
    MqttClient client;
    FirebaseFirestore db =FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    CollectionReference usersensores = db.collection("usuarios").document(user.getUid()).collection("sensores");
    TextView tempsalons;
    TextView humesalons;
    TextView tempdorm1s;
    TextView humedorm1s;
    TextView tempdorm2s;
    TextView humedorm2s;
    TextView tempcomes;
    TextView humecomes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperatura_humedad);

        setUpDatos();
        tempcomes = (TextView) findViewById(R.id.tempcome);
        humecomes = (TextView) findViewById(R.id.humecome);
        loadLastDataFirestoreComedor();

        tempsalons = (TextView) findViewById(R.id.tempsalon);
        humesalons = (TextView) findViewById(R.id.humesalon);
        loadLastDataFirestoreSalon();

        tempdorm1s = (TextView) findViewById(R.id.tempdorm1);
        humedorm1s = (TextView) findViewById(R.id.humedorm1);
        loadLastDataFirestoreDorm1();

        tempdorm2s = (TextView) findViewById(R.id.tempdorm2);
        humedorm2s = (TextView) findViewById(R.id.humedorm2);
        loadLastDataFirestoreDorm2();

        /*Button a = (Button) findViewById(R.id.mapa);
        a.setOnClickListener(View.OnClickListener());*/

        try {
            Log.i(TAG, "Conectando al broker " + broker);
            client = new MqttClient(broker, clientId, new MemoryPersistence());
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setKeepAliveInterval(60);
            connOpts.setWill(topicRoot + "WillTopic", "App desconectada".getBytes(),
                    qos, false);

            client.connect(connOpts);
        } catch (MqttException e) {
            Log.e(TAG, "Error al conectar.", e);
        }

        try {
            Log.i(TAG, "Suscrito a " + topicRoot + "POWER");
            client.subscribe(topicRoot + "POWER", qos);
            client.setCallback(this);
        } catch (MqttException e) {
            Log.e(TAG, "Error al suscribir.", e);
        }

        try {
            Log.i(TAG, "Publicando mensaje: " + user.getUid());
            MqttMessage message = new MqttMessage(user.getUid().getBytes());
            message.setQos(qos);
            message.setRetained(false);
            client.publish(topicRoot + "temp/IdUsuario", message);
            Log.v("ID USUARIO", user.getUid());
        } catch (MqttException e) {
            Log.e(TAG, "Error al publicar.", e);
        }

    }

    public void escribirMqtt(View view) {
        try {
            Log.i(TAG, "Publicando mensaje: " + "toggle");
            MqttMessage message = new MqttMessage("toggle".getBytes());
            message.setQos(qos);
            message.setRetained(false);
            client.publish(topicRoot + "cmnd/POWER", message);
        } catch (MqttException e) {
            Log.e(TAG, "Error al publicar.", e);
        }
    }


    @Override
    public void onDestroy() {
        try {
            Log.i(TAG, "Desconectado");
            client.disconnect();
        } catch (MqttException e) {
            Log.e(TAG, "Error al desconectar.", e);
        }
        super.onDestroy();
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

    @Override
    public void connectionLost(Throwable cause) {
        Log.d(TAG, "Conexión perdida");
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        Log.d(TAG, "Entrega completa");
    }

    @Override
    public void messageArrived(String topic, MqttMessage message)
            throws Exception {
        final String payload = new String(message.getPayload());
        Log.d(TAG, "Recibiendo: " + topic + "->" + payload);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                TextView a = (TextView) findViewById(R.id.msgsalon);
                a.setText(payload);

                TextView b = (TextView) findViewById(R.id.msgcomedor);
                b.setText(payload);

                TextView d = (TextView) findViewById(R.id.msgdorm1);
                d.setText(payload);

                TextView e = (TextView) findViewById(R.id.msgdorm2);
                e.setText(payload);


            }
        });
    }

    private void setUpDatos() {

        db = FirebaseFirestore.getInstance();
    }

    private void loadLastDataFirestoreComedor(){
        final CollectionReference medidasInfo = db.collection("usuarios").document(user.getUid()).collection("comedor");

        medidasInfo.orderBy("fecha", Query.Direction.DESCENDING).limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                            Log.d(ContentValues.TAG, documentSnapshot.getId() + " => " + documentSnapshot.getData());

                            Temperatura mimedida = new Temperatura(documentSnapshot.getString("fecha"),documentSnapshot.getDouble("temperatura"));
                            tempArrayList0.add(mimedida);
                            String temp = Double.toString(mimedida.getTemp());
                            tempcomes.setText(temp);

                            Humedad mimedida1 = new Humedad(documentSnapshot.getString("fecha"),documentSnapshot.getDouble("humedad"));
                            humeArrayList0.add(mimedida1);
                            String hume = Double.toString(mimedida1.getHume());
                            humecomes.setText(hume);

                        }
                    }
                });
    }

    private void loadLastDataFirestoreSalon(){
        final CollectionReference medidasInfo = db.collection("usuarios").document(user.getUid()).collection("salon");

        medidasInfo.orderBy("fecha", Query.Direction.DESCENDING).limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                            Log.d(ContentValues.TAG, documentSnapshot.getId() + " => " + documentSnapshot.getData());

                            Temperatura mimedida = new Temperatura(documentSnapshot.getString("fecha"),documentSnapshot.getDouble("temperatura"));
                            tempArrayList1.add(mimedida);
                            String temp = Double.toString(mimedida.getTemp());
                            tempsalons.setText(temp);

                            Humedad mimedida1 = new Humedad(documentSnapshot.getString("fecha"),documentSnapshot.getDouble("humedad"));
                            humeArrayList1.add(mimedida1);
                            String hume = Double.toString(mimedida1.getHume());
                            humesalons.setText(hume);

                        }
                    }
                });
    }

    private void loadLastDataFirestoreDorm1(){
        final CollectionReference medidasInfo = db.collection("usuarios").document(user.getUid()).collection("dorm1");

        medidasInfo.orderBy("fecha", Query.Direction.DESCENDING).limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                            Log.d(ContentValues.TAG, documentSnapshot.getId() + " => " + documentSnapshot.getData());

                            Temperatura mimedida = new Temperatura(documentSnapshot.getString("fecha"),documentSnapshot.getDouble("temperatura"));
                            tempArrayList2.add(mimedida);
                            String temp = Double.toString(mimedida.getTemp());
                            tempdorm1s.setText(temp);

                            Humedad mimedida1 = new Humedad(documentSnapshot.getString("fecha"),documentSnapshot.getDouble("humedad"));
                            humeArrayList2.add(mimedida1);
                            String hume = Double.toString(mimedida1.getHume());
                            humedorm1s.setText(hume);

                        }
                    }
                });
    }

    private void loadLastDataFirestoreDorm2(){
        final CollectionReference medidasInfo = db.collection("usuarios").document(user.getUid()).collection("dorm2");

        medidasInfo.orderBy("fecha", Query.Direction.DESCENDING).limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                            Log.d(ContentValues.TAG, documentSnapshot.getId() + " => " + documentSnapshot.getData());

                            Temperatura mimedida = new Temperatura(documentSnapshot.getString("fecha"),documentSnapshot.getDouble("temperatura"));
                            tempArrayList3.add(mimedida);
                            String temp = Double.toString(mimedida.getTemp());
                            tempdorm2s.setText(temp);

                            Humedad mimedida1 = new Humedad(documentSnapshot.getString("fecha"),documentSnapshot.getDouble("humedad"));
                            humeArrayList3.add(mimedida1);
                            String hume = Double.toString(mimedida1.getHume());
                            humedorm2s.setText(hume);

                        }
                    }
                });
    }
}
