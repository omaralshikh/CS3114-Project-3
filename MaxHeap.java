import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Max heap algorithm class
 * 
 * @author omaralshikh ahmadmalik
 * @version 8/12/2020
 *
 */
public class MaxHeap {

    private BufferPool pool; // The Buffer Pool to get data from
    private int blockSize; // Size of one block in bytes
    private int recordSize; // Size of one record in bytes
    private int size; // Size of the current heap (number of records in heap)


    /**
     * constructor
     * 
     * @param filename
     *            file to be sorted
     * @param stats
     *            output file stats
     * @param numBuffers
     *            number of buffers
     * @param blockSize
     *            size of block
     * @param recordSize
     *            size of the record
     * @throws IOException
     * 
     */
    public MaxHeap(
        String filename,
        PrintStats stats,
        int numBuffers,
        int blockSize,
        int recordSize)
        throws IOException {
        /* Initialize variables from passed parameters */
        this.blockSize = blockSize;
        this.recordSize = recordSize;
        /* Make a new file object with the file name passed */
        File file = new File(filename);
        /* Create a new buffer pool */
        pool = new BufferPool(file, stats, numBuffers, blockSize);
        /* Get the size of the heap */
        size = totalNumRecords();
    }


    /**
     * calls on build heap to start the sorting process
     * 
     * @throws IOException
     * @throws IOException
     */
    public void sort() throws IOException {
        /* First Build the heap before we sort */
        buildHeap();
        /* Keep removing max value to from the heap */
        for (int i = 0; i < totalNumRecords(); i++) {
            removeMax();
        }
    }


    /**
     * Build the heap by sifting down the calculated indexes
     * 
     * @throws IOException
     */
    public void buildHeap() throws IOException {
        /* Build the heap by calling siftdown on relevant indices */
        for (int i = (size / 2) - 1; i >= 0; i--) {
            siftdown(i);
        }
    }


    /**
     * sifts down the heap to move an object into correct position
     * 
     * @param index
     *            the index of the node to siftdown
     * @throws IOException
     */
    private void siftdown(int index) throws IOException {
        /* For invalid index return */
        if ((index < 0) || (index >= size)) {
            return;
        }
        /* While the index does not represent a leaf node */
        while (!isLeafNode(index)) {
            /* get the left and right childs of the index */
            int leftChild = 2 * index + 1;
            int rightChild = leftChild + 1;

            /* Make comparison to increment left child */
            if ((leftChild < (size - 1)) && (getKey(leftChild) < getKey(
                rightChild))) {
                leftChild++;
            }

            /* return if already sorted */
            if (getKey(index) >= getKey(leftChild)) {
                return;
            }

            /* Swap if not sorted */
            swap(index, leftChild);

            /* Update index */
            index = leftChild;
        }
    }


    /**
     * removes the max record of the heap
     * 
     * @throws IOException
     */
    public void removeMax() throws IOException {
        /* Swap the first and last element in heap */
        swap(0, size - 1);
        /* Decrement heap size */
        size--;
        /* Sift down starting at index 0 */
        siftdown(0);
    }


    /**
     * checks to see if index of node refers to a leaf
     * 
     * @param index
     *            the index to check
     * @return if index is a leaf
     */
    public boolean isLeafNode(int index) {
        /* Calculate wheather the index represents a leaf node */
        return ((index >= size / 2) && (index < size));
    }


    /**
     * gets the key for a record in the array
     * 
     * @param index
     *            key index
     * @return the key of the record
     * @throws IOException
     */
    public short getKey(int index) throws IOException {
        /* Get the block at the index */
        int block = getBlockIndex(index);
        /* Get the offset index in the block */
        int offset = getOffsetIndex(index);
        /* get the buffer which contains the block */
        byte[] buffer = pool.getBuffer(block).readBlock();
        /* Get the key in the buffer */
        short key = ByteBuffer.wrap(buffer).getShort(offset);
        /* Return the key */
        return key;
    }


    /**
     * gets the value of the array record
     * 
     * @param index
     *            index of record
     * @return value at index
     * @throws IOException
     */
    public short getValue(int index) throws IOException {
        /* Get the block at the index */
        int block = getBlockIndex(index);
        /* Get the offset index in the block */
        int offset = getOffsetIndex(index);
        /* get the buffer which contains the block */
        byte[] buffer = pool.getBuffer(block).readBlock();
        /* Get the value in the buffer */
        short val = ByteBuffer.wrap(buffer).getShort(offset + 2);
        /* Return the value */
        return val;
    }


    /**
     * gets the block at the index
     * 
     * @param index
     *            index of record
     * @return the index
     */
    private int getBlockIndex(int index) {
        /* Get the index of the block for the index of a record */
        return (index * recordSize) / blockSize;
    }


    /**
     * gets the offset at the index
     * 
     * @param index
     *            index of the block
     * @return the index
     */
    private int getOffsetIndex(int index) {
        /* Get the index in a block from the index of the record in the heap */
        return (index * recordSize) % blockSize;
    }


    /**
     * swap two records in the array
     * 
     * @param n1
     *            first number to be swapped
     * @param n2
     *            second number to be swapped
     * @throws IOException
     */
    public void swap(int n1, int n2) throws IOException {
        /* Get the block index for both record indexes */
        int block1 = getBlockIndex(n1);
        int block2 = getBlockIndex(n2);

        /* Acquire the buffers holding the blocks */
        Buffer buffer1 = pool.getBuffer(block1);
        Buffer buffer2 = pool.getBuffer(block2);

        /* make 2 byte variables to store temporarily */
        byte[] rec1 = new byte[recordSize];
        byte[] rec2 = new byte[recordSize];

        /* Get the offset for where the numbers are stored in the heap */
        int offset1 = getOffsetIndex(n1);
        int offset2 = getOffsetIndex(n2);

        /* Call helper to perform the copys */
        swapHelper(buffer1, buffer2, rec1, rec2, offset1, offset2);
    }


    /**
     * helper method to perform swap
     * 
     * @param buffer1
     *            first buffer to be swapped
     * @param buffer2
     *            second buffer to be swapped
     * @param rec1
     *            byte array of buffer 1
     * @param rec2
     *            rec1 byte array of buffer 2
     * @param offset1
     *            offset of buffer 1
     * @param offset2
     *            offset of buffer 2
     * @throws IOException
     */
    private void swapHelper(
        Buffer buffer1,
        Buffer buffer2,
        byte[] rec1,
        byte[] rec2,
        int offset1,
        int offset2)
        throws IOException {
        /* Temp variable to hold the buffer values */
        byte[] buffer;

        /* Get the first Buffer and copy value into it */
        buffer = buffer1.readBlock();
        System.arraycopy(buffer, offset1, rec1, 0, recordSize);

        /* Get the first Buffer and copy value into it */
        buffer = buffer2.readBlock();
        System.arraycopy(buffer, offset2, rec2, 0, recordSize);

        /* Copy temp value into it rec1 */
        System.arraycopy(rec1, 0, buffer, offset2, recordSize);

        /* Write back to buffer2 */
        buffer2.write(buffer);

        /* get buffer1 */
        buffer = buffer1.readBlock();

        /* Copy value into rec2 */
        System.arraycopy(rec2, 0, buffer, offset1, recordSize);

        /* Write back to buffer1 */
        buffer1.write(buffer);
    }


    /**
     * gets the number of records in the array
     * 
     * @return number of records
     */
    public int totalNumRecords() {
        /* Get the size of the pool first */
        int numRec = pool.getMaxSize();
        /* Adjust the number of records with block size and record size */
        numRec = numRec * (blockSize / recordSize);
        /* Return calculated number of records */
        return numRec;
    }


    /**
     * flushes the data
     * 
     * @throws IOException
     */
    public void flush() throws IOException {
        /* Call pools flush function to flush all buffers in the pool */
        pool.flush();
    }


    /**
     * prints the data in correct format
     * 
     * @throws IOException
     */
    public void printFormattedOutput() throws IOException {
        /* Get size of one record */
        int oneRec = blockSize / recordSize;
        /* Get total number of records */
        int totalRecords = totalNumRecords();
        /* calculate the number of iterations our for loop will need */
        int iterations = (totalRecords / oneRec);
        /* Iterate through the the file to get the key and value and print */
        for (int i = 0; i < iterations; i++) {
            System.out.print(getKey(i * oneRec) + " " + getValue(i * oneRec)
                + " ");
            if (((i + 1) % 8 == 0)) {
                System.out.print("\n");
            }
        }
        System.out.print("\n");
    }
}
