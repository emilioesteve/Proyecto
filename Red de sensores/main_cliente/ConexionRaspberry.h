class ConexionRaspberry{
  public:
  ConexionRaspberry(){}
    void enviarDatos(String altura){
        if(Serial.available() > 0){
            char command = (char)Serial.read();
            switch(command){
                case 'H':
                Serial.println("Hola Mundo!");
                break;
                case 'D':
                Serial.print("Distancia: ");
                Serial.println(altura);
                break;
            }
        }
    }
}
