import student.TestCase;

/**
 * test class for doubly linked list
 * 
 * @author omaralshikh ahmad malik
 * @version 8/12/2020
 *
 */
public class DLListTest extends TestCase {

    private DLList list;


    /**
     * set up method
     */
    public void setUp() {
        list = new DLList<String>(10);
    }


    /**
     * tests the size method
     */
    public void testSize() {
        assertEquals(0, list.size());

    }


    /**
     * tests the move up method
     */

    @SuppressWarnings("unchecked")
    public void testMoveupOrAdd() {
        assertEquals(0, list.size());
        list.moveupOradd("PP");
        assertEquals(1, list.size());
        list.moveupOradd("PP");
        assertEquals(1, list.size());
        list.moveupOradd(null);
        assertEquals(2, list.size());
    }


    /**
     * tests the remove methods
     * 
     * 
     */
    @SuppressWarnings("unchecked")
    public void testRemove() {
        assertEquals(0, list.size());

        list.moveupOradd("pp");
        assertEquals(1, list.size());
        list.remove("pp");
        assertEquals(0, list.size());
        list.remove("pppp");

    }

}
