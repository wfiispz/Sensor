package Sensor;


import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

public class MemoryMeasurement extends Measurement {
    
    MemoryMeasurement(Sigar _sigar){
        super("Memory measure", _sigar);
    }

    private static Long bytesToMegabytes(Long memoryUsageValueBytes){
        final Long bytesToKilobytesDivider = 1024L;
        final Long kilobytesToMegabytesDivider = 1024L;
        return memoryUsageValueBytes / bytesToKilobytesDivider / kilobytesToMegabytesDivider;
    }

    public String getActualMeasure(){
        try {
            Long memoryUsageValueBytes = sigar.getMem().getActualUsed(); //getActualUsed() returns memory in bytes
            Long memoryUsageValueMegabytes = bytesToMegabytes(memoryUsageValueBytes);
            return Long.toString(memoryUsageValueMegabytes) + "MB";
        } catch (SigarException sigarException) {
            sigarException.printStackTrace();
        }

        return "Memory measure error";
    }
}
