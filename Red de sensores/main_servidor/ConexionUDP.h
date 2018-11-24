#include "AsyncUDP.h"

class ConexionUDP{
    private:
    AsyncUDP udp;
    unsigned int puerto;
    public:
    ConexionUDP(unsigned int puerto_){
        (*this).puerto = puerto_;
    }
        
    void enviarDatos(char* datos){
      udp.broadcastTo(datos, (*this).puerto);
    }
    
    bool recibirDatos(ConexionWiFi wifi, char texto[200]){
        Serial.print("UDP escuchando en IP: ");
        Serial.print(wifi.obtenerIP());
        Serial.print(":");
        Serial.println(puerto);
        udp.onPacket([=](AsyncUDPPacket packet) {
            int i=200;
            while (i--) {*(texto+i)=*(packet.data()+i);}
            return true;      //recepcion de un mensaje
        });
      }    
};
