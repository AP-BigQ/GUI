import java.awt.*;

import javax.swing.*;


public class EvaluateChart extends JDialog{
	public int Width = 710;
	public int Height = 700;
	Dimension screenSize;
	int centerX, centerY;
	
	JChartPanel chart;
	JPanel rubricsPanel;
	JLegendPanel legendPanel;
	final JTable rubricsTable;
	final static String[] columnNames = {"Letter Grade", "Grade Point"};
	final Object[][] rubricsData = { {"A",  "4.0"}, 
							   {"A-", "3.7"}, 
							   {"B+", "3.3"}, 
							   {"B ",  "3.0"}, 
							   {"B-", "2.7"}, 
							   {"C+", "2.3"}, 
							   {"C ",  "2.0"}, 
							   {"C-", "1.7"}, 
							   {"D ",  "1.0"} };
	
	public EvaluateChart(double[] meanGrades) {
		
		this.setPreferredSize(new Dimension(Width, Height));
		
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int w = this.getWidth();
		int h = this.getHeight();
		centerX = screenSize.width / 8;
		centerY = screenSize.height / 8;
		setLocation(centerX, centerY);
		
		setTitle("Chart");
		JButton titleButton = new JButton("Chart Analysis");
		//titleButton.setEnabled(false);
		titleButton.setForeground(Color.RED);
		add(titleButton, BorderLayout.NORTH);
		
		chart = new JChartPanel(meanGrades);		
		add(chart, BorderLayout.CENTER);
		
		rubricsPanel = new JPanel();
		legendPanel = new JLegendPanel();
		legendPanel.setPreferredSize(new Dimension(20, 20));
		rubricsTable = new JTable(rubricsData, columnNames);
		//rubricsTable.setPreferredScrollableViewportSize(new Dimension(150, 80));
		rubricsTable.setFillsViewportHeight(true);
		JTableUtilities.setCellsAlignment(rubricsTable, SwingConstants.CENTER, Color.LIGHT_GRAY);
		

		rubricsPanel.add(rubricsTable, BorderLayout.SOUTH);
		this.add(rubricsPanel, BorderLayout.WEST);
		
		//rubricsPanel.add(legendPanel, BorderLayout.SOUTH);
		this.add(legendPanel, BorderLayout.EAST);
		
		//add(rubricsPanel, BorderLayout.EAST);

		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	    pack();
	    this.setResizable(false);
	    this.setVisible(true);
	}

}
class JChartPanel extends JPanel {
	
	public int Width = 500;
	public int Height = 600;
	
	Rectangle[] fieldRects ;
	final String[] fieldNames = {"Major", "Science", "Technology", "Art", "Liberals"};
	
	double[] means = new double[5];
	
	public JChartPanel(double[] meanGrades){
		setSize(new Dimension(Width, Height));

		means = meanGrades;
		fieldRects = new Rectangle[5];
		for(int i=0; i<fieldRects.length; i++) {
			fieldRects[i] = new Rectangle(30 + Width*i/fieldRects.length, 
										  (int)(Height - Height*means[i]/fieldRects.length), 
											    Width/fieldRects.length/4, 
											    (int)(Height*means[i]/fieldRects.length));
		}
		
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;

		// draw field names horizontal & charts
		for(int i=0; i<fieldRects.length; i++) {
			Rectangle r = fieldRects[i];
			
			if(r.y == Height) { r.y = Height-1; r.height = 1;}
			g2.setColor(colorSelector(r.height*5.0/Height));
		    g2.fillRect(r.x, r.y, r.width, r.height);
		    
		    g2.setColor(Color.BLACK);		    
		    g2.drawString(fieldNames[i], r.x, Height+15);
		    // draw x-axis ticks
		    g2.drawLine(Width*(i+1)/fieldRects.length, Height, Width*(i+1)/fieldRects.length, Height+5);

		    
		    g2.drawString(String.format("%2.2f", new Double(means[i])), 
		    			r.x, (int)(Height - Height*means[i]/fieldRects.length) - 5);
		}
		//draw vertical & horizontal lines
		Graphics2D g2d = (Graphics2D) g2.create();
	    Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{5}, 0);
		for(int i= 4; i >= 0; i--){
			g2.setColor(Color.BLACK);
			g2.drawString(String.format("%2.1f", new Double(i)), 4, Height/5*(5-i)+10);		
		    
	        g2d.setStroke(dashed);        
		    g2d.setColor(Color.RED);	
			g2d.drawLine(2, Height/5*(5-i), Width, Height/5*(5-i));	

		}
	    
	    double[] gradeLevel = {3.7, 3.3, 2.7, 2.3, 1.7, 1.3};
	    dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{12}, 0);
		for(int i= 0; i<gradeLevel.length; i++){

			g2.drawString(String.format("%2.1f", (float)(gradeLevel[i])), 4, (int)(Height/5*(5-gradeLevel[i])+10));		
		    
	        g2d.setStroke(dashed);        
		    g2d.setColor(Color.BLUE);	
		    int ht = (int)(Height/5*(5-gradeLevel[i]));
			g2d.drawLine(2, ht, Width, ht);	

		}
	    g2d.dispose();	    
	    
	    g2.setFont(new Font("TimesRoman", Font.PLAIN, 20)); 
		g2.drawString("Grade Point Average", Width/3, Height/5-30);
		
		g.setColor(Color.BLACK);
		//bounding box
		g2.drawRect(2, Height/5-20, Width, Height/5*4+40);
		
		
	}
	
	Color colorSelector(double grade){
		if(grade >= 3.7) return Color.ORANGE;
		else if(grade >= 2.7) return Color.BLUE;
		else if(grade >= 1.7) return Color.MAGENTA;
		else if(grade >= 1.0) return Color.GREEN;
		else return Color.RED;
	}
	
}
class JLegendPanel extends JPanel {
	
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		int width = getWidth();
		int height = getHeight();
		g2.setFont(new Font("TimesRoman", Font.PLAIN, 18)); 
		for(int i=1; i < 20; i += 4){
			g2.setColor(colorSelect(i));
			g2.fillRect(width/10, (int)(height/20.0*i), width/4, height/10);
			g2.drawString(letterSelect(i), width/8*3, (int)(height/20.0*i));
		}
	}
	Color colorSelect(int row){
		Color co; 
		row = row/4;
		switch(row)
		{
			case 0: co =  Color.ORANGE; break;
			case 1: co =   Color.BLUE; break;
			case 2: co =   Color.MAGENTA; break;
			case 3: co =   Color.GREEN; break;
			case 4: co =   Color.RED; break;
			default: co =   Color.GRAY; break;
		}
		
		return co;
	}
	String letterSelect(int row){
		String str; 
		row = row/4;
		switch(row)
		{
			case 0: str =  "A"; break;
			case 1: str =  "B"; break;
			case 2: str =  "C"; break;
			case 3: str =  "D"; break;
			case 4: str =  "F"; break;
			default: str = "Error"; break;
		}
		
		return str;
	}
}