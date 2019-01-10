package com.example.grupo6.appgrup6;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ServicioAcelerometro extends Service implements SensorEventListener {

    private NotificationManager notificationManager;
    static final String CANAL_ID = "mi_canal";
    static final int NOTIFICACION_ID = 1;

    @Override
    public void onCreate() {
        Toast.makeText(this, "Servicio Caída ON",
                Toast.LENGTH_SHORT).show();
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> listaSensores = sensorManager.
                getSensorList(Sensor.TYPE_ACCELEROMETER);
        for (Sensor sensor : listaSensores) {
            Log.v("Sensores", sensor.getName());
        }
        listaSensores = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (!listaSensores.isEmpty()) {
            Sensor acelerometerSensor = listaSensores.get(0);
            sensorManager.registerListener(this, acelerometerSensor,
                    SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int precision) {
    }

    @Override
    public void onSensorChanged(SensorEvent evento) {
        for (int i = 0; i < 3; i++) {
            Log.v("Acelerometro", "Acelerómetro " + i + ": " + evento.values[i]);
            if(evento.values[i] >= 30 ){
                llamadaTelefono(null);
            }
        }

    }

    @Override
    public int onStartCommand(Intent intenc, int flags, int idArranque) {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    CANAL_ID, "App",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("App");
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificacion =
                new NotificationCompat.Builder(ServicioAcelerometro.this, CANAL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("CAÍDA")
                        .setContentText("Se ha caído");
        notificationManager.notify(NOTIFICACION_ID, notificacion.build());
        Log.d("Acelerómetro", "Ha habido una aceleración");
        return START_STICKY;
    }

    public void llamadaTelefono(View view) {
        startActivity(new Intent(Intent.ACTION_DIAL,
                Uri.parse("tel:" + "646601542")));
    }


    @Override
    public IBinder onBind(Intent intencion) {
        return null;
    }


}
