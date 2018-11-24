#include "WiFi.h"
class ConexionWiFi{
    private:
    char* ssid;
    char* password;
    
    public:
    ConexionWiFi(char* ssid_, char* password_):
    ssid(ssid_), password(password_){}
    void conectar(){
      WiFi.mode(WIFI_STA);
      WiFi.begin((*this).ssid, (*this).password);
      if(WiFi.waitForConnectResult() != WL_CONNECTED){
        Serial.println("La conexión Wi-Fi no ha podido establecerse");
        while(1){
          delay(1000);
        }
      }
    }
    IPAddress obtenerIP(){
      return WiFi.localIP();
    }
};
