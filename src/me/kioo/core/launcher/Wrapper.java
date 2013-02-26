package me.kioo.core.launcher;

import java.applet.Applet;
import java.io.File;
import java.lang.reflect.Field;

import me.kioo.Launcher;
import me.kioo.util.Util;

public class Wrapper {
	
	
	public static Applet wrap(final Launcher launcher) throws Exception {
		final Class<?> clazz = launcher.getClassLoader().loadClass("net.minecraft.client.MinecraftApplet");

		Wrapper.doChanges(launcher);

		final Applet applet = (Applet) clazz.newInstance();
		return applet;
	}

	public static void doChanges(final Launcher launcher) {
		try {
			final Class<?> c = launcher.getClassLoader().loadClass("net.minecraft.client.Minecraft");

			for (final Field field : c.getDeclaredFields()) {
				if (field.getType() == File.class) {
					field.setAccessible(true);
					try {
						field.get(c);
						field.set(null, Util.getWorkingDirectory());
					}
					catch (final IllegalArgumentException | IllegalAccessException e) {
					}
				}
			}
		} catch (final ClassNotFoundException e) {}
	}
}