package Sensor;


import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

public class MemoryMeasurement extends Measurement {
    
    MemoryMeasurement(Sigar _sigar){

        super(_sigar,"MemoryUsage", "Megabytes");

        try{
            Long memoryTotalValueBytes = sigar.getMem().getTotal();
            Long memoryTotalValueMegabytes = bytesToMegabytes(memoryTotalValueBytes);
            setMeasureMaxValue(Long.toString(memoryTotalValueMegabytes));
        } catch (SigarException sigarException) {
            sigarException.printStackTrace();
        }



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
            return Long.toString(memoryUsageValueMegabytes);
        } catch (SigarException sigarException) {
            sigarException.printStackTrace();
        }

        return "Memory measure error";
    }
}
