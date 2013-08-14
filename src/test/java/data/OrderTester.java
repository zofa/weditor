package data;


import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;


/**
 * Created with IntelliJ IDEA.
 * User:
 * Date: 8/2/13
 * Time: 5:22 PM
 */
public class OrderTester {

    private final String theOrder = "H|KRAU1|SOPRM|sdfg|259445|20130430|KRAUS001|USD|KRAUS001|SHP|20160104|Sharon Devries|Sharon Devries|1135 Westview Dr.|sdfg|sdfg|Boulder|CO|80303|sdfg|KRAU1|UPS|GND|sdfg|720-287-3947|||\n";
    private final String theOrderEntry = "D|asd|7-Sprayer-Holder-KPF-1602|EA|1|0.00|1|\n";
    OrderEntry oe = new OrderEntry(theOrderEntry);
    private Order fixture = new Order(theOrder);

//    @Before
//    OrderTester() {
//        logging.disable(logging.CRITICAL);
//    }

    @Test
    public void testGetRecordType() throws Exception {

        assertNotNull(fixture.getColumn1());
    }

    @Test
    public void testValidateMandatoryFields() throws Exception {
        assertNotNull(fixture.getColumn1());
        assertNotNull(fixture.getColumn2());
        assertNotNull(fixture.getColumn3());
        assertNotNull(fixture.getColumn4());
        assertNotNull(fixture.getColumn5());
        assertNotNull(fixture.getColumn6());
        assertNotNull(fixture.getColumn7());
        assertNotNull(fixture.getColumn8());
        assertNotNull(fixture.getColumn9());
        assertNotNull(fixture.getColumn10());
        assertNotNull(fixture.getColumn11());
        assertNotNull(fixture.getColumn12());
        assertNotNull(fixture.getColumn13());
        assertNotNull(fixture.getColumn14());
        assertNotNull(fixture.getColumn15());
        assertNotNull(fixture.getColumn16());
        assertNotNull(fixture.getColumn17());
        assertNotNull(fixture.getColumn18());
        assertNotNull(fixture.getColumn19());
    }

    @Test
    public void testSetRecordType() throws Exception {

    }

    @Test
    public void testGetOrderEntries() throws Exception {

    }

    @Test
    public void testValidateOrder() throws Exception {
        assertNull(fixture.validateOrder());
    }

    @Test
    public void testToString() throws Exception {
        assertNotNull(fixture.toString());
    }

    @Test
    public void testValidateOrderEntries() throws Exception {

    }

    @Test
    public void testFixOrderEntries() throws Exception {

    }


}
