package de.homelab.madgaksha.cgca.path;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

class TimePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private EditCanvas canvas;
	private final TimeList list;
	private final JButton btnAddTime;
	private final JButton btnSetTime;
	private ListSelectionListener selectionListener;
	private ActionListener addTimeActionListener;
	private ActionListener setTimeActionListener;

	public TimePanel() {
		list = new TimeList();

		final JPanel timeControlPanel = new JPanel();
		timeControlPanel.setLayout(new FlowLayout());
		btnAddTime = new JButton("Add time"); //$NON-NLS-1$
		btnSetTime = new JButton("Set time"); //$NON-NLS-1$

		timeControlPanel.add(btnAddTime);
		timeControlPanel.add(btnSetTime);

		setLayout(new BorderLayout(5,5));
		add(timeControlPanel, BorderLayout.NORTH);
		add(list, BorderLayout.CENTER);
	}

	public void setCanvas(final EditCanvas canvas) {
		this.canvas = canvas;
		setListeners();
	}

	private void setListeners() {
		if (selectionListener != null)
			list.removeListSelectionListener(selectionListener);
		if (addTimeActionListener != null)
			btnAddTime.removeActionListener(addTimeActionListener);
		if (setTimeActionListener != null)
			btnSetTime.removeActionListener(setTimeActionListener);

		addTimeActionListener = (final ActionEvent actionEvent) -> {
			canvas.addKeyFrame();
		};
		setTimeActionListener = (final ActionEvent actionEvent) -> {
			final String time = JOptionPane.showInputDialog(this, "Enter time"); //$NON-NLS-1$
			if (time == null)
				return;
			try {
				final float f = Float.parseFloat(time);
				if (f >= 0f)
					canvas.setTime(f);
			}
			catch (final NumberFormatException e) {
				e.printStackTrace();
			}
		};
		selectionListener = (final ListSelectionEvent event) -> {
			canvas.repaint();
		};
		btnAddTime.addActionListener(addTimeActionListener);
		btnSetTime.addActionListener(setTimeActionListener);
		list.addListSelectionListener(selectionListener);
	}

	public TimeList getList() {
		return list;
	}
}