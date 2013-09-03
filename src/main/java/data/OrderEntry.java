package data;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import org.apache.log4j.Logger;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * User:
 * Date: 7/31/13
 * Time: 5:27 PM
 */
public class OrderEntry {


    private static Logger logger = Logger.getLogger(OrderEntry.class);
    OrderEntryFixer theFixer;
    String metaColumn;
    String sageId;
    String sku;
    String column4;
    String qty;
    String price;
    private String splitter = "|";

    public OrderEntry(String line) {
        if (!isNullOrEmpty(line)) {
            String record[] = Iterables.toArray(Splitter.on(splitter).trimResults().split(line), String.class);
            this.setMetaColumn(record[0]);
            this.setSageId(record[1]);
            this.setSku(record[2]);
            this.setColumn4(record[3]);
            this.setQty(record[4]);
            this.setPrice(record[5]);
        } else {
            throw new UnsupportedOperationException("Cannot construct order entry with empty data.");
        }
    }

    /**
     * Returns error message when order is not valid and null otherwise.
     *
     * @return
     */
    public String validateOrderEntry() {
        String errors = "";
        if (isNullOrEmpty(getSageId())) {
            errors = "Missing sage_id";
        } else if (isNullOrEmpty(getSku())) {
            errors = "Missing SKU";
        } else if (getSageId().equals(getSku())) {
            if (errors != null) {
                errors = errors + (" Duplicate SKU and sage_id");
            } else {
                errors = "Duplicate SKU and sage_id";
            }
        } else if (
                isNullOrEmpty(getSku())
                        ||
                        isNullOrEmpty(getColumn4())
                        ||
                        isNullOrEmpty(getQty())
                        ||
                        isNullOrEmpty(getPrice())
                ) {
            errors = "Missing mandatory fields";
        }
        return errors;
    }

    /**
     * Returns one line representation of order entry.
     *
     * @return text representation of order entry.
     */
    @Override
    public String toString() {
        String splitter = getSplitter();
        return getMetaColumn() + splitter +
                getSageId() + splitter +
                getSku() + splitter +
                getColumn4() + splitter +
                getQty() + splitter +
                getPrice() + splitter;
    }

    public String getSplitter() {
        return splitter;
    }

    public void setSplitter(String splitter) {
        this.splitter = splitter;
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

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public OrderEntryFixer getTheFixer() {
        return theFixer;
    }

    public void setTheFixer(OrderEntryFixer theFixer) {
        this.theFixer = theFixer;
    }

    /**
     * Fixes the entry based on assigned algorithm. Part of strategy pattern.
     *
     * @return returns fixed @OrderEntry
     */
    public OrderEntry fixEntry() {
        logger.info("Launching order entry fixer with following algorithm - " + theFixer.getClass().toString());
        return theFixer.fixOrderEntry(this);
    }
}