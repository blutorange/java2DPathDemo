package de.homelab.madgaksha.cgca.path;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.logging.Logger;

import javax.swing.JFrame;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	final static Logger LOG = Logger.getLogger("MyFrame"); //$NON-NLS-1$
	private final EditCanvas canvas;
	private final DrawControlPanel controlPanel;
	private final TimePanel timePanel;

	public MainFrame() {
		timePanel = new TimePanel();
		canvas = new EditCanvas(timePanel.getList());
		controlPanel = new DrawControlPanel(canvas, this);
		timePanel.setCanvas(canvas);

		setMinimumSize(new Dimension(640, 480));
		setLayout(new BorderLayout(5,5));

		add(canvas, BorderLayout.CENTER);
		add(controlPanel, BorderLayout.NORTH);
		add(timePanel, BorderLayout.WEST);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public EditCanvas getDrawingCanvas() {
		return canvas;
	}

	public static void main(final String[] args) {
		final JFrame frame = new MainFrame();
		frame.pack();
		frame.setVisible(true);
	}
}