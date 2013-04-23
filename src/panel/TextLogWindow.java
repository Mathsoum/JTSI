package panel;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class TextLogWindow {

	
	JFrame frame = new JFrame("textLog");

	JTextArea textArea = new JTextArea();
	
	
	public TextLogWindow() {

		frame.getContentPane().setLayout(new BorderLayout());
		
		JScrollPane scrpane = new JScrollPane(textArea);
		
		frame.add(scrpane,BorderLayout.CENTER);
		
		frame.setPreferredSize(new Dimension(300,300));
		frame.pack();
		
		frame.setVisible(true);
	}
	
	
	public void print(String s){
		textArea.append(s);
	}
	
	public void println(String s){
		textArea.append(s+"\n");
	}
	
	
	public static void main(String[] args) {
		TextLogWindow tlw = new TextLogWindow();
		
		for(int i = 0 ; i<100; ++i){
			tlw.println("hello TextLogWindow "+i);
		}
	}
	
	
}
