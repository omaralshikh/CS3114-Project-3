import java.io.IOException;

/**
 * Heapsort class This is the main class which will run the sorting algorithm in
 * main
 * 
 * @author omaralshikh ahmad malik
 * @version 8/12/2020
 */

public class HeapSort {

    /* Change these depending on the data file. Each number is in bytes */
    private final static int BLOCK_SIZE = 4096;
    private final static int RECORD_SIZE = 4;


    /**
     * @param arg
     *            program arguments
     * @throws IOException
     */
    public static void main(String[] arg) throws IOException {

        /* Name of the data file */
        String dataFile;
        /* Number of buffers */
        int numBuffers;
        /* Name of output file */
        String outputFile;

        /* Acquire args into variables */
        dataFile = arg[0];
        numBuffers = Integer.parseInt(arg[1]);
        outputFile = arg[2];

        /*
         * Print Stats File which holds all the statistics of the sorting
         * algorithm
         */
        PrintStats stats = new PrintStats(dataFile, outputFile);

        /* Make the max heap object to run the algorithm */
        MaxHeap maxHeap = new MaxHeap(dataFile, stats, (numBuffers), BLOCK_SIZE,
            RECORD_SIZE);

        /* Save the start time of the algorithm */
        long startTime = System.currentTimeMillis();

        /* Run the Sorting algorithm and flush to the file */
        maxHeap.sort();
        maxHeap.flush();

        /* Save the end time of the algorithm */
        long endTime = System.currentTimeMillis();

        /* Record the time to run the algorithm */
        stats.setTime(endTime - startTime);

        /* Print out the statistics to the output file */
        stats.writeToFile();

        /* Print the first record of from each block */
        maxHeap.printFormattedOutput();
    }
}
