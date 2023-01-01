package gui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import algrm.*;


public class HistDialog extends JDialog{
	
	JTabbedPane tabpane;
	JPanel histPanel;
	HistEqPanel histEqPanel;
	
	JScrollPane scPaneHistEq;
	JPanel canvasPanel;
	JPanel statPanel;
	
	
	JLabel txtPixels, txtMedian,  
			txtMean, txtSD;
	JLabel RnumPixels, GnumPixels, BnumPixels, 
			RnumMedian,RnumMean, RnumSD;
	JLabel  GnumMedian, GnumMean, GnumSD;
	JLabel  BnumMedian, BnumMean, BnumSD;
	
	ImageStat imgStat;
	
	public HistDialog(JFrame parent, BufferedImage bufImg){
	
		super(parent, "Histogram Analysis", true);
		
		if(parent != null){
			Dimension parentSize = parent.getSize(); 
		      Point p = parent.getLocation(); 
		      setLocation(p.x + parentSize.width/8, p.y + parentSize.height /8);
		}
			
		imgStat = new ImageStat(bufImg);
		
		tabpane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT );
		
		histPanel = new JPanel();		
		histEqPanel = new HistEqPanel(bufImg);
		histEqPanel.repaint();
		
		statPanel = new JPanel();
		//canvasPanel = new JPanel();	
		canvasPanel = new HistEqPanel(bufImg);
		scPaneHistEq = new JScrollPane(canvasPanel,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scPaneHistEq.setOpaque(true);
		
		txtPixels = new JLabel("Pixels:");	
		txtMedian = new JLabel("Median:");
		txtMean = new JLabel("Mean:");
		txtSD = new JLabel("Std Dev:");
		
		RnumPixels = new JLabel(new Integer(imgStat.pixelsNum).toString());
		GnumPixels = new JLabel(new Integer(imgStat.pixelsNum).toString());
		BnumPixels = new JLabel(new Integer(imgStat.pixelsNum).toString());
		
		
		RnumMedian = new JLabel(new Double(imgStat.pixelsMedian[0]).toString());
		GnumMedian = new JLabel(new Double(imgStat.pixelsMedian[1]).toString());
		BnumMedian = new JLabel(new Double(imgStat.pixelsMedian[2]).toString());
		
		
		RnumMean = new JLabel(String.format("%4.6s", new Double(imgStat.pixelsMean[0]).toString()));
		GnumMean = new JLabel(String.format("%4.6s", new Double(imgStat.pixelsMean[1]).toString()));
		BnumMean = new JLabel(String.format("%4.6s", new Double(imgStat.pixelsMean[2]).toString()));
		
		RnumSD = new JLabel(String.format("%4.6s", new Double(imgStat.pixelsSD[0]).toString()));
		GnumSD = new JLabel(String.format("%4.6s", new Double(imgStat.pixelsSD[1]).toString()));
		BnumSD = new JLabel(String.format("%4.6s", new Double(imgStat.pixelsSD[2]).toString()));
		


		statPanel.setLayout(new GridBagLayout());
		
		Image iconImg = bufImg.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
		
		statPanel.add(new JLabel(new ImageIcon(iconImg)), new GBC(0,0).setAnchor(GBC.WEST));
		statPanel.add(new JLabel("R"), new GBC(1,0).setAnchor(GBC.CENTER).setWeight(100, 0));
		statPanel.add(new JLabel("G"), new GBC(2,0).setAnchor(GBC.CENTER).setWeight(100, 0));
		statPanel.add(new JLabel("B"), new GBC(3,0).setAnchor(GBC.CENTER).setWeight(100, 0));
		
		
		statPanel.add(txtPixels, new GBC(0,1).setAnchor(GBC.WEST));
		statPanel.add(RnumPixels, new GBC(1,1).setAnchor(GBC.CENTER).setWeight(100, 0).setInsets(1));
		statPanel.add(GnumPixels, new GBC(2,1).setAnchor(GBC.CENTER).setWeight(100, 0).setInsets(1));
		statPanel.add(BnumPixels, new GBC(3,1).setAnchor(GBC.CENTER).setWeight(100, 0).setInsets(1));
		
		statPanel.add(txtMedian, new GBC(0,2).setAnchor(GBC.WEST));
		statPanel.add(RnumMedian, new GBC(1,2).setAnchor(GBC.CENTER).setWeight(100, 0).setInsets(1));
		statPanel.add(GnumMedian, new GBC(2,2).setAnchor(GBC.CENTER).setWeight(100, 0).setInsets(1));
		statPanel.add(BnumMedian, new GBC(3,2).setAnchor(GBC.CENTER).setWeight(100, 0).setInsets(1));
		
		statPanel.add(txtMean, new GBC(0,3).setAnchor(GBC.WEST));
		statPanel.add(RnumMean, new GBC(1,3).setAnchor(GBC.CENTER).setWeight(100, 0).setInsets(1));
		statPanel.add(GnumMean, new GBC(2,3).setAnchor(GBC.CENTER).setWeight(100, 0).setInsets(1));
		statPanel.add(BnumMean, new GBC(3,3).setAnchor(GBC.CENTER).setWeight(100, 0).setInsets(1));
		
		statPanel.add(txtSD, new GBC(0,4).setAnchor(GBC.WEST));
		statPanel.add(RnumSD, new GBC(1,4).setAnchor(GBC.CENTER).setWeight(100, 0).setInsets(1));
		statPanel.add(GnumSD, new GBC(2,4).setAnchor(GBC.CENTER).setWeight(100, 0).setInsets(1));
		statPanel.add(BnumSD, new GBC(3,4).setAnchor(GBC.CENTER).setWeight(100, 0).setInsets(1));
		


		
		//histPanel.add(canvasPanel, BorderLayout.CENTER);
		histPanel.add(scPaneHistEq, BorderLayout.CENTER);		
		
		//canvasPanel.setPreferredSize(new Dimension(500, 400));
		scPaneHistEq.setPreferredSize(new Dimension(500, 400));
		
		histPanel.add(statPanel, BorderLayout.SOUTH);
		statPanel.setPreferredSize(new Dimension(500-5, 200));
		
		Border blackline, raisedetched, loweredetched, raisedbevel, loweredbevel, empty, compound;
		blackline = BorderFactory.createLineBorder(Color.black);
		raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		raisedbevel = BorderFactory.createRaisedBevelBorder();
		loweredbevel = BorderFactory.createLoweredBevelBorder();
		empty = BorderFactory.createEmptyBorder();
		
		TitledBorder title;
		title = BorderFactory.createTitledBorder("Statistics");
		title = BorderFactory.createTitledBorder(
				loweredetched, "Statistics");
				title.setTitleJustification(TitledBorder.RIGHT);
		
		statPanel.setBorder(title);
		
		histPanel.setPreferredSize(new Dimension(500, 600));
		

		tabpane.addTab("Histogram", histPanel);
		tabpane.addTab("Histogram Equalization", histEqPanel);
		tabpane.setPreferredSize(new Dimension(500, 600));

	
		this.add(tabpane);		
	    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	    pack();
	    //this.setResizable(false);
	    this.setVisible(true);

	}
	
	public class ImageStat {
		
		int[][] pixArray;
		public int pixelsNum;
		public int imgWidth, imgHeight;
		public double[] pixelsMedian, pixelsMean, pixelsSD;
		public int[] Rpix, Gpix, Bpix;
		public int[] RpixCount, GpixCount, BpixCount;
		
		public ImageStat(BufferedImage bufImg){
			imgWidth = bufImg.getWidth();
			imgHeight = bufImg.getHeight();
			pixelsNum = imgHeight*imgWidth;
			
			pixArray = new int[imgHeight][imgWidth];
			Rpix = new int[pixelsNum];
			Gpix = new int[pixelsNum];
			Bpix = new int[pixelsNum];
			
			RpixCount = new int[pixelsNum];
			GpixCount = new int[pixelsNum];
			BpixCount = new int[pixelsNum];
			
			pixelsMean = new double[3];
			pixelsSD = new double[3];
			pixelsMedian = new double[3];
			
			for(int i=0; i<imgHeight; i++)
				for(int j=0; j<imgWidth; j++)
					{
						pixArray[i][j] = bufImg.getRGB(j, i);
						
						Rpix[i*(imgWidth) + j] = (pixArray[i][j]& 0x00FF0000) >>> 16;
						Gpix[i*(imgWidth) + j] = (pixArray[i][j]& 0x0000FF00) >>> 8;						
						Bpix[i*(imgWidth) + j] = (pixArray[i][j]& 0x000000FF) >>> 0;
					}			
			
			pixelsMean[0] = mean(Rpix);
			pixelsMean[1] = mean(Gpix);
			pixelsMean[2] = mean(Bpix);
			
			pixelsSD[0] = sd(Rpix, 0);
			pixelsSD[1] = sd(Gpix, 1);
			pixelsSD[2] = sd(Bpix, 2);
			
			pixelsMedian[0] = median(Rpix);
			pixelsMedian[1] = median(Gpix);
			pixelsMedian[2] = median(Bpix);						
			
		}
		
		public double mean(int[] pix){
			double mean = 0;
			for(int k=0; k<pixelsNum; k++){
				mean += pix[k];
			}
			mean /= pixelsNum;
			return mean;
		}
		
		public double sd(int[] pix, int index){
			double sd = 0;
			for(int k=0; k<pixelsNum; k++){
				sd += (pix[k] - pixelsMean[index])*(pix[k] - pixelsMean[index]);
			}
			sd /= pixelsNum;
			return Math.sqrt(sd);
		}
		
		public double median(int[] pix){
			Arrays.sort(pix);
			int middle = pix.length/2;
			if(pix.length%2 == 0){
				int left = pix[middle-1];
				int right = pix[middle];
				return (left+right)/2.0;
			}else{
				return pix[middle];
			}
		}
	}
	
	public class HistEqPanel extends JPanel
	{
		public BufferedImage image;
		public HistogramEqualization histEq;
		
		HistEqPanel(BufferedImage bufImg){
			image = bufImg;
			histEq = new HistogramEqualization(image);

		}
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			
			Graphics2D g2 = (Graphics2D)g;
			int x = (this.getWidth() - image.getWidth()) / 2;
			int y = (this.getHeight() - image.getHeight()) / 2;

			g2.drawImage(histEq.getEqImg(), x, y, null);
			
			revalidate();

		}
	}
}


