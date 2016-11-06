package de.homelab.madgaksha.cgca.path.paths;

import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.util.HashSet;
import java.util.Set;

import de.homelab.madgaksha.cgca.path.IPathCommand;
import de.homelab.madgaksha.cgca.path.IPathPoint;
import de.homelab.madgaksha.cgca.path.IPathPoint.PathPoint;

public class QuadToCommand implements IPathCommand {
	private final float x1,y1,x2,y2;
	private final Set<IPathPoint> set = new HashSet<>(2);
	public QuadToCommand(final Point p1, final Point p2) {
		this(p1.getX(), p1.getY(), p2.getX(), p2.getY());
	}
	public QuadToCommand(final double x1, final double y1, final double x2, final double y2) {
		this((float)x1,(float)y1,(float)x2,(float)y2);
	}
	public QuadToCommand(final float x1, final float y1, final float x2, final float y2) {
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		set.add(new PathPoint(x1, y1, getName()));
		set.add(new PathPoint(x2, y2, getName()));
	}
	@Override
	public void apply(final GeneralPath path) {
		path.quadTo(x1, y1, x2, y2);
	}
	@Override
	public String getName() {
		return "QuadTo"; //$NON-NLS-1$
	}
	@Override
	public Set<IPathPoint> getPathPointSet() {
		return set;
	}
}