package de.homelab.madgaksha.cgca.path.paths;

import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.util.HashSet;
import java.util.Set;

import de.homelab.madgaksha.cgca.path.IPathCommand;
import de.homelab.madgaksha.cgca.path.IPathPoint;
import de.homelab.madgaksha.cgca.path.IPathPoint.PathPoint;

abstract class OnePointCommand implements IPathCommand {
	private final float x,y;
	private final Set<IPathPoint> set = new HashSet<>(1);
	public OnePointCommand(final Point p) {
		this(p.getX(), p.getY());
	}
	public OnePointCommand(final double x, final double y) {
		this((float)x, (float)y);
	}
	public OnePointCommand(final float x, final float y) {
		this.x = x;
		this.y = y;
		set.add(new PathPoint(x,y,getName()));
	}
	@Override
	public Set<IPathPoint> getPathPointSet() {
		return set;
	}
	@Override
	public final void apply(final GeneralPath path) {
		applySubclass(x,y,path);
	}
	protected abstract void applySubclass(float x, float y, GeneralPath path);
	@Override
	public abstract String getName();

}