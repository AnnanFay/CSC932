/**
 * The FunnyFace application is an identikit prototype.
 * 
 * I have made four small changes to the design spec, knowing 
 * that this will not improve my mark and may decrease it.
 * 1. Double buffering to remove graphics flicker.
 * 2. Anti-aliasing in order to draw visually appealing shapes.
 * 3. Using a natural caucasian colour as the skin colour.
 * 4. Drawing the graphics at program start. 
 * 
 * @author 1922542
 * @version 07 March 2011
 */

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

public class FunnyFace extends JFrame
                          implements ChangeListener 
                          {
    //GUI Components
    private JPanel panel;
    private JSlider mouthControl, eyeControl, earControl, hairControl;
    private Image buffer;
    
    //Settings
    private Color backgroundColor = Color.white;
    private Color foregroundColor = Color.gray.darker();
    private int surfaceWidth = 350;
    private int surfaceHeight = 550;
    
    //Counter for drawing data
    private int lastData = 0;
    
    //Face Settings
    private int faceCentreX = 175;
    private int faceCentreY = 200;
    private int faceRadius = 100;
    //[Small Change 3]
    private Color faceColor = new Color(229, 198, 197);

    // Face Components
    // For face parts min and max are the bounds of the sliders.
    // These are followed by the initial value of the sliders, which 
    // is also used to store the current value, and the colour.
    // [Intermediate Feature 9]
    
    // Mouth
    private int mouthSizeMin = 10;
    private int mouthSizeMax = 100; 
    private int mouthSize = 30;
    private Color mouthColor = Color.red;
    
    // Eye
    private int eyeSizeMin = 5;
    private int eyeSizeMax = 50; 
    private int eyeSize = 20;
    private Color eyeColor = Color.cyan;
    
    // Eye
    private int noseWidth = 10;
    private int noseHeight = 40;
    private Color noseColor = Color.pink.darker();
    
    // Ear
    private int earSizeMin = 0;
    private int earSizeMax = 40; 
    private int earSize = 10;
    private Color earColor = faceColor;
    
    // Hair
    private int hairSizeMin = 50;
    private int hairSizeMax = 150; 
    private int hairSize = faceRadius;
    private Color hairColor = Color.orange.darker();
    
    
    public static void main( String[] args ) {

        FunnyFace frame = new FunnyFace();
        
        // Set window dimensions and title
        // [Basic Feature 1]
        frame.setTitle("FunnyFace - [1922542]");
        frame.setSize( frame.surfaceWidth, frame.surfaceHeight + 120 ); // height + space for controls
        
        // Position window in centre of screen
        // [Basic Feature 2]
        frame.setLocationRelativeTo(null);
        
        // Initiate GUI
        frame.createGUI();
        frame.setVisible( true );

    } 

    /**
     * Sets up the graphical interface
     */
    private void createGUI() {

        // Set up main window characteristics
        setDefaultCloseOperation( EXIT_ON_CLOSE );
        Container window = getContentPane();
        window.setLayout( new FlowLayout() );

        // Add controls & labels
        // [Basic Feature 3]
        mouthControl = addSlider(window, mouthSizeMin, mouthSizeMax, mouthSize, "Mouth width");
        eyeControl = addSlider(window, eyeSizeMin, eyeSizeMax, eyeSize, "Eye size");
        earControl = addSlider(window, earSizeMin, earSizeMax, earSize, "Ear size");
        hairControl = addSlider(window, hairSizeMin, hairSizeMax, hairSize, "Hair quantity");
        
        // Create the panel for drawing on and add it to the window
        panel = new JPanel();
        // Set dimensions and background colour
        panel.setPreferredSize( new Dimension( surfaceWidth, surfaceHeight ) );
        panel.setBackground( backgroundColor );
        window.add( panel );
    }
    /**
     * Sets up graphics and drawing on the panel.
     * [Small Change 1]
     */
    public void drawDoubleBuffered(){
    	
    	// If the buffer doesn't exist create it
        if (buffer == null){
            buffer = createImage(surfaceWidth, surfaceHeight);
        }
        // Downcast to Graphics2D in order to use antialiasing
        //[Small Change 2]
        Graphics2D bufferGraphic = (Graphics2D)buffer.getGraphics();
        bufferGraphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                       RenderingHints.VALUE_ANTIALIAS_ON);
        // Clear the screen
        bufferGraphic.setColor( backgroundColor );
        bufferGraphic.fillRect( 0, 0, surfaceWidth, surfaceHeight );
        // Reset data counter
        lastData = 0;
        
        // Paint the buffer
        paintScreen(bufferGraphic);
        // Copy buffer to visible screen
        Graphics2D g = (Graphics2D)panel.getGraphics();
        g.drawImage(buffer, 0, 0, this);
    }
    /**
     * Paints the container and graphics panel when program starts
     * [Small Change 4]
     */
    public void paint(Graphics g) {
    	// Call the original so that the interface is painted
    	super.paint(g);
    	// Draw the panel
    	drawDoubleBuffered();
    }
    /**
     * Event handler for sliders, called when a slider is changed.
     */
    public void stateChanged(ChangeEvent e) {

        // Record the current slider setting so that the face is updated.
        // [Intermediate Feature 1]
        mouthSize = mouthControl.getValue();
        eyeSize = eyeControl.getValue();
        earSize = earControl.getValue();
        hairSize = hairControl.getValue();
        // Redraw the screen
        drawDoubleBuffered();
    }
    /**
     * Paints the graphics panel with face, face data and face description
     */
    public void paintScreen(Graphics g) {
    	paintFace(g);
    	paintData(g);
    	paintDescription(g);
    }
    /**
     * Draws a series of overlapping shapes to represent a face.
     * Yeah, they are symmetrical [Intermediate Feature 8]
     */
    public void paintFace(Graphics g) {
        
    	// Hair
    	// [Intermediate Feature 2]
        g.setColor(hairColor);
        fillCentredOval(g, faceCentreX, faceCentreY-faceRadius/2, 2*hairSize, 2*hairSize, false);
        
        // Ears
        // [Intermediate Feature 3]
        g.setColor(earColor);
        fillCentredOval(g, faceCentreX-faceRadius, faceCentreY, earSize*2, earSize*4, true);
        fillCentredOval(g, faceCentreX+faceRadius, faceCentreY, earSize*2, earSize*4, true);
        
        // Face
        g.setColor(faceColor);
        fillCentredOval(g, faceCentreX, faceCentreY, 2*faceRadius, 2*faceRadius, true);

        // Eyes
        // [Intermediate Feature 4]
        int eyeY = faceCentreY-(faceRadius/2);
        int leftEyeX = faceCentreX+(faceRadius/3);
        int rightEyeX = faceCentreX-(faceRadius/3);
        // Draws three overlapping filled ovals for each eye
        g.setColor(Color.gray.brighter());
        fillCentredOval(g, rightEyeX, eyeY, eyeSize, eyeSize*2, true);
        fillCentredOval(g, leftEyeX, eyeY, eyeSize, eyeSize*2, true);
        g.setColor(eyeColor);
        fillCentredOval(g, rightEyeX, eyeY, (eyeSize*2)/3, (eyeSize*4)/3, false);
        fillCentredOval(g, leftEyeX, eyeY, (eyeSize*2)/3, (eyeSize*4)/3, false);
        g.setColor(Color.black);
        fillCentredOval(g, rightEyeX, eyeY, eyeSize/3, (eyeSize*2)/3, false);
        fillCentredOval(g, leftEyeX, eyeY, eyeSize/3, (eyeSize*2)/3, false);

        // Nose
        // [Intermediate Feature 5]
        g.setColor(noseColor);
        g.fillRoundRect(faceCentreX-(noseWidth/2), faceCentreY-(noseHeight/2), noseWidth, noseHeight, 20, 20);
        
        // Mouth
        // Draws overlapping ovals to represent lips 
        // [Intermediate Feature 6]
        g.setColor(mouthColor);
        fillCentredOval(g, faceCentreX, faceCentreY+(faceRadius/2), mouthSize, 20, false);
        g.setColor(faceColor);
        fillCentredOval(g, faceCentreX, faceCentreY+(faceRadius/2)-8, mouthSize, 20, false);
        
    }
    /**
     * Draws the facial data below the face.
     * [Basic Feature 4]
     */
    public void paintData(Graphics g) {
        
        // Write data about the face
        g.setColor(foregroundColor);
        addData(g, "Numerical face data:");
        addData(g, "");
        addData(g, "Mouth width: "+mouthSize);
        addData(g, "Eye width: "+eyeSize);
        addData(g, "Ear width: "+earSize);
        addData(g, "Hair width: "+hairSize);
        addData(g, "");
    }
    /**
     * Draws the face description.
     * [Advanced Feature 1]
     */
    public void paintDescription(Graphics g) {

        g.setColor(foregroundColor);
        //Write the face description
        addData(g, "Description of the face:");
        addData(g, "");

        // Print face component descriptions while counting bad traits [Advanced Feature 2]
        // If there are more than 2 bad traits
        // 		Print ugliness to face description [Advanced Feature 3]
        
        int badTraits = 0;
        
        if (hairSize == 50){
        	addData(g, "Completely bald!");
        	badTraits++;
        } else if (hairSize < 71){
        	addData(g, "Short hair");
        } else if (hairSize < 121){
        	addData(g, "Medium length hair");
        } else {
        	addData(g, "Afro hair!");
        }
        
        if (eyeSize < 15){
        	addData(g, "Small eyes");
        	badTraits++;
        } else if (eyeSize > 25){
        	addData(g, "Large eyes");
        	badTraits++;
        }
        
        if (earSize == 0){
        	addData(g, "No ears!");
        	badTraits++;
        } else if (earSize < 11){
        	addData(g, "Small ears");
        	badTraits++;
        } else if (earSize > 20){
        	addData(g, "Large ears");
        	badTraits++;
        }
        
        if (mouthSize < 21){
        	addData(g, "Rather small mouth");
        	badTraits++;
        } else if (mouthSize < 71){
        	addData(g, "Normal size mouth");
        } else {
        	addData(g, "Rather large mouth");
        	badTraits++;
        }

        if (badTraits > 2){
            g.setColor(Color.red);
        	addData(g, "Quite an ugly specimen!");
        }
    }
    /**
     * Utility method to draw strings on the interface.
     * Uses lastData to work out the current position of text.
     */
    private void addData(Graphics g, String text) {
        g.drawString(text, 60, 320+lastData*15);
        lastData++;
    }
    /**
     * Utility method to draw an oval in the current colour at a given position.
     */
    private void drawCentredOval(Graphics g, int centreX, int centreY, int width, int height) {
        g.drawOval(centreX-width/2, centreY-height/2, width, height);
    }
    /**
     * Utility method to draw a filled oval in the current colour at a given position.
     * Optionally with a black border.
     */
    private void fillCentredOval(Graphics g, int centreX, int centreY, int width, int height, boolean border) {
        g.fillOval(centreX-width/2, centreY-height/2, width, height);
        
        // Draws a black border around the shape if asked to
        // [Intermediate Feature 7]
        if (border){
            Color c = g.getColor();
            g.setColor(Color.black);
            drawCentredOval(g, centreX, centreY, width, height);
            g.setColor(c);
        }

    }
    /**
     * Utility method to create and add sliders to the interface.
     */
    private JSlider addSlider(Container window, int min, int max, int init, String label){
        
    	JSlider control = new JSlider(JSlider.HORIZONTAL, min, max, init);
        JLabel controlLabel = new JLabel(label + " ("+min+" - "+max+")");

        // add control & label to the window
        window.add(controlLabel);
        window.add(control);
        // add event listener
        control.addChangeListener(this);
        
        return control;
    }
}