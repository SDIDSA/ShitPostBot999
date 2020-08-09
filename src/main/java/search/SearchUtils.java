package search;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import profile.PostUtils;
import utils.DriverUtils;

public class SearchUtils {
	public static SearchResult searchForPost(WebDriver driver, String content) throws InterruptedException {
		String acceptedChars = ":-―', ";
		
		String searchFor = "";
		
		for(char c:content.toCharArray()) {
			if(Character.isAlphabetic(c) || Character.isDigit(c) || acceptedChars.indexOf(c) != -1) {
				searchFor += c;
			}
		}
		
		String query = "https://www.facebook.com/search/posts/?q="+searchFor+"&epa=FILTERS&filters=eyJycF9hdXRob3IiOiJ7XCJuYW1lXCI6XCJhdXRob3JfbWVcIixcImFyZ3NcIjpcIlwifSJ9";
		driver.get(query);
		DriverUtils.waitForPageLoaded(driver);
		
		Thread.sleep(4000);
		
		try {
			WebElement topRes = driver.findElement(By.cssSelector("#BrowseResultsContainer>div>div>div>div>div>div>div>div>div"));
			
			String[] lines = topRes.getText().split("\n");
			
			String[] parts = null;
			
			for(String line : lines) {
				if(line.indexOf(" ·  · ") != -1) {
					parts = line.split(" ·  · ");
					break;
				}
			}
			
			String posterName = lines[0];
			String time = parts[0];
			String fullContent = parts[1];
			
			String link = topRes.findElement(By.xpath("//a[text()='"+time+"']")).getAttribute("href");
			
			driver.get(link);
			
			DriverUtils.waitForPageLoaded(driver);
			
			fullContent = PostUtils.getPostText(driver);
			time = PostUtils.getPostTime(driver);
			
			return new SearchResult(posterName, fullContent, time);
		}catch(Exception x) {
			x.printStackTrace();
			return null;
		}
	}
}
