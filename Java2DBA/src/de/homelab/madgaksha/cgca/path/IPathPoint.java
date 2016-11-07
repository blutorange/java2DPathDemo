package de.homelab.madgaksha.cgca.path;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

import de.homelab.madgaksha.cgca.path.IKeyFramedPoint.KeyFramedPoint;

public interface IPathPoint extends ListModel<IKeyFramedPoint> {
	public String getLabel();
	public float getPointX();
	public float getPointY();
	public void selectKeyFrame(float time);
	public void setPoint(float x, float y);
	@Override
	public abstract boolean equals(Object o);
	@Override
	public abstract int hashCode();

	public static class PathPoint implements IPathPoint {
		private final List<IKeyFramedPoint> list = new ArrayList<>();
		private final Set<ListDataListener> set = new HashSet<>();
		
		private final String label;
		private float x,y;
		public PathPoint(final Point p, final String label) {
			this((float)p.getX(), (float)p.getY(), label);
		}
		public PathPoint(final float x, final float y, final String label) {
			this.x = x;
			this.y = y;
			this.label = label;
			list.add(new KeyFramedPoint(this,0f));
		}
		public void setPoint(float x, float y) {
			this.x = x;
			this.y = y;
		}
		@Override
		public String getLabel() {
			return label;
		}
		@Override
		public float getPointX() {
			return x;
		}
		@Override
		public float getPointY() {
			return y;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((label == null) ? 0 : label.hashCode());
			result = prime * result + Float.floatToIntBits(x);
			result = prime * result + Float.floatToIntBits(y);
			return result;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(final Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof PathPoint))
				return false;
			final PathPoint other = (PathPoint) obj;
			if (label == null) {
				if (other.label != null)
					return false;
			}
			else if (!label.equals(other.label))
				return false;
			if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
				return false;
			if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
				return false;
			return true;
		}
		@Override
		public String toString() {
			return String.format("(%.01f,%.01f)", x,y);
		}
		@Override
		public int getSize() {
			return list.size();
		}
		@Override
		public IKeyFramedPoint getElementAt(int index) {
			return list.get(index);
		}
		@Override
		public void addListDataListener(ListDataListener l) {
			set.add(l);
		}
		@Override
		public void removeListDataListener(ListDataListener l) {
			set.remove(l);
		}
		@Override
		public void selectKeyFrame(float time) {
			// TODO Auto-generated method stub
		}
	}
}