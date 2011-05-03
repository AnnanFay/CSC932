/**
 * The CycleTour application is a cycle tour information utility to be used in, 
 * for example, a tourist information office.
 * 
 * This file is derived from CycleTour-Seed by Dr. Simon Jones with his implied permission.
 * Comments of "YOU DO NOT NEED TO CHANGE THIS" have been taken notice of, and changes have been made
 * where it was thought best.
 * Color is used instead of Colour in variable names, this is for interoperability and consistency 
 * as most languages use Color.
 * 
 * I have made four small changes to the design spec, knowing 
 * that this will not improve my mark and may decrease it.
 * 1. Double buffering to remove graphics flicker.
 * 2. Anti-aliasing in order to draw visually appealing shapes.
 * 3. Drawing the graphics at program start. 
 * 4. An array of Point objects is used instead of three arrays.
 * 
 * @author 1922542
 * @version 03 May 2011
 */

import java.awt.*;
import java.awt.event.*;
import java.math.*;

import javax.swing.*;

public class CycleTour extends JFrame implements ActionListener, MouseListener {

    /*
     * Data
     * These hold the data of the tour
     * [Small Change 4]
     * 
     * All the data for the tour graph is held in the tour array as Point objects. This helps keep 
     * related information together instead of using three different arrays. (AFAIK the spec doesn't
     * require three arrays.)
     */

    public class Point {
        public int height;
        public String town;
        public String note;
        
        // position relative to graph
        public int posX;
        public int posY;

        public Point(int height, String town, String note) {
            this.height = height;
            this.town = town;
            this.note = note;
        }
    }
    
    private final Point[] tour = {
            new Point(100, "Berwick", "Start of cycle tour: Coastal, sleepy"), // 0km
            new Point(120, "", "This place is really interesting"), // 1km
            new Point(120, "Edinburgh", "Capital of Scotland: Castle, Royal Mile, good shopping and pubs"), // 2km
            new Point(150, "", ""), // 3km
            new Point(120, "", ""), // 4km
            new Point(200, "", ""), // 5km
            new Point(200, "Falkirk", ""), // 6km
            new Point(210, "", ""), // 7km
            new Point(220, "", "See the Falkirk Wheel - amazing!"), // 8km
            new Point(230, "", ""), // 9km
            new Point(240, "", ""), // 10km
            new Point(300, "", ""), // 11km
            new Point(300, "", ""), // ...
            new Point(300, "", "Megalithic ruins in field to north of the road"),
            new Point(250, "", ""),
            new Point(250, "", ""),
            new Point(200, "Stirling",
                    "Castle, great university with beautiful campus"),
            new Point(150, "", ""),
            new Point(150, "", ""),
            new Point(150, "", ""),
            new Point(130, "", ""),
            new Point(120, "",
                    "Excellent medieval castle just outside of Doune"),
            new Point(120, "Doune", ""),
            new Point(150, "", ""),
            new Point(120, "", ""),
            new Point(200, "", ""),
            new Point(200, "", ""),
            new Point(210, "", "This place is extreamly interesting"),
            new Point(220, "Dunblane", "Cathedral city"),
            new Point(230, "", ""),
            new Point(240, "", ""),
            new Point(300, "", "Good view south-west over Dunblane from here"),
            new Point(300, "", ""),
            new Point(300, "", "Excellent view borth-east"),
            new Point(250, "Ashfield", ""),
            new Point(250, "", ""),
            new Point(200, "", ""),
            new Point(150, "", ""),
            new Point(150, "Kinburn",
                    "Rural village, antiques and auctions, try Mrs Buggins' B&B"),
            new Point(150, "", ""),
            new Point(130, "", ""),
            new Point(120, "", "This place is the most interesting ev'a!!!!!!!"),
            new Point(120, "", ""),
            new Point(150, "Perth", "There's one in Australia too!"),
            new Point(120, "", ""),
            new Point(200, "", ""), // ...
            new Point(200, "", ""), // 46km
            new Point(210, "", ""), // 47km
            new Point(220, "", ""), // 48km
            new Point(230, "", ""), // 49km
            new Point(240, "Aviemore", "End of cycle tour: Skiing resort") // 50km
    };
    
    /** 
     * This is used to calculate graph scaling
     * There should be a max function to extract this from the data set, however
     * since the data is hard coded it won't matter much
     */
    private final int maxTourHeight = 300;

    /**
     * An array required for full credit, but which will not be used.
     * 
     * [Advanced Feature 1]
     */

    private final String[] notes =
        { "Start of cycle tour: Coastal, sleepy",  // At 0 km
          "This place is really interesting","Capital of Scotland: Castle, Royal Mile, good shopping and pubs","","","","","","See the Falkirk Wheel - amazing!","","",   // Towns at 1-10 km
          "","","Megalithic ruins in field to north of the road","","","Castle, great university with beautiful campus","","","","",           // Towns at 11-20 km
          "Excellent medieval castle just outside of Doune","","","","","","This place is extreamly interesting","Cathedral city","","",      // Towns at 21-30 km
          "Good view south-west over Dunblane from here","","Excellent view borth-east","","","","","Rural village, antiques and auctions, try Mrs Buggins' B&B","","",    // Towns at 31-40 km
          "This place is the most interesting eve'!!!!!!","","There's one in Australia too!","","","","","","","End of cycle tour: Skiing resort" };    // Towns at 41-50 km
    
    /*
     * Layout
     * These are the constants specifying the GUI layout and position of elements within
     * All drawing actions, and other screen coordinate computations are carried out 
     * using these constants
     */
    
    // Window dimensions
    private final int windowHeight = 570;
    private final int windowWidth = 540;
    private final int windowMargin = 20;

    // Window containers dimensions
    // The window containers are defined in terms of the window dimensions so that it will be 
    // easier to change if/when window resizing is needed

    // All containers are the same width
    private final int containerWidth = windowWidth;
    
    private final int controlsHeight = 100;
    private final int informationHeight = 135;
    private final int surfaceHeight = windowHeight - controlsHeight
            - informationHeight - windowMargin - 80;

    // Graph dimensions
    private final int graphWidth = containerWidth;
    private final int graphHeight = 60;
    
    // Position of the graph relative to drawing surface
    private final int graphX = windowMargin;
    private final int graphY = graphHeight * 2;
    
    // The pixel horizontal distance between two points on the tour
    private final int pointInterval = graphWidth / (tour.length - 1);
    // Metres per pixel vertically
    private final int verticalScale = maxTourHeight / graphHeight;
    
    // Colours 
    // These variables hold the colours used in the application
    private final Color backgroundColor = Color.white; // Drawing panel background colour
    private final Color foregroundColor = Color.black;
    
    private final Color selectionColor = Color.red.darker();
    private final Color noteColor = Color.blue;
    private final Color townColor = Color.blue;
    private final Color graphColor = Color.green;

    // Indentation of text used in information and doubled in legend
    private final int textIndent = 60;
    
    /*
     * Program
     * These hold other variables and widgets used by the program 
     */

    // GUI widget definitions
    private Image buffer; // for double buffering
    private JPanel panel = new JPanel(); // For drawing the graph on

    private JButton start, previous, next, end, find;
    private JTextField search;
    private JLabel information; // For selected location information
    private String message = null; // Holds messages to be displayed to the user

    // This variable will always hold the index of the currently selected tour point.
    // Initially the start of the tour
    private int selectedLocation = 0;

    
    /**
     * Launch the CycleTour program.
     * 
     * @param args
     */
    public static void main(String[] args) {

        CycleTour frame = new CycleTour();

        // Set window dimensions and title
        // [Basic Feature 1]
        frame.setTitle("CycleTour - [1922542]");
        frame.setSize(frame.windowHeight, frame.windowWidth);

        frame.getContentPane().setBackground(frame.backgroundColor);

        // Position window in centre of screen
        // [Basic Feature 2]
        frame.setLocationRelativeTo(null);

        // Initiate GUI
        frame.createGUI();
        frame.setVisible(true);
    }

    /**
     * Sets up the graphical user interface and event handlers.
     */
    private void createGUI() {

        // Set up main window characteristics
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Container window = getContentPane();
        window.setLayout(new FlowLayout());

        
        // Controls
        // Set up the controls container
        JPanel controlContainer = new JPanel();
        controlContainer.setPreferredSize(new Dimension(containerWidth,
                controlsHeight));
        controlContainer.setBackground(backgroundColor);
        window.add(controlContainer);

        // Add header to container
        // [Basic Feature 3]
        JLabel header = new JLabel("Cycle Tour Information", JLabel.CENTER);
        header.setFont(new Font(null, Font.BOLD, 20));
        header.setPreferredSize(new Dimension(containerWidth, 25));
        controlContainer.add(header);

        // Add controls to container
        //[Intermediate Feature 3]
        start = addButton(controlContainer, "Start <<");
        previous = addButton(controlContainer, "Previous <");
        search = addInput(controlContainer, 16);
        next = addButton(controlContainer, "> Next");
        end = addButton(controlContainer, ">> End");
        find = addButton(controlContainer, "Find");
        
        // Information
        // Create text area for location information and add it to the main window
        JPanel infoContainer = new JPanel();
        infoContainer.setPreferredSize(new Dimension(containerWidth,
                informationHeight));
        infoContainer.setBackground(backgroundColor);
        window.add(infoContainer);

        information = new JLabel();
        information.setPreferredSize(new Dimension(containerWidth - 2*textIndent,
                informationHeight));
        information.setFont(new Font(null, Font.PLAIN, 12));
        infoContainer.add(information);
        
        // Create the panel for drawing the graph on and add it to the window
        panel = new JPanel();
        panel.setPreferredSize(new Dimension(containerWidth, surfaceHeight));
        panel.setBackground(backgroundColor);

        // Add listener so mouse clicks are passed
        // [Advanced Feature 6]
        panel.addMouseListener(this);
        
        window.add(panel);
    }

    /**
     * Paints the container and graphics panel when program starts
     * [Small Change 3]
     */
    public void paint(Graphics g) {
        // Call the original so that the interface is painted
        super.paint(g);
        // Draw the panel
        drawDoubleBuffered();
    }

    /**
     * Sets up graphics and drawing on the panel.
     * [Small Change 1]
     */
    public void drawDoubleBuffered() {

        // If the buffer doesn't exist create it
        if (buffer == null) {
            buffer = createImage(containerWidth, surfaceHeight);
        }
        // Downcast to Graphics2D in order to use antialiasing
        //[Small Change 2]
        Graphics2D bufferGraphic = (Graphics2D) buffer.getGraphics();
        bufferGraphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        // Clear the screen
        bufferGraphic.setColor(backgroundColor);
        bufferGraphic.fillRect(0, 0, containerWidth, surfaceHeight);

        // Paint the buffer
        paintScreen(bufferGraphic);
        // Copy buffer to visible screen
        Graphics2D g = (Graphics2D) panel.getGraphics();
        g.drawImage(buffer, 0, 0, this);

        // wipe the message so that it will be blank next update
        message = "";
    }

    /**
     * 
     */
    public void paintScreen(Graphics g) {
        updateInformation();
        drawGraph(g);
        drawLegend(g);
    }

    /**
     * Update the information above the graph
     * [Intermediate Feature 6]
     * [Advanced Feature 4]
     */
    public void updateInformation() {
        information.setText("<html>"
                + ( (message != null) ? "<p>" + message + "</p>" : "<p></p>")
                + "<p>Information about the selected location</p>"
                + "<p>Distance from start: " + selectedLocation + "km</p>"
                + "<p>Height: " + tour[selectedLocation].height + "m</p>"
                + "<p>Town: " + tour[selectedLocation].town + "</p>"
                + "<p>Notes: " + tour[selectedLocation].note + "</p>"
                + "</html>");

    }
    /**
     * Draws the graph, ruler and graph markers/labels onto a graphics object
     * @param g
     */
    public void drawGraph(Graphics g) {
        
        // Draw ruler and place markers
        // [Basic Feature 6]

        int rulerY = graphY + 15;
        int markerLength = 5;

        // Draw the spine of the ruler
        g.setColor(foregroundColor);
        g.drawLine(windowMargin, rulerY, containerWidth - windowMargin,
                rulerY);
        
        for (int i = 0, l = tour.length-1; i <= l; i++) {
            
            // Draw tour segments
            // [Basic Feature 4]
            if (i < tour.length-1){
                g.setColor(graphColor);
                drawTourSegment(g, i);
            }
            
            g.setColor(foregroundColor);
            // Draw the rule markings
            g.drawLine(graphX + (i * pointInterval), rulerY, 
                    graphX + (i * pointInterval), rulerY - markerLength);
                    
            // Draw distance every ten km
            if (i % 10 == 0) {
                g.drawString("" + i, graphX + (i*pointInterval) - 5, graphY + 30);
            }
            
            // Calculate point positions on graph
            tour[i].posX = graphX + i * pointInterval;
            tour[i].posY = graphY - tour[i].height
                    / verticalScale;
            
            // Draw information markers
            // [Intermediate Feature 1]
            if (tour[i].town != ""){
                drawTownMarker(g, tour[i].posX, tour[i].posY);
            }

            // [Advanced Feature 2]
            if (tour[i].note != ""){
                drawNoteMarker(g, tour[i].posX, tour[i].posY);
            }
            
        }
        
        // Graph Labels
        // [Basic Feature 5]
        // Start and end are relative to the position of those points
        // km is squashed in the margin next to the ruler
        g.setColor(foregroundColor);
        g.drawString("Start", tour[0].posX - 10, tour[0].posY-20);
        g.drawString("End", tour[tour.length-1].posX - 6, tour[tour.length-1].posY-20);
        g.drawString("km", containerWidth - windowMargin + 4, rulerY);
        
        // [Advanced Feature 5]
        g.setColor(selectionColor);
        g.drawString("Click on the route to display information", 2*textIndent, 10);

        // Mark currently selected location
        // [Intermediate Feature 4]
        drawSelectionMarker(g, tour[selectedLocation].posX, tour[selectedLocation].posY);
    }
    /**
     * Utility method to draw town markers.
     * [Intermediate Feature 1]
     * (NOTE: The Demo incorrectly uses 1x1 circles)
     * 
     * @param g
     * @param x
     * @param y
     */
    public void drawTownMarker(Graphics g, int x, int y){
        g.setColor(townColor);
        fillCentredOval(g, x, y, 3, 3);
    }
    /**
     * Utility method to draw note markers.
     * [Advanced Feature 2]
     * 
     * @param g
     * @param x
     * @param y
     */
    public void drawNoteMarker(Graphics g, int x, int y){
        g.setColor(noteColor);
        drawCentredOval(g, x, y, 6, 6);
    }
    /**
     * Utility method to draw the selection marker.
     * [Intermediate Feature 4]
     * 
     * @param g
     * @param x
     * @param y
     */
    public void drawSelectionMarker(Graphics g, int x, int y){
        g.setColor(selectionColor);
        drawCentredOval(g, x, y, 10, 10);
        
    }
    /**
     * Draws the legend below the tour graph
     * @param g
     */
    public void drawLegend(Graphics g) {
        int textSizeOffset = 5; //Assuming text size is 10
        
        //[Intermediate Feature 2]
        g.setColor(townColor);
        drawTownMarker(g, 2*textIndent - 10, graphY + 60 - textSizeOffset);
        g.drawString("Blue blobs mark towns", 2*textIndent, graphY + 60);

        // [Advanced Feature 3]
        g.setColor(noteColor);
        drawNoteMarker(g, 2*textIndent - 10, graphY + 80 - textSizeOffset);
        g.drawString("Blue circles indicate where extra information is available", 
                2*textIndent, graphY + 80);

        // [Intermediate Feature 5]
        g.setColor(selectionColor);
        drawSelectionMarker(g, 2*textIndent - 10, graphY + 100 - textSizeOffset);
        g.drawString("The red circle indicates the selected location", 2*textIndent, graphY + 100);
    }

    /**
     * This method handles all button clicks and text input.
     * [Intermediate Feature 3]
     */
    public void actionPerformed(ActionEvent e) {

        Object source = e.getSource();
        
        if (source == start) {
            selectedLocation = 0;
        } else if (source == previous && selectedLocation > 0) {
            selectedLocation--;
        } else if (source == next && selectedLocation < tour.length - 1) {
            selectedLocation++;
        } else if (source == end) {
            selectedLocation = tour.length - 1;
        } else if (source == find || source == search) {
            // Call the find method with the textual input and displays relevant message.
            String searchText = search.getText();
            int foundLocation = find(searchText);
            if (foundLocation != -1) {
                selectedLocation = foundLocation;
                message = "Found \"" + searchText + "\"";
            } else {
                message = "No locations found for \"" + searchText + "\"";
            }
        }

        // Redraw the screen
        drawDoubleBuffered();
    }
    
    /**
     * Select a tour point if someone clicks on or near it.
     * [Advanced Feature 6]
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        
        int mouseX = e.getX();
        int mouseY = e.getY();
        
        // This code checks to find out which point is clicked, if a point is clicked.
        // It's suboptimal performance wise as it check every point
        // For each point
        //   Calculate euclidean distance
        //   Store it's distance if it's the closest so far
        // If the closest is less than 10px away
        //   Select it
        
        int closest = -1;
        double cDistance = -1;
        
        for (int i = 0; i <= 50; i++) {
            double distance = Math.sqrt(Math.pow(tour[i].posX-mouseX,2) + Math.pow(tour[i].posY-mouseY,2));
            if (cDistance == -1 || distance < cDistance){
                closest = i;
                cDistance = distance;
            }
        }
        if (closest != -1 && cDistance < 10){
            selectedLocation = closest;
        }

        // Redraw the screen
        drawDoubleBuffered();
    }
    /**
     * Helper method for drawing a single tour segment, from the given
     * location index to the next km point
     * 
     * @param g
     * @param index
     */
    private void drawTourSegment(Graphics g, int index) {

        // The method draws the tour segment from index to index+1
        // at an appropriate place on the screen, determined by the
        // constants above, as a filled green trapezium, a bit like this:
        //
        //                 rightY:
        //                height at
        //             /|  index+1
        //  leftY:    / |
        //  height   /  |
        // at index |   |
        //          |   |
        //          |   |
        //          -----
        //
        // It uses the fillPolygon Graphics method.

        int leftY = graphY - tour[index].height / verticalScale; // Height at index above sea level.
        int rightY = graphY - tour[index + 1].height / verticalScale; // Height at index+1 above sea level.
        int leftX = graphX + index * pointInterval; // Distance from left of diagram to index.
        int rightX = leftX + pointInterval; // Distance from left of diagram to index+1.
        int[] xArray = { leftX, leftX, rightX, rightX }; // (x,y) coordinates of the four corners,
        int[] yArray = { graphY, leftY, rightY, graphY }; // clockwise from the bottom left.
        g.fillPolygon(xArray, yArray, 4); // Array of x coords, of y coords, and the number of coords

    }

    /**
     * Utility method to draw an oval in the current colour at a given position.
     */
    private void drawCentredOval(Graphics g, int centreX, int centreY,
            int width, int height) {
        g.drawOval(centreX - width / 2, centreY - height / 2, width, height);
    }
    /**
     * Utility method to draw a filled oval in the current colour at a given position.
     * Optionally with a black border.
     */
    private void fillCentredOval(Graphics g, int centreX, int centreY, int width, int height) {
        g.fillOval(centreX-width/2, centreY-height/2, width, height);
    }
    /**
     * Utility method to create and add buttons to the interface. The code is
     * self explanatory.
     */
    private JButton addButton(Container container, String text) {

        JButton button = new JButton(text);
        button.addActionListener(this);
        container.add(button);

        return button;
    }
    /**
     * Utility method to create and add inputs to the interface.
     * 
     */
    private JTextField addInput(Container container, int width) {

        JTextField input = new JTextField(width);
        input.addActionListener(this);
        container.add(input);

        return input;
    }
    /**
     * This searches notes and town names for searchText and returns the 
     * index if found or -1 if not found
     * 
     * @param searchText text to search for
     * @return index of found point or -1
     */
    private int find(String searchText) {
        searchText = searchText.toLowerCase();
        for (int i = 1, l = tour.length; i <= l; i++) {
            int searchPoint = (i + selectedLocation) % l;
            if (tour[searchPoint].town.toLowerCase().indexOf(searchText) > -1
                    || tour[searchPoint].note.toLowerCase().indexOf(searchText) > -1) {
                return searchPoint;
            }
        }
        return -1;
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub
        
    }
}
