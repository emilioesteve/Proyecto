package com.example.grupo6.appgrup6;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.type.LatLng;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import static com.example.grupo6.appgrup6.Mqtt.broker;
import static com.example.grupo6.appgrup6.Mqtt.clientId;
import static com.example.grupo6.appgrup6.Mqtt.qos;
import static com.example.grupo6.appgrup6.Mqtt.topicRoot;
import static com.example.grupo6.appgrup6.Mqtt.TAG;

public class SensorsActivity extends AppCompatActivity implements MqttCallback {
    MqttClient client;
    private NotificationManager notificationManager;
    static final String CANAL_ID = "mi_canal";
    static final int NOTIFICACION_ID = 1;
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


                if (payload.equals("ON")) {
                    notificacion(null);
                }

                TextView a = (TextView) findViewById(R.id.msgsalon);
                a.setText(payload);

                TextView b = (TextView) findViewById(R.id.msgcomedor);
                b.setText(payload);

                TextView c = (TextView) findViewById(R.id.msgbaño);
                c.setText(payload);

                TextView d = (TextView) findViewById(R.id.msgdorm1);
                d.setText(payload);

                TextView e = (TextView) findViewById(R.id.msgdorm2);
                e.setText(payload);


            }
        });
    }


    public void pgWeb(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://mqtt.org/"));
        startActivity(intent);
    }

    public void notificacion(View view) {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    CANAL_ID, "Mis Notificaciones",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Descripcion del canal");
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificacion =
                new NotificationCompat.Builder(SensorsActivity.this, CANAL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("ATENCIÖN")
                        .setContentText("Tienes algunas luces encendidas");
        PendingIntent intencionPendiente = PendingIntent.getActivity(
                this, 0, new Intent(this, SensorsActivity.class), 0);
        notificacion.setContentIntent(intencionPendiente);
        notificationManager.notify(NOTIFICACION_ID, notificacion.build());

    }

    public void verMapa(View view) {
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        // AUNQUE DE ERROR EN ESTA LINEA SIGUE FUNCIONANDO
        Location location = locationManager.getLastKnownLocation(locationManager
                .getBestProvider(criteria, false));
        double lat = location.getLatitude();
        double lon = location.getLongitude();
        Uri uri;
        //double lat = 0.0;
        //double lon = 0.0;
        //if (lat != 0 || lon != 0) {
            uri = Uri.parse("geo:" + lat + "," + lon);
        //}
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}