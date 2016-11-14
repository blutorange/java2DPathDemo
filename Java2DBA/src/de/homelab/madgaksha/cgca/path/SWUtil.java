package de.homelab.madgaksha.cgca.path;

import java.awt.event.ItemEvent;
import java.util.EventObject;
import java.util.Optional;

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
	public static <T> Optional<T> cboxSelAs(final ItemEvent event, final Class<T> clazz) {
		final Object s = event.getItem();
		if (event.getStateChange() == ItemEvent.SELECTED && s != null && clazz.isAssignableFrom(s.getClass()))
			return Optional.of((T)s);
		return Optional.empty();
	}
}
