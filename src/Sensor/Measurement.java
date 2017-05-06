package Sensor;

import org.hyperic.sigar.Sigar;

import java.util.UUID;


abstract public class Measurement {

    /**
     * Type of measurement, set by inheriting classes, readonly for user
     */
    private String measurementType;
    private String measureUnit;
    private String measureID;
    private String measureMaxValue;


    protected Sigar sigar;

    Measurement(Sigar _sigar, String _measurementType, String _measureUnit){
        measurementType = _measurementType;
        measureUnit = _measureUnit;
        measureID = UUID.randomUUID().toString();
        sigar = _sigar;
    }

    String getMeasurementType(){
        return measurementType;
    }

    public String getActualMeasure(){
        return "-1";
    }
    public String getMeasureUnit(){
        return measureUnit;
    }
    public String getMeasureID(){
        return measureID;
    }
    public String getMeasureMaxValue() {
        return measureMaxValue;
    }

    protected void setMeasureMaxValue(String _measureMaxValue) {
        measureMaxValue = _measureMaxValue;
    }
}
