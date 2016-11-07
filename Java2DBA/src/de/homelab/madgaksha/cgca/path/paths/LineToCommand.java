package de.homelab.madgaksha.cgca.path.paths;

import java.awt.Point;
import java.awt.geom.Path2D;

public class LineToCommand extends OnePointCommand {
	public LineToCommand(final Point p) {
		super(p);
	}
	public LineToCommand(final double x, final double y) {
		super(x,y);
	}
	public LineToCommand(final float x, final float y) {
		super(x,y);
	}
	@Override
	public String getName() {
		return "LineTo"; //$NON-NLS-1$
	}
	@Override
	protected void applySubclass(final float x, final float y, final Path2D.Float path) {
		path.lineTo(x, y);
	}
}