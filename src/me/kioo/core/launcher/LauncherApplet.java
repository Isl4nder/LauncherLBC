package me.kioo.core.launcher;

import java.applet.Applet;
import java.applet.AppletStub;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.HeadlessException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

public class LauncherApplet extends Applet implements AppletStub {

	private static final long serialVersionUID = 1L;
	private final Map<String, String> params = new LinkedHashMap<String, String>();
	private Applet applet;

	private boolean active = false;
	private int context = 0;

	// public LauncherApplet(LauncherAPI api) throws HeadlessException
	public LauncherApplet(String userName, String sessionId) throws HeadlessException {
		// this.api = api;

		params.put("stand-alone", "true");
		params.put("username", String.valueOf(userName));
		params.put("sessionid", String.valueOf(sessionId));
		setBackground(Color.black);
	}

	public void appletResize(int width, int height) {
	}

	public void replace(Applet applet) {
		this.applet = applet;
		applet.setStub(this);
		applet.setSize(getWidth(), getHeight());

		setLayout(new BorderLayout());
		add(applet, "Center");

		applet.init();
		active = true;
		applet.start();
		validate();
	}

	public boolean isActive() {
		if (context == 0) {
			context = -1;
			try {
				if (getAppletContext() != null) {
					context = 1;
				}
			} catch (final Exception localException) {
			}
		}
		if (context == -1) {
			return active;
		}
		return super.isActive();
	}

	public String getParameter(String key) {
		String value = this.params.get(key);
		if (value == null) {
			try {
				value = super.getParameter(key);
			} catch (final Exception e) {
				System.out.println("Minecraft can't want param '" + key);
				//this.params.put(key, null);
			}
		
		/*String custom = api.getConfig().getString(name);
		if (custom == null) {
			custom = params.get(name);
			if (custom == null) {
				try {
					custom = super.getParameter(name);
				} catch (final Exception e) {
					params.put(name, null);
				}
			}*/
		}
		System.out.println("Minecraft want param '" + key + "' = '" + value + "'");
		return value;
	}

	public URL getDocumentBase() {
		try {
			return new URL("http://www.minecraft.net/game/");
		} catch (final MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void stop() {
		if (applet != null) {
			active = false;
			applet.stop();
			return;
		}
	}

	public void destroy() {
		if (applet != null) {
			applet.destroy();
			return;
		}
	}

	public Map<String, String> getParams() {
		return params;
	}

	public Applet getApplet() {
		return applet;
	}

	public int getContext() {
		return context;
	}

}
