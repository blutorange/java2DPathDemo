package de.homelab.madgaksha.cgca.path;
import java.util.Iterator;

import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

public interface IPathPoint extends ListModel<IKeyFramedPoint>, ListSelectionModel {
	public String getLabel();
	public float getPointX();
	public float getPointY();
	public float getPointX(float time);
	public float getPointY(float time);
	public void setPoint(float x, float y);
	public void setTime(float f);
	public void addKeyFrame();
	public void removeKeyFrame();
	public Iterator<IKeyFramedPoint> getElementIterator();

	@Override
	public abstract boolean equals(Object o);
	@Override
	public abstract int hashCode();
}