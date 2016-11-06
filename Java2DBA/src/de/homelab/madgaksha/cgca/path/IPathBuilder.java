package de.homelab.madgaksha.cgca.path;
import java.awt.Point;
import java.util.Set;

public interface IPathBuilder {
	public void mouseClick(Point p);
	public boolean isFinished();
	public IPathCommand build();
	public String getName();
	public Set<IPathPoint> getPathPointSet();
	default IPathBuilder getNew() {
		try {
			return this.getClass().newInstance();
		}
		catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException("Could not create new path builder.", e); //$NON-NLS-1$
		}
	}
}
