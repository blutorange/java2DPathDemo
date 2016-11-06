package de.homelab.madgaksha.cgca.path;
import java.awt.Point;

public interface IPathPoint {
	public String getLabel();
	public float getPointX();
	public float getPointY();
	@Override
	public boolean equals(Object o);
	@Override
	public int hashCode();
	public static class PathPoint implements IPathPoint {
		private final String label;
		private final float x,y;
		public PathPoint(final Point p, final String label) {
			this((float)p.getX(), (float)p.getY(), label);
		}
		public PathPoint(final float x, final float y, final String label) {
			this.x = x;
			this.y = y;
			this.label = label;
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
	}
}
