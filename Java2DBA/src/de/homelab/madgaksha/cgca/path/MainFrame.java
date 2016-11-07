package de.homelab.madgaksha.cgca.path;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	final static Logger LOG = Logger.getLogger("MyFrame"); //$NON-NLS-1$
	private final CustomCanvas canvas;
	private final JPanel panel;
	private final JList<IKeyFramedPoint> list;
	
	public MainFrame() {
		list = new JList<>();
		canvas = new CustomCanvas(list);
		panel = new ControlPanel(canvas);
		
		setMinimumSize(new Dimension(640, 480));
		setLayout(new BorderLayout());

		add(canvas, BorderLayout.CENTER);
		add(panel, BorderLayout.NORTH);
		add(list, BorderLayout.WEST);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public CustomCanvas getDrawingCanvas() {
		return canvas;
	}

	public static void main(final String[] args) {
		final JFrame frame = new MainFrame();
		frame.pack();
		frame.setVisible(true);
	}
}