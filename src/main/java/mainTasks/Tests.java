package mainTasks;

import java.awt.Dimension;
import java.io.IOException;

import content.TextFetchingUtils;
import content.textFetching.IceBreakerIdeas;
import content.textFetching.TextFetchingStrategy;
import utils.Entity;
import utils.Group;

public class Tests {

	public static void main(String[] args) throws InterruptedException, IOException {
		LogHandler logHandler = LogHandler.startLogHandler(new Dimension(1920, 1000));

		Entity where = Group.I_AM_DZ_AND_I_SPEAK_ENGLISH;
		TextFetchingStrategy how = new IceBreakerIdeas();
		
		TextFetchingUtils.getTextAsImage(where, how, logHandler);
	}
}
