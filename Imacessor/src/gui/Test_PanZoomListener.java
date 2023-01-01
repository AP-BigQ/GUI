package gui;


import java.awt.*;
import java.awt.geom.AffineTransform;

import javax.swing.*;


// this class is to test PanZoomListener class
public class Test_PanZoomListener extends JPanel{

	public static void main(String[] args){
		JFrame frame = new JFrame("Test PanZoomListner Class");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		Test_PanZoomListener testPanZoomListener = new Test_PanZoomListener();
		
		frame.add(testPanZoomListener, BorderLayout.CENTER);
		frame.pack();
	}
	
	private PanZoomListener panzoom;
	private boolean init = true;
    private Point[] points = {
            new Point(100, 100),
            new Point(-100, 100),
            new Point(-100, -100),
            new Point(100, -100)
    };
	
	public Test_PanZoomListener() {
		panzoom= new PanZoomListener(this);
		this.addMouseListener(panzoom);
		this.addMouseMotionListener(panzoom);
		this.addMouseWheelListener(panzoom);
	}

	public Test_PanZoomListener(int minzoomlevel, int maxzoomlevel, double multiplezoom){
		panzoom= new PanZoomListener(this, minzoomlevel, maxzoomlevel, multiplezoom);
		this.addMouseListener(panzoom);
		this.addMouseMotionListener(panzoom);
		this.addMouseWheelListener(panzoom);
	}
    public Dimension getPreferredSize() {
        return new Dimension(600, 500);
    }
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;		
		
	       if (init) {
	    	   
	    	   // move coordinate sys. to center of window
	    	   // invert Y-axis
	            init = false;
	            Dimension d = getSize();
	            int xc = d.width / 2;
	            int yc = d.height / 2;
	            g2.translate(xc, yc);
	            g2.scale(1, -1);
	            
	            // set the Transformation
	            panzoom.setTransfm(g2.getTransform());
	        } else {
	            // get the Transformed coordinate sys. from listener
	        	g2.setTransform(panzoom.getTransfm());
	        }
	       
		
        // Draw the axes
        g2.drawLine(-1000, 0, 1000, 0);
        g2.drawLine(0, -1000, 0, 1000);
                
        double RAD = 1000.0;
        int N =  40;
        for(int i=0; i<=N; i++)
        {
        	int rad = (int)(RAD/N)*i;
            g2.drawOval(-rad, -rad, 2*rad, 2*rad);
            //g2.drawOval(rad, rad, 2*rad, 2*rad);
        }    
        
        // invert font back
        Font font = g2.getFont();
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.scale(1, -1);
        g2.setFont(font.deriveFont(affineTransform));
        
        int index = 0;
        for(Point p: points){

			g2.drawLine((int)p.getX() - 5, (int)p.getY(), (int)p.getX() + 5, (int)p.getY());			
			g2.drawLine((int)p.getX(), (int)p.getY() -5, (int)p.getX(), (int)p.getY() + 5);
			
			g2.drawString("Quadrant "+ (++index) + "(" + p.getX() + "," + p.getY() + ")", (float) p.getX(), (float) p.getY());

	}
	}
}
