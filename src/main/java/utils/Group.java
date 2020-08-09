package utils;

public class Group extends Entity {
	public static final Group I_AM_DZ_AND_I_SPEAK_ENGLISH = new Group("I Am Dz And I Speak English",
			"IAMALGERIANANDISPEAKENGLISH");

	public Group(String name, String id) {
		super(name, "https://www.facebook.com/groups/" + id);
	}

}
