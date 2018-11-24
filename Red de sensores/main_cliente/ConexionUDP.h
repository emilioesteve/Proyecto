#include "AsyncUDP.h"

class ConexionUDP{
    private:
    AsyncUDP udp;
    unsigned int puerto;
    public:
    ConexionUDP(unsigned int puerto_):
    puerto(puerto_){}
    void escuchar(ConexionWiFi wifi){
        if(udp.listen( (*this).puerto )){
            Serial.print("UDP escuchando en IP: ");
            Serial.print(wifi.obtenerIP());
            Serial.print(":");
            Serial.println(puerto);

            udp.onPacket([](AsyncUDPPacket packet){
                Serial.write(packet.data(), packet.length());
                Serial.println();
            });
        }
    }
    void enviarDatos(char* datos){
      udp.broadcastTo(datos, puerto);
    }
};
