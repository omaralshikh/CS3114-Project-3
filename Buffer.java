import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Buffer class that makes the buffer
 * 
 * @author omaralshikh ahmad malik
 * @version 8/12/2020
 */
public class Buffer {
    private BufferPool pool; // The pool this buffer in is.
    private RandomAccessFile file; // the file the data is taken from.
    private int offset; // the offset of this buffer in the file.
    private int size; // the size of this buffer.
    private byte[] data; // the array that will hold the buffer data.
    private boolean isUsed; // Bit to mark if this buffer has been used yet.
    private boolean filled; // Bit to mark of this buffer is filled with data or
                            // not.
    private PrintStats stat; // The stat file that will record important
                             // statistics


    /**
     * constructor
     * 
     * @param pool
     *            buffer pool
     * @param stats
     *            output file
     * @param file
     *            random access file
     * @param offset
     *            offset of buffer
     * @param size
     *            size of buffer
     */
    public Buffer(
        BufferPool pool,
        PrintStats stats,
        RandomAccessFile file,
        int offset,
        int size) {

        /* Initialize these fields from passed parameters */
        this.stat = stats;
        this.pool = pool;
        this.file = file;
        this.offset = offset;
        this.size = size;

        /* Initialize both as false */
        this.isUsed = false;
        this.filled = false;

    }


    /**
     * reads the bytes of data from the input file
     * 
     * @return the data stored in an array
     * @throws IOException
     */
    public byte[] readBlock() throws IOException {
        /* Mark this buffer as a used buffer */
        pool.used(this);
        /* If this buffer is not filled with data */
        if (!filled) {
            /* Increment number of misses */
            stat.incremenMisses();
            /* make a new data array to hold the buffer data */
            data = new byte[size];
            /* Seek the start point of this buffer in the file */
            file.seek(offset);
            /* read the data from the file into the data array */
            file.read(data);
            /* Mark this buffer as filled as it now has data in it */
            filled = true;
            /* Mark this buffer as not used as new data has been put in it */
            isUsed = false;
        }
        else {
            /* Else increase hits statistic */
            stat.incrementHits();
        }
        /* Return the data array */
        return this.data;
    }


    /**
     * writes the data to an array
     * 
     * @param dataa
     *            data to be written to
     * @throws IOException
     */
    public void write(byte[] dataa) throws IOException {
        /* Mark this buffer as used in the pool */
        pool.used(this);
        /* Make this buffers data array point to the new array passed in */
        this.data = dataa;
        /* Mark this buffer as filled */
        this.filled = true;
        /* Mark this buffer as filled */
        this.isUsed = true;
    }


    /**
     * flushes the file after use
     * 
     * @throws IOException
     */
    public void flush() throws IOException {
        /*
         * Only write this buffers data to the file if it has been marked as
         * used
         */
        if (isUsed) {
            /* Increment the number of writes */
            stat.incrementWrites();
            /* Find the point in the file where this buffer will be written */
            file.seek(offset);
            /* Write this buffers data in to the file */
            file.write(data);
            /* Mark this buffer as not used any more */
            isUsed = false;
        }
        /* null out this buffers data */
        data = null;
        /* Mark this buffer as not filled anymore */
        filled = false;
    }


    /**
     * Returns the value of the isUsed variable
     * 
     * @return boolean
     *         isUsed
     */
    public boolean isThisBufferUsed() {
        /* Return the boolean value of isUsed */
        return this.isUsed;
    }
}
