package jtsi.util.subtype1;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class Window extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5174522157401439944L;
	private JTextArea textarea;
	private JScrollPane scrollpane;
	public boolean notReady = true;
	public boolean playMode = false;
	private JPanel buttonsPanel;
	private JButton startButton;
	private JButton pauseButton;
	private JButton stepButton;
	public JLabel timestep;

	public Window() {
		this.setPreferredSize(new Dimension(500, 400));
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(scrollpane(), BorderLayout.CENTER);
		this.getContentPane().add(buttonsPanel(), BorderLayout.NORTH);
		this.setResizable(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.addWindowListener(new WindowAdapter(){

			@Override
			public void windowClosing(WindowEvent arg0) {
				super.windowClosing(arg0);
				playMode = true;
			}
			
		});
		this.pack();
		this.setVisible(true);
	}
	public void addText(String _text) {
		textarea().append(_text+"\n");
		textarea().setCaretPosition(textarea().getText().length());
		
	}
	public JLabel timestep() {
		if (timestep == null) {
			timestep = new JLabel();
		}
		return timestep;
	}
	public JPanel buttonsPanel() {
		if (buttonsPanel == null) {
			buttonsPanel = new JPanel();
			buttonsPanel.add(startButton());
			buttonsPanel.add(pauseButton());
			buttonsPanel.add(stepButton());
			buttonsPanel.add(timestep());
		}
		return buttonsPanel;
	}
	public JTextArea textarea() {
		if (textarea == null) {
			textarea = new JTextArea();
		}
		return textarea;
	}
	public JScrollPane scrollpane() {
		if (scrollpane == null) {
			scrollpane = new JScrollPane(textarea());
			scrollpane.setMinimumSize(new Dimension(100,0));
		}
		return scrollpane;
	}
	public JButton stepButton() {
		if (stepButton == null) {
			stepButton = new JButton("next step");
			stepButton.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					notReady = false;
					notify();
				}

			});
		}
		return stepButton;
	}
	public JButton startButton() {
		if (startButton == null) {
			startButton = new JButton("run");
			startButton.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					playMode = true;
					notify();
				}

			});
		}
		return startButton;
	}
	public JButton pauseButton() {
		if (pauseButton == null) {
			pauseButton = new JButton("pause");
			pauseButton.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					playMode = false;
				}

			});
		}
		return pauseButton;
	}
}
