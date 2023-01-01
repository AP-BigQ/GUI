import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;


public class EvaluateDialog extends JDialog implements ActionListener
{
	
	JTabbedPane tabpane;
	JPanel[] studnentPanels;
	JTable[] tables;
	JScrollPane[] scrollPanes; 
	JPanel buttonPanel;
	JButton selfCompButton, peerCompButton, standardCompButton;
	
	ArrayList<Student> students;
	
	Dimension screenSize;
	int centerX, centerY;
	
	final static String[] columnNames = {"Course Field",    
										"Course Name",    
										"Credits",
										"Earned Grade",
										"Self-Evaluated ",  
										"Pass-Time ", 
										"Interest",
										"Confidence",
										"Comprehensive"};
	
	public EvaluateDialog() {}
	public EvaluateDialog(Container parent, ArrayList<Student> students, int studentNum) {
	
		this.students = students;
		studnentPanels = new JPanel[studentNum];
		tables = new JTable[studentNum];
		scrollPanes = new JScrollPane[studentNum];
		
		buttonPanel = new JPanel();
		selfCompButton = new JButton("Self Evaluate");
		peerCompButton = new JButton("Group Evaluate");
		standardCompButton = new JButton("Standard Comparison");
		selfCompButton.addActionListener(this);
		peerCompButton.addActionListener(this);
		standardCompButton.addActionListener(this);

		
		for(int i=0; i <studentNum; i++ ){
			Student stu = students.get(i);
			for(int j=0; j < stu.courses.size(); j++){
				Object[][] courseInfo = new Object[stu.courses.size()][columnNames.length];
				
				for(int row=0; row<stu.courses.size(); row++){
					for(int col=0; col < columnNames.length; col++){
						Course crs = stu.courses.get(row);
						courseInfo[row][col] = courseComps(crs, col);
					}
				}
		        tables[i] = new JTable(courseInfo, columnNames);
			        tables[i].setPreferredScrollableViewportSize(new Dimension(850, 500));
			        tables[i].setFillsViewportHeight(true);
			        
			    JTableUtilities.setCellsAlignment(tables[i], SwingConstants.CENTER, Color.lightGray);
			}
		}
            
		tabpane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT );
		
		for(int i=0; i<studentNum; i++){			
			studnentPanels[i] = new JPanel();
			scrollPanes[i] = new JScrollPane(tables[i]);
			studnentPanels[i].add(scrollPanes[i]);
			tabpane.addTab(students.get(i).name, studnentPanels[i]);
		}
		
		this.add(tabpane, BorderLayout.CENTER);		
		this.add(buttonPanel, BorderLayout.SOUTH);		
		buttonPanel.add(selfCompButton);
		buttonPanel.add(peerCompButton);
		buttonPanel.add(standardCompButton);
		

		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		centerX = screenSize.width / 6;
		centerY = screenSize.height /6;
		setLocation(centerX, centerY);
		
	    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	    pack();
	    //this.setResizable(false);
	    this.setVisible(true);
	}
	
	String courseComps(Course crs, int column){
		String str = null;
		switch(column)
		{
			case 0: str = crs.getFieldName(crs.getField()); break;
			case 1: str = crs.getCourseName(); break;
			case 2: str = new Integer(crs.getCredits()).toString(); break;
			case 3: str = new Double(crs.getGradeObj()).toString(); break;
			case 4: str = new Double(crs.getGradeSubj()).toString(); break;
			case 5: str = new Integer(crs.getPasstime()).toString(); break;
			case 6: str = new Integer(crs.getSubjInterest()).toString(); break;
			case 7: str = new Integer(crs.getSubjFactor()).toString(); break;
			//case 8: str = new Double(crs.gradeCalculation()).toString(); break;
			case 8: str = String.format("%4.1f", crs.gradeCalculation()); break;
			default: break;
		}
		
		return str;
	}
	public void actionPerformed(ActionEvent event){
		if(selfCompButton == event.getSource()){

			int currentIndex = tabpane.getSelectedIndex();
			double[] meanGrades = students.get(currentIndex).mean();
			EvaluateChart selfChart = new EvaluateChart(meanGrades);
			selfChart.setVisible(true);
/*			
			for(int i=0; i<meanGrades.length; i++){
				System.out.println(meanGrades[i]);
			}*/
		}
		if(peerCompButton == event.getSource()){
			
		}
		if(standardCompButton == event.getSource()){
			
		}

	}
}
