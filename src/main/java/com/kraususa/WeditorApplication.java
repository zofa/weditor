/**
 * 
 */
package com.kraususa;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.Application;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

/**
 * Main application class.
 */
public class WeditorApplication extends Application {

	@Override
	public void init() {
		Window mainWindow = new Window("Weditor app");

		//List<String> files = new ArrayList<String>();

		/*try {
			files.addAll(this.listFiles("/home/z1/java_workspace/weditor/infiles/"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		/*if (!files.isEmpty()) {
			for (String string : files) {
				mainWindow.addComponent(new Label(string));
				
			}
		}*/
		setMainWindow(mainWindow);
	}

	protected List<String> listFiles(String path) throws IOException {

		File root = new File(path);
		File[] list = root.listFiles();

		if (list == null)
			return null;

		List<String> fileList = new ArrayList<String>();

		for (File f : list) {
			System.out.println("File:" + f.getPath());
			fileList.add(f.getName());
		}

		return fileList;
	}

}