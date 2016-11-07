package de.homelab.madgaksha.cgca.path;

import javax.swing.JList;
import javax.swing.ListSelectionModel;

class TimeList extends JList<IKeyFramedPoint> {
	private static final long serialVersionUID = 1L;
	public TimeList() {
		super(DummyModel.INSTANCE);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
}