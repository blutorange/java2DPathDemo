package de.homelab.madgaksha.cgca.path.paths;

import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.util.HashSet;
import java.util.Set;

import de.homelab.madgaksha.cgca.path.IPathCommand;
import de.homelab.madgaksha.cgca.path.IPathPoint;
import de.homelab.madgaksha.cgca.path.IPathPoint.PathPoint;

public class CurveToCommand implements IPathCommand {
	private final IPathPoint p1,p2,p3; 
	private final Set<IPathPoint> set = new HashSet<>(3);
	public CurveToCommand(final Point p1, final Point p2, final Point p3) {
		this(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY());
	}
	public CurveToCommand(final double x1, final double y1, final double x2, final double y2, final double x3, final double y3) {
		this((float)x1,(float)y1,(float)x2,(float)y2,(float)x3,(float)y3);
	}
	public CurveToCommand(final float x1, final float y1, final float x2, final float y2, final float x3, final float y3) {
		p1 = new PathPoint(x1, y1, getName());
		p2 = new PathPoint(x2, y2, getName());
		p3 = new PathPoint(x3, y3, getName());
		set.add(p1);
		set.add(p2);
		set.add(p3);
	}
	@Override
	public void apply(final GeneralPath path) {
		path.curveTo(p1.getPointX(),p1.getPointY(),p2.getPointX(),p2.getPointY(),p3.getPointX(),p3.getPointY());
	}
	@Override
	public String getName() {
		return "CurveTo"; //$NON-NLS-1$
	}
	@Override
	public Set<IPathPoint> getPathPointSet() {
		return set;
	}
}