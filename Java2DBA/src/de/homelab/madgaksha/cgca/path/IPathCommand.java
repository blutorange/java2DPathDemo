package de.homelab.madgaksha.cgca.path;
import java.awt.geom.Path2D;
import java.util.Set;

public interface IPathCommand {
	public void apply(Path2D.Float path);
	public void apply(Path2D.Float path, float time);
	public String getName();
	public Set<IPathPoint> getPathPointSet();
}
