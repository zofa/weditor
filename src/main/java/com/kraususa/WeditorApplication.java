/**
 *
 */
package com.kraususa;


import com.vaadin.Application;
import com.vaadin.data.Property;
import com.vaadin.data.util.FilesystemContainer;
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
    private Label topLabel;
    private Button saveAndMoveButton, saveButton, fixButton;
    private String selectedFile = null;
    private Button moveButton;
    private final String FILE_OKAY = "File is Okay.";

    @Override
    public void init() {
        final Window mainWindow = new Window("Weditor app");

        mainWindow.addListener(new Window.CloseListener() {
            @Override
            public void windowClose(Window.CloseEvent e) {
                logger.info("User refreshes the page..reloading components.");
                getMainWindow().getApplication().close();
            }
        });

        HorizontalLayout hLayout = new HorizontalLayout();
        VerticalLayout verticalLayout = new VerticalLayout();
        logger.info("Initializing the application.");
        // -------------------------------------------------------------------------------------------------------------
        table = new Table("List of error files");
        table.setCaption("Error file editor");
        table.addContainerProperty("File name", String.class, null);
        table.addContainerProperty("# of records", Integer.class, null);
        table.addContainerProperty("Dealer list", String.class, null);
        table.addContainerProperty("Errors", String.class, null);
        table.setWidth("30%");
        table.setColumnFooter(table.firstItemId(), "2");
        table.setFooterVisible(true);
        table.setSelectable(true);
        table.setSizeFull();
        table.setColumnReorderingAllowed(true);
        table.setImmediate(true);
        table.setColumnCollapsingAllowed(true);

        List<String> files = new ArrayList<String>(10);

        if (files != null) {
            files.addAll(this.listFiles(inFileDir));
            if (!files.isEmpty()) {
                for (int i = 0; i < files.size(); i++) {
                    String theDealers = this.getDealersList(inFileDir + files.get(i)).toString();
                    table.addItem(new Object[]
                            {
                                    files.get(i),
                                    this.getDealersList(inFileDir + files.get(i)).size(),
                                    //  "",
                                    theDealers,
                                    // "Fix & sumbit"
                                    validateFile(inFileDir + files.get(i))
                            }, new Integer(i));
                }
            }
        } else {
            mainWindow.showNotification("Error", "Cannot obtain file list");
        }

        table.setColumnFooter("File name", "Total files");
        table.setColumnFooter("# of records", String.valueOf(files.size()));

        final Label current = new Label("Selected: -");
        table.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                if (table.getValue() != null) {
                    current.setValue("Selected: " + table.getValue());
                    Object rowId = event.getProperty().getValue();
                    BufferedReader br = null;

                    try {
                        selectedFile = inFileDir + (String) table.getContainerProperty(rowId, "File name").getValue();
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
                        e.printStackTrace();
                    }
                    saveButton.setEnabled(false);
                    saveAndMoveButton.setEnabled(false);
                    fixButton.setEnabled(true);
                    if (table.getContainerProperty(rowId, "Errors").getValue() == FILE_OKAY)
                        moveButton.setEnabled(true);
                    else
                        moveButton.setEnabled(false);
                } else {
                    fixButton.setEnabled(false);
                }
            }
        });

        // -------------------------------------------------------------------------------------------------------------
        FilesystemContainer fc = new FilesystemContainer(new File(outFileDir));
        Table processingTable = new Table("Re-processing queue", fc);
        processingTable.setSizeFull();
        processingTable.setSelectable(true);

        // -------------------------------------------------------------------------------------------------------------
        fileEditor = new com.vaadin.ui.TextArea("FIle content");
        fileEditor.setWidth("70%");
        fileEditor.setRows(20);
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
        split.setSplitPosition(35, Sizeable.UNITS_PERCENTAGE);
        split.addComponent(table);
        VerticalLayout v = new VerticalLayout();
        HorizontalLayout h = new HorizontalLayout();

        saveAndMoveButton = new Button("Save changes and move file");
        saveAndMoveButton.setEnabled(false);
        saveButton = new Button("Save changes to file");
        saveButton.setEnabled(false);

        saveButton.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                logger.info("Saving file.");
                getMainWindow().showNotification(doSave(false));
                getMainWindow().getApplication().close();
                getMainWindow().showNotification("Changes saved to file");
                logger.info("File changes saved.");
            }
        });

        fixButton = new Button("Fix selected");
        fixButton.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {

                getMainWindow().showNotification("TODO:To be implemented", Window.Notification.TYPE_ERROR_MESSAGE);
                logger.info("Trying to fix file..");
            }
        });

        moveButton = new Button("Move file");
        moveButton.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (table.getValue() != null) {
                    moveFile(selectedFile);
                }
            }
        }
        );


        h.addComponent(saveButton);
        h.addComponent(saveAndMoveButton);
        h.addComponent(fixButton);
        h.addComponent(moveButton);

        v.addComponent(h);
        //v.setComponentAlignment(saveAndMoveButton, Alignment.BOTTOM_RIGHT);

        saveAndMoveButton.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                logger.info("Saving and moving file...");
                getMainWindow().showNotification(doSave(true), Window.Notification.POSITION_TOP_RIGHT);
                getMainWindow().getApplication().close();
                logger.info("File moved - reloading application UI");
            }
        });
        v.addComponent(fileEditor);
        v.addComponent(current);
        split.addComponent(v);
        split.setHeight("80%");


        //verticalLayout.addComponent(topLabel);
        verticalLayout.addComponent(split);
        verticalLayout.addComponent(processingTable);
        verticalLayout.setMargin(true);
        verticalLayout.setWidth("100%");
        verticalLayout.setHeight("100%");

        mainWindow.setContent(verticalLayout);
        setMainWindow(mainWindow);
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

        List<String> fileList = new ArrayList<String>();

        for (File f : list) {
            fileList.add(f.getName());
        }
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

                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return dealers;
    }

    /**
     * Moves @filename the file from input to output directory.
     *
     * @param fileName should be exactly file name only.
     */
    protected void moveFile(String fileName) {
        File theFile = new File(fileName);
        if (!theFile.renameTo(new File(outFileDir + theFile.getName()))) {
            getMainWindow().showNotification("Failed to move file", Window.Notification.TYPE_ERROR_MESSAGE);
        }
    }

    /**
     * Saves changes made in the editor to the file.
     *
     * @param move
     * @return null when everything is okay and error msg when something went wrong.
     */
    public String doSave(boolean move) {
        String status = null;
        if (!isNullOrEmpty(selectedFile) && !fileEditor.getValue().toString().isEmpty()) {
            try {
                // overwriting existing file
                FileWriter fw = new FileWriter(selectedFile, false);
                fw.append((String) fileEditor.getValue());
                fw.close();
                status = "Changes saved.";
                if (move)
                    moveFile(selectedFile);
            } catch (IOException e) {
                status = e.getMessage();
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
        String errMsg = null;
        BufferedReader br = null;
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
            System.out.println(ioEx.getMessage() + " " + ioEx.getCause());
        }
        if (errMsg == null) errMsg = FILE_OKAY;
        return errMsg;
    }

    private void fixEntry() {

    }
}