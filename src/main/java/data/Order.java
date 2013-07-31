package data;

/**
 * User:
 * Date: 7/31/13
 * Time: 12:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class Order {


    private String Column1;
    private String Column2;
    private String Column7;
    private String Column8;
    private String Column3;
    private String Column4;
    private String Column9;
    private String Column10;
    private String Column5;
    private String Column6;
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


    Order(String line) {
        String record[] = line.split("\\|");
        if (!line.isEmpty()) {
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
            this.Column15 = record[18];
            this.Column16 = record[19];
            this.Column17 = record[20];
            this.Column18 = record[21];
        }
    }

    /**
     * @return
     */
    public String validateOrder() {

        return null;
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
