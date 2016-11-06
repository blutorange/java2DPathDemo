package de.homelab.madgaksha.cgca.path.paths;

import java.awt.Point;

import de.homelab.madgaksha.cgca.path.IPathCommand;

public class MoveToBuilder extends OnePathBuilder {
	@Override
	public String getName() {
		return "MoveTo"; //$NON-NLS-1$
	}
	@Override
	protected IPathCommand buildSubclass(final Point p) {
		return new MoveToCommand(p);
	}
}