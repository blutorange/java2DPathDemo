package de.homelab.madgaksha.cgca.path;

public enum EInterpolation {
	LINEAR {
		@Override
		public float apply(final float alpha) {
			return alpha;
		}
	},
	QUADRATIC {
		@Override
		public float apply(final float alpha) {
			return alpha*alpha;
		}
	},
	CUBIC {
		@Override
		public float apply(final float alpha) {
			return alpha*alpha*alpha;
		}
	},
	SQRT {
		@Override
		public float apply(final float alpha) {
			return (float)Math.sqrt(alpha);
		}
	},
	CUBIC_ROOT {
		@Override
		public float apply(final float alpha) {
			return (float)Math.pow(alpha, 1f/3f);
		}
	}
	;
	public abstract float apply(float alpha);
}
