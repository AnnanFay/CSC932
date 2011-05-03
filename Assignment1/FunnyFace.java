/* CSC932 First assignment Spring 2011   Starter seed file

   Funny face/identikit application

   The final solution will allow the construction of face with the proportions of the features
   controlled via sliders, and will provide a textual description of the face.

   This template provides some parts of the basic functionality.

   SBJ March 2011
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class FunnyFace extends JFrame
                       implements ChangeListener {

    // The hair slider
    private JSlider hairSizeControl;

    // The drawing area
    private JPanel panel;

    // Some useful fixed values: defining them here makes changes much easier later on
    private int faceCentreX = 175, faceCentreY = 200;           // Centre coordinates of main face disk
    private int faceRadius = 100;                               // Radius of main face disk
    private int hairSizeMin = 50, hairSizeMax = 150;            // Range of hair disk radius
    private int initialHairSize = faceRadius;                   // Initial setting of hair size slider
    private Color hairColour = Color.orange.darker();


    // main: Launch the FunnyFace program.
    public static void main( String[] args ) {

        FunnyFace frame = new FunnyFace();
        // The drawing panel is 350 x 550, and the frame is 120 taller for the sliders
        //  at the top and a margin at the bottom, so:
        frame.setSize( 350, 670 );
        frame.createGUI();
        frame.setVisible( true );

    } // End of main

    // createGUI: Set up the graphical user interface.
    private void createGUI() {

        // Set up main window characteristics
        setDefaultCloseOperation( EXIT_ON_CLOSE );
        Container window = getContentPane();
        window.setLayout( new FlowLayout() );

        // Create the hir slider
        hairSizeControl = new JSlider(JSlider.HORIZONTAL, hairSizeMin, hairSizeMax, initialHairSize);
        window.add(new JLabel("Hair quantity ("+hairSizeMin+" - "+hairSizeMax+")"));
        window.add(hairSizeControl);
        hairSizeControl.addChangeListener(this);

        // Create the panel for drawing on
        panel = new JPanel();
        panel.setPreferredSize( new Dimension( 350, 550 ) );
        panel.setBackground( Color.white );
        window.add( panel );

    } // End of createGUI

    // stateChanged: React to a slider change
    public void stateChanged(ChangeEvent e) {

        Graphics g = panel.getGraphics();

        // Record the current slider setting
        int hairSize = hairSizeControl.getValue();

        // Draw the hair: a disk overlapped by the face disk
        // The hair disk has its centre at (faceCentreX, faceCentreY-faceRadius/2) - that is
        // half way up from the centre of the face to the top of the face.
        // hairSize is the radius of the hair disk, so the overall width and height of the
        // disk (oval) are 2*hairSize.
        g.setColor(hairColour);
        drawCentredOval(g, faceCentreX, faceCentreY-faceRadius/2, 2*hairSize, 2*hairSize);

        // Draw the main face disk
        g.setColor(Color.pink);
        drawCentredOval(g, faceCentreX, faceCentreY, 2*faceRadius, 2*faceRadius);

    } // End of stateChanged

    // drawCentredOval is a 'helper' method that does as its name suggests:
    // It draws an oval, in the current drawing colour, with the given width and height,
    // with the *centre* of the oval positioned at the given coordinates centreX, centreY.
    // (This is easier to use than the standard drawOval method when you know where the centre
    // has to be, and you want the size to vary.)
    private void drawCentredOval(Graphics g, int centreX, int centreY, int width, int height) {

        g.drawOval(centreX-width/2, centreY-height/2, width, height);

    } // end of drawCentredOval

    // A fillCentredOval method would also be useful ... almost like drawCentredOval
    // but filling instead of drawing

} // End of FunnyFace
