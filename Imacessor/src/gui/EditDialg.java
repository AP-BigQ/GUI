package gui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Scrollbar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTextField;

public class EditDialg extends JDialog{

	JTextField text;
	int textValue;
	JScrollBar scbar;
	JCheckBox checkAvgColor;
	static boolean fakeColor = true;
	
	public EditDialg(JFrame parent, String title, int kValue) {
		
		super(parent, title, true);
		textValue = kValue;
		
		if(parent != null){
			Dimension parentSize = parent.getSize(); 
		      Point p = parent.getLocation(); 
		      setLocation(p.x + parentSize.width / 3, p.y + parentSize.height /3);
		}
		
		JPanel messagePane = new JPanel();
		messagePane.add(new JLabel("Cluster Number: "));
		messagePane.add(text = new JTextField());
		text.setText(new Integer(kValue).toString());
		text.setPreferredSize(new Dimension(30, 20));
		
		text.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				textValue = Integer.parseInt(text.getText());
			}
		});
		
		messagePane.add(scbar = new JScrollBar(JScrollBar.VERTICAL, kValue, 1, 2, 15));
		scbar.setPreferredSize(new Dimension(20, 80));
		scbar.addAdjustmentListener(new AdjustmentListener(){
			public void adjustmentValueChanged(AdjustmentEvent event) {
				
				int val = event.getValue();
				text.setText(new Integer(val).toString());
			}
			
		});

		messagePane.add(checkAvgColor = new JCheckBox("Fake Color"));
		checkAvgColor.setSelected(true);
		fakeColor = true;
		checkAvgColor.addItemListener(new ItemListener(){

			public void itemStateChanged(ItemEvent event) {

				if(event.getStateChange() == ItemEvent.DESELECTED)
					fakeColor = false;			
				else {
					checkAvgColor.setSelected(true);
					fakeColor = true;
				}
					
			}
			
		});
		
		getContentPane().add(messagePane);
		    JPanel buttonPane = new JPanel();

		    JButton buttonApply = new JButton("Apply"); 
		    JButton buttonOK = new JButton("OK"); 
		    buttonPane.add(buttonApply); 
		    buttonPane.add(buttonOK); 
		    
		    buttonApply.addActionListener(new ActionListener(){
		    	  public void actionPerformed(ActionEvent e) {
		    		  textValue = Integer.parseInt(text.getText());
		    		  }
		    });
		    
		    buttonOK.addActionListener(new ActionListener(){
		    	  public void actionPerformed(ActionEvent e) {
		    		  textValue = Integer.parseInt(text.getText());
		    		    setVisible(false); 
		    		    dispose(); 
		    		  }
		    });
		    
		    getContentPane().add(buttonPane, BorderLayout.SOUTH);
		    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		    pack();
		    setVisible(true);
		
	}

	public int getTextValue(){
		return textValue;
	}
	public class k_color{
		public int kValue;
		public boolean avgColor;
		
		public k_color(){
			kValue = getTextValue();
			avgColor = fakeColor;
		}
	}

}
