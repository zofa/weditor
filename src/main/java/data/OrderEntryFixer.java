package data;

/**
 * User: z1
 * Date: 9/3/13
 * Time: 12:02 PM
 * Strategy pattern for fixing order entries due to many fixing algorithms in the future to fix an errors.
 */
public interface OrderEntryFixer {
    /**
     * The method to be called for fixing an order entry.
     *
     * @return an fixed @OrderEntry on success and with no changes when fix failed.
     */
    OrderEntry fixOrderEntry(OrderEntry entry);
}
