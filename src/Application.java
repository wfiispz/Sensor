import java.lang.*;

import Sensor.SensorController;

public class Application {
    public static void main(String[] args) throws Exception{

        SensorController sensor = new SensorController();
        sensor.startSensor();

    }

}