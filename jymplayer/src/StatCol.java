import java.awt.Color;
import java.awt.Dimension;

import com.google.gdata.client.youtube.YouTubeService;


public class StatCol {
	static YouTubeService myService = new YouTubeService("My Application");
	static String username = "p0jk3n";
	static final String MAXRESULT = "?max-results=";
	static final String BASEURL = "http://gdata.youtube.com/feeds/api/users/";
	static final String FAVORITES = "/favorites";
	static final String SUBSCRIPTIONS = "/subscriptions";
	static final String UPLOADS = "/uploads";
	static final String NEWSUBSCIPTIONS ="/newsubscriptionvideos";
	static final String FIRSTINDEX = "&start-index=";
	public static final String FULLHD = "37";
	public static final String HD = "22";
	public static final String SD = "18";
	public static final String LD = "17";
	static Color globalFade = Color.black;
	public static String nItems;
	public static final Dimension small = new Dimension(122, 115);
	public static final Dimension medium = new Dimension(338, 107);
}
