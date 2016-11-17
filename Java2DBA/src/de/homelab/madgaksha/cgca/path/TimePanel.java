package de.homelab.madgaksha.cgca.path;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
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
	private final JButton btnRemoveTime;
	private final JComboBox<EInterpolation> selInter;
	private ListSelectionListener selectionListener;
	private ActionListener addTimeActionListener;
	private ActionListener setTimeActionListener;
	private ActionListener removeTimeActionListener;
	private ItemListener selectInterpolationListener;

	public TimePanel() {
		super();
		list = new TimeList();

		final JPanel timeControlPanel = new JPanel();

		timeControlPanel.setLayout(new GridBagLayout());

		final GridBagConstraints cons = new GridBagConstraints();
		cons.fill = GridBagConstraints.HORIZONTAL;
		cons.weightx = 1;
		cons.gridx = 0;

		btnAddTime = new JButton("Add time"); //$NON-NLS-1$
		btnSetTime = new JButton("Set time"); //$NON-NLS-1$
		btnRemoveTime = new JButton("Remove time"); //$NON-NLS-1$
		final EInterpolation[] ipolValues = EInterpolation.values();
		Arrays.sort(ipolValues,
				(final EInterpolation o1, final EInterpolation o2) -> o1.toString().compareTo(o2.toString()));
		selInter = new JComboBox<EInterpolation>(ipolValues);
		selInter.setSelectedItem(EInterpolation.POWER_HAT);

		setPreferredSize(new Dimension(200,-1));
		timeControlPanel.add(btnAddTime, cons);
		timeControlPanel.add(btnSetTime, cons);
		timeControlPanel.add(btnRemoveTime, cons);
		timeControlPanel.add(new JLabel("Interpolation"), cons); //$NON-NLS-1$
		timeControlPanel.add(selInter, cons);

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
		if (removeTimeActionListener != null)
			btnRemoveTime.removeActionListener(removeTimeActionListener);
		if (selectInterpolationListener != null)
			selInter.removeItemListener(selectInterpolationListener);

		addTimeActionListener = (final ActionEvent actionEvent) -> {
			canvas.addKeyFrame();
		};
		setTimeActionListener = (final ActionEvent actionEvent) -> {
			if (list.getModel().getSize()==0)
				return;
			final String time = JOptionPane.showInputDialog(this, "Enter time"); //$NON-NLS-1$
			if (time == null)
				return;
			try {
				final float f = Float.parseFloat(time);
				if (f >= 0f)
					canvas.setKeyFrame(f);
			}
			catch (final NumberFormatException e) {
				e.printStackTrace();
			}
		};
		removeTimeActionListener = (final ActionEvent actionEvent) -> {
			canvas.removeKeyFrame();
		};
		selectionListener = (final ListSelectionEvent event) -> {
			final IKeyFramedPoint sel = list.getSelectedValue();
			if (sel != null) {
				canvas.enterSelectMode(sel.getPathPoint());
				final EInterpolation ipol = sel.getInterpolation();
				if (ipol!= null) {
					selInter.setSelectedItem(ipol);
				}
			}
			canvas.repaint();
		};
		selectInterpolationListener = (final ItemEvent event) -> {
			SWUtil.cboxSelAs(event, EInterpolation.class).ifPresent(ipol -> {
				final IKeyFramedPoint sel = list.getSelectedValue();
				if (sel != null) {
					sel.setInterpolation(ipol);
				}
			});
		};
		btnAddTime.addActionListener(addTimeActionListener);
		btnSetTime.addActionListener(setTimeActionListener);
		btnRemoveTime.addActionListener(removeTimeActionListener);
		selInter.addItemListener(selectInterpolationListener);
		list.addListSelectionListener(selectionListener);
	}

	public TimeList getList() {
		return list;
	}
}