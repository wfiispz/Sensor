package Sensor;




import org.hyperic.sigar.Sigar;

import java.util.ArrayList;
import java.util.List;
/**
 *
 * Created by jak12 on 30.04.2017.
 */
public class SensorController {

    private List<Measurement> MeasurementsList = new ArrayList<>();
    private String data;

    public void startSensor(){
        if(registerSensor()) {
            chooseMeasurements();
            sendMetadata();


            //noinspection InfiniteLoopStatement
            while(true) run();

        }
        else{
            System.out.println("Rejestracja nieudana, program konczy dzialanie.");
        }
    }

    private boolean registerSensor(){
        //this function will register sensor and return true on succes
        return true;
    }

    private void chooseMeasurements(){
        //this function will allows to choose proper measurements
        //in reality it will probably just add all avaible measurements
        Sigar sigar = new Sigar();
        MeasurementsList.add(new MemoryMeasurement(sigar));
        MeasurementsList.add(new CpuMeasurement(sigar));
    }

    private void sendMetadata(){
        //this function will send metadata
    }

    private void run(){
        prepateMeasurementsData();
        sendMeasurementsData();
        /// This will be probably changed to ScheduledExecutor
        try {
            Thread.sleep(1000);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

    }

    private void prepateMeasurementsData(){
        //in the future this function will probably return Json
        //for now it just gathers all the measurements
        data="";
        for (Measurement measurement: MeasurementsList) {
            //noinspection StringConcatenationInLoop
            data+=measurement.getMeasurementType() + " " + measurement.getActualMeasure() + "\n";
        }

    }

    private void sendMeasurementsData() {
        //this function will send data to monitor
        System.out.println(data);
    }

}
