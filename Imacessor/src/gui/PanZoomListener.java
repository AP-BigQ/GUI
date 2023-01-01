package gui;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;

public class PanZoomListener implements MouseListener, 
										MouseMotionListener,
										MouseWheelListener
{

	/*Fields*/
	public static final int MIN_ZOOMLEVEL = -50;
	public static final int MAX_ZOOMLEVEL = 50;
	public static final double MULTIPLE_ZOOM = 1.05;
	
	private Component targetComp;
	private int zoomlevel = 0;
	private int minZoomlevel = MIN_ZOOMLEVEL;
	private int maxZoomlevel = MAX_ZOOMLEVEL;
	private double zmultiple = MULTIPLE_ZOOM;
	
	private Point panStartPt;
	private Point panEndPt;
	private AffineTransform afTransfm = new AffineTransform();
	
	public PanZoomListener(Component comp){		
		this.targetComp = comp;
	}
	public PanZoomListener(Component comp, int minZm, int maxZm, double multipleZm){
		this.targetComp = comp;
		this.minZoomlevel = minZm;
		this.maxZoomlevel = maxZm;
		this.zmultiple = multipleZm;
		
	}
	
	/* MouseWheelListener Method */
	public void mouseWheelMoved(MouseWheelEvent event) {
		zoomAction(event);
	}
	
	/* MouseListener & MouseMotionListener Methods */
	public void mousePressed(MouseEvent event) {
		
		if((event.getModifiers() & MouseEvent.BUTTON2_MASK) != 0){
			targetComp.setCursor(new Cursor(Cursor.HAND_CURSOR));
			
			panStartPt = event.getPoint();
			panEndPt = null;
		}

	}
	public void mouseDragged(MouseEvent event) {
		if((event.getModifiers() & MouseEvent.BUTTON2_MASK) != 0){
			targetComp.setCursor(new Cursor(Cursor.HAND_CURSOR));
			panAction(event);
		}
		
	}

	public void mouseMoved(MouseEvent event) {}
	public void mouseClicked(MouseEvent event) {
		//System.out.println("x:"+ event.getX()+" y:"+ event.getY());
	}
	public void mouseReleased(MouseEvent event) {
		targetComp.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
	public void mouseEntered(MouseEvent event) {}
	public void mouseExited(MouseEvent event) {}


	/*Methods*/
	private void panAction(MouseEvent event){
		try{
			panEndPt = event.getPoint();
			
			Point2D.Float invsPanStartPt = invsTransfmPoint(panStartPt);
			Point2D.Float invsPanEndPt = invsTransfmPoint(panEndPt);
			
			double dx = invsPanEndPt.getX() - invsPanStartPt.getX();
			double dy = invsPanEndPt.getY() - invsPanStartPt.getY();
			
			afTransfm.translate(dx, dy);
			
			panStartPt = panEndPt;
			panEndPt = null;
			targetComp.repaint();
		}
		catch(NoninvertibleTransformException e){
			e.printStackTrace();
		}
	}
	private void zoomAction(MouseWheelEvent event){
		try{
			if(event.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL){
				
				java.awt.Toolkit toolkit = java.awt.Toolkit.getDefaultToolkit();
				Image iconZoomIn = toolkit.getImage("icons\\zoomIn.gif");
				Image iconZoomOut = toolkit.getImage("icons\\zoomOut.gif");
				
				int whlrot = event.getWheelRotation();
				Point pt = event.getPoint();
				Point2D p1, p2;
				p1 = invsTransfmPoint(pt);
				
				if(whlrot < 0){
					if(zoomlevel < maxZoomlevel){
						Cursor cur = toolkit.createCustomCursor(iconZoomIn, new Point(targetComp.getX(), targetComp.getY()), "zoomIn" );
						targetComp.setCursor(cur);
						
						zoomlevel++;						
						// zoom out
						afTransfm.scale(zmultiple, zmultiple);
					}
				}
				else{
					if(zoomlevel > minZoomlevel){
						Cursor cur = toolkit.createCustomCursor(iconZoomOut, new Point(targetComp.getX(), targetComp.getY()), "zoomOut" );
						targetComp.setCursor(cur);
						
						zoomlevel--;
						//zoom in
						afTransfm.scale(1.0/zmultiple, 1.0/zmultiple);
					}					
				}
				
				p2 = invsTransfmPoint(pt);
				double dx = p2.getX() - p1.getX();
				double dy = p2.getY() - p1.getY();
				afTransfm.translate(dx, dy);
				
				targetComp.repaint();
				
			}
		}
		catch(NoninvertibleTransformException e){
			e.printStackTrace();
		}
	}
	private Point2D.Float invsTransfmPoint(Point pt1) throws NoninvertibleTransformException {
		
		Point2D.Float pt2 = new Point2D.Float();
		afTransfm.inverseTransform(pt1, pt2);
		
		return pt2;
	}

	private void affineMatrix(AffineTransform afTransfm){
		
	}
	
	/* setter & getter*/
	public int getZoomlevel(){
		return this.zoomlevel;
	}
	public void setZoomlevel(int zmlevel){
		this.zoomlevel = zmlevel;
	}
	public AffineTransform getTransfm(){
		return this.afTransfm;
	}
	public void setTransfm(AffineTransform afTransfm){
		this.afTransfm = afTransfm;
	}
}
