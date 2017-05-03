package Sensor;


import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

public class MemoryMeasurement extends Measurement {
    
    MemoryMeasurement(Sigar _sigar){
        super("Memory measure");
        sigar = _sigar;
    }

    public String getActualMeasure(){
        Long value = (long) -1;

        try {
            value = sigar.getMem().getActualUsed(); //getActualUsed() returns memory in bytes
        } catch (SigarException var2) {
            var2.printStackTrace();
        }
        return Long.toString(value  / 1024L / 1024L)  + " MB";
    }
}
