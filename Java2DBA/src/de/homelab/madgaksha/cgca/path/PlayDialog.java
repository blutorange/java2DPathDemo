package de.homelab.madgaksha.cgca.path;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JPanel;

public class PlayDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private final AnimCanvas animCanvas;
	public PlayDialog(final MainFrame frame, final EditCanvas canvas) {
		super(frame, true);
		setLayout(new BorderLayout(5,5));

		animCanvas = canvas.getAnimationCanvas();
		final JPanel controlPanel = new AnimControlPanel(animCanvas);

		add(controlPanel, BorderLayout.NORTH);
		add(animCanvas, BorderLayout.CENTER);

		setMinimumSize(new Dimension(640, 480));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(final WindowEvent e) {
				animCanvas.startAnim();
			}
			@Override
			public void windowIconified(final WindowEvent e) {
				animCanvas.stopAnim();
			}
			@Override
			public void windowDeiconified(final WindowEvent e) {
				animCanvas.startAnim();
			}
			@Override
			public void windowDeactivated(final WindowEvent e) {
				animCanvas.stopAnim();
			}
			@Override
			public void windowClosing(final WindowEvent e) {
				animCanvas.stopAnim();
			}
			@Override
			public void windowClosed(final WindowEvent e) {
				animCanvas.stopAnim();
			}
			@Override
			public void windowActivated(final WindowEvent e) {
				animCanvas.startAnim();
			}
		});
	}
}