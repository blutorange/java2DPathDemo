package de.homelab.madgaksha.cgca.path;

import java.util.EventObject;
import java.util.Optional;

import javax.swing.JComboBox;

public final class SWUtil {
	private SWUtil() {}

	@SuppressWarnings("unchecked")
	public static <T> Optional<T> srcAs(final EventObject event, final Class<T> clazz) {
		final Object o = event.getSource();
		if (o != null && clazz.isAssignableFrom(o.getClass()))
			return Optional.of((T)o);
		return Optional.empty();
	}
	@SuppressWarnings("unchecked")
	public static <T> Optional<T> cboxAs(final EventObject event, final Class<T> clazz) {
		@SuppressWarnings("rawtypes")
		final Optional<JComboBox> cb = srcAs(event, JComboBox.class);
		if (cb.isPresent()) {
			final Object s = cb.get().getSelectedItem();
			if (s != null && clazz.isAssignableFrom(s.getClass()))
				return Optional.of((T)s);
		}
		return Optional.empty();
	}
}
