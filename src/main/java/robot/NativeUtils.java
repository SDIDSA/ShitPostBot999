package robot;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

public class NativeUtils {
	private static Robot ROBOT;

	public static void init() {
		try {
			ROBOT = new Robot();
			ROBOT.setAutoDelay(200);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	public static void selectImage(String filename) {
		pressKey(KeyEvent.VK_TAB, 5);
		pressKey(KeyEvent.VK_DOWN, 2);
		pressKey(KeyEvent.VK_ENTER, 1);
		pressKey(KeyEvent.VK_TAB, 2);
		pressKeys(filename.substring(0, Math.min(5, filename.length() - 4)));
		pressKey(KeyEvent.VK_ENTER);
	}

	public static void pressKey(int key) {
		ROBOT.keyPress(key);
		ROBOT.keyRelease(key);
	}

	public static void pressKeys(String text) {
		for (char c : text.toCharArray()) {
			int keyCode = java.awt.event.KeyEvent.getExtendedKeyCodeForChar(c);
			pressKey(keyCode);
		}
	}

	public static void pressKey(int key, int times) {
		for (int i = 0; i < times; i++) {
			pressKey(key);
		}
	}
}
