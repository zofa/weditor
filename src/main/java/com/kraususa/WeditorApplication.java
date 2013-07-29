/**
 *
 */
package com.kraususa;

import com.vaadin.Application;
import com.vaadin.data.Property;
import com.vaadin.data.util.FilesystemContainer;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
    protected String inFileDir = "/errfiles/infiles/", outFileDir = "/errfiles/outfiles/";
    private Table table;
    private TextArea fileEditor;
    private String selectedFile = null;

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
        table.addContainerProperty("File name", String.class, null);
        table.addContainerProperty("# of records", Integer.class, null);
        //table.addContainerProperty("Arrived at", String.class, null);
        table.addContainerProperty("Dealer list", String.class, null);
        // table.addContainerProperty("Action", NativeButton.class, null);
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
                    logger.info("Saved content to file.");
                } catch (Exception e) {
                    e.printStackTrace();
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
        //fileEditor.addListener();
        fileEditor.setImmediate(true);
        fileEditor.setWordwrap(false);

        // -------------------------------------------------------------------------------------------------------------
        HorizontalSplitPanel split = new HorizontalSplitPanel();
        split.setSplitPosition(35, Sizeable.UNITS_PERCENTAGE);
        split.addComponent(table);
        VerticalLayout v = new VerticalLayout();
        HorizontalLayout h = new HorizontalLayout();

        Button saveAndMoveButton = new Button("Save and Move");
        Button saveButton = new Button("Save changes");
        saveAndMoveButton.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                logger.info("Saving with no file move.");
                doSave(false);
                getMainWindow().getApplication().close();
                logger.info("Saving with no file move.");
            }
        });

        h.addComponent(saveButton);
        h.addComponent(saveAndMoveButton);

        v.addComponent(h);
        //v.setComponentAlignment(saveAndMoveButton, Alignment.BOTTOM_RIGHT);

        saveAndMoveButton.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                logger.info("Saving moving file...");
                doSave(true);
                getMainWindow().getApplication().close();
                logger.info("File moved - reloading application UI");
            }
        });
        v.addComponent(fileEditor);
        v.addComponent(current);
        split.addComponent(v);

        verticalLayout.addComponent(split);
        verticalLayout.addComponent(processingTable);
        verticalLayout.setMargin(true);
        verticalLayout.setWidth("100%");
        verticalLayout.setHeight("100%");

        mainWindow.setContent(verticalLayout);
        setMainWindow(mainWindow);

    }

    protected List<String> listFiles(String path) {

        logger.info("Listing directory " + path);

        File root = new File(path);
        File[] list = root.listFiles();

        List<String> fileList = new ArrayList<String>();

        for (File f : list) {
            // System.out.println("File:" + f.getName());
            fileList.add(f.getName());
        }
        return fileList;
    }

    protected List<String> getDealersList(String file) {

        List<String> dealers = new ArrayList<String>(5);

        BufferedReader br = null;
        String line;
        String splitter = "\\|";

        try {
            br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                String[] tmp = line.split(splitter);
                try {
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
     * @param fileName should be exactly file name only - should not include
     */
    protected void moveFile(String fileName) {
        File theFile = new File(fileName);
        if (!theFile.renameTo(new File(outFileDir + theFile.getName()))) {
            getMainWindow().showNotification("Failed to move file", Window.Notification.TYPE_ERROR_MESSAGE);
        }
    }

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
}