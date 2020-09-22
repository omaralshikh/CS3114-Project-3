
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import student.TestCase;

/**
 * main test
 * 
 * @author omaralshikh ahmad malik
 * @version 8/12/2020
 */
public class HeapSortTest extends TestCase {

    private final ByteArrayOutputStream outContent =
        new ByteArrayOutputStream();


    /**
     * tests main in heap sort
     * 
     * @throws IOException
     */
    public void testMain() throws IOException {

        System.setOut(new PrintStream(outContent));

        HeapSort sorter = new HeapSort();
        String[] arr = { "p3_input_sample.txt", "2", "output.txt" };
        sorter.main(arr);

        assertEquals("5 8404 8131 244 16634 2746 24619 6627 \n", outContent
            .toString());
        outContent.reset();
        String[] arr2 = { "t.txt", "2", "output.txt" };
        sorter.main(arr2);

        assertEquals("\n", outContent.toString());
        outContent.reset();

    }

}
