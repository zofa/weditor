package com.kraususa;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;


/**
 * Created with IntelliJ IDEA.
 * User: z1
 * Date: 7/29/13
 * Time: 12:25 PM
 * The class has to provide monitoring of the folder for deletion and creation of new files.
 * TO be used for checking the input and output folders.
 */
public class FolderMonitor {

    private static String FOLDER = null;


    public FolderMonitor(String folder) throws Exception {

        this.FOLDER = folder;
        File monitoringFolder = new File(FOLDER);

        if (!monitoringFolder.exists()) {
            // Test to see if monitored folder exists
            throw new RuntimeException("Directory not found: " + FOLDER);
        }

        FileAlterationObserver observer = new FileAlterationObserver(folder);
        FileAlterationMonitor monitor = new FileAlterationMonitor(10);
        FileAlterationListener listener = new FileAlterationListenerAdaptor() {
            // Is triggered when a file is created in the monitored folder
            @Override
            public void onFileCreate(File file) {
                //TODO: add code later
            }

            // Is triggered when a file is deleted from the monitored folder
            @Override
            public void onFileDelete(File file) {
                //TODO: add code later
            }
        };


        observer.addListener(listener);
        monitor.addObserver(observer);
        monitor.start();

    }
}
