import java.io.IOException;
import student.TestCase;

/**
 * test class for buffer
 * 
 * @author omaralshikh ahmad malik
 * @version 8/12/2020
 *
 */
public class BufferTest extends TestCase {

    private Buffer buf;


    /**
     * set up method
     */
    public void setUp() {

        buf = new Buffer(null, null, null, 0, 0);
    }


    /**
     * tests is used
     * 
     * @throws IOException
     */
    public void testFlush() throws IOException {

        buf.flush();
        assertFalse(buf.isThisBufferUsed());

    }
}
