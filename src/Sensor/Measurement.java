package Sensor;

import org.hyperic.sigar.Sigar;


abstract public class Measurement {

    /**
     * Type of measurement, set by inheriting classes, readonly for user
     */
    private String MeasurementType;
    Sigar sigar;

    Measurement(String _MeasurementType){
        MeasurementType=_MeasurementType;
    }

    String getMeasurementType(){
        return MeasurementType;
    }

    public String getActualMeasure(){return "-1";}

}
