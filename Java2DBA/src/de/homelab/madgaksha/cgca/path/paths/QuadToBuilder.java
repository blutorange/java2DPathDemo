package de.homelab.madgaksha.cgca.path.paths;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import de.homelab.madgaksha.cgca.path.IPathBuilder;
import de.homelab.madgaksha.cgca.path.IPathCommand;
import de.homelab.madgaksha.cgca.path.IPathPoint;
import de.homelab.madgaksha.cgca.path.PathPoint;

public class QuadToBuilder implements IPathBuilder {
	private Point p1,p2;
	private final Set<IPathPoint> set = new HashSet<>(1);
	@Override
	public void mouseClick(final Point p) {
		if (p1 == null)
			p1 = new Point(p);
		else if (p2 == null)
			p2 = new Point(p);
		else {
			p1.setLocation(p);
			set.clear();
			p2 = null;
		}
		set.add(new PathPoint(p1, getName()));
	}
	@Override
	public boolean isFinished() {
		return p1 != null && p2 != null;
	}
	@Override
	public IPathCommand build() {
		if (!isFinished())
			throw new IllegalStateException("not yet finished"); //$NON-NLS-1$
		return new QuadToCommand(p1, p2);
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