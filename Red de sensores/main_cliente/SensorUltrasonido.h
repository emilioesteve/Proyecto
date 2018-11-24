class SensorUltrasonido{
    private: 
    unsigned int echoPin;      //Pin ECHO del sensor
    unsigned int triggerPin;   //Pin //TRIGGER del sensor
    public:
    SensorUltrasonido(unsigned int echoPin_, unsigned int triggerPin_):
    echoPin(echoPin_), triggerPin(triggerPin_){
        pinMode(triggerPin, OUTPUT);
        pinMode(echoPin, INPUT);
    }
    
    unsigned int calcularDistancia(){
        long duracion, distanciaCm;
        digitalWrite((*this).triggerPin, LOW);  //nos aseguramos señal baja al principio
        delayMicroseconds(4);
        digitalWrite((*this).triggerPin, HIGH); //generamos pulso de 10us
        delayMicroseconds(10);
        digitalWrite((*this).triggerPin, LOW);
        duracion = pulseIn((*this).echoPin, HIGH);  //medimos el tiempo del pulso
        distanciaCm = duracion * 10 / 292 / 2;
        return distanciaCm;
    }
};
