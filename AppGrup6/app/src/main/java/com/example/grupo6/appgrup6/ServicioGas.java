package com.example.grupo6.appgrup6;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class ServicioGas extends Service {

    private NotificationManager notificationManager;
    static final String CANAL_ID = "mi_canal";
    static final int NOTIFICACION_ID = 1;

    @Override
    public void onCreate(){
        Toast.makeText(this, "Servicio Gas ON",
                Toast.LENGTH_SHORT).show();
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
                new NotificationCompat.Builder(ServicioGas.this, CANAL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Servicio")
                        .setContentText("Notificacion del servicio");
        notificationManager.notify(NOTIFICACION_ID, notificacion.build());
        Log.d("Servicio", "Servicio lanzado");
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
