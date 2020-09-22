import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * buffer pool class that uses buffer
 * 
 * @author omaralshikh ahmadmalik
 * @version 8/12/2020
 */
public class BufferPool {

    private RandomAccessFile disk; // The file the data is taken from and
                                   // written to.
    private DLList<Buffer> bufferList; // The LRU list of buffers.
    private int blockSize; // The size of a single block.
    private Buffer[] pool; // An array which holds direct reference to the
                           // buffers in the list. This will allow us to access
                           // the buffers in O(1) time.
    private int maxBuff; // The maximum number of buffers that can be made for
                         // the data file.
    private PrintStats stats; // The class which holds all the important
                              // statistics information.


    /**
     * constructor
     * 
     * @param file
     *            file to be sorted
     * @param stats
     *            output file
     * @param numBuffers
     *            number of buffers
     * @param blockSize
     *            size of block of data
     * @throws IOException
     *             thrown if file doesnt exist
     */
    public BufferPool(
        File file,
        PrintStats stats,
        int numBuffers,
        int blockSize)
        throws IOException {
        /* Initialize with passed in parameters */
        this.blockSize = blockSize;
        this.stats = stats;
        /* Make a new RandomAccessFile to read and write data from */
        disk = new RandomAccessFile(file, "rw");
        /* Calculate the max number of buffers this data file can accommodate */
        maxBuff = ((int)disk.length() / blockSize);
        /*
         * Make a buffer array with of size maxBuff so that all buffers can be
         * referenced through out the LRU list
         */
        pool = new Buffer[maxBuff];
        /*
         * Initialize a new list of size number of buffers allowed for the sort
         */
        bufferList = new DLList<Buffer>(numBuffers);
    }


    /**
     * gets the buffer at the desired index point
     * 
     * @param index
     *            index of buffer
     * 
     * @return index at the buffer pool
     */
    public Buffer getBuffer(int index) {
        /* If the buffer at the specified index is null make a new buffer */
        if (pool[index] == null) {
            pool[index] = new Buffer(this, this.stats, disk, (index
                * blockSize), blockSize);
        }
        /* Else return the buffer at the index */
        return pool[index];
    }


    /**
     * checks to see if buffer was used
     * 
     * @param buffer
     *            buffer to be checked
     * @throws IOException
     */
    public void used(Buffer buffer) throws IOException {
        /* Attempt to promote or remove a buffer */
        Buffer removed = bufferList.moveupOradd(buffer);
        /* If it was removed and the buffer had been used flush it */
        if (removed != null && buffer.isThisBufferUsed()) {
            buffer.flush();
        }
    }


    /**
     * gets the max size of pool
     * 
     * @return maximum buffers
     */
    public int getMaxSize() {
        /* Return the size of this pool array */
        return maxBuff;
    }


    /**
     * flushes the data
     * 
     * @throws IOException
     */
    public void flush() throws IOException {
        /* Flush out all the buffers in the pool */
        for (int i = 0; i < maxBuff; i++) {
            pool[i].flush();
        }
    }

}
