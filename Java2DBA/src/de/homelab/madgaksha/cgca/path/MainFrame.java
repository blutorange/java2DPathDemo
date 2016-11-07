package de.homelab.madgaksha.cgca.path;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	final static Logger LOG = Logger.getLogger("MyFrame"); //$NON-NLS-1$
	private final DrawingCanvas canvas;
	private final JPanel panel;
	private final TimeList list;
	
	public MainFrame() {
		list = new TimeList();
		canvas = new DrawingCanvas(list);
		panel = new ControlPanel(canvas, this);
	
		JPanel timePanel = new JPanel(new BorderLayout(5,5));
		JPanel timeControlPanel = new JPanel();
		timeControlPanel.setLayout(new FlowLayout());
		JButton btnAddTime = new JButton("Add time");
		JButton btnSetTime = new JButton("Set time");
		
		timeControlPanel.add(btnAddTime);
		timeControlPanel.add(btnSetTime);
		timePanel.add(timeControlPanel, BorderLayout.NORTH);
		timePanel.add(list, BorderLayout.CENTER);
		
		setMinimumSize(new Dimension(640, 480));
		setLayout(new BorderLayout(5,5));

		add(canvas, BorderLayout.CENTER);
		add(panel, BorderLayout.NORTH);
		add(timePanel, BorderLayout.WEST);
		
		btnAddTime.addActionListener((ActionEvent actionEvent) -> {
			canvas.addKeyFrame();
		});
		btnSetTime.addActionListener((ActionEvent actionEvent) -> {
			String time = JOptionPane.showInputDialog(this, "Enter time");
			if (time == null)
				return;
			try {
				float f = Float.parseFloat(time);
				if (f >= 0f)
					canvas.setTime(f);
			}
			catch (NumberFormatException e) {
				e.printStackTrace();
			}
		});
		list.addListSelectionListener((ListSelectionEvent event) -> {
			canvas.repaint();
		});
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public DrawingCanvas getDrawingCanvas() {
		return canvas;
	}

	public static void main(final String[] args) {
		final JFrame frame = new MainFrame();
		frame.pack();
		frame.setVisible(true);
	}	
}