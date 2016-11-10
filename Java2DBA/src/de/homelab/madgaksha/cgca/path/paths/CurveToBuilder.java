package de.homelab.madgaksha.cgca.path.paths;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import de.homelab.madgaksha.cgca.path.IPathBuilder;
import de.homelab.madgaksha.cgca.path.IPathCommand;
import de.homelab.madgaksha.cgca.path.IPathPoint;
import de.homelab.madgaksha.cgca.path.PathPoint;

public class CurveToBuilder implements IPathBuilder {
	private Point p1,p2,p3;
	private final Set<IPathPoint> set = new HashSet<>(2);
	@Override
	public void mouseClick(final Point p) {
		if (p1 == null)
			p1 = new Point(p);
		else if (p2 == null)
			p2 = new Point(p);
		else if (p3 == null)
			p3 = new Point(p);
		else {
			p1.setLocation(p);
			set.clear();
			p2 = null;
			p3 = null;
		}
		if (p1 != null) set.add(new PathPoint(p1, getName()));
		if (p2 != null) set.add(new PathPoint(p2, getName()));
	}
	@Override
	public boolean isFinished() {
		return p1 != null && p2 != null && p3 != null;
	}
	@Override
	public IPathCommand build() {
		if (!isFinished())
			throw new IllegalStateException("not yet finished"); //$NON-NLS-1$
		return new CurveToCommand(p1, p2, p3);
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