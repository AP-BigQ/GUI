import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;



public class GradeSystem extends JFrame{

	public static void main(String[] args){
		EventQueue.invokeLater(new Runnable(){
			public void run(){				
				new GradeSystem().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
				try{
					Scanner in = new Scanner(new FileReader("coursefield0.txt"));
					Course[] MajorCourses = readData(in);
					in.close();
					
					for(int i=0; i < MajorCourses.length; i++){
						System.out.print(MajorCourses[i].coursename);
						System.out.print("|");
						System.out.println(MajorCourses[i].credits);
					}
					
/*					Course[] tmp = fillComboBox(0);
					
					for(int i=0; i < tmp.length; i++){
						System.out.print(tmp[i].coursename);
						System.out.print("|");
						System.out.println(tmp[i].credits);
					}*/
				}
				catch(IOException e){e.printStackTrace();}				
				
			}
		});

	}
/*	static Course[] fillComboBox(int index){
		
		StringBuilder strbld = new StringBuilder("coursefield");
		strbld.append(index);
		strbld.append(".txt");
		
		try{
			Scanner in = new Scanner(new FileReader(strbld.toString()));
			Course[] courses = readData(in);
			in.close();
			return courses;
		}
		catch(IOException e){e.printStackTrace();}			
		
		return null;
	}*/
	
static Course[] readData(Scanner in){
		int n = in.nextInt();
		in.nextLine();
		
		Course[] courses = new Course[n];
		for(int i=0; i<n; i++){
			courses[i] = new Course();
			courses[i].readData(in);
		}
		return courses;
	}
	
	final static int Width = 900;
	final static int Height = 750;
	Dimension screenSize;
	int centerX, centerY;
	
	JMenuBar menuBar;
	JMenu fileMenu, aboutMenu;
	
	JList studentsList;
	DefaultListModel listModel;
	JCoursePanel coursePanel;
	
	ArrayList<Student> students = new ArrayList<Student>();

	public GradeSystem() {
		super();
		setTitle("GradeSystem");
		setSize(Width, Height);

		init();
	
	}
	void init(){
		Container contentPane = this.getContentPane();

		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int w = getWidth();
		int h = getHeight();
		centerX = screenSize.width / 2 - w / 2;
		centerY = screenSize.height / 2 - h / 2;
		setLocation(centerX, centerY);
		
		coursePanel = new JCoursePanel();
	    
		listModel = new DefaultListModel();

		studentsList = new JList(listModel);
		studentsList.setBorder(BorderFactory.createEtchedBorder());
		studentsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		studentsList.setSelectedIndex(0);
		studentsList.setVisible(false);
		
		studentsList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				//change2SelectedFile();
			}
		});

		studentsList.setVisibleRowCount(50);
		studentsList.setBackground(new Color(240, 255, 255));
		studentsList.setPreferredSize(new Dimension(w / 6, h));
		
		contentPane.add(studentsList, BorderLayout.LINE_START);

		contentPane.add(coursePanel, BorderLayout.CENTER);
		coursePanel.setVisible(false);
		
		
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		fileMenu = new JMenu("File");
		menuBar.add(fileMenu);

		JMenuItem newStudentItem = new JMenuItem("New Student");
		newStudentItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				
				JTextField studentName = new JTextField();
				JTextField studentID = new JTextField();
				Object[] message = {
				    "Student Name:", studentName,
				    "Student ID:", studentID
				};

				int option = JOptionPane.showConfirmDialog(null, message, "New Student", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
				    if (studentName.getText().trim().length()  != 0 ) {
				    	//&& studentID.getText() != null
				        System.out.println("New Student: "+studentName.getText().trim());
				        System.out.println("New Student ID: "+studentID.getText().trim());
				        
				        studentsList.setVisible(true);
				        coursePanel.setVisible(true);
				        listModel.addElement(studentName.getText());
				        studentsList.setSelectedIndex(listModel.getSize() - 1);
				        coursePanel.logText.append("\n"+ "Student: "+ (String) studentsList.getSelectedValue());
				        students.add(new Student(studentName.getText()));
				    } else {
				        System.out.println("Create failed");
				    }
				} else {
				    System.out.println("Canceled");
				}
			}
		});
		fileMenu.add(newStudentItem);
		
		
		JMenuItem loadStudentItem = new JMenuItem("Load");
		loadStudentItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				//loadImage();
			}
		});
		fileMenu.add(loadStudentItem);
		
		JMenuItem saveStudentItem = new JMenuItem("Save");
		saveStudentItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				//saveImage();
			}
		});
		fileMenu.add(saveStudentItem);
		
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});
		fileMenu.add(exitItem);
		
		
		// About
		aboutMenu = new JMenu("Help");
		JMenuItem AItem = new JMenuItem("About");
		aboutMenu.add(AItem);

		AItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				popupAbout();
			}
		});
		menuBar.add(aboutMenu);
		
		setVisible(true);
		// pack();

		repaint();
	}
	
	void popupAbout() {
		JDialog diag = new JDialog(this, "About", true);

		diag.setSize(180, 100);
		diag.setLocationRelativeTo(this);
		diag.add(new JLabel("  Grade System 2016"), BorderLayout.CENTER);
		Image img = Toolkit.getDefaultToolkit().getImage("map.gif");
		diag.setIconImage(img);
		diag.setResizable(false);
		diag.setVisible(true);
	}
	public class JCoursePanel extends JPanel
							  implements ItemListener, ActionListener
	{
/*		JCheckBox majorCheck, sciCheck, techCheck, artCheck, libCheck;
		JComboBox majorCombo, sciCombo, techCombo, artCombo, libCombo;
		JLabel creditsLabel, passtimeLabel, gradeLabel, interestLabel, selfevaluateLabel, confidenceLabel;
		JComboBox creditsCombo, passtimeCombo, gradeCombo, interestCombo, selfevaluateCombo, confidenceCombo;*/
		
		JCheckBox[] checkBoxes = new JCheckBox[5];
		JComboBox[] comboBoxes = new JComboBox[11];
		JLabel[] labels = new JLabel[11];
		JTextArea logText = new JTextArea();
		JScrollPane logScrPane = new JScrollPane(logText);
		JLabel note = new JLabel();

		JButton applyButton = new JButton("Apply");
		int appliedCourseNum = 0;
		JButton evaluateButton = new JButton("Confirm&Evaluate");
		ComboboxCellRenderer[] renderer = new ComboboxCellRenderer[5];
		
		String[] coursefields = new String[]{"Major: ", "Science: ", "Technology:  ", "Art: ", "Liberal: "};
		String[] courseProperties = new String[]{"     Credits: ", "     Grade: ", "     Self-Evaluate: ", 
												 "     Pass-Time: ", "     Interest: ", "     Confidence: "};
		
		public JCoursePanel(){		
			
			GridBagLayout layout = new GridBagLayout();
			setLayout(layout);
			
			for(int i=0; i<checkBoxes.length; i++){
				checkBoxes[i] = new JCheckBox(coursefields[i]);		
				checkBoxes[i].addItemListener(this);
			}
			
			for(int i=0; i<labels.length; i++){
				if(i<=4) {labels[i] = null; continue;}
				labels[i] = new JLabel(courseProperties[i-5]);				
			}
			
			for(int i=0; i<comboBoxes.length; i++){
				if(i<5) {
					
					Course[] crs = fillComboBox(i);
					String[] str = new String[crs.length];
					for(int j=0; j < crs.length; j++){
						str[j] = crs[j].coursename;
					}				
					comboBoxes[i] = new JComboBox(str);
					
					comboBoxes[i].setEnabled(false);
					}
				else if(i==5){
					comboBoxes[i] = new JComboBox(new String[]{"1", "2", "3", "4", "5"});
				}
				else if(i == 6){
					comboBoxes[i] = new JComboBox(new String[]{"A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D"});
				}	
				else if(i == 7){
					comboBoxes[i] = new JComboBox(new String[]{"A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D"});
				}	
				else if(i == 8){
					comboBoxes[i] = new JComboBox(new String[]{"1", "2", ">=3"});
				}	
				else if(i==9){
					comboBoxes[i] = new JComboBox(new String[]{"5", "4", "3", "2", "1"});
				}
				else if(i==10){
					comboBoxes[i] = new JComboBox(new String[]{"5", "4", "3", "2", "1"});
				}			
				
			}
			for(int i=0; i<comboBoxes.length; i++){
				if(i<5) { comboBoxes[i].setPreferredSize(new Dimension(180, 20));}
				else {comboBoxes[i].setPreferredSize(new Dimension(40, 20));}
			}
		
			note.setText("Note: Red---Had Been Selected; Gray---Not Yet.");
			note.setOpaque(true);
			//note.setBackground(new Color(240, 255, 255));
			
			logText.setText("Coures Information");
			logText.setEditable(false);
			logText.setLineWrap(true);
			logText.setBorder(BorderFactory.createEtchedBorder());
			
			
			add(checkBoxes[0], new GBC(0,0).setAnchor(GBC.WEST));
			add(checkBoxes[1], new GBC(0,1).setAnchor(GBC.WEST));
			add(checkBoxes[2], new GBC(0,2).setAnchor(GBC.WEST));
			add(checkBoxes[3], new GBC(0,3).setAnchor(GBC.WEST));
			add(checkBoxes[4], new GBC(0,4).setAnchor(GBC.WEST));
			
			add(comboBoxes[0], new GBC(1,0).setAnchor(GBC.EAST).setInsets(2));
			add(comboBoxes[1], new GBC(1,1).setAnchor(GBC.EAST).setInsets(2));
			add(comboBoxes[2], new GBC(1,2).setAnchor(GBC.EAST).setInsets(2));
			add(comboBoxes[3], new GBC(1,3).setAnchor(GBC.EAST).setInsets(2));
			add(comboBoxes[4], new GBC(1,4).setAnchor(GBC.EAST).setInsets(2));

			add(labels[5], new GBC(2,0).setAnchor(GBC.WEST));
			add(labels[6], new GBC(2,1).setAnchor(GBC.WEST));
			add(labels[7], new GBC(2,2).setAnchor(GBC.WEST));

			
			add(comboBoxes[5], new GBC(3,0).setAnchor(GBC.WEST));
			add(comboBoxes[6], new GBC(3,1).setAnchor(GBC.WEST));
			add(comboBoxes[7], new GBC(3,2).setAnchor(GBC.WEST));

			
			add(labels[8], new GBC(4,0).setAnchor(GBC.WEST));
			add(labels[9], new GBC(4,1).setAnchor(GBC.WEST));
			add(labels[10], new GBC(4,2).setAnchor(GBC.WEST));
			
			add(comboBoxes[8], new GBC(5,0).setAnchor(GBC.WEST));
			add(comboBoxes[9], new GBC(5,1).setAnchor(GBC.WEST));
			add(comboBoxes[10], new GBC(5,2).setAnchor(GBC.WEST));
			
			add(applyButton, new GBC(2,4,2,1).setAnchor(GBC.CENTER).setFill(GBC.VERTICAL));
			add(evaluateButton, new GBC(4,4,2,1).setAnchor(GBC.CENTER).setFill(GBC.VERTICAL));
			
			add(note, new GBC(0,5,6,1).setFill(GBC.BOTH).setWeight(0, 0).setInsets(1));
			//add(logText, new GBC(0,6,6,4).setFill(GBC.BOTH).setWeight(50, 50).setInsets(3));			
			add(logScrPane, new GBC(0,6,6,4).setFill(GBC.BOTH).setWeight(50, 50).setInsets(3));
			
			
			//Initializing & ActionListener
			checkBoxes[0].setSelected(true);			
			comboBoxes[0].setEnabled(true);
			comboBoxes[5].setSelectedIndex(2);
			applyButton.addActionListener(this);
			evaluateButton.addActionListener(this);
			
			
			Course[] crs = fillComboBox(0);
			String[] str = new String[crs.length];
			for(int j=0; j < crs.length; j++){
				str[j] = crs[j].coursename;
			}
				
			for(int i=0; i<checkBoxes.length; i++){
				renderer[i] = new ComboboxCellRenderer(comboBoxes[i]);
				comboBoxes[i].setRenderer(renderer[i]);
				comboBoxes[i].setEditable(true);
			}
/*			renderer[0] = new ComboboxCellRenderer(comboBoxes[0]);
			comboBoxes[0].setRenderer(renderer[0]);			
			comboBoxes[0].setEditable(true);*/
			

		}
				
	Course[] fillComboBox(int index){
			
			StringBuilder strbld = new StringBuilder("coursefield");
			strbld.append(index);
			strbld.append(".txt");
			
			try{
				Scanner in = new Scanner(new FileReader(strbld.toString()));
				Course[] courses = readData(in);
				in.close();
				return courses;
			}
			catch(IOException e){e.printStackTrace();}			
			
			return null;
		}
		
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			Graphics2D g2 = (Graphics2D) g;

		}

		@Override
		public void actionPerformed(ActionEvent event) {
			
			if(applyButton == event.getSource()){
				int studentIndex = studentsList.getSelectedIndex();
				
				for(int i=0; i<checkBoxes.length; i++){
					if(checkBoxes[i].isSelected()){						
						
						renderer[i].setColor(comboBoxes[i].getSelectedIndex());
						appliedCourseNum++;
						log(studentIndex, i);						

					}
				}			
			}		
			
			if(evaluateButton == event.getSource()){
				if(appliedCourseNum < 2){
					//System.out.print("You chose " + appliedCourseNum+ " courses, more please\n");
					JOptionPane.showMessageDialog(this, "You chose " + appliedCourseNum+ " courses, more please", 
							"Error", JOptionPane.ERROR_MESSAGE);
				}
				else{
					for(int i=0; i<students.size(); i++){
						for(int j=0; j < students.get(i).courses.size(); j++){
							double totalgrade = students.get(i).courses.get(j).gradeCalculation();
							System.out.println(totalgrade);
						}
					}
					
					evaluateGrade(students);
				}
			}
		}
		void evaluateGrade(ArrayList<Student> students) {
			EvaluateDialog ediag = new EvaluateDialog(this.getParent(), students, students.size()) ;

		}
		void log(int studentIndex, int coursefield){
			String coursename = comboBoxes[coursefield].getSelectedItem().toString();
			
			int credits = Integer.parseInt(comboBoxes[5].getSelectedItem().toString());
			String grade = comboBoxes[6].getSelectedItem().toString();
			String selfEvaluate = comboBoxes[7].getSelectedItem().toString();
			String passTime = comboBoxes[8].getSelectedItem().toString();
			int interests = Integer.parseInt(comboBoxes[9].getSelectedItem().toString());
			int confidence = Integer.parseInt(comboBoxes[10].getSelectedItem().toString());
			
			logText.append("\n");	
			logText.append(coursename);
			logText.append("-->");
			logText.append("Credits: "+ credits + "   ");
			logText.append("Grade: "+ grade + "   ");
			logText.append("Self_Evaluate: "+ selfEvaluate + "   ");
			logText.append("Pass-Time: "+ passTime + "   ");
			logText.append("Interests: "+ interests + "   ");
			logText.append("Confidence: "+ confidence + "   ");
			
			
			Course crs = new Course(coursefield, coursename, credits, grade, selfEvaluate, passTime, interests, confidence);
			students.get(studentIndex).addCourse(crs);
		}
		@Override
		public void itemStateChanged(ItemEvent event) {
						
			if(event.getStateChange() == ItemEvent.SELECTED){
				for(int i=0; i<checkBoxes.length; i++){
					if(checkBoxes[i] == event.getSource()){
						checkBoxes[i].setSelected(true);
						lightComboBoxes(i);
					}
					else {
						checkBoxes[i].setSelected(false);
						greyComboBoxes(i);
					}
				}
				
			}							
			
		}
		
		void greyComboBoxes(int index){
			comboBoxes[index].setEnabled(false);
		}
		void lightComboBoxes(int index){
			comboBoxes[index].setEnabled(true);
		}
		
		class ComboboxCellRenderer extends JPanel implements ListCellRenderer{
			
			int selectedIndex = -1;
			Color[] colors;
			JPanel textPanel;
			JLabel textLabel;
			
			public ComboboxCellRenderer(JComboBox combo){
				textPanel = new JPanel();
		        textPanel.add(this);
		        textLabel = new JLabel();
		        textLabel.setOpaque(true);
		        textPanel.add(textLabel);
		        
		        colors = new Color[combo.getItemCount()];
		        for(int i=0; i < colors.length; i++)
		        {
		        	colors[i] = Color.GRAY;
		        }
			}

			void setColor(int index){
				selectedIndex = index;
				colors[index] = Color.RED;
			}
			public Component getListCellRendererComponent(JList list, 
															Object value, 
															int index,
															boolean isSelected, 
															boolean cellHasFocus)
			{		
		        		        
				if(isSelected){
					setBackground(list.getSelectionBackground()); //(163,184,204)	
					//System.out.print(list.getSelectionBackground());
				}
				else {
					setBackground(Color.WHITE);
				}
				
				textLabel.setBackground(getBackground());
				textLabel.setText(value.toString());
				  if (index>-1 && selectedIndex  != -1) {
					  textLabel.setForeground(colors[index]);
			        }

				return textLabel;
			}
		} 
	}

}
