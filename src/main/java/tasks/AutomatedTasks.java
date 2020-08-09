package tasks;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import mainTasks.LogHandler;
import memes.GeneratedMeme;
import profile.PostUtils;
import translation.TranslationResult;
import translation.TranslationUtils;
import utils.DriverUtils;

public class AutomatedTasks {
	public static void createPost(WebDriver driver, String content, LogHandler logHandler) {
		boolean success = false;
		int attempts = 0;

		while (!success) {
			try {
				PostUtils.createPost(driver, content);
				success = true;
			} catch (Exception x) {
				success = false;
				logHandler.append("Failed to create post due to " + x.getMessage().split("\n")[0] + ", retrying...");
				attempts++;
				if (attempts > 4 && !success) {
					driver.navigate().refresh();
					DriverUtils.waitForPageLoaded(driver);
					attempts = 0;
				}
			}
		}

		success = false;
		attempts = 0;

		while (!success) {
			try {
				PostUtils.publish(driver);
				success = true;
			} catch (Exception x) {
				success = false;
				logHandler.append("Failed to publish due to " + x.getMessage().split("\n")[0] + ", retrying...");
				attempts++;
				if (attempts > 4 && !success) {
					driver.navigate().refresh();
					DriverUtils.waitForPageLoaded(driver);
					attempts = 0;
				}
			}
		}
	}

	public static void postMemeInAlbum(WebDriver driver, GeneratedMeme meme, LogHandler logHandler, String albumLink)
			throws InterruptedException {
		driver.get(albumLink);
		DriverUtils.waitForPageLoaded(driver);

		driver.findElement(By.cssSelector("#album_header_pagelet button + button")).click();

		Thread.sleep(5000);

		File f = new File("logs/meme.jpg");
		try {
			ImageIO.write(meme.getMeme(), "JPG", new FileOutputStream(f));
		} catch (IOException e) {
			e.printStackTrace();
		}

		driver.findElements(By.cssSelector("div[role='textbox']>div>div>div")).get(1).click();

		driver.findElement(By.cssSelector("#fbTimelinePhotosContent input[type='file']")).sendKeys(f.getAbsolutePath());

		Thread.sleep(40000);

		driver.findElement(By.cssSelector(".uiOverlayFooter button[type='submit']")).click();

		Thread.sleep(5000);

		DriverUtils.waitForPageLoaded(driver);
	}

	public static void postMeme(WebDriver driver, GeneratedMeme meme, LogHandler logHandler) {
		try {
			File f = new File("logs/meme.jpg");
			ImageIO.write(meme.getMeme(), "JPG", new FileOutputStream(f));
			postImageInGroup(driver, f, logHandler);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void postImageInGroup(WebDriver driver, File image, LogHandler logHandler) {
		boolean success = false;
		int attempts = 0;

		while (!success) {
			try {

				PostUtils.addImage(driver, image.getAbsolutePath());
				PostUtils.addImage(driver, image.getAbsolutePath());

				success = true;
			} catch (Exception x) {
				success = false;
				logHandler.append("Failed to create post due to " + x.getMessage().split("\n")[0] + ", retrying...");
				attempts++;
				if (attempts > 4 && !success) {
					driver.navigate().refresh();
					DriverUtils.waitForPageLoaded(driver);
					attempts = 0;
				}
			}
		}

		success = false;
		attempts = 0;

		try {
			Thread.sleep(40000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		while (!success) {
			try {
				PostUtils.publishInGroup(driver);
				success = true;
			} catch (Exception x) {
				success = false;
				logHandler.append("Failed to publish due to " + x.getMessage().split("\n")[0] + ", retrying...");
				attempts++;
				if (attempts > 4 && !success) {
					driver.navigate().refresh();
					DriverUtils.waitForPageLoaded(driver);
					attempts = 0;
				}
			}
		}
	}

	public static void translatePostById(WebDriver postDriver, WebDriver translateDriver, String postId,
			LogHandler logHandler) throws InterruptedException {

		PostUtils.openPostById(postDriver, postId);

		translatePost(postDriver, translateDriver, null, logHandler);
	}

	public static void translatePostByLink(WebDriver postDriver, WebDriver translateDriver, String link, String text,
			LogHandler logHandler) throws InterruptedException {

		logHandler.append("Attempting to translate");

		logHandler.append("\tLoading post using: URL (" + link + ")");

		PostUtils.openPostByLink(postDriver, link);

		translatePost(postDriver, translateDriver, text, logHandler);
	}

	private static void translatePost(WebDriver postDriver, WebDriver translateDriver, String text,
			LogHandler logHandler) throws InterruptedException {
		logHandler.append("\tGetting post content...");

		String postText = text == null ? PostUtils.getPostText(postDriver) : text;
		logHandler.append("\t\t" + postText);

		logHandler.append("\tSending content to google translate with source language = auto");

		TranslationResult tr = TranslationUtils.translate(translateDriver, postText, null, null);

		if (tr.getSourceLang().equalsIgnoreCase("english")) {
			logHandler.append("quote does not need translation (source language: " + tr.getSourceLang() + ")");
		} else {
			logHandler.append("Successfully translated from : " + tr.getSourceLang());
			logHandler.append("Posting to comments...");
			PostUtils.postComment(postDriver, tr.toString(), true);
			logHandler.append("\tTranslation posted into comments with sucess");
		}

		logHandler.separator();
	}

}
