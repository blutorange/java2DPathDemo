package de.homelab.madgaksha.cgca.path;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

class TimeList extends JList<IKeyFramedPoint> {
	private static final long serialVersionUID = 1L;
	private IPathPoint selectedPoint;
	public TimeList() {
		super(DummyModel.INSTANCE);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	public void forPathPoint(IPathPoint selectedPoint) {
		if (selectedPoint == null)
			throw new NullPointerException("point must not be null");
		if (getModel() != selectedPoint)
			setModel(selectedPoint);
		if (getSelectionModel() != selectedPoint)
			setSelectionModel(selectedPoint);
		this.selectedPoint = selectedPoint;
	}
	public void forNothing() {
			setModel(DummyModel.INSTANCE);
			setSelectionModel(new DefaultListSelectionModel());
	}
	/**
	 * @return the selectedPoint
	 */
	public IPathPoint getSelectedPoint() {
		return selectedPoint;
	}	
}