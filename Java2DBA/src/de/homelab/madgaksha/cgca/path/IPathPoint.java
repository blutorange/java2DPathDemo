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
	default boolean equalsIPathPointSpatially(IPathPoint o) {
		if (o == null)
			return false;
		return getPointX() == o.getPointX() && getPointY() == o.getPointY();
	}
	@Override
	public abstract int hashCode();
}