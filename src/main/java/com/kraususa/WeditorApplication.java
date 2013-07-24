/**
 * 
 */
package com.kraususa;

import java.awt.Panel;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.vaadin.Application;
import com.vaadin.ui.*;
//import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickEvent;


/**
 * Main application class.
 */
public class WeditorApplication  extends Application {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3601221463880485916L;

	@Override
	public void init() {
		final Window mainWindow = new Window("Weditor app");
		
		

		Table table = new Table("This is my Table");

		/* Define the names and data types of columns.
		 * The "default value" parameter is meaningless here. */
		table.addContainerProperty("First Name", String.class,  null);
		table.addContainerProperty("Last Name",  String.class,  null);
		table.addContainerProperty("Year",       Integer.class, null);

		/* Add a few items in the table. */
		table.addItem(new Object[] {
		    "Nicolaus","Copernicus",new Integer(1473)}, new Integer(1));
		table.addItem(new Object[] {
		    "Tycho",   "Brahe",     new Integer(1546)}, new Integer(2));
		table.addItem(new Object[] {
		    "Giordano","Bruno",     new Integer(1548)}, new Integer(3));
		table.addItem(new Object[] {
		    "Galileo", "Galilei",   new Integer(1564)}, new Integer(4));
		table.addItem(new Object[] {
		    "Johannes","Kepler",    new Integer(1571)}, new Integer(5));
		table.addItem(new Object[] {
		    "Isaac",   "Newton",    new Integer(1643)}, new Integer(6));
		
		 
		mainWindow.addComponent(table);
		mainWindow.addComponent(new Label("asdfasdf"));
		
		
		List<String> files = new ArrayList<String>();

		if (files != null) {
			
			try {				
				files.addAll(this.listFiles("/infiles/"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				mainWindow.addComponent(new Label(e.getMessage()));
			}

			if (!files.isEmpty()) {
				for (String string : files) {
					mainWindow.addComponent(new Label(string));

				}
			}
		} else {
			mainWindow.showNotification("This is the caption",
					"This is the description");
			
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

}