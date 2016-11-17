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
	},
	POWER_HAT {
		@Override
		public float apply(final float alpha) {
			return 4f*alpha*alpha*(0.5f*alpha-1f)*(0.5f*alpha-1f);
		}
	}

	;
	public abstract float apply(float alpha);
	@Override
	public String toString() {
		return this.name().replace('_', ' ');
	}
}
