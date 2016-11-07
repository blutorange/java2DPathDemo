package de.homelab.madgaksha.cgca.path.paths;

import java.awt.Point;

import de.homelab.madgaksha.cgca.path.IPathCommand;

public class LineToBuilder extends OnePointBuilder {
	@Override
	public String getName() {
		return "LineTo"; //$NON-NLS-1$
	}
	@Override
	protected IPathCommand buildSubclass(final Point p) {
		return new LineToCommand(p);
	}
}