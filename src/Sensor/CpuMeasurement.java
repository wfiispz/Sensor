package Sensor;


import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

public class CpuMeasurement extends Measurement{

    final static String MAX_PERCENTAGE_MEASURE_VALUE = "100";

    CpuMeasurement(Sigar _sigar){
        super( _sigar, "CpuUsage", "Percentage");
        setMeasureMaxValue(MAX_PERCENTAGE_MEASURE_VALUE);
    }

    public String getActualMeasure(){
        double cpuUsageValue = -1;

        try {
            cpuUsageValue = sigar.getCpuPerc().getCombined()*100;
        } catch (SigarException sigarException) {
            System.out.println(sigarException.getMessage());
            sigarException.printStackTrace();
        }

        return Double.toString(cpuUsageValue);
    }

}
