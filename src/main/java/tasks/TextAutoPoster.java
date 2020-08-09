package tasks;

import java.awt.AWTException;
import java.awt.Dimension;
import java.io.IOException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import content.TextFetchingUtils;
import content.textFetching.TextFetchingStrategy;
import mainTasks.Creds;
import mainTasks.LogHandler;
import profile.ProfileUtils;
import search.SearchRequest;
import search.SearchUtils;
import utils.DriverUtils;
import utils.SessionUtils;
import utils.Where;

public class TextAutoPoster {

	static WebDriver postingDriver;
	static WebDriver utilsDriver;

	static SearchRequest search = null;

	private static void textPostingLoop(Creds creds, TextFetchingStrategy tfs, LogHandler logHandler, Dimension screenSize,
			String where) {
		Thread postingThread = new Thread() {
			public void run() {
				postingDriver = new FirefoxDriver();
				org.openqa.selenium.Dimension size = new org.openqa.selenium.Dimension(
						(int) (screenSize.getWidth() / 2), (int) (screenSize.getHeight() / 2));
				Point location = new Point((int) (screenSize.getWidth() / 2), 0);
				DriverUtils.resizeAndRelocate(postingDriver, size, location);
				try {
					boolean loggedIn = SessionUtils.login(postingDriver, creds);
					if (loggedIn) {
						postingDriver.get(where);
						DriverUtils.waitForPageLoaded(postingDriver);
						int count = 0;
						while (true) {
							count++;
							boolean posted = false;
							while (!posted) {
								logHandler.append("getting random text string from " + tfs.getIndex());
								String text = TextFetchingUtils.getText(tfs, logHandler);
								int end = text.lastIndexOf("�");
								String searchFor = text.substring(1, Math.min(text.lastIndexOf(" ", 100), end));
								logHandler.append("text successfully stolen...\n\t" + text);
								logHandler.append("making sure it wasn't previously posted :");
								logHandler.append("\tSearching for : " + searchFor);
								search = new SearchRequest(searchFor);
								while (!search.isReady()) {
									sleep(10);
								}
								if (search.getRes() == null) {
									logHandler.append("\tNo Results");
								} else {
									logHandler.append(search.getRes().toString());
								}
								if (search.getRes() == null
										|| (!search.getRes().getPosterName().equalsIgnoreCase("Lukas Owen")
												|| !search.getRes().getFullContent().toLowerCase()
														.contains(searchFor.toLowerCase()))) {
									if (search.getRes() == null) {
										logHandler.append("Nothing found\n\tPosting allowed");
									} else {
										logHandler.append(
												"Results were not posted by \"Lukas Owen\" And/Or don't contain the same content\n\tPosting allowed");
									}

									logHandler.append("Posting the text...");

									AutomatedTasks.createPost(postingDriver, text, logHandler);
									posted = true;
									logHandler.append("text posted successfully.");
									logHandler.separator();

									sleep(5000);

									new Thread() {
										public void run() {
											try {
												PostAutoTranslator.translatePostById(utilsDriver, creds,
														ProfileUtils.getLastPostId(postingDriver), logHandler,
														screenSize);
											} catch (InterruptedException e) {
												e.printStackTrace();
											}
										}
									}.start();
								}
							}
							sleep(1000 * 60 * 5);
							if (count >= 5) {
								postingDriver.get(where);
								DriverUtils.waitForPageLoaded(postingDriver);
								count = 0;
							}
						}
					}
				} catch (InterruptedException | IOException e) {
					e.printStackTrace();
				}
			}
		};

		Thread searchThread = new Thread() {
			public void run() {
				utilsDriver = new FirefoxDriver();
				org.openqa.selenium.Dimension size = new org.openqa.selenium.Dimension((int) screenSize.getWidth() / 2,
						(int) (screenSize.getHeight() / 2));
				Point location = new Point(0, 0);
				DriverUtils.resizeAndRelocate(utilsDriver, size, location);

				try {
					boolean loggedin = SessionUtils.login(utilsDriver, creds);

					if (loggedin) {
						while (true) {
							if (search != null && !search.isReady()) {

								search.setRes(SearchUtils.searchForPost(utilsDriver, search.getText()));
								search.setReady(true);

							}

							sleep(10);
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};

		postingThread.start();
		searchThread.start();

		logHandler.append("Loading facebook.com and logging in...");
	}

	private static void textPostingLoopInGroup(Creds creds, TextFetchingStrategy tfs, LogHandler logHandler,
			Dimension screenSize, String groupId) {
		textPostingLoop(creds, tfs, logHandler, screenSize, "https://www.facebook.com/groups/" + groupId);
	}

	private static void textPostingLoopInProfile(Creds creds, TextFetchingStrategy tfs, LogHandler logHandler,
			Dimension screenSize) throws InterruptedException, IOException, AWTException {
		textPostingLoop(creds, tfs, logHandler, screenSize, "https://facebook.com/profile.php");
	}
	
	public static void textPostingLoop(Creds creds, TextFetchingStrategy tfs, LogHandler logHandler,
			Dimension screenSize, Where mode,String where) throws InterruptedException, IOException, AWTException {
		switch (mode) {
		case PROFILE: {
			textPostingLoopInProfile(creds, tfs, logHandler, screenSize);
			break;
		}
		case GROUP: {
			textPostingLoopInGroup(creds, tfs, logHandler, screenSize, where);
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + mode);
		}
	}
}
