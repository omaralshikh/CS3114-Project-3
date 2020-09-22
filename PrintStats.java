import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * class that prints out the correct format for the output file
 * 
 * @author omaralshikh ahmad malik
 * @version 8/12/2020
 */
public class PrintStats {

    private static String dataFileName;
    private static String outputFileName;
    private static int cacheHits;
    private static int cacheMisses;
    private static int diskWrites;
    private static long timeToSort;


    /**
     * @param dataFileName
     *            file name used
     * @param outputFileName
     *            file that data prints to
     */
    @SuppressWarnings("static-access")
    public PrintStats(String dataFileName, String outputFileName) {
        this.dataFileName = dataFileName;
        this.outputFileName = outputFileName;
        cacheHits = 0;
        cacheMisses = 0;
        diskWrites = 0;
        timeToSort = 0;
    }


    /**
     * Increment the number of cache hits
     */
    public void incrementHits() {
        cacheHits++;
    }


    /**
     * Increment the number of misses
     */
    public void incremenMisses() {
        cacheMisses++;
    }


    /**
     * Increment the number of disk writes
     */
    public void incrementWrites() {
        diskWrites++;
    }


    /**
     * @param time
     *            the time taken to run the sorting algorithm
     */
    public void setTime(long time) {
        timeToSort = time;
    }


    /**
     * output data
     * 
     * @return the correct format string
     */
    public static String outPutString() {
        String outPutString = "------  STATS ------\n";
        outPutString += "File name: " + dataFileName + "\n";
        outPutString += "Cache Hits: " + cacheHits + "\n";
        outPutString += "Cache Misses: " + cacheMisses + "\n";
        outPutString += "Disk Reads: " + cacheMisses + "\n";
        outPutString += "Disk Writes: " + diskWrites + "\n";
        outPutString += "Time to Sort: " + timeToSort;
        outPutString += "\n\n";
        return outPutString;
    }


    /**
     * Write the contents of this class to the output file.
     * 
     * @throws IOException
     */
    public void writeToFile() throws IOException {
        File statsFile = new File(outputFileName);
        statsFile.createNewFile();
        FileWriter statfileWriter = new FileWriter(statsFile, true);
        BufferedWriter statOut = new BufferedWriter(statfileWriter);
        statOut.write(PrintStats.outPutString());
        statOut.flush();

    }
}
