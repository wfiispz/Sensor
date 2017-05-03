package Sensor;


import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

public class MemoryMeasurement extends Measurement {
    
    MemoryMeasurement(Sigar _sigar){
        super("Memory measure", _sigar);
    }

    public String getActualMeasure(){
        Long memoryUsageValueBytes = (long) -1;
        Long memoryUsageValueMegabytes = (long) -1;
        final Long bytesToKilobytesDivider = 1024L;
        final Long kilobytesToMegabytesDivider = 1024L;

        try {
            memoryUsageValueBytes = sigar.getMem().getActualUsed(); //getActualUsed() returns memory in bytes
            memoryUsageValueMegabytes = memoryUsageValueBytes / bytesToKilobytesDivider / kilobytesToMegabytesDivider;
        } catch (SigarException sigarException) {
            sigarException.printStackTrace();
        }

        return Long.toString(memoryUsageValueMegabytes)  + "MB";
    }
}
