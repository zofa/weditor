package data;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * User:
 * Date: 7/31/13
 * Time: 12:17 PM
 */
public class Order {

    private static final String splitter = "|";
    static Logger logger = Logger.getLogger(OrderEntry.class);
    private String Column1;
    private String Column2;
    private String Column3; // record type
    private String Column4;
    private String PONum;
    private String OrderDate;
    private String DealerCol7;
    private String Currency;
    private String DealerCol9;
    private String Column10;
    private String Column11;
    private String CustomerName1;
    private String CustomerName2;
    private String AddressLine1;
    private String Column15;
    private String Column16;
    private String City;
    private String State;
    private String ZipCode;
    private String CountryCode;
    private String Column21;
    private String ShippingCarrier;
    private RecordTypes RecordType;
    private List<OrderEntry> orderEntries = new ArrayList<OrderEntry>(3);

    public Order(String line) {

        if (!isNullOrEmpty(line)) {
            String record[] = Iterables.toArray(Splitter.on("|").trimResults().split(line), String.class);
            this.Column1 = record[0];
            this.Column2 = record[1];
            this.Column3 = record[2];
            this.Column4 = record[3];
            this.PONum = record[4];
            this.OrderDate = record[5];
            this.DealerCol7 = record[6];
            this.Currency = record[7];
            this.DealerCol9 = record[8];
            this.Column10 = record[9];
            this.Column11 = record[10];
            this.CustomerName1 = record[11];
            this.CustomerName2 = record[12];
            this.AddressLine1 = record[13];
            this.Column15 = record[14];
            this.Column16 = record[15];
            this.City = record[16];
            this.State = record[17];
            this.ZipCode = record[18];
            this.CountryCode = record[19];
            this.Column21 = record[20];
            this.ShippingCarrier = record[21];
        } else {
            throw new UnsupportedOperationException("Cannot construct record with empty string.");
        }
    }

    public Order() {
        throw new UnsupportedOperationException();
    }

    public RecordTypes getRecordType() {
        return RecordType;
    }

    public void setRecordType(RecordTypes recordType) {
        RecordType = recordType;
    }

    public List<OrderEntry> getOrderEntries() {
        return orderEntries;
    }

    public void setOrderEntries(List<OrderEntry> orderEntries) {
        this.orderEntries = orderEntries;
    }

    public void addEntry(String line) {
        OrderEntry entry = new OrderEntry(line);
        orderEntries.add(entry);
    }

    public void addEntry(OrderEntry entry) {
        orderEntries.add(entry);
    }

    /**
     * Validates order entry and returns error message when
     * mandatory fields are missing, null otherwise.
     *
     * @return
     */
    public String validateOrder() {

        StringBuilder sb = new StringBuilder(10).append("Missing ");

        if (isNullOrEmpty(getColumn1()) ||
                isNullOrEmpty(getColumn2()) ||
                isNullOrEmpty(getColumn3())
                ) {
            sb.append("first 4th columns, ");
        } else if (isNullOrEmpty(getPONum())) {
            sb.append("PO#, ");
        } else if (isNullOrEmpty(getOrderDate())) {
            sb.append(" Order Date, ");
        } else if (isNullOrEmpty(getDealerCol7())) {
            sb.append(" DealerCol7, ");
        } else if (isNullOrEmpty(getCurrency())) {
            sb.append(" Currency, ");
        } else if (isNullOrEmpty(getDealerCol9())) {
            sb.append(" DealerCol9, ");
        } else if (isNullOrEmpty(getColumn10()) ||
                isNullOrEmpty(getColumn11())) {
            sb.append(" Columns 10 and 11, ");
        } else if (isNullOrEmpty(getCustomerName1())) {
            sb.append(" Customer Name1 ");
        }
        if (isNullOrEmpty(getCustomerName2())) {
            sb.append(" Customer Name1 ");
//        } else if (isNullOrEmpty(getAddressLine1()) ||
//                isNullOrEmpty(getColumn15()) ||
//                isNullOrEmpty(getColumn16())) {
//            sb.append(" Columns 15, 16");
        } else if (
                isNullOrEmpty(getCity())) {
            sb.append(" City");
        } else if (
                isNullOrEmpty(getState())) {
            sb.append(" State ");
        } else if (isNullOrEmpty(getZipCode())) {
            sb.append("ZipCode");
        }

        if ("Missing ".equals(sb.toString())) {
            return null;
        } else {
            return sb.toString();
        }
    }


    /**
     * Returns original string representation of the record including order entries.
     *
     * @return
     */
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append(
                getColumn1() + splitter +
                        getColumn2() + splitter +
                        getColumn3() + splitter +
                        getColumn4() + splitter +
                        getPONum() + splitter +
                        getOrderDate() + splitter +
                        getDealerCol7() + splitter +
                        getCurrency() + splitter +
                        getDealerCol9() + splitter +
                        getColumn10() + splitter +
                        getColumn11() + splitter +
                        getCustomerName1() + splitter +
                        getCustomerName2() + splitter +
                        getAddressLine1() + splitter +
                        getColumn15() + splitter +
                        getColumn16() + splitter +
                        getCity() + splitter +
                        getState() + splitter +
                        getZipCode() + splitter +
                        getCountryCode() + splitter +
                        getColumn21() + splitter +
                        getShippingCarrier() + splitter + "\n");

        for (OrderEntry details : orderEntries) {
            sb.append(details.toString());
            /* windows windows windows EOL :0*/
            sb.append("\r\n");
        }
        return sb.toString();
    }

    /**
     * looks up for the missing fields.
     *
     * @return
     */
    public String fixOrderEntries() {
        logger.info("Trying to fix entries in the order.");
        String status = null;
        if (orderEntries.size() != 0) {
            try {
                for (OrderEntry entry : orderEntries) {
                    /* this will fix based on sku and SageID if either is empty by looking up  values in db.
                    * Any other fixer could be called here in chain.
                    * */
                    entry.setTheFixer(new SKUorSageIDOrderEntryFixer());
                    entry.fixEntry();
                }
            } catch (Exception e) {
                logger.error(e);
                status = e.getCause().toString();
                logger.error("Cannot fix entry, due to: " + e.getCause());

            }
        }
        return status;
    }

    public String getColumn1() {
        return Column1;
    }

    public void setColumn1(String column1) {
        Column1 = column1;
    }

    public String getColumn2() {
        return Column2;
    }

    public void setColumn2(String column2) {
        Column2 = column2;
    }

    public String getDealerCol7() {
        return DealerCol7;
    }

    public void setDealerCol7(String dealerCol7) {
        DealerCol7 = dealerCol7;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }

    public String getColumn3() {
        return Column3;
    }

    public void setColumn3(String column3) {
        Column3 = column3;
    }

    public String getColumn4() {
        return Column4;
    }

    public void setColumn4(String column4) {
        Column4 = column4;
    }

    public String getDealerCol9() {
        return DealerCol9;
    }

    public void setDealerCol9(String dealerCol9) {
        DealerCol9 = dealerCol9;
    }

    public String getColumn10() {
        return Column10;
    }

    public void setColumn10(String column10) {
        Column10 = column10;
    }

    public String getPONum() {
        return PONum;
    }

    public void setPonum(String Ponum) {
        this.PONum = Ponum;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public String getColumn11() {
        return Column11;
    }

    public void setColumn11(String column11) {
        Column11 = column11;
    }

    public String getCustomerName1() {
        return CustomerName1;
    }

    public void setCustomerName1(String customerName1) {
        CustomerName1 = customerName1;
    }

    public String getCustomerName2() {
        return CustomerName2;
    }

    public void setCustomerName2(String customerName2) {
        CustomerName2 = customerName2;
    }

    public String getAddressLine1() {
        return AddressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        AddressLine1 = addressLine1;
    }

    public String getColumn15() {
        return Column15;
    }

    public void setColumn15(String column15) {
        Column15 = column15;
    }

    public String getColumn16() {
        return Column16;
    }

    public void setColumn16(String column16) {
        Column16 = column16;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getZipCode() {
        return ZipCode;
    }

    public void setZipCode(String zipCode) {
        ZipCode = zipCode;
    }

    public String getCountryCode() {
        return CountryCode;
    }

    public void setCountryCode(String countryCode) {
        CountryCode = countryCode;
    }

    public String getColumn21() {
        return Column21;
    }

    public void setColumn21(String column21) {
        Column21 = column21;
    }

    public String getShippingCarrier() {
        return ShippingCarrier;
    }

    public void setShippingCarrier(String shippingCarrier) {
        ShippingCarrier = shippingCarrier;
    }

    public String getOrderEntry(int index) {
        return orderEntries.get(index).toString();
    }

    public int getOrderEntryCount() {
        return orderEntries.size();
    }

}