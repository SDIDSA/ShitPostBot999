package mainTasks;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.IOException;

import tasks.MessageMocker;

public class MainLauncher {
	static final String email = "<EMAIL>";
	static final String password = "<PASSWORD>";
	
	public static LogHandler logHandler;
	public static Dimension screenSize;
	public static Creds creds;

	static {
		System.setProperty("webdriver.gecko.driver", MainLauncher.class.getResource("/geckodriver.exe").getFile());
		Rectangle winSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenSize.setSize(winSize.getWidth(), winSize.getHeight());
		logHandler = LogHandler.startLogHandler(screenSize);
		creds = new Creds(email, password);
	}
	
	public static void main(String[] args) throws InterruptedException, IOException, AWTException {
		MessageMocker.startMessageMocking(creds, "2198531073605120");
	}
}
