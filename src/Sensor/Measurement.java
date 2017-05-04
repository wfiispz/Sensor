package Sensor;

import org.hyperic.sigar.Sigar;


abstract public class Measurement {

    /**
     * Type of measurement, set by inheriting classes, readonly for user
     */
    private String measurementType;
    protected Sigar sigar;

    Measurement(String _measurementType, Sigar _sigar){
        measurementType = _measurementType;
        sigar = _sigar;
    }

    String getMeasurementType(){
        return measurementType;
    }

    public String getActualMeasure(){return "-1";}

}
