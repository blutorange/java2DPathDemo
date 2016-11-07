package de.homelab.madgaksha.cgca.path;

import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

public enum DummyModel implements ListModel<IKeyFramedPoint> {
	INSTANCE;

	@Override
	public int getSize() {
		return 0;
	}

	@Override
	public IKeyFramedPoint getElementAt(int index) {
		throw new ArrayIndexOutOfBoundsException();
	}

	@Override
	public void addListDataListener(ListDataListener l) {
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
	}
}
