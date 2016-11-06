package de.homelab.madgaksha.cgca.path;
import java.awt.geom.GeneralPath;
import java.util.Set;

public interface IPathCommand {
	public void apply(GeneralPath path);
	public String getName();
	public Set<IPathPoint> getPathPointSet();
}
