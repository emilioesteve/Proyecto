#include "SensorUltrasonido.h"
#include "ConexionWiFi.h"
#include "ConexionUDP.h"
#include "SensorMagnetico.h"
#include "SensorInfrarrojos.h"
#include <TimeLib.h>
#include <ArduinoJson.h>
#include <MQTT.h>
#include "soc/rtc.h"
#include "HX711.h"
#define DOUT  5
#define CLK  2
const char broker[] = "iot.eclipse.org";
WiFiClient net;
MQTTClient client;
unsigned long lastMillis = 0;
const char ssid1[] = "Grupo6";
const char passw[] = "12345678";

//BÁSCULA
HX711 balanza(DOUT, CLK);

//SENSOR DE ULTRASONIDOS  // (echoPin, triggerPin)
SensorUltrasonido distancia = SensorUltrasonido(12, 14);

//SENSOR MAGNÉTICO  //  pin de entrada
SensorMagnetico puerta = SensorMagnetico(2);

//SENSOR INFRARROJOS  //  pin de entrada
SensorInfrarrojos movimiento = SensorInfrarrojos(17);

//CREDENCIALES CONEXIÓN WI-FI // (ssid, password)
ConexionWiFi wifi = ConexionWiFi("Grupo6", "12345678");

//Conexión UDP  // PUERTO
ConexionUDP udp = ConexionUDP(1234);

void connect() {
 Serial.print("checking wifi...");
 while (WiFi.status() != WL_CONNECTED) {
 Serial.print(".");
 delay(1000);
 }
 Serial.print("\nconnecting...");
  while (!client.connect("Arduinoo", "try", "try")) {
 Serial.print(".");
 delay(1000);
 }
 Serial.println("\nconnected!");
 client.subscribe("grupo6/proyecto/#");
 //client.unsubscribe("grupo6/proyecto/#");
}

void messageReceived(String &topic, String &payload) {
 Serial.println("incoming: " + topic + " - " + payload);
}

void setup(){
    Serial.begin(115200);
    WiFi.begin(ssid1, passw);
    client.begin(broker, net);
    client.onMessage(messageReceived);
    connect();

    rtc_clk_cpu_freq_set(RTC_CPU_FREQ_80M);   //bajo la frecuencia a 80MHz
    setTime (12, 40, 0, 30, 11, 2018); //hora minuto segundo dia mes año
    wifi.conectar();
    udp.escuchar(wifi);
    balanza.set_scale(25400); // Establecemos la escala
    balanza.tare(20);  //El peso actual es considerado Tara.
}


void loop(){
 
   StaticJsonBuffer<200> jsonBuffer;                 //tamaño maximo de los datos
   JsonObject& envio = jsonBuffer.createObject();    //creación del objeto "envio"

    //Se crea el array de caracteres que posteriormente
    //se rellenará con los datos a enviar
    char envioTxt[200];
    balanza.read();
    //Se procesan todos los datos obtenidos de los sensores
    String fecha = String(day()) + "/" + String(month()) + "/" + String(year()) + "  " + String( hour()) + ":" + String(minute()) + ":" + String(second()); 
    String altura = String(distancia.calcularDistancia());
    bool mov = movimiento.detectarMovimiento();
//    double peso = ;

   client.loop();
   delay(10); // <- Esperamos a que WiFi sea estable
   if (!client.connected()) {
   connect();
   }
   // publicamos un mensaje cada segundo
   if (millis() - lastMillis > 1000) {
   lastMillis = millis();
// client.publish("grupo6/proyecto/temp/peso", float(balanza.get_units(20),3));
   client.publish("grupo6/proyecto/temp/distancia", altura);
   client.publish("grupo6/proyecto/temp/movimiento", "" + mov);
   } 

   Serial.print(mov);

    //Se recopilan todos esos datos en un objeto JSON
    envio["Hora"]=fecha;
    envio["Altura"] = altura;
    envio["Peso"] = balanza.get_units(20),3; 
    envio["Movimiento"] = mov;

    //Se copia el objeto JSON a un array de carácteres
    envio.printTo(envioTxt);

    //Los datos se envían vía UDP en formato texto
    udp.enviarDatos(envioTxt);

    //Envío de datos a la Raspberry
    if(Serial.available() > 0){
    char command = (char)Serial.read();
    switch(command){
    case 'H':
      Serial.println("Hola Mundo!");
      //udp.enviarDatos("Hola Mundo!");
      break;
    case 'D':
      Serial.print("DISTANCIA: ");
      Serial.println(altura);
      Serial.print("PESO: ");
      Serial.println( balanza.get_units(20),3);
      break;
    }
    }
    //Retardo de 1s entre medición y medición
    delay(5000);
}
