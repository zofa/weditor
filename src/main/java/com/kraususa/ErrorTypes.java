package com.kraususa;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: z1
 * Date: 7/31/13
 * Time: 4:55 PM
 * To change this template use File | Settings | File Templates.
 */
public enum ErrorTypes {

    //--------------------------------------------------------
    MISSING_SKU("Order entry missing SKU info"),
    MISSING_SAGE_ID("Order entry missing SAGE_ID info");

    //--------------------------------------------------------
    private static final Map<String, ErrorTypes> lookup = new HashMap<String, ErrorTypes>();

    // Reverse-lookup map for getting a day from an abbreviation
    static {
        for (ErrorTypes erd : ErrorTypes.values())
            lookup.put(erd.getAbbreviation(), erd);
    }

    private String abbreviation = null;

    private ErrorTypes(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public static ErrorTypes get(String abbreviation) {
        return lookup.get(abbreviation);
    }

    public String getAbbreviation() {
        return abbreviation;
    }
}
