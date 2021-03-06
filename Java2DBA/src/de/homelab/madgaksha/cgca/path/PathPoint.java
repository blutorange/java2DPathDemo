package de.homelab.madgaksha.cgca.path;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultListSelectionModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class PathPoint extends DefaultListSelectionModel implements IPathPoint {
	private static final long serialVersionUID = 1L;
	private final List<IKeyFramedPoint> list = new ArrayList<>();
	private final Set<ListDataListener> set = new HashSet<>();
	private final String label;
	private int selectedKeyFrame;
	public PathPoint(final Point p, final String label) {
		this((float)p.getX(), (float)p.getY(), label);
	}
	public PathPoint(final float x, final float y, final String label) {
		this.label = label;
		selectedKeyFrame = 0;
		list.add(new KeyFramedPoint(this, x,y,0f));
	}
	@Override
	public void setPoint(final float x, final float y) {
		list.get(selectedKeyFrame).set(x,y);
		notifyListenersChanged(selectedKeyFrame, selectedKeyFrame);
	}
	@Override
	public void setTime(final float time) {
		list.get(selectedKeyFrame).setTime(time);
		Collections.sort(list);
		notifyListenersChanged(0, list.size()-1);
	}
	@Override
	public String getLabel() {
		return label;
	}
	@Override
	public float getPointX() {
		return list.get(selectedKeyFrame).getX();
	}
	@Override
	public float getPointY() {
		return list.get(selectedKeyFrame).getY();
	}

	@Override
	public float getPointX(final float time) {
		final int i = getFrameIndexAt(time);
		if (i < 0)
			return list.get(0).getX();
		if (i >= list.size() -1 )
			return list.get(list.size()-1).getX();
		final IKeyFramedPoint p1 = list.get(i);
		final IKeyFramedPoint p2 = list.get(i+1);
		final float alpha = p1.getTime() == p2.getTime() ? 0.5f : (time-p1.getTime())/(p2.getTime()-p1.getTime());
		return p1.getX()+ p1.getInterpolation().apply(alpha)*(p2.getX()-p1.getX());
	}
	@Override
	public float getPointY(final float time) {
		final int i = getFrameIndexAt(time);
		if (i < 0)
			return list.get(0).getY();
		if (i >= list.size() -1 )
			return list.get(list.size()-1).getY();
		final IKeyFramedPoint p1 = list.get(i);
		final IKeyFramedPoint p2 = list.get(i+1);
		final float alpha = p1.getTime() == p2.getTime() ? 0.5f : (time-p1.getTime())/(p2.getTime()-p1.getTime());
		return p1.getY()+ p1.getInterpolation().apply(alpha)*(p2.getY()-p1.getY());
	}

	private int getFrameIndexAt(final float time) {
		for (int i = list.size(); i --> 0;) {
			if (time >= list.get(i).getTime())
				return i;
		}
		return -1;
	}

	@Override
	public String toString() {
		return list.isEmpty() ? "" : String.format("%d %s", selectedKeyFrame, list.get(selectedKeyFrame).toString());  //$NON-NLS-1$//$NON-NLS-2$
	}
	@Override
	public int getSize() {
		return list.size();
	}
	@Override
	public IKeyFramedPoint getElementAt(final int index) {
		return list.get(index >= list.size() ? list.size()-1 : index);
	}
	@Override
	public void addListDataListener(final ListDataListener l) {
		set.add(l);
	}
	@Override
	public void removeListDataListener(final ListDataListener l) {
		set.remove(l);
	}
	@Override
	public void setSelectionInterval(final int from, final int to) {
		super.setSelectionInterval(from, to);
		selectedKeyFrame  = from;
	}
	@Override
	public void addKeyFrame() {
		final float time = list.isEmpty() ? 0f : list.get(list.size()-1).getTime() + 1f;
		final float x = list.isEmpty() ? 0f : list.get(list.size()-1).getX();
		final float y = list.isEmpty() ? 0f : list.get(list.size()-1).getY();
		list.add(new KeyFramedPoint(this, x, y, time));
		notifyListenersAdded(list.size()-1, list.size()-1);
	}
	@Override
	public void removeKeyFrame() {
		if (selectedKeyFrame >= 0) {
			list.remove(selectedKeyFrame);
			notifyListenersRemoved(selectedKeyFrame, selectedKeyFrame);
		}
	}
	private void notifyListenersAdded(final int from, final int to) {
		final ListDataEvent event = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, from, to);
		for (final ListDataListener l : set)
			l.intervalAdded(event);
	}
	private void notifyListenersRemoved(final int from, final int to) {
		final ListDataEvent event = new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, from, to);
		for (final ListDataListener l : set)
			l.intervalRemoved(event);
	}
	private void notifyListenersChanged(final int from, final int to) {
		final ListDataEvent event = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, from, to);
		for (final ListDataListener l : set)
			l.contentsChanged(event);
	}

	@Override
	public Iterator<IKeyFramedPoint> getElementIterator() {
		return list.iterator();
	}

}