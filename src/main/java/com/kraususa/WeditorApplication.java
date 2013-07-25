/**
 *
 */
package com.kraususa;

import com.vaadin.Application;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.FilesystemContainer;
import com.vaadin.ui.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Main application class.
 */
public class WeditorApplication extends Application {


    /**
     *
     */
    private static final long serialVersionUID = 3601221463880485916L;
    protected String inFileDir = "/errfiles/infiles/", outFileDir = "/errfiles/outfiles/";

    private Table table;
    @Override
    public void init() {
        final Window mainWindow = new Window("Weditor app");

        HorizontalLayout hLayout = new HorizontalLayout();
        VerticalLayout verticalLayout = new VerticalLayout();
        // -------------------------------------------------------------------------------------------------------------
        table = new Table("List of error files");
        table.addContainerProperty("File name", String.class, null);
        table.addContainerProperty("# of records", Integer.class, null);
        table.addContainerProperty("Arrived at", String.class, null);
        table.addContainerProperty("Dealer list", String.class, null);
        table.addContainerProperty("Action", NativeButton.class, null);
        table.setWidth("50%");
        table.setColumnFooter(table.firstItemId(), "2");
        table.setFooterVisible(true); table.setSelectable(true); table.setSizeFull();
        table.setImmediate(true);
        table.setColumnCollapsingAllowed(true);

        List<String> files = new ArrayList<String>(10);

        if (files != null) {

            try {
                files.addAll(this.listFiles(inFileDir));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (!files.isEmpty()) {
                for (int i = 0; i < files.size(); i++) {
                    String theDealers = this.getDealersList(inFileDir+files.get(i)).toString();
                    table.addItem(new Object[]
                            {
                                    files.get(i),
                                 this.getDealersList(files.get(i)).size(),
                                 "",
                                 theDealers,
                                 "Fix & sumbit"
                            }, new Integer(i));
                }
            }
        } else {
            mainWindow.showNotification("Error", "Cannot obtain file list");
        }

        final Label current = new Label("Selected: -");
        table.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {

                //Item item  = table.getItem();
               // current.setValue("Selected: " + item.getItemProperty();

            }
        });
        // -------------------------------------------------------------------------------------------------------------
        FilesystemContainer fc = new FilesystemContainer(new File(outFileDir));
        Table processingTable = new Table("Re-processing queue",fc);
        processingTable.setSizeFull();
        processingTable.setSelectable(true);

        // -------------------------------------------------------------------------------------------------------------
        TextArea fileEditor = new com.vaadin.ui.TextArea("File Editor");
        fileEditor.setWidth("50%");
        fileEditor.setRows(20);
        fileEditor.setColumns(40);
        fileEditor.setSizeFull();
        //fileEditor.addListener();
        fileEditor.setImmediate(true);
        fileEditor.setWordwrap(true);

        // -------------------------------------------------------------------------------------------------------------
        HorizontalSplitPanel split = new HorizontalSplitPanel();
        split.addComponent(table);
        VerticalLayout v = new VerticalLayout();
        v.addComponent(fileEditor);
        Button saveButton = new Button("Save");
        v.addComponent(saveButton);
        v.setComponentAlignment(saveButton, Alignment.BOTTOM_RIGHT);
        v.addComponent(current);
        split.addComponent(v);

        verticalLayout.addComponent(split);
        verticalLayout.addComponent(processingTable);
        verticalLayout.setMargin(true);
        verticalLayout.setWidth("80%");
        verticalLayout.setHeight("80%");
        mainWindow.setContent(verticalLayout);
        setMainWindow(mainWindow);

    }

    protected List<String> listFiles(String path) throws IOException {

        File root = new File(path);
        File[] list = root.listFiles();

        List<String> fileList = new ArrayList<String>();

        for (File f : list) {
            System.out.println("File:" + f.getName());
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

    protected void moveFile(String fileName) {

    }


}