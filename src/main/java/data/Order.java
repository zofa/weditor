package data;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * User:
 * Date: 7/31/13
 * Time: 12:17 PM
 */
public class Order {

    private static final String SPLITTER = "|";
    private String Column1;
    private String Column2;
    private String Column3; // record type
    private String Column4;
    private String Column5;
    private String Column6;
    private String Column7;
    private String Column8;
    private String Column9;
    private String Column10;
    private String Column11;
    private String Column12;
    private String Column13;
    private String Column14;
    private String Column15;
    private String Column16;
    private String Column17;
    private String Column18;
    private String Column19;
    private String Column20;
    private String Column21;
    private String Column22;
    private RecordTypes RecordType;
    private List<OrderEntry> orderEntries = new ArrayList<OrderEntry>(3);

    public Order(String line) {

        if (!isNullOrEmpty(line)) {
            String record[] = Iterables.toArray(Splitter.on("|").trimResults().split(line), String.class);
            this.Column1 = record[0];
            this.Column2 = record[1];
            this.Column3 = record[2];
            this.Column4 = record[3];
            this.Column5 = record[4];
            this.Column6 = record[5];
            this.Column7 = record[6];
            this.Column8 = record[7];
            this.Column9 = record[8];
            this.Column10 = record[9];
            this.Column11 = record[10];
            this.Column12 = record[11];
            this.Column13 = record[12];
            this.Column14 = record[13];
            this.Column15 = record[14];
            this.Column16 = record[15];
            this.Column17 = record[16];
            this.Column18 = record[17];
            this.Column19 = record[18];
            this.Column20 = record[19];
            this.Column21 = record[20];
            this.Column22 = record[21];
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

    private void addEntry(String line) {

        OrderEntry entry = new OrderEntry(line);
        orderEntries.add(entry);
    }

    /**
     * Validates order entry and returns error message when mandatory fields are missing.
     *
     * @return
     */
    public String validateOrder() {

        if (isNullOrEmpty(getColumn1()) ||
                isNullOrEmpty(getColumn2()) ||
                isNullOrEmpty(getColumn3()) ||
                isNullOrEmpty(getColumn4()) ||
                isNullOrEmpty(getColumn5()) ||
                isNullOrEmpty(getColumn6()) ||
                isNullOrEmpty(getColumn7()) ||
                isNullOrEmpty(getColumn8()) ||
                isNullOrEmpty(getColumn9()) ||
                isNullOrEmpty(getColumn10()) ||
                isNullOrEmpty(getColumn11()) ||
                isNullOrEmpty(getColumn12()) ||
                isNullOrEmpty(getColumn13()) ||
                isNullOrEmpty(getColumn14()) ||
                isNullOrEmpty(getColumn15()) ||
                isNullOrEmpty(getColumn16()) ||
                isNullOrEmpty(getColumn17()) ||
                isNullOrEmpty(getColumn18()) ||
                isNullOrEmpty(getColumn19())
                ) {
            return "Order missing mandatory fields.";
        }
        return null;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append(getColumn1() + SPLITTER +
                getColumn1() + SPLITTER +
                getColumn2() + SPLITTER +
                getColumn3() + SPLITTER +
                getColumn4() + SPLITTER +
                getColumn5() + SPLITTER +
                getColumn6() + SPLITTER +
                getColumn7() + SPLITTER +
                getColumn8() + SPLITTER +
                getColumn9() + SPLITTER +
                getColumn10() + SPLITTER +
                getColumn11() + SPLITTER +
                getColumn12() + SPLITTER +
                getColumn13() + SPLITTER +
                getColumn14() + SPLITTER +
                getColumn15() + SPLITTER +
                getColumn16() + SPLITTER +
                getColumn17() + SPLITTER +
                getColumn18() + SPLITTER +
                getColumn19() + SPLITTER +
                getColumn20() + SPLITTER +
                getColumn21() + SPLITTER +
                getColumn22() + SPLITTER);

        for (OrderEntry details : orderEntries)
            sb.append(details.toString());

        return sb.toString();
    }

    /**
     * @return
     */
    public String validateOrderEntries() {

        return null;
    }

    /**
     * @return
     */
    public String fixOrderEntries() {

        return null;
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

    public String getColumn7() {
        return Column7;
    }

    public void setColumn7(String column7) {
        Column7 = column7;
    }

    public String getColumn8() {
        return Column8;
    }

    public void setColumn8(String column8) {
        Column8 = column8;
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

    public String getColumn9() {
        return Column9;
    }

    public void setColumn9(String column9) {
        Column9 = column9;
    }

    public String getColumn10() {
        return Column10;
    }

    public void setColumn10(String column10) {
        Column10 = column10;
    }

    public String getColumn5() {
        return Column5;
    }

    public void setColumn5(String column5) {
        Column5 = column5;
    }

    public String getColumn6() {
        return Column6;
    }

    public void setColumn6(String column6) {
        Column6 = column6;
    }

    public String getColumn11() {
        return Column11;
    }

    public void setColumn11(String column11) {
        Column11 = column11;
    }

    public String getColumn12() {
        return Column12;
    }

    public void setColumn12(String column12) {
        Column12 = column12;
    }

    public String getColumn13() {
        return Column13;
    }

    public void setColumn13(String column13) {
        Column13 = column13;
    }

    public String getColumn14() {
        return Column14;
    }

    public void setColumn14(String column14) {
        Column14 = column14;
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

    public String getColumn17() {
        return Column17;
    }

    public void setColumn17(String column17) {
        Column17 = column17;
    }

    public String getColumn18() {
        return Column18;
    }

    public void setColumn18(String column18) {
        Column18 = column18;
    }

    public String getColumn19() {
        return Column19;
    }

    public void setColumn19(String column19) {
        Column19 = column19;
    }

    public String getColumn20() {
        return Column20;
    }

    public void setColumn20(String column20) {
        Column20 = column20;
    }

    public String getColumn21() {
        return Column21;
    }

    public void setColumn21(String column21) {
        Column21 = column21;
    }

    public String getColumn22() {
        return Column22;
    }

    public void setColumn22(String column22) {
        Column22 = column22;
    }

}
