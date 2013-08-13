package data;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
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
    String metaColumn;
    String sageId;
    String sku;
    String column4;
    String column5;
    String column6;
    Properties props;
    private String SPLITTER = "|";
    private Connection connect = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet rs = null;

    public OrderEntry(String line) {

        if (!isNullOrEmpty(line)) {
            String record[] = Iterables.toArray(Splitter.on("|").trimResults().split(line), String.class);

            this.setMetaColumn(record[0]); // sage_id
            this.setSageId(record[1]); // sku
            this.setSku(record[2]);
            this.setColumn4(record[3]);
            this.setColumn5(record[4]);
            this.setColumn6(record[5]);

            //dbConnect();
        } else {
            throw new UnsupportedOperationException("Cannot construct order entry with empty data.");
        }
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
     * Returns error message when order is not valid and null otherwise.
     *
     * @return
     */
    public String validateOrderEntry() {

        String errors = null;
        if (isNullOrEmpty(getSageId())) {
            errors = "Missing sage_id";
        } else if (isNullOrEmpty(getSku())) {
            errors = "Missing SKU";
        } else if (
                isNullOrEmpty(getSku())
                        ||
                        isNullOrEmpty(getColumn4())
                        ||
                        isNullOrEmpty(getColumn5())
                        ||
                        isNullOrEmpty(getColumn6())
                )

        {
            errors = "Missing mandatory fields";
        }
        return errors;
    }

    public void fixBasedOnSKUorSageID() throws SQLException {
        preparedStatement = null;
        String tmp = null;
        // if either of the columns are empty
        if (isNullOrEmpty(getSageId()) || isNullOrEmpty(getSku())) {
            dbConnect();
            // sage_id is empty looking based on sku
            if (isNullOrEmpty(getSageId())) {
                preparedStatement = connect.prepareStatement("SELECT sage_id FROM edi_errors.products WHERE sku like '" + getSku() + "' limit 2");
                logger.debug("sage_id based lookup");
                logger.debug(preparedStatement.toString());
                rs = preparedStatement.executeQuery();

                while (rs.next()) {
                    tmp = rs.getString("sage_id");
                }
                if (!isNullOrEmpty(tmp))
                    this.setSageId(tmp);
            }
            // sku is empty looking up based on sage_id
            else if (isNullOrEmpty(getSku())) {
                preparedStatement = connect.prepareStatement("SELECT  sku FROM edi_errors.products WHERE sage_id like '" + getSageId() + "' limit 2");
                logger.debug("sku based lookup");
                logger.debug(preparedStatement.toString());
                rs = preparedStatement.executeQuery();

                while (rs.next())
                    tmp = rs.getString("sku");

                if (!isNullOrEmpty(tmp))
                    this.setSku(tmp);
            }

        }

        // if the fields are dups
        if (getSku() == getSageId()) {
            // first remove braces if there are any
            if (getSku().contains("(") && getSku().contains(")")) {
                setSku(getSku().replaceAll("\\b(\\(.+\\))\\b", ""));
            }

            preparedStatement = connect.prepareStatement("SELECT  sku FROM edi_errors.products WHERE sage_id like '" + getSageId() + "' limit 2");
            logger.debug("sku based lookup");
            logger.debug(preparedStatement.toString());
            rs = preparedStatement.executeQuery();

            while (rs.next())
                tmp = rs.getString("sku");

            if (!isNullOrEmpty(tmp))
                this.setSku(tmp);


        }

    }

    @Override
    public String toString() {
        return getMetaColumn() + getSplitter() +
                getSageId() + getSplitter() +
                getSku() + getSplitter() +
                getColumn4() + getSplitter() +
                getColumn5() + getSplitter() + "\n";
    }

    public String getSplitter() {
        return SPLITTER;
    }

    public void setSplitter(String splitter) {
        this.SPLITTER = splitter;
    }

    public String getMetaColumn() {
        return metaColumn;
    }

    public void setMetaColumn(String metaColumn) {
        this.metaColumn = metaColumn;
    }

    public String getSageId() {
        return sageId;
    }

    public void setSageId(String sageId) {
        this.sageId = sageId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
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