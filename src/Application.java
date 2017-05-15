import java.lang.*;

import Sensor.SensorController;
import Sensor.UDPConnectionController;


public class Application {
    public static void main(String[] args) throws Exception{

        SensorController sensor = new SensorController();
        sensor.startSensor();

    }

}