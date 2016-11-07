package de.homelab.madgaksha.cgca.path;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

class AnimControlPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	final JTextField tfRepeat;
	final JCheckBox cbForthBack;
	final AnimCanvas animCanvas;
	public AnimControlPanel(final AnimCanvas animCanvas) {
		this.animCanvas = animCanvas;
		cbForthBack = new JCheckBox("forthAndBack"); //$NON-NLS-1$
		tfRepeat = new JTextField("1"); //$NON-NLS-1$

		setLayout(new FlowLayout());
		add(new JLabel("Repeat time")); //$NON-NLS-1$
		add(tfRepeat);
		add(cbForthBack);

		tfRepeat.addActionListener((final ActionEvent ev) ->  {
			setRepeatTime();
		});
		tfRepeat.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(final FocusEvent e) {
				setRepeatTime();
			}
			@Override
			public void focusGained(final FocusEvent e) {
			}
		});

		cbForthBack.addActionListener((final ActionEvent event) -> {
			SWUtil.srcAs(event, JCheckBox.class).ifPresent(cb -> animCanvas.setForthAndBack(cb.isSelected()));
		});

		animCanvas.setForthAndBack(cbForthBack.isSelected());
		setRepeatTime();
	}
	protected void setRepeatTime() {
		try {
			final String text = tfRepeat.getText();
			if (text != null)
				animCanvas.setRepeat(Float.parseFloat(text));
		}
		catch (final NumberFormatException ex) {
			ex.printStackTrace();
		}
	}
}