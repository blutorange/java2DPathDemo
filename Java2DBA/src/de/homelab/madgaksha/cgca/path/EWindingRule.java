package de.homelab.madgaksha.cgca.path;

import java.awt.geom.GeneralPath;

public enum EWindingRule {
	EVEN_ODD(GeneralPath.WIND_EVEN_ODD),
	NON_ZERO(GeneralPath.WIND_NON_ZERO)
	;
	private final int i;
	private EWindingRule(final int i) {
		this.i = i;
	}
	public int getInt() {
		return i;
	}
}
