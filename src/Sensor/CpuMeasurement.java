package Sensor;


import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

public class CpuMeasurement extends Measurement{


    CpuMeasurement(Sigar _sigar){
        super("CPU measure",_sigar);
    }

    public String getActualMeasure(){
        double cpuUsageValue = -1;

        try {
            cpuUsageValue = sigar.getCpuPerc().getCombined()*100;
        } catch (SigarException sigarException) {
            sigarException.printStackTrace();
        }

        return Double.toString(cpuUsageValue)  + " %";
    }

}
