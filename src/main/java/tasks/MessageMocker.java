package tasks;

import java.awt.Dimension;

import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import mainTasks.Creds;
import mainTasks.MainLauncher;
import mainTasks.LogHandler;
import messaging.MessagingUtils;
import utils.DriverUtils;
import utils.SessionUtils;

public class MessageMocker {
	static WebDriver driver;

	public static void startMessageMocking(Creds creds, String chatId) throws InterruptedException {
		LogHandler logHandler = MainLauncher.logHandler;
		Dimension screenSize = MainLauncher.screenSize;

		logHandler.append("Loading facebook.com and logging in...");

		driver = new FirefoxDriver();
		org.openqa.selenium.Dimension size = new org.openqa.selenium.Dimension(
				(int) (screenSize.getWidth() * 3.0 / 4.0), (int) (screenSize.getHeight() * 2 / 3));
		Point location = new Point((int) (screenSize.getWidth() / 4), 0);
		DriverUtils.resizeAndRelocate(driver, size, location);

		boolean in = SessionUtils.login(driver, creds);

		if (in) {
			MessagingUtils.openConversation(driver, chatId);

			while (true) {
				try {
					String lastMsg = MessagingUtils.readLastMessage(driver);

					if (lastMsg != null) {
						MessagingUtils.sendText(driver, mock(lastMsg));

					}
				} catch (Exception x) {
					logHandler.append("couldn't send text because " + x.getMessage());
				}

				Thread.sleep(10);
			}
		}
	}

	private static String mock(String s) {
		String res = "";
		boolean cap = false;
		for (String c : s.split("")) {
			if (c.equals(" ")) {
				res += c;
			} else {
				res += cap ? c.toUpperCase() : c.toLowerCase();
				cap = !cap;
			}
		}
		return res;
	}
}
