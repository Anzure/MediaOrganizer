package net.hydrotekz.PlexFC.graphics;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import net.hydrotekz.PlexFC.PlexFC;

public class CriticalError extends JFrame {

	private static final long serialVersionUID = 2452233478210697897L;

	private JButton okay = new JButton("OK");
	public static JLabel title = new JLabel("");
	public static JLabel message = new JLabel("");

	public CriticalError(){
		super("PlexFC v" + PlexFC.version);
		setLayout(new FlowLayout());

		add(title);
		
		okay.setToolTipText("Close application");
		add(okay);
		
		add(message);

		HandlerClass handler = new HandlerClass();
		okay.addActionListener(handler);
	}
	
	private class HandlerClass implements ActionListener {
		public void actionPerformed(ActionEvent e){
			System.exit(0);
		}
	}
}