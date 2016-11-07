package de.homelab.madgaksha.cgca.path.paths;

import java.awt.Point;
import java.awt.geom.Path2D;

public class MoveToCommand extends OnePointCommand {
	public MoveToCommand(final Point p) {
		super(p);
	}
	public MoveToCommand(final double x, final double y) {
		super(x,y);
	}
	public MoveToCommand(final float x, final float y) {
		super(x,y);
	}
	@Override
	public String getName() {
		return "MoveTo"; //$NON-NLS-1$
	}
	@Override
	protected void applySubclass(final float x, final float y, final Path2D.Float path) {
		path.moveTo(x, y);
	}
}