package Sensor;


import org.hyperic.sigar.Sigar;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * Created by jak12 on 30.04.2017.
 */
public class SensorController {

    //json mapping
    private final static String DATATYPE_KEY = "datatype";
    private final static String RESOURCE_ID_KEY = "resourceID";
    private final static String DATA_VALUE = "data";
    private final static String METADATA_VALUE = "metadata";
    private final static String NAME_KEY = "name";
    private final static String DESCRIPTION_KEY = "description";
    private final static String MEASURE_TYPE_KEY = "measureType";
    private final static String MEASURE_UNIT_KEY = "unit";
    private final static String MEASURE_ID_KEY = "measureID";
    private final static String MAX_VALUE_KEY = "maxValue";
    private final static String MEASURESARRAY_KEY = "measuresArray";
    private final static String MEASURE_VALUE_KEY = "value";
    private final static String TIMESTAMP_KEY = "timestamp";

    private final static String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

    private final static String CONFIG_FILE = "configuration.json";
    private final static String HOSTNAME_KEY = "hostname";
    private final static String HOST_DESCRIPTION_KEY = "hostDescription";
    private final static String MONITOR_IP_KEY = "monitorIP";
    private final static String MONITOR_PORT_KEY = "monitorPortNumber";

    private JSONObject dataForMonitor = new JSONObject();
    private JSONArray measuresJsonArray = new JSONArray();
    private String resourceID = UUID.randomUUID().toString();
    private String hostname = "";
    private String hostDescription = "";


    private UDPConnectionController udpController = new UDPConnectionController();
    private List<Measurement> measurementsList = new ArrayList<>();

    private Sigar sigar = new Sigar();

    public void startSensor(){
        configureSensor();

        //noinspection InfiniteLoopStatement
        while(true) {
            sendMetadata();
            waitOneSecond();

            for(int i = 0; i < 10; ++i)
                run();
        }
    }

    private boolean readConfigFromFile(){
        JSONParser parser = new JSONParser();

        try {
            JSONObject configJSON = (JSONObject) parser.parse(new FileReader(CONFIG_FILE));
            hostname = (String) configJSON.get(HOSTNAME_KEY);
            hostDescription = (String) configJSON.get(HOST_DESCRIPTION_KEY);
            udpController.setIPAddress((String) configJSON.get(MONITOR_IP_KEY));
            udpController.setPortNumber((String) configJSON.get(MONITOR_PORT_KEY));
        }
        catch (Exception configException){
            System.out.println(configException.getMessage());
            configException.printStackTrace();
            return false;
        }

        return true;
    }

    private void configureSensor(){
        if(!readConfigFromFile()) { //let the user enter config data if config file is absent or broken
            getHostInformationFromUser();
            getMonitorAdressFromUser();
        }
        chooseMeasurements();
    }
    
    private void getMonitorAdressFromUser(){
        Scanner reader = new Scanner(System.in);
        System.out.println("Enter monitor ip adress ");
        udpController.setIPAddress(reader.nextLine());
        System.out.println("Enter port number ");
        udpController.setPortNumber(reader.nextLine());
    }
    private void getHostInformationFromUser(){
        Scanner reader = new Scanner(System.in);
        System.out.println("Enter hostname ");
        hostname = reader.nextLine();
        System.out.println("Enter host description ");
        hostDescription = reader.nextLine();
    }

    private void chooseMeasurements(){
        measurementsList.add(new MemoryMeasurement(sigar));
        measurementsList.add(new CpuMeasurement(sigar));
        measurementsList.add(new DiskMeasurement(sigar));
    }

    private void run(){
        prepareMeasurementsData();
        udpController.sendData(dataForMonitor.toJSONString());

        waitOneSecond();
    }

    private void sendMetadata(){
        prepareMetadata();
        udpController.sendData(dataForMonitor.toJSONString());
    }

    private void prepareMetadata() {
        cleanDataJson();

        dataForMonitor.put(DATATYPE_KEY, METADATA_VALUE);
        dataForMonitor.put(RESOURCE_ID_KEY, resourceID);
        dataForMonitor.put(NAME_KEY, hostname);
        dataForMonitor.put(DESCRIPTION_KEY, hostDescription);

        for (Measurement measurement: measurementsList) {
            JSONObject measureMetada = prepareMeasureMetadata(measurement);
            measuresJsonArray.add(measureMetada);
        }

        dataForMonitor.put(MEASURESARRAY_KEY, measuresJsonArray);
    }

    JSONObject prepareMeasureMetadata(Measurement measurement){
        JSONObject measureMetadata = new JSONObject();
        measureMetadata.put(MEASURE_TYPE_KEY, measurement.getMeasurementType());
        measureMetadata.put(MEASURE_UNIT_KEY, measurement.getMeasureUnit());
        measureMetadata.put(MEASURE_ID_KEY, measurement.getMeasureID());
        measureMetadata.put(MAX_VALUE_KEY, measurement.getMeasureMaxValue());
        return measureMetadata;
    }

    private void prepareMeasurementsData(){
        cleanDataJson();

        dataForMonitor.put(DATATYPE_KEY, DATA_VALUE);
        dataForMonitor.put(RESOURCE_ID_KEY, resourceID);
        dataForMonitor.put(TIMESTAMP_KEY, getCurrentTime());

        for (Measurement measurement: measurementsList) {
            JSONObject measureData = prepareMeasureData(measurement);
            measuresJsonArray.add(measureData);
        }

        dataForMonitor.put(MEASURESARRAY_KEY, measuresJsonArray);
    }

    private void cleanDataJson(){
        dataForMonitor.clear();
        measuresJsonArray.clear();
    }

    private String getCurrentTime(){
        DateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }

    JSONObject prepareMeasureData(Measurement measurement){
        JSONObject measureData = new JSONObject();
        measureData.put(MEASURE_ID_KEY, measurement.getMeasureID());
        measureData.put(MEASURE_VALUE_KEY, measurement.getActualMeasure());
        return measureData;
    }

    void waitOneSecond(){
        try {
            Thread.sleep(1000);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

}
