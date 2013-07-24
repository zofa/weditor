/**
 *
 */
package com.kraususa;

import java.awt.Panel;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.Application;
import com.vaadin.ui.*;
//import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickEvent;

/**
 * Main application class.
 */
public class WeditorApplication extends Application {

    /**
     *
     */
    private static final long serialVersionUID = 3601221463880485916L;

    @Override
    public void init() {
        final Window mainWindow = new Window("Weditor app");

        Table table = new Table("List of error files");

        table.addContainerProperty("File name", String.class, null);
        table.addContainerProperty("Arrived at", Date.class, null);
        table.addContainerProperty("Dealer list", String.class, null);
        table.addContainerProperty("Action", String.class, null);

		/* Add a few items in the table. */
        table.addItem(new Object[]{"Nicolaus", new Date(), "asads", "Link"

        });

        mainWindow.addComponent(table);

        List<String> files = new ArrayList<String>();

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

                }
            }
        } else {
            mainWindow.showNotification("Error", "Cannot obtain file list");

        }

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
        String line = "";
        String cvsSplitBy = "|";

        try {

            Map<String, String> maps = new HashMap<String, String>();

            br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] country = line.split(cvsSplitBy);

                maps.put(country[4], country[5]);

            }

            // loop map
            for (Map.Entry<String, String> entry : maps.entrySet()) {

                System.out.println("Country [code= " + entry.getKey()
                        + " , name=" + entry.getValue() + "]");

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

        return null;

    }

}