package data;


import junit.framework.TestCase;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;


public class OrderTest {

    private final String theOrder = "H|KRAU1|SOPRM|sdfg|259445|20130430|KRAUS001|USD|KRAUS001|SHP|20160104|Sharon Devries|Sharon Devries|1135 Westview Dr.|sdfg|sdfg|Boulder|CO|80303|sdfg|KRAU1|UPS|GND|sdfg|720-287-3947|||\n";
    private final String theOrderEntry = "D|asd|7-Sprayer-Holder-KPF-1602|EA|1|0.00|1|\n";
    OrderEntry OrderEntryFixture = new OrderEntry(theOrderEntry);
    private Order fixture = new Order(theOrder);

    @Test
    public void TestGetRecordType() throws Exception {
        assertNotNull(fixture.getColumn1());
    }

    @Test
    public void TestValidateMandatoryFields() throws Exception {
        assertNotNull(fixture.getColumn1());
        assertNotNull(fixture.getColumn2());
        assertNotNull(fixture.getColumn3());
        assertNotNull(fixture.getColumn4());
        assertNotNull(fixture.getPONum());
        assertNotNull(fixture.getOrderDate());
        assertNotNull(fixture.getDealerCol7());
        assertNotNull(fixture.getCurrency());
        assertNotNull(fixture.getDealerCol9());
        assertNotNull(fixture.getColumn10());
        assertNotNull(fixture.getColumn11());
        assertNotNull(fixture.getCustomerName1());
        assertNotNull(fixture.getCustomerName2());
        assertNotNull(fixture.getAddressLine1());
        assertNotNull(fixture.getColumn15());
        assertNotNull(fixture.getColumn16());
        assertNotNull(fixture.getCity());
        assertNotNull(fixture.getState());
        assertNotNull(fixture.getZipCode());
    }

    @Test
    public void TestSetRecordType() throws Exception {
        fixture.setRecordType(RecordTypes.ORDER_CANCEL);
        TestCase.assertEquals(fixture.getRecordType(), RecordTypes.ORDER_CANCEL);
        fixture.setRecordType(RecordTypes.ORDER_INFO);
        TestCase.assertEquals(fixture.getRecordType(), RecordTypes.ORDER_INFO);
    }

    @Test
    public void TestOrderEntries() throws Exception {
        fixture.addEntry(OrderEntryFixture);
        assertEquals(1, fixture.getOrderEntryCount());
        TestCase.assertEquals(OrderEntryFixture.toString(), fixture.getOrderEntry(0));
    }

    @Test
    public void TestValidateOrder() throws Exception {
        assertNull(fixture.validateOrder());
    }

    @Test
    public void TestToString() throws Exception {
        assertNotNull(fixture.toString());
        // assertEquals(theOrder , fixture.toString());
    }

    @Test
    public void TestValidateOrderEntries() throws Exception {

    }

    @Test
    public void TestFixOrderEntries() throws Exception {

    }

/*  H|KRAU1|SON||185584979|20130904|DOMC0143|USD|DOMC0143|SHP|20160104|COMPLETE BUILDI SERVICE|COMPLETE BUILDI SERVICE|6750 VETERANS PKWY.|||COLUMBUS|GA|31909||KRAU1|UPS|GND||7063209292|||
    D|GS000011|GV-104|EA|1|75|1|
    D|KGW1700-PU10SN-BLFR|KGW1700-PU10SN-BLFR|EA|1|127.5|2|
    */

}
