package tasks;

import java.awt.AWTException;
import java.awt.Dimension;
import java.io.IOException;

import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import mainTasks.Creds;
import mainTasks.LogHandler;
import memes.GeneratedMeme;
import memes.MemeUtils;
import profile.PostUtils;
import profile.ProfileUtils;
import search.SearchRequest;
import utils.DriverUtils;
import utils.SessionUtils;

public class MemeBot {

	static WebDriver postingDriver;

	static SearchRequest search = null;

	static GeneratedMeme meme;

	public static void memePostingLoop(Creds creds, LogHandler logHandler, Dimension screenSize)
			throws InterruptedException, IOException, AWTException {

		Thread postingThread = new Thread() {
			public void run() {
				postingDriver = new FirefoxDriver();
				org.openqa.selenium.Dimension size = new org.openqa.selenium.Dimension((int) screenSize.getWidth(),
						(int) (screenSize.getHeight() / 2));
				Point location = new Point(0, 0);
				DriverUtils.resizeAndRelocate(postingDriver, size, location);

				try {
					boolean loggedIn = SessionUtils.login(postingDriver, creds);

					if (loggedIn) {
						while (true) {
							boolean posted = false;
							while(!posted) {
								try {
									long start = System.currentTimeMillis();
									postingDriver.get("https://www.facebook.com/groups/712124039622475/");

									DriverUtils.waitForPageLoaded(postingDriver);

									logHandler.append("randomly generating a meme");

									boolean succ = false;

									while (!succ) {
										try {
											meme = MemeUtils.generateMeme(logHandler);
											succ = true;
										} catch (IOException e) {
											logHandler.append(
													"failed to generate meme due to " + e.getMessage() + ", retrying...");
										}
									}

									logHandler.append("Posting the meme...");

									AutomatedTasks.postMeme(postingDriver, meme, logHandler);

									logHandler.append("meme posted successfully.");
									logHandler.separator();

									Thread.sleep(15000);

									postingDriver.get(ProfileUtils.getLastPostLink(postingDriver));

									Thread.sleep(10000);

									int count = 0;
									succ = false;
									while (!succ) {
										count++;
										try {
											PostUtils.postComment(postingDriver, "template used : " + meme.getTempLink(), true);
											succ = true;
										} catch (Exception x) {
											succ = false;
										}
										if(count > 4) {
											postingDriver.navigate().refresh();
											DriverUtils.waitForPageLoaded(postingDriver);
											count = 0;
										}
									}

									for (String src : meme.getSources()) {
										count = 0;
										succ = false;
										while (!succ) {
											count++;
											try {
												PostUtils.postComment(postingDriver, "source used : " + src, false);
												succ = true;
											} catch (Exception x) {
												succ = false;
											}
										}
										if(count > 4) {
											postingDriver.navigate().refresh();
											DriverUtils.waitForPageLoaded(postingDriver);
											count = 0;
										}
									}

									long end = System.currentTimeMillis();

									long waitFor = (1000 * 60 * 60) - (end - start);

									logHandler.append("will wait for " + miliToString(waitFor) + " before posting again");
									logHandler.separator();

									sleep(waitFor);
									
									posted = true;
								}catch(Exception x) {
									logHandler.append("failed post shitpost due to " + x.getMessage() +", retrying...");
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

	public static String miliToString(long val) {
		int total = (int) (val / 1000);

		int seconds = total % 60;
		int minutes = total / 60;

		return minutes + " minutes and " + seconds + " seconds";
	}
}
