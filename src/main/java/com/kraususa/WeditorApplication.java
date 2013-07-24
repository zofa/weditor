/**
 *
 */
package com.kraususa;

import com.vaadin.Application;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Main application class.
 */
public class WeditorApplication extends Application {


    protected String inFileDir,outFileDir;

    /**
     *
     */
    private static final long serialVersionUID = 3601221463880485916L;

    @Override
    public void init() {
        final Window mainWindow = new Window("Weditor app");

        Table table = new Table("List of error files");

        table.addContainerProperty("File name", String.class, null);
        table.addContainerProperty("Arrived at", String.class, null);
        table.addContainerProperty("Dealer list", String.class, null);
        table.addContainerProperty("Action", String.class, null);

        table.setWidth("60%");

		/* Add a few items in the table. */
        table.addItem(new Object[]{"Nicolaus", "asdf", "asads", "Link"});

        table.setColumnFooter(table.firstItemId(),"2");

        table.setFooterVisible(true);

        mainWindow.addComponent(table);

        List<String> files = new ArrayList<String>(10);

        if (files != null) {

            try {
                files.addAll(this.listFiles("/infiles/"));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (!files.isEmpty()) {
                for (String string : files) {
                    mainWindow.addComponent(new Label(string));
                    List<String> theDealers = this.getDealersList(string);
                    theDealers.toString();
                }
            }
        } else {
            mainWindow.showNotification("Error", "Cannot obtain file list");
        }



        Table processingTable = new Table("List of files on re-processing queue.");

        processingTable.addContainerProperty("File name", String.class, null);
        processingTable.addContainerProperty("Moved at", String.class, null);

        processingTable.addItem(new Object[]{"Nicolaus", "asdfasdf"});

        processingTable.setWidth("60%");
        mainWindow.addComponent(processingTable);

        setMainWindow(mainWindow);

    }

    protected List<String> listFiles(String path) throws IOException {

        File root = new File(path);
        File[] list = root.listFiles();

        List<String> fileList = new ArrayList<String>();

        for (File f : list) {
            System.out.println("File:" + f.getAbsolutePath());
            fileList.add(f.getAbsolutePath());
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
                    ;
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

    protected void moveFile(String fileName){



    }



}