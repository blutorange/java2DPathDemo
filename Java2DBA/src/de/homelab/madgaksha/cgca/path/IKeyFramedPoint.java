package de.homelab.madgaksha.cgca.path;

public interface IKeyFramedPoint extends Comparable<IKeyFramedPoint> {
	public float getX();
	public float getY();
	public float getTime();
	public EInterpolation getInterpolation();
	public void setInterpolation(EInterpolation interpolation);
	public void set(float x, float y);
	public void setTime(float time);
	public IPathPoint getPathPoint();
}
