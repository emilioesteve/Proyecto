package com.example.grupo6.appgrup6;

import android.app.NotificationManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import static com.example.grupo6.appgrup6.Mqtt.TAG;
import static com.example.grupo6.appgrup6.Mqtt.broker;
import static com.example.grupo6.appgrup6.Mqtt.clientId;
import static com.example.grupo6.appgrup6.Mqtt.qos;
import static com.example.grupo6.appgrup6.Mqtt.topicRoot;

public class TemperaturaHumedad extends AppCompatActivity implements MqttCallback {

    MqttClient client;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);

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
}
