/*  CSC932 Second assignment Spring 2011   Starter seed file

    A Cycle Tour Information system

    This is a Java application that could be used by, say, a tourist
    information office to present the details of a cycle ride in
    the area. The application could be left running on a computer screen
    in the office, and visitors could browse the tour details as they
    wished.

    SBJ April 2011
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class CycleTour extends JFrame
                       implements ActionListener {

    // See Java for Students page 250 for information about "array initializers":
    // the convenient way used below to create an array and fill it with data in one statement:

    // First an initialized array containing the spot heights for the cycle route:
    // 50 kilometres of spot heights: metres above sea level at 1 kilometre (= 10 pixel) intervals
    // The kilometre points are at 0km, 1km, 2km, ... 49km, 50km, so 51 heights altogether
    // **** YOU DO NOT NEED TO CHANGE THIS ARRAY ****
    private final int[] spotHeights =
                               { 100,  // At 0 km, the start
                                 120,120,150,120,200,200,210,220,230,240,   // Heights at 1-10 km
                                 300,300,300,250,250,200,150,150,150,130,   // Heights at 11-20 km
                                 120,120,150,120,200,200,210,220,230,240,   // Heights at 21-30 km
                                 300,300,300,250,250,200,150,150,150,130,   // Heights at 31-40 km
                                 120,120,150,120,200,200,210,220,230,240 }; // Heights at 41-50 km (the end)

    // Next an initialized array of town names at the 51 kilometre points:
    // Where there is no town, there is an empty string
    // **** YOU DO NOT NEED TO CHANGE THIS ARRAY ****
    private final String[] towns =
                            { "Berwick",  // At 0 km
                              "","Edinburgh","","","","Falkirk","","","","",   // Towns at 1-10 km
                              "","","","","","Stirling","","","","",           // Towns at 11-20 km
                              "","Doune","","","","","","Dunblane","","",      // Towns at 21-30 km
                              "","","","Ashfield","","","","Kinbuck","","",    // Towns at 31-40 km
                              "","","Perth","","","","","","","Aviemore" };    // Towns at 41-50 km

    // Now some constants that fix the position of the route image on the screen,
    // and provide scale information. All drawing actions, and other screen coordinate
    // computations are carried out using these constants.
    // You should NOT NEED to alter these definitions. You may need to add to them.
    private final int seaLevelY = 270,                                // Pixels down from top of window for baseline of route diagram
                      fromLeftX = 20,                                 // Pixels from left of window for spotHeights[0]
                      spotHeightInterval = 10,                        // Pixels per km horizontally (that is, between spot heights)
                      rightSideX = fromLeftX+(spotHeights.length-1)*spotHeightInterval,  // Pixels from left of window for spotHeights[50]
                      verticalScale = 5;                              // Metres per pixel vertically

    // This is a useful colour for drawing the selected location marker.
    // The idea is that it will help red/green colour blind people see the red marker
    // against the green route segments.
    private final Color selectionColour = Color.red.darker();

    // This variable will always hold the index in the arrays of the currently selected location,
    // which can only be at a kilometre point.
    private int selectedLocation = 0;                                 // Initially the start of the cycle route

    // GUI widget definition:
    private JButton display =         new JButton("Display cycle route");  // To prompt the initial display
    private JPanel panel =            new JPanel();                    // For drawing the cycle route on
    private Color backgroundColour =  Color.white;                     // Drawing panel background colour

    // main: Launch the CycleTour program.
    public static void main( String[] args ) {

        CycleTour frame = new CycleTour();
        frame.setSize( 550+20, 400+140 );               // +... for borders, for the buttons and search field at the top
        frame.createGUI();
        frame.setVisible( true );

    } // End of main

    // createGUI: Set up the graphical user interface.
    private void createGUI() {

        // Set up main window characteristics
        setDefaultCloseOperation( EXIT_ON_CLOSE );
        Container window = getContentPane();
        window.setLayout( new FlowLayout() );

        // The initial prompt button
        window.add(display);
        display.addActionListener(this);

        // Set up the panel for drawing on, with response to mouse clicks
        panel.setPreferredSize( new Dimension( 550, 400 ) );
        panel.setBackground( backgroundColour );
        window.add( panel );

    } // End of createGUI

    // Handle prompt for initial display
    public void actionPerformed(ActionEvent e) {

        Graphics g = panel.getGraphics();

        // The following two lines show how drawRouteSegment (see below) can be used to
        // draw just two of the route segments.
        // You need to draw the full sequence of route segments using
        // concise, manageable code
        drawRouteSegment(g, 0);          // Segment from 0 km to 1 km
        drawRouteSegment(g, 1);          // Segment from 1 km to 2 km

        // The following formulae show how the constants (private final ints)
        // defined above can be used in the later stages of the assignment:

        // Given that selectedLocation contains the index of the currently selected location,
        // here is how to calculate the screen coordinates of currently selected location
        // (This information can be used to draw the location red circle)
        int locationX = fromLeftX + selectedLocation * spotHeightInterval;
        int locationY = seaLevelY - spotHeights[selectedLocation] / verticalScale;

    }// End of actionPerformed

    // Helper method for drawing a single green route segment,
    // from the given location index to the next km point
    // *** THIS METHOD IS COMPLETE AS GIVEN.          ***
    // *** YOU DO NOT NEED TO MAKE ANY CHANGES TO IT, ***
    // *** NOR EVEN NECESSARILY UNDERSTAND IT WELL.   ***
    private void drawRouteSegment(Graphics g, int index) {

        // The method draws the route segment from index to index+1
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

        int leftY = seaLevelY- spotHeights[index] / verticalScale;        // Height at index above sea level.
        int rightY = seaLevelY - spotHeights[index+1] / verticalScale;     // Height at index+1 above sea level.
        int leftX = fromLeftX + index * spotHeightInterval;                // Distance from left of diagram to index.
        int rightX = leftX + spotHeightInterval;                         // Distance from left of diagram to index+1.
        int[] xArray = { leftX, leftX, rightX, rightX };               // (x,y) coordinates of the four corners,
        int[] yArray = { seaLevelY, leftY, rightY, seaLevelY };        // clockwise from the bottom left.
        g.setColor(Color.green);
        g.fillPolygon(xArray, yArray, 4);                              // Array of x coords, of y coords, and the number of coords

    } // End of drawRouteSegment

} // End of CycleTour application
