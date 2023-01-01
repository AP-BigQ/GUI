/**
 * Image project 
 *
 */
import gui.*;

import java.awt.*;

import javax.swing.JFrame;

public class ImagePrj {	

	
	public static void main(String[] args)
	{
				
		EventQueue.invokeLater(new Runnable(){
			public void run(){
				
				new ImageFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);;
			}
		});

	}

}
