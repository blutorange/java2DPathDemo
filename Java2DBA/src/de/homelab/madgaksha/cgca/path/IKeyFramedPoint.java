package de.homelab.madgaksha.cgca.path;

public interface IKeyFramedPoint {
	public IPathPoint getPathPoint();
	public float getTime();
	public void setTime(float time);
	public static class KeyFramedPoint implements IKeyFramedPoint {
		private final IPathPoint point;
		private float time;
		public KeyFramedPoint(IPathPoint point, float time) {
			this.point = point;
			this.time = time;
		}
		@Override
		public IPathPoint getPathPoint() {
			return point;
		}

		@Override
		public void setTime(float time) {
			this.time = time;
		}
		
		@Override
		public String toString() {
			return String.format("time=%.01f %s", time, point);
		}
		
		@Override
		public float getTime() {
			return time;
		}
		
	}
}
