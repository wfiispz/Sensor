package Sensor;


import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

public class CpuMeasurement extends Measurement{


    CpuMeasurement(Sigar _sigar){
        super("CPU measure");
        sigar=_sigar;


    }

    public String getActualMeasure(){
        double value=-1;

        try {
            value = sigar.getCpuPerc().getCombined()*100;
        } catch (SigarException var2) {
            var2.printStackTrace();
        }

        return Double.toString(value)  + " %";
    }

}
