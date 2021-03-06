/**
 *
 */
package com.kraususa;


import com.google.common.io.Files;
import com.kraususa.widgetset.BottomPanel;
import com.kraususa.widgetset.TopPanel;
import com.kraususa.widgetset.processingQueueWidget;
import com.vaadin.Application;
import com.vaadin.data.Property;
import com.vaadin.event.FieldEvents;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;
import data.Order;
import data.OrderEntry;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;


/**
 * Main application class.
 */
public class WeditorApplication extends Application {

    /**
     *
     */
    private static final long serialVersionUID = 3601221463880485916L;
    private static Logger logger = Logger.getLogger(WeditorApplication.class);
    private final String fileFilter = "*.err";
    protected String inFileDir = "/errfiles/infiles/", outFileDir = "/errfiles/outfiles/";
    private Table table;
    private TextArea fileEditor;
    private Button saveAndMoveButton, saveButton, fixButton;
    private String selectedFile = null;
    private Button moveButton;

    @Override
    public void init() {

        setTheme("weditortheme");
        final Window mainWindow = new Window("Kraus USA EDI Errors");

        mainWindow.addListener(new Window.CloseListener() {
            @Override
            public void windowClose(Window.CloseEvent e) {
                logger.info("User refreshes the page..reloading components.");
                getMainWindow().getApplication().close();
            }
        });

        VerticalLayout verticalLayout = new VerticalLayout();
        logger.info("Initializing the application.");
        // -------------------------------------------------------------------------------------------------------------
        table = new Table("List of error files");
        table.addContainerProperty("File name", String.class, null);
        table.addContainerProperty("Dealer list", String.class, null);
        table.addContainerProperty("Errors", String.class, null);
        table.setWidth("100%");
        table.setColumnFooter(table.firstItemId(), "2");
        table.setFooterVisible(true);
        table.setSelectable(true);
        table.setSizeFull();
        table.setColumnReorderingAllowed(true);
        table.setImmediate(true);
        table.setColumnCollapsingAllowed(true);

        List<String> files = new ArrayList<String>(10);

        files.addAll(this.listFiles(inFileDir));
        if (!files.isEmpty()) {
            for (int i = 0; i < files.size(); i++) {
                String theDealers = this.getDealersList(inFileDir + files.get(i)).toString();
                table.addItem(new Object[]
                        {
                                files.get(i),
                                // this.getDealersList(inFileDir + files.get(i)).size(),
                                //  "",
                                theDealers,
                                // "Fix & sumbit"
                                validateFile(inFileDir + files.get(i))
                        }, new Integer(i));
            }
        }

        //mainWindow.showNotification("Error", "Cannot obtain file list", Window.Notification.TYPE_TRAY_NOTIFICATION);


        table.setColumnFooter("File name", "Total files");
        table.setColumnFooter("Dealer list", String.valueOf(files.size()));

        table.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                if (table.getValue() != null) {
                    Object rowId = event.getProperty().getValue();
                    BufferedReader br;

                    try {
                        selectedFile = inFileDir + table.getContainerProperty(rowId, "File name").getValue();
                        br = new BufferedReader(new FileReader(selectedFile));
                        StringBuilder sb = new StringBuilder();
                        String line = br.readLine();

                        while (line != null) {
                            sb.append(line);
                            sb.append("\n");
                            line = br.readLine();
                        }
                        fileEditor.setValue(sb.toString());
                    } catch (Exception e) {
                        logger.error(e);
                    }
                    saveButton.setEnabled(false);
                    saveAndMoveButton.setEnabled(false);

                    if (isNullOrEmpty(table.getContainerProperty(rowId, "Errors").getValue().toString())) {
                        moveButton.setEnabled(true);
                        fixButton.setEnabled(false);
                    } else if (table.getContainerProperty(rowId, "Errors").getValue().toString().toLowerCase().contains("mandatory fields")) {
                        moveButton.setEnabled(false);
                        fixButton.setEnabled(false);
                    } else if (table.getContainerProperty(rowId, "Errors").getValue().toString().toLowerCase().contains("missing")) {
                        moveButton.setEnabled(false);
                        fixButton.setEnabled(true);
                    }

                } else {
                    fixButton.setEnabled(false);
                }
            }
        });
        // -------------------------------------------------------------------------------------------------------------
        fileEditor = new com.vaadin.ui.TextArea("FIle content");
        fileEditor.setWidth("70%");
        fileEditor.setRows(20);
        fileEditor.setHeight("100%");
        fileEditor.setWordwrap(false);
        fileEditor.setColumns(40);
        fileEditor.setSizeFull();
        fileEditor.setImmediate(true);
        fileEditor.setWordwrap(false);
        FieldEvents.TextChangeListener inputEventListener = new

                FieldEvents.TextChangeListener() {
                    @Override
                    public void textChange(FieldEvents.TextChangeEvent event) {
                        saveAndMoveButton.setEnabled(true);
                        saveButton.setEnabled(true);
                    }
                };
        fileEditor.addListener(inputEventListener);

        // -------------------------------------------------------------------------------------------------------------
        HorizontalSplitPanel split = new HorizontalSplitPanel();
        split.setSplitPosition(40, Sizeable.UNITS_PERCENTAGE);
        split.addComponent(table);
        VerticalLayout v = new VerticalLayout();
        // for action buttons
        HorizontalLayout h = new HorizontalLayout();

        saveAndMoveButton = new Button("Save changes and move file");
        saveAndMoveButton.setEnabled(false);
        saveButton = new Button("Save changes to file");
        saveButton.setEnabled(false);

        saveButton.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                getMainWindow().getApplication().close();
                logger.info("Saving file " + selectedFile);
                getMainWindow().showNotification(doSave(false));
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    logger.error(e);
                }
                getMainWindow().showNotification("Changes saved to file", Window.Notification.TYPE_TRAY_NOTIFICATION);
                logger.info("Changes saved to file.");
            }
        });

        fixButton = new Button("Fix selected");
        fixButton.setEnabled(false);
        fixButton.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                logger.info("Trying to fix file " + selectedFile);
                fixEntry();
            }
        });

        moveButton = new Button("Move file");
        moveButton.setEnabled(false);
        moveButton.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (table.getValue() != null) {
                    moveFile(selectedFile);
                }
                getMainWindow().getApplication().close();
            }
        }
        );

        h.addComponent(saveButton);
        h.addComponent(saveAndMoveButton);
        h.addComponent(fixButton);
        h.addComponent(moveButton);
        h.setSizeFull();
        v.addComponent(h);
        v.setExpandRatio(h, 2);

        //v.setComponentAlignment(saveAndMoveButton, Alignment.BOTTOM_RIGHT);

        saveAndMoveButton.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                logger.info("Saving and preparing for move..");
                getMainWindow().showNotification(doSave(true), Window.Notification.POSITION_TOP_RIGHT);
                logger.info("Saved. Validating..");
                if (validateFile(selectedFile) == null) {
                    logger.info("Validation ok. Moving file..");
                    moveFile(selectedFile);
                    getMainWindow().getApplication().close();
                }
                logger.info("File moved - reloading application UI");
            }
        });
        v.addComponent(fileEditor);

        split.addComponent(v);
        split.setCaption("Error files");


        TopPanel t = new TopPanel();

        verticalLayout.setSpacing(false);
        verticalLayout.addComponent(t);
        verticalLayout.addComponent(split);

        // -------------------------------------------------------------
        // Processing information
        processingQueueWidget psw = new processingQueueWidget();
        psw.setSizeFull();
        verticalLayout.addComponent(psw);
//

        // and the bottom
        BottomPanel bottom = new BottomPanel();
        verticalLayout.addComponent(bottom);

        verticalLayout.setMargin(true);
        verticalLayout.setWidth("100%");
        verticalLayout.setHeight("100%");
        verticalLayout.setExpandRatio(t, 0.8f);
        verticalLayout.setExpandRatio(split, 4);
        verticalLayout.setExpandRatio(psw, 4);
        verticalLayout.setExpandRatio(bottom, 0);

        // TabSheet tabsheet = new TabSheet();

        mainWindow.setContent(verticalLayout);


        setMainWindow(mainWindow);

        //  setMainWindow(new LoginWindow());

    }

    /**
     * returns list of files in the passed @path.
     *
     * @param path
     * @return list of files in specified directory.
     */
    protected List<String> listFiles(String path) {

        logger.info("Listing directory " + path);
        File root = new File(path);
        FileFilter ff = new WildcardFileFilter(fileFilter);
        File[] list = root.listFiles(ff);

        List<String> fileList = new ArrayList<String>(3);

        for (File f : list) {
            fileList.add(f.getName());
        }
        logger.info("Following files has been found:");
        logger.info(fileList.toString());
        return fileList;
    }

    /**
     * Returns unique list of dealers from the @file.
     *
     * @param file
     * @return
     */
    protected Set<String> getDealersList(String file) {

        Set<String> dealers = new HashSet<String>();

        BufferedReader br = null;
        String line;
        String splitter = "\\|";

        try {
            br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null && line.startsWith("H")) {
                String[] tmp = line.split(splitter);
                try {
                    if (!isNullOrEmpty(tmp[6]))
                        dealers.add(tmp[6]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    logger.error(e);
                }
            }

        } catch (FileNotFoundException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    logger.error(e);
                }
            }
        }
        return dealers;
    }

    /**
     * Moves @filename the file from input to output directory across the file systems.
     *
     * @param fileName should be exactly file name only.
     */
    protected void moveFile(String fileName) {
        logger.info("About to move file " + fileName + " to output directory.");
        File theFile = new File(fileName);
        checkNotNull(fileName, "Move source file name must NOT be null.");

        String TargetFileName = theFile.getName().substring(0, theFile.getName().length() - 4);

        if (!theFile.renameTo(new File(outFileDir + TargetFileName))) {
            File targetFile = new File(outFileDir + TargetFileName);
            try {
                Files.move(theFile, targetFile);
            } catch (IOException e) {
                logger.error(e);
                getMainWindow().showNotification("Failed to move file. See log for details. Either file exists or permission denied.", Window.Notification.TYPE_ERROR_MESSAGE);
            }
            getMainWindow().getApplication().close();
            logger.error("Unable to move file " + fileName + " to " + outFileDir + theFile.getName());
        } else {
            logger.info("File " + fileName + " moved to output directory.");
            getMainWindow().getApplication().close();
        }
    }

    /**
     * Saves changes made in the editor to the file.
     *
     * @param move
     * @return null when everything is okay and error msg when something went wrong.
     */
    public String doSave(boolean move) {
        String status;
        if (!isNullOrEmpty(selectedFile) && !fileEditor.getValue().toString().isEmpty()) {
            try {
                logger.info("Save triggered. Saving file " + selectedFile + (move ? " with further moving to output directory." : " with no further move."));
                // overwriting existing file
                FileWriter fw = new FileWriter(selectedFile, false);
                fw.append((String) fileEditor.getValue());
                fw.close();
                status = "Changes saved.";
                if (move)
                    moveFile(selectedFile);
            } catch (IOException e) {
                status = e.getMessage();
                logger.error("Cannot properly save the file. Error cause follows: " + e.getCause());
            }

        } else {
            status = "Nothing to save";
        }
        return status;
    }

    /**
     * Makes sure that file has valid structure
     *
     * @param file - full path to file.
     * @return Returns null if everything is okay or error message otherwise.
     * @throws IOException
     */
    private String validateFile(String file) {
        //TODO: move logic to class
        String errMsg = null;
        BufferedReader br;
        String line;
        int record = 0, dataEntry = 0;

        try {
            br = new BufferedReader(new FileReader(file));
            outer_loop:
            while ((line = br.readLine()) != null) {
                while (line != null && line.startsWith("H")) {
                    record++;
                    line = br.readLine();
                    while (line != null && line.startsWith("D")) {
                        dataEntry++;
                        line = br.readLine();
                    }
                    if (!(record >= 1 && dataEntry > 0)) {
                        errMsg = "Missing product Line";
                        break outer_loop;
                    } else {
                        record = dataEntry = 0;
                    }
                } // record
                record = dataEntry = 0;
            } // line reading loop

            // *********************************************
            // validation of the records
            if (isNullOrEmpty(errMsg)) {
                br = null;
                // seek to beginning of the file
                br = new BufferedReader(new FileReader(file));
                OrderEntry orderEntry = null;
                Order order = null;
                outer_loop:
                while ((line = br.readLine()) != null) {
                    while (line != null && line.startsWith("H")) {
                        order = new Order(line);
                        // if order is okay
                        if (order.validateOrder() == null) {
                            while ((line = br.readLine()) != null) {
                                if (line.startsWith("D"))
                                    orderEntry = new OrderEntry(line);
                                if (orderEntry.validateOrderEntry() != null) {
                                    errMsg = orderEntry.validateOrderEntry();
                                    break outer_loop;
                                }
                            }
                        } else {
                            errMsg = order.validateOrder();
                            break outer_loop;
                        }
                    }
                }
            }
        } catch (IOException ioEx) {
            // we should not be here;
            logger.error(ioEx);
        }
        return errMsg;
    }

    /**
     * Tries to fix selected file and saves changes when succeeded.
     * Displays error msg when something went wrong.
     */
    private String fixEntry() {
        String status = null;
        //Object rowId = null;
        getMainWindow().showNotification("", Window.Notification.TYPE_ERROR_MESSAGE);
        if (!isNullOrEmpty(selectedFile)) {
            BufferedReader br;
            String line;
            List<Order> OrdersInFile = new ArrayList<Order>(2);
            try {
                br = new BufferedReader(new FileReader(selectedFile));
                OrderEntry orderEntry;
                Order order;

                while ((line = br.readLine()) != null) {
                    while (line != null && line.startsWith("H")) {
                        order = new Order(line);
                        while ((line = br.readLine()) != null && line.startsWith("D")) {
                            orderEntry = new OrderEntry(line);
                            order.addEntry(orderEntry);
                        }
                        OrdersInFile.add(order);
                    }
                }
            } catch (Exception e) {
                logger.error(e);
            }
            // all orders are red from file.
            for (Order ord : OrdersInFile) {
                ord.fixOrderEntries();
            }
            StringBuilder sb = new StringBuilder(2);
            for (Order ord : OrdersInFile) {
                sb.append(ord.toString());
            }

            logger.debug("-------------------------------------------");
            logger.debug(sb.toString());
            logger.debug("-------------------------------------------");

            fileEditor.setValue(sb.toString());
            getMainWindow().getApplication().close();
            getMainWindow().showNotification(doSave(false), Window.Notification.TYPE_TRAY_NOTIFICATION);
        }
        return status;
    }

}