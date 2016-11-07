package de.homelab.madgaksha.cgca.path;

public interface IKeyFramedPoint extends Comparable<IKeyFramedPoint> {
	public float getX();
	public float getY();
	public float getTime();
	public void set(float x, float y);
	public void setTime(float time);
	
	public static class KeyFramedPoint implements IKeyFramedPoint {
		private float time;
		private float x,y;
		public KeyFramedPoint(float x, float y, float time) {
			this.x = x;
			this.y = y;
			this.time = time;
		}

		@Override
		public void setTime(float time) {
			this.time = time;
		}
		
		@Override
		public String toString() {
			return String.format("time=%.01f (%.01f,%.01f)", time, x, y);
		}
		
		@Override
		public float getTime() {
			return time;
		}
		@Override
		public float getX() {
			return x;
		}
		@Override
		public float getY() {
			return y;
		}
		@Override
		public void set(float x, float y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public int compareTo(IKeyFramedPoint other) {
			if (getTime() != other.getTime())
				return getTime() < other.getTime() ? -1 : 1;
			if (getX() != other.getX())
				return getX() < other.getX() ? -1 : 1;
			return getY() < other.getY() ? -1 : getY() == other.getY() ? 0 : 1;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + Float.floatToIntBits(time);
			result = prime * result + Float.floatToIntBits(x);
			result = prime * result + Float.floatToIntBits(y);
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			KeyFramedPoint other = (KeyFramedPoint) obj;
			if (Float.floatToIntBits(time) != Float.floatToIntBits(other.time))
				return false;
			if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
				return false;
			if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
				return false;
			return true;
		}
		
	}
}
