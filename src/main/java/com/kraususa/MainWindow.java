package com.kraususa;


import com.vaadin.event.FieldEvents;
import com.vaadin.ui.*;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * User:
 * Date: 8/14/13
 * Time: 5:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class MainWindow extends Window {

    private static Logger logger = Logger.getLogger(MainWindow.class);
    private final String fileFilter = "*.err";
    protected String inFileDir = "/errfiles/infiles/", outFileDir = "/errfiles/outfiles/";
    VerticalLayout verticalLayout = new VerticalLayout();
    List<String> files = new ArrayList<String>(10);
    FieldEvents.TextChangeListener inputEventListener = new

            FieldEvents.TextChangeListener() {
                @Override
                public void textChange(FieldEvents.TextChangeEvent event) {
                    saveAndMoveButton.setEnabled(true);
                    saveButton.setEnabled(true);
                }
            };
    private Table table;
    private TextArea fileEditor;
    private Button saveAndMoveButton, saveButton, fixButton;
    private String selectedFile = null;
    private Button moveButton;
    // -------------------------------------------------------------------------------------------------------------
}
