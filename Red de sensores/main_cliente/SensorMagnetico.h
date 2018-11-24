class SensorMagnetico{
    unsigned int pin;
    public:
    SensorMagnetico(unsigned int pin_):
      pin(pin_){}
    
    void puertaAbierta(){
        if(digitalRead((*this).pin) == HIGH){
            Serial.println("PUERTA ABIERTA");
            delay(10000);
        }
    }
};
