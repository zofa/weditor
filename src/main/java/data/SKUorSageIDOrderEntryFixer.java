package data;

import org.apache.log4j.Logger;

import java.sql.*;
import java.util.Properties;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * The class is used to fix an order entry based on the lookup in the db.
 * Utilizes strategy pattern - one of the order entry fixing algorithm.
 * <p/>
 * User: z1
 * Date: 9/3/13
 * Time: 12:09 PM
 */
public class SKUorSageIDOrderEntryFixer implements OrderEntryFixer {

    private static final Logger logger = Logger.getLogger(OrderEntryFixer.class);
    private Properties props;
    private Connection connect = null;

    @Override
    public OrderEntry fixOrderEntry(OrderEntry entry) {

        OrderEntry theEntry = entry;

        PreparedStatement preparedStatement;
        String tmp;

        logger.info("About to fix an order entry with - " + this.getClass().getName());
        logger.debug("Initial entry follows");
        logger.debug(entry.toString());
        // first remove braces if there are any
        if (theEntry.getSku().contains("(") && theEntry.getSku().contains(")")) {
            theEntry.setSku(theEntry.getSku().replaceAll("\\b(\\(.+\\))\\b", ""));
        }

        if (theEntry.getSageId().contains("(") && theEntry.getSageId().contains(")")) {
            theEntry.setSageId(theEntry.getSageId().replaceAll("\\b(\\(.+\\))\\b", ""));
        }

        try {
            // if either of the columns are empty
            // will try to lookup based each other
            if (isNullOrEmpty(theEntry.getSageId()) || isNullOrEmpty(theEntry.getSku())) {
                dbConnect();
                /* sage_id is empty looking based on sku*/
                ResultSet rs;
                if (isNullOrEmpty(theEntry.getSageId())) {
                    preparedStatement = connect.prepareStatement("SELECT sage_id FROM edi_errors.products  WHERE sku like '" +
                            theEntry.getSku() + "' limit 2");
                    logger.info("sage_id based lookup");
                    logger.debug(preparedStatement.toString());
                    rs = preparedStatement.executeQuery();

                    tmp = null;
                    while (rs.next()) {
                        tmp = rs.getString("sage_id");
                    }
                    if (!isNullOrEmpty(tmp)) {
                        theEntry.setSageId(tmp);
                    }
                }
                // sku is empty looking up based on sage_id
                else if (isNullOrEmpty(theEntry.getSku())) {
                    preparedStatement = connect.prepareStatement("SELECT  sku FROM edi_errors.products WHERE sage_id like '" +
                            theEntry.getSageId() + "' limit 2");
                    logger.info("sku based lookup");
                    logger.debug(preparedStatement.toString());

                    rs = preparedStatement.executeQuery();

                    tmp = null;
                    while (rs.next()) {
                        tmp = rs.getString("sku");
                    }

                    if (!isNullOrEmpty(tmp)) {
                        theEntry.setSku(tmp);
                    }
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        return theEntry;
    }

    private void dbConnect() {
        try {
            props = new Properties();
            props.load(OrderEntry.class.getClassLoader().getResourceAsStream("dbConnection.properties"));
            String host = props.getProperty("host", "dev-db");
            String user = props.getProperty("user", "developer");
            String pwd = props.getProperty("password", "developerICG2013");

            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://" + host + "/edi_errors?" + "user=" + user + "&password=" + pwd);
            /*  we will only read from db.*/
            connect.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            logger.info("Successfully connected to " + host + " using user " + user);

        } catch (Exception e) {
            logger.error(e.getMessage() + "  " + e.getCause(), e);
            logger.error(e);
        }
    }
}
