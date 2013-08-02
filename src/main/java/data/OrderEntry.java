package data;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.mysql.jdbc.PreparedStatement;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.Properties;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * User:
 * Date: 7/31/13
 * Time: 5:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class OrderEntry {
    static Logger logger = Logger.getLogger(OrderEntry.class);
    protected String host, user, pwd;
    String column1;
    String column2;
    String column3;
    String column4;
    String column5;
    String column6;
    Properties props;
    private String SPLITTER = "|";
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet rs = null;

    public OrderEntry(String line) {

        if (!isNullOrEmpty(line)) {
            String record[] = Iterables.toArray(Splitter.on("|").trimResults().split(line), String.class);

            this.setColumn1(record[0]); // sage_id
            this.setColumn2(record[1]); // sku
            this.setColumn3(record[2]);
            this.setColumn4(record[3]);
            this.setColumn5(record[4]);
            this.setColumn6(record[5]);
        }
        dbConnect();
    }

    private void dbConnect() {
        try {

            props = new Properties();
            props.load(OrderEntry.class.getClassLoader().getResourceAsStream("dbConnection.properties"));
            this.host = props.getProperty("host", "dev-db");
            this.user = props.getProperty("user", "developer");
            this.pwd = props.getProperty("password", "developerICG2013");

            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://" + this.host + "/edi_errors?" + "user=" + this.user + "&password=" + this.pwd);

            logger.info("Successfully connected to " + this.host + " using user " + this.user);

        } catch (Exception e) {
            logger.error(e.getMessage() + "  " + e.getCause(), e);
            e.printStackTrace();
        }
    }

    /**
     * returns error message when order is not valid and null otherwise.
     *
     * @return
     */
    public String validateOrderEntry() {

        // all fields are mandatory
        String errors = null;
        if (isNullOrEmpty(getColumn1())
                ||
                isNullOrEmpty(getColumn1())
                ||
                isNullOrEmpty(getColumn2())
                ||
                isNullOrEmpty(getColumn3())
                ||
                isNullOrEmpty(getColumn4())
                ||
                isNullOrEmpty(getColumn5())
                ||
                isNullOrEmpty(getColumn6())
                )

            errors = "Missing mandatory fields";

        return errors;
    }

    public void fixBasedOnSKUorSageID() throws SQLException {

        dbConnect();
        statement = connect.createStatement();
        String tmp = null;

        // if either  of the columns are not empty
        if (!(isNullOrEmpty(getColumn2())) && isNullOrEmpty(getColumn3())) {
            // sage_id based lookup
            if (isNullOrEmpty(getColumn2())) {

                rs = statement.executeQuery("SELECT sku, sage_id FROM edi_errors.products WHERE sku like '" + getColumn3() + "' limit 2");
                rs = preparedStatement.executeQuery();

                while (rs.next()) {
                    tmp = rs.getString("sage_id");
                }
                if (!isNullOrEmpty(tmp))
                    this.setColumn2(tmp);
            }
            // sku based lookup
            else if (isNullOrEmpty(getColumn3())) {
                rs = statement.executeQuery("SELECT sku, sage_id FROM edi_errors.products WHERE sage_id like '" + getColumn2() + "' limit 2");
                rs = preparedStatement.executeQuery();

                if (!isNullOrEmpty(tmp))
                    this.setColumn3(tmp);
            }

        }
    }

    @Override
    public String toString() {
        return getColumn1() + getSplitter() +
                getColumn2() + getSplitter() +
                getColumn3() + getSplitter() +
                getColumn4() + getSplitter() +
                getColumn5() + getSplitter();
    }

    public String getSplitter() {
        return SPLITTER;
    }

    public void setSplitter(String splitter) {
        this.SPLITTER = splitter;
    }

    public String getColumn1() {
        return column1;
    }

    public void setColumn1(String column1) {
        this.column1 = column1;
    }

    public String getColumn2() {
        return column2;
    }

    public void setColumn2(String column2) {
        this.column2 = column2;
    }

    public String getColumn3() {
        return column3;
    }

    public void setColumn3(String column3) {
        this.column3 = column3;
    }

    public String getColumn4() {
        return column4;
    }

    public void setColumn4(String column4) {
        this.column4 = column4;
    }

    public String getColumn5() {
        return column5;
    }

    public void setColumn5(String column5) {
        this.column5 = column5;
    }

    public String getColumn6() {
        return column6;
    }

    public void setColumn6(String column6) {
        this.column6 = column6;
    }
}
