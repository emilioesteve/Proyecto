class SensorInfrarrojos{
    private:
    unsigned int pin;
    public:
    SensorInfrarrojos(unsigned int pin_):
    pin(pin_){
        pinMode(pin , INPUT);
    }
    bool detectarMovimiento(){
        unsigned int value = digitalRead(pin);
        if(value == HIGH){
            Serial.println("Obstáculo detectado");
            return true;
        }
    }
};
