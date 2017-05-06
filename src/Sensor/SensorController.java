package Sensor;


import org.hyperic.sigar.Sigar;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 *
 * Created by jak12 on 30.04.2017.
 */
public class SensorController {

    //Constants for json mapping
    //I will probably move this to some helper file in the future
    final static String DATATYPE_KEY = new String("datatype");
    final static String RESOURCE_ID_KEY = new String("resourceID");
    final static String DATA_VALUE = new String("data");
    final static String METADATA_VALUE = new String("metadata");
    final static String NAME_KEY = new String("name");
    final static String DESCRIPTION_KEY = new String("description");
    final static String MEASURE_TYPE_KEY = new String("measureType");
    final static String MEASURE_UNIT_KEY = new String("unit");
    final static String MEASURE_ID_KEY = new String("measureID");
    final static String MAX_VALUE_KEY = new String("maxValue");
    final static String MEASURESARRAY_KEY = new String("measuresArray");
    final static String MEASURE_VALUE_KEY = new String("value");
    final static String TIMESTAMP_KEY = new String("timestamp");

    private JSONObject data = new JSONObject();
    private JSONArray measuresJsonArray = new JSONArray();
    private String resourceID = new String();
    private String hostname = new String();
    private String hostDescription = new String();


    private UDPConnectionController udpController = new UDPConnectionController();

    private List<Measurement> measurementsList = new ArrayList<>();

    public void startSensor(){
        configureSensor();
        sendMetadata();

        //noinspection InfiniteLoopStatement
        while(true) run();

    }



    private void configureSensor(){
        chooseMeasurements();
    }

    private void chooseMeasurements(){
        Sigar sigar = new Sigar();
        resourceID = UUID.randomUUID().toString();

        measurementsList.add(new MemoryMeasurement(sigar));
        measurementsList.add(new CpuMeasurement(sigar));
    }

    private void run(){
        prepareMeasurementsData();
        udpController.sendData(data.toJSONString());

        /// This will be probably changed to ScheduledExecutor
        try {
            Thread.sleep(1000);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

    }

    private void sendMetadata(){
        prepareMetadata();
        udpController.sendData(data.toJSONString());

    }

    private void cleanDataJson(){
        data.clear();
        measuresJsonArray.clear();
    }

    private void prepareMetadata() {
        cleanDataJson();

        data.put(DATATYPE_KEY,METADATA_VALUE);
        data.put(RESOURCE_ID_KEY,resourceID);
        data.put(NAME_KEY, hostname);
        data.put(DESCRIPTION_KEY, hostDescription);

        for (Measurement measurement: measurementsList) {
            JSONObject measureMetadata = new JSONObject();
            measureMetadata.put(MEASURE_TYPE_KEY, measurement.getMeasurementType());
            measureMetadata.put(MEASURE_UNIT_KEY, measurement.getMeasureUnit());
            measureMetadata.put(MEASURE_ID_KEY, measurement.getMeasureID());
            measureMetadata.put(MAX_VALUE_KEY, measurement.getMeasureMaxValue());

            measuresJsonArray.add(measureMetadata);
        }

        data.put(MEASURESARRAY_KEY, measuresJsonArray);
    }



    private String getCurrentTime(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }

    private void prepareMeasurementsData(){
        cleanDataJson();

        data.put(DATATYPE_KEY,DATA_VALUE);
        data.put(RESOURCE_ID_KEY,resourceID);
        data.put(TIMESTAMP_KEY, getCurrentTime());


        for (Measurement measurement: measurementsList) {
            JSONObject measureData = new JSONObject();
            measureData.put(MEASURE_ID_KEY, measurement.getMeasureID());
            measureData.put(MEASURE_VALUE_KEY, measurement.getActualMeasure());

            measuresJsonArray.add(measureData);
        }

        data.put(MEASURESARRAY_KEY, measuresJsonArray);

    }



}
