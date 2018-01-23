package util;

public class Preferences extends XMLReaderWriter {
	private static final String ADDRESS = "res/preferences.xml";
	
	public Preferences() {
		readXML(ADDRESS);
	}
	
	public static boolean isLink() {
		System.out.println(link);
		return link;
	}
	
	public static void setLink(boolean l) {
		link = l;
		writeXML(ADDRESS);
	}
	
}
