package jtsi.util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class LogWindow extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4337326436523162790L;
	private JTextArea textarea;

	public LogWindow() throws HeadlessException {
		super();
		this.setPreferredSize(new Dimension(500, 400));
		this.getContentPane().setLayout(new BorderLayout());
		
		textarea = new JTextArea();
		JScrollPane scrollpane = new JScrollPane(textarea);
		this.getContentPane().add(scrollpane, BorderLayout.CENTER);
		
		this.setResizable(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.pack();
		this.setVisible(true);
	}
	
	public void addText(String _text) {
		textarea.append(_text);
		textarea.setCaretPosition(textarea.getText().length());
		
	}
	
	public void setTitle(String s){
		this.setTitle(s);
	}
	public static void main(String[] args) {
		LogWindow w = new LogWindow();
		
		for(int i = 0; i<100;++i){
			w.addText(i+"LOLOLOLOL\n");
		}
	}
	
}
