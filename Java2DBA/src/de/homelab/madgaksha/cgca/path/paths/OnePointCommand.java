package de.homelab.madgaksha.cgca.path.paths;

import java.awt.Point;
import java.awt.geom.Path2D;
import java.util.HashSet;
import java.util.Set;

import de.homelab.madgaksha.cgca.path.IPathCommand;
import de.homelab.madgaksha.cgca.path.IPathPoint;
import de.homelab.madgaksha.cgca.path.IPathPoint.PathPoint;

abstract class OnePointCommand implements IPathCommand {
	private final IPathPoint p;
	private final Set<IPathPoint> set = new HashSet<>(1);
	public OnePointCommand(final Point p) {
		this(p.getX(), p.getY());
	}
	public OnePointCommand(final double x, final double y) {
		this((float)x, (float)y);
	}
	public OnePointCommand(final float x, final float y) {
		p = new PathPoint(x,y,getName());
		set.add(p);
	}
	@Override
	public Set<IPathPoint> getPathPointSet() {
		return set;
	}
	@Override
	public final void apply(final Path2D.Float path) {
		applySubclass(p.getPointX(), p.getPointY(), path);
	}
	@Override
	public final void apply(final Path2D.Float path, final float time) {
		applySubclass(p.getPointX(time), p.getPointY(time), path);
	}
	protected abstract void applySubclass(float x, float y, Path2D.Float path);
	@Override
	public abstract String getName();

}