package Sensor;

import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

/**
 * Created by jak12 on 26.05.2017.
 */
public class DiskMeasurement extends Measurement {

    FileSystem[] fileSystemList = null;

    DiskMeasurement(Sigar _sigar) {
        super(_sigar, "DiskUsage", "Megabytes");

        prepareFileSystemInfo();
        setMeasureMaxValue(calculateMaxValue());
    }

    void prepareFileSystemInfo(){
        try {
            fileSystemList = sigar.getFileSystemList();
        } catch (SigarException sigarException) {
            System.out.println(sigarException.getMessage());
            sigarException.printStackTrace();
        }
    }

    FileSystemUsage prepareFileSystemUsage(FileSystem fileSystem){
        FileSystemUsage fileSystemUsage = new FileSystemUsage();

        try {
            fileSystemUsage.gather(sigar, fileSystem.getDirName());
        } catch (SigarException sigarException) {
            System.out.println(sigarException.getMessage());
            sigarException.printStackTrace();
        }
        return fileSystemUsage;
    }

    String calculateMaxValue() {
        Long memoryTotalValueKilobytes = 0L;

        for (FileSystem fileSystem : fileSystemList) {
            FileSystemUsage fileSystemUsage = prepareFileSystemUsage(fileSystem);
            memoryTotalValueKilobytes += fileSystemUsage.getTotal();
        }

        Long memoryTotalValueMegabytes = kilobytesToMegabytes(memoryTotalValueKilobytes);
        return Long.toString(memoryTotalValueMegabytes);
      }

    private static Long kilobytesToMegabytes(Long memoryUsageValueBytes) {
        final Long kilobytesToMegabytesDivider = 1024L;
        return memoryUsageValueBytes / kilobytesToMegabytesDivider;
    }

    public String getActualMeasure() {
        Long memoryUsageValueKilobytes = 0L;

        for (FileSystem fileSystem : fileSystemList) {
            FileSystemUsage fileSystemUsage = prepareFileSystemUsage(fileSystem);
            memoryUsageValueKilobytes += fileSystemUsage.getUsed();
        }

        Long memoryUsageValueMegabytes = kilobytesToMegabytes(memoryUsageValueKilobytes);
        return Long.toString(memoryUsageValueMegabytes);
    }

}