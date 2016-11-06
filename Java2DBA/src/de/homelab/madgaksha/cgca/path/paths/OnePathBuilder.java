package de.homelab.madgaksha.cgca.path.paths;

import java.awt.Point;
import java.util.Collections;
import java.util.Set;

import de.homelab.madgaksha.cgca.path.IPathBuilder;
import de.homelab.madgaksha.cgca.path.IPathCommand;
import de.homelab.madgaksha.cgca.path.IPathPoint;

abstract class OnePathBuilder implements IPathBuilder {
	private Point p;
	@Override
	public final void mouseClick(final Point p) {
		if (this.p != null)
			this.p.setLocation(p);
		else
			this.p = new Point(p);
	}
	@Override
	public final boolean isFinished() {
		return p != null;
	}
	@Override
	public final IPathCommand build() {
		if (!isFinished())
			throw new IllegalStateException("not yet finished"); //$NON-NLS-1$
		return buildSubclass(p);
	}
	@Override
	public final Set<IPathPoint> getPathPointSet() {
		return Collections.emptySet();
	}
	@Override
	public abstract String getName();
	protected abstract IPathCommand buildSubclass(Point p);
}