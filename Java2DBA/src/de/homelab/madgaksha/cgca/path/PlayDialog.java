package de.homelab.madgaksha.cgca.path;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PlayDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private final AnimCanvas animCanvas;
	private JTextField tfRepeat;
	public PlayDialog(MainFrame frame, DrawingCanvas canvas) {
		super(frame, true);
		setLayout(new BorderLayout(5,5));
		
		animCanvas = canvas.getAnimationCanvas();
		JPanel controlPanel = new JPanel();
		JCheckBox cbForthBack = new JCheckBox("forthAndBack");
		tfRepeat = new JTextField("1");
		
		controlPanel.setLayout(new FlowLayout());
		controlPanel.add(new JLabel("Repeat time"));
		controlPanel.add(tfRepeat);
		controlPanel.add(cbForthBack);
		add(controlPanel, BorderLayout.NORTH);
		add(animCanvas, BorderLayout.CENTER);
			
		setMinimumSize(new Dimension(640, 480));
		pack();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		tfRepeat.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				setRepeatTime();
			}			
			@Override
			public void focusGained(FocusEvent e) {
			}
		});
		tfRepeat.addActionListener((ActionEvent ev) ->  {
			setRepeatTime();
		});
		cbForthBack.addActionListener((ActionEvent ev) -> {
			Object s = ev.getSource();
			if (s instanceof JCheckBox) {
				animCanvas.setForthAndBack(((JCheckBox)s).isSelected());
			}
		});
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				animCanvas.startAnim();
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				animCanvas.stopAnim();
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				animCanvas.startAnim();
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				animCanvas.stopAnim();
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				animCanvas.stopAnim();
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				animCanvas.stopAnim();				
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				animCanvas.startAnim();
			}
		});
		animCanvas.setForthAndBack(cbForthBack.isSelected());
		setRepeatTime();
	}
	protected void setRepeatTime() {
		try {
			String text = tfRepeat.getText();
			if (text != null)
				animCanvas.setRepeat(Float.parseFloat(text));
		}
		catch (NumberFormatException ex) {
			ex.printStackTrace();
		}
	}
}