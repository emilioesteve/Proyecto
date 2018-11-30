package localizacion.example.org.uartprojectem5_raspberry;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;


//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONObject;

//import com.google.gson.JsonObject;
//import com.google.gson.JsonParse;

import java.util.HashMap;
import java.util.Map;

import static gabo.example.com.comun.Mqtt.broker;
import static gabo.example.com.comun.Mqtt.qos;
import static gabo.example.com.comun.Mqtt.topicRoot;
import static gabo.example.com.comun.Mqtt.clientId;
import static gabo.example.com.comun.Mqtt.TAG;



public class MainActivity extends Activity implements MqttCallback {
    private static final String TAG1 = MainActivity.class.getSimpleName();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    MqttClient client;
    private static final int INTERVALO = 1000; // (ms)
    private Handler handler = new Handler();
    ArduinoUart uart = new ArduinoUart("UART0", 115200);

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG1, "Lista de UART disponibles: " + ArduinoUart.disponibles());

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
            Log.i(TAG, "Suscrito a " + topicRoot);
            client.subscribe(topicRoot + "#", qos);
            client.setCallback(this);
        } catch (MqttException e) {
            Log.e(TAG, "Error al suscribir.", e);
        }

        handler.post(runnable);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
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
    public void messageArrived(final String topic, MqttMessage message)
            throws Exception {
        final String payload = new String(message.getPayload());
        Log.d(TAG, "Recibiendo: " + topic + "->" + payload);


        Map<String, Object> datos = new HashMap<>();
        if(topic == topicRoot + "temp/distancia"){
            datos.put("distancia", payload);
            db.collection("usuarios").document("values").set(datos);
        }

        if ( topic == topicRoot + "temp/movimiento"){
            datos.put("movimiento", payload);
            db.collection("usuarios").document("values").set(datos);
        }

        datos.put(topic, payload);
        db.collection("usuarios").document("values").update(datos);


    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            Log.d(TAG1, "Mandado a Arduino: H");
            uart.escribir("H");


            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Log.w(TAG1, "Error en sleep()", e);
            }


            String s = uart.leer();
            Log.d(TAG1, "Recibido de Arduino: " + s);   //{"Hora":"22/10/2018  17:46:49","Altura":"1cm","Peso":0,"Movimiento":false}

          /*  JsonParser jsonParser = new JsonParser();

            JSONObject medidas = jsonParser.parse(s).getAsJsonObject();

            //Map<String, Object> altura = new HashMap<>();

            double altura = medidas.getAsJsonObject().get("Altura").getAsDouble();*/


            handler.postDelayed(runnable, INTERVALO);

        }

    };


}
