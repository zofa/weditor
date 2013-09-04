package com.kraususa.widgetset;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.Property;
import com.vaadin.data.util.FilesystemContainer;
import com.vaadin.ui.*;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


public class processingQueueWidget extends CustomComponent {
    private static final String outFileDir = "/errfiles/outfiles/";
    private Logger logger = Logger.getLogger(getClass());
    /*- Vaadin EditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */
    private HorizontalLayout mainLayout;
    private HorizontalSplitPanel hSplitPanel;
    private TextArea fileContainText;
    private Table filesInQueueTable;

    /**
     * The constructor should first build the main layout, set the
     * composition root and then do any custom initialization.
     * <p/>
     * The constructor will not be automatically regenerated by the
     * visual editor.
     */
    public processingQueueWidget() {
        buildMainLayout();
        setCompositionRoot(mainLayout);
    }

    @AutoGenerated
    private HorizontalLayout buildMainLayout() {
        // common part: create layout
        mainLayout = new HorizontalLayout();
        mainLayout.setImmediate(false);
        mainLayout.setSizeFull();
        mainLayout.setMargin(false);

        // top-level component properties
        setWidth("100.0%");
        setHeight("100.0%");

        // mainLayout.addComponent(new Label("Files in the processing queue."));

        mainLayout.addComponent(buildHorizontalSplitPanel_1());
        mainLayout.addComponent(hSplitPanel);
        return mainLayout;
    }

    @AutoGenerated
    private HorizontalSplitPanel buildHorizontalSplitPanel_1() {
        // common part: create layout
        hSplitPanel = new HorizontalSplitPanel();
        hSplitPanel.setImmediate(false);
        hSplitPanel.setWidth("100.0%");
        hSplitPanel.setHeight("100.0%");
        hSplitPanel.setSplitPosition(30);
        hSplitPanel.setMargin(false);
        hSplitPanel.setCaption("Files on the queue for processing.");

        // filesInQueueTable
        logger.info("Listing output directory " + outFileDir);

        FilesystemContainer fc = new FilesystemContainer(new File(outFileDir));
        filesInQueueTable = new Table("Re-processing queue", fc);
        if (fc.size() > 0) {
            logger.info("found " + fc.size() + " files:");
            //TODO: implement output of file names
        }
        filesInQueueTable.setImmediate(true);
        filesInQueueTable.setSizeFull();
        filesInQueueTable.setNullSelectionAllowed(false);
        filesInQueueTable.setSelectable(true);
        filesInQueueTable.setFooterVisible(true);
        filesInQueueTable.setColumnCollapsingAllowed(true);
        filesInQueueTable.setVisibleColumns(new String[]{"Name", "Last Modified"});
        filesInQueueTable.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (filesInQueueTable.getValue() != null) {
                    Object rowId = event.getProperty().getValue();
                    BufferedReader br = null;
                    String selectedOutFile;
                    try {
                        selectedOutFile = outFileDir + filesInQueueTable.getContainerProperty(rowId, "Name").getValue();
                        br = new BufferedReader(new FileReader(selectedOutFile));
                        StringBuilder sb = new StringBuilder();
                        String line = br.readLine();

                        while (line != null) {
                            sb.append(line);
                            sb.append("\n");
                            line = br.readLine();
                        }
                        fileContainText.setReadOnly(false);
                        fileContainText.setValue(sb.toString());
                        fileContainText.setReadOnly(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // fileContainText
        fileContainText = new TextArea();
        fileContainText.setImmediate(false);
        fileContainText.setWidth("100.0%");
        fileContainText.setHeight("100.0%");
        fileContainText.setReadOnly(true);
        fileContainText.setWordwrap(false);
        hSplitPanel.addComponent(filesInQueueTable);
        hSplitPanel.addComponent(fileContainText);

        return hSplitPanel;
    }

}
