package Sensor;


import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

public class MemoryMeasurement extends Measurement {
    
    MemoryMeasurement(Sigar _sigar){

        super(_sigar,"MemoryUsage", "Megabytes");
        setMeasureMaxValue(calculateMaxValue());
    }

    String calculateMaxValue(){
        try{
            Long memoryTotalValueBytes = sigar.getMem().getTotal();
            Long memoryTotalValueMegabytes = bytesToMegabytes(memoryTotalValueBytes);
            return Long.toString(memoryTotalValueMegabytes);
        } catch (SigarException sigarException) {
            System.out.println(sigarException.getMessage());
            sigarException.printStackTrace();
        }
        return "-1"; // this should be a signal for Monitor that sensor is not working as intended, altough it shouldn't stop the program, as other measurements might work properly.
        // Some format for error messages could be added but it would also require some changes in monitor, so we leave this for the future.
    }

    private static Long bytesToMegabytes(Long memoryUsageValueBytes){
        final Long bytesToKilobytesDivider = 1024L;
        final Long kilobytesToMegabytesDivider = 1024L;
        return memoryUsageValueBytes / bytesToKilobytesDivider / kilobytesToMegabytesDivider;
    }

    public String getActualMeasure(){
        try {
            Long memoryUsageValueBytes = sigar.getMem().getActualUsed();
            Long memoryUsageValueMegabytes = bytesToMegabytes(memoryUsageValueBytes);
            return Long.toString(memoryUsageValueMegabytes);
        } catch (SigarException sigarException) {
            System.out.println(sigarException.getMessage());
            sigarException.printStackTrace();
        }

        return "Memory measure error";
    }
}
