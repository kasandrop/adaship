package com.ada.marcin;

import com.ada.marcin.config.GameConfig;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setWindowedMode(GameConfig.WINDOWS_WIDTH, GameConfig.WINDOWS_HEIGHT);
		config.setTitle("AdaShip");
		new Lwjgl3Application(new AdashipGame(), config);
	}
}
