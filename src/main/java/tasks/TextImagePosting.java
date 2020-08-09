package tasks;

import java.awt.AWTException;
import java.awt.Dimension;
import java.io.IOException;

import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import content.TextFetchingUtils;
import content.TextImage;
import content.textFetching.TextFetchingStrategy;
import mainTasks.Creds;
import mainTasks.MainLauncher;
import mainTasks.LogHandler;
import profile.ProfileUtils;
import utils.DriverUtils;
import utils.Entity;
import utils.SessionUtils;

public class TextImagePosting {

	static WebDriver postingDriver;

	static TextImage textImage;
	
	public static void textImageLoop(Entity entity, TextFetchingStrategy tfs)
			throws InterruptedException, IOException, AWTException {

		Creds creds = MainLauncher.creds;
		Dimension screenSize = MainLauncher.screenSize;
		LogHandler logHandler = MainLauncher.logHandler;
		
		Thread postingThread = new Thread() {
			public void run() {
				postingDriver = new FirefoxDriver();
				org.openqa.selenium.Dimension size = new org.openqa.selenium.Dimension((int) screenSize.getWidth() / 2,
						(int) (screenSize.getHeight() * 2.0 / 3.0));
				Point location = new Point((int) (screenSize.getWidth() / 2.0), 0);
				DriverUtils.resizeAndRelocate(postingDriver, size, location);

				try {
					boolean loggedIn = SessionUtils.login(postingDriver, creds);

					if (loggedIn) {
						while (true) {
							boolean posted = false;
							while (!posted) {
								try {
									long start = System.currentTimeMillis();
									postingDriver.get(entity.getLink());

									DriverUtils.waitForPageLoaded(postingDriver);

									boolean succ = false;

									while (!succ) {
										try {
											textImage = TextFetchingUtils.getTextAsImage(entity, tfs, logHandler);
											succ = true;
										} catch (IOException e) {
											logHandler.append("failed to generate image due to " + e.getMessage()
													+ ", retrying...");
										}
									}

									logHandler.append("Posting the image...");

									AutomatedTasks.postImageInGroup(postingDriver, textImage.getFile(), logHandler);

									logHandler.append("image posted successfully.");
									logHandler.separator();

									Thread.sleep(15000);

									String link = ProfileUtils.getLastPostLink(postingDriver);

									PostAutoTranslator.translatePostByLink(postingDriver, creds, link, textImage.getText(), logHandler, screenSize);

									Thread.sleep(10000);

									long end = System.currentTimeMillis();

									long waitFor = (1000 * 60 * 60) - (end - start);

									logHandler
											.append("will wait for " + MemeBot.miliToString(waitFor) + " before posting again");
									logHandler.separator();

									sleep(waitFor);

									posted = true;
								} catch (Exception x) {
									logHandler
											.append("failed post image due to " + x.getMessage() + ", retrying...");
								}
							}
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};

		postingThread.start();

		logHandler.append("Loading facebook.com and logging in...");
		logHandler.separator();
	}
}
