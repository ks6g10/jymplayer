
/**
 * @author Kim Svensson
 *
 */

import com.google.gdata.client.youtube.YouTubeService;
import com.google.gdata.data.youtube.SubscriptionEntry;
import com.google.gdata.data.youtube.SubscriptionFeed;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.VideoFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;


public class YouTube {
	static YouTubeService myService = new YouTubeService("My Application");
	static mPlayerWrapper mplayer = new mPlayerWrapper();
	static HashMap<String, HashMap<String, String>> allURLMap = new HashMap<String, HashMap<String,String>>();
	static VideoStreamFetcher vSF = new VideoStreamFetcher();
	static String nItems = "2";
	static String username = "p0jk3n";
	static final String MAXRESULT = "?max-results=";
	static final String BASEURL = "http://gdata.youtube.com/feeds/api/users/";
	static final String FAVORITES = "/favorites";
	static final String SUBSCRIPTIONS = "/subscriptions";
	static final String UPLOADS = "/uploads";
	static final String NEWSUBSCIPTIONS ="/newsubscriptionvideos";
	static final String FIRSTINDEX = "&start-index=";
	static boolean isSmall = true;
	static int xspace = 5, yspace = 5;
	static JFrame myFrame = new JFrame();
	static JPanel myPanel = new JPanel();
	static Component[] thumbs;
	static Color globalFade = Color.black;
	static NewSubVideoPanel newSubPanel;
	
	public static List<VideoEntry> getNextVideos(String argFeed, int argNItems, int argStartIndex){
		nItems = Integer.toString(argNItems+1);//plus 1 for index correction
		String index = Integer.toString(argStartIndex+1);//plus one for index correction
		String feedUrl =  BASEURL.concat(username).concat(argFeed).concat(MAXRESULT).concat(nItems).concat(FIRSTINDEX).concat(index);
		System.out.println(feedUrl);
		VideoFeed feed = null;
		try {
			feed = myService.getFeed(new URL(feedUrl), VideoFeed.class);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return feed.getEntries();
	}
	
	
	public static void showAuthorVideos(String argAuthor) {
		
		for(int i=0;i< thumbs.length;i++) {
			((VideoThumb) thumbs[i]).checkAuthorShow(argAuthor);
		}
	}
	public static void unshowAuthorVideos() {
		for(int i=0;i< thumbs.length;i++) {
			((VideoThumb) thumbs[i]).unshowAuthorVideos();
		}
	}

	public static void playVideo(String argVideoUrl) {
		System.out.println(argVideoUrl);
		String tmpURL;
		if(!allURLMap.containsKey(argVideoUrl)) {
			vSF.produceEntity(argVideoUrl);
//			System.out.println("steg 2");
			vSF.generateStreamURL();
	//		System.out.println("steg 3");
			allURLMap.put(argVideoUrl, vSF.getURLMap());
		}

		//end of if

		tmpURL = getVideoURL(VideoStreamFetcher.HD, allURLMap.get(argVideoUrl));
		//boolean tmpURL1 = allURLMap.get(argVideoUrl).containsKey(VideoStreamFetcher.HD);
		//System.out.println("tmpurl"+tmpURL1);
		mplayer.run(tmpURL);
		//System.out.println("steg 4");
	}

	public static String getVideoURL(String argResolution, HashMap<String, String> argMap) {
		if(argResolution.equals(VideoStreamFetcher.FULLHD) ) {
			if(argMap.containsKey(VideoStreamFetcher.FULLHD))
				return argMap.get(VideoStreamFetcher.FULLHD);
		}
		if(argResolution.equals(VideoStreamFetcher.FULLHD) || argResolution.equals(VideoStreamFetcher.HD))  { //if full hd is not avaliable
			if(argMap.containsKey(VideoStreamFetcher.HD))
				return argMap.get(VideoStreamFetcher.HD);
		} 

		if(argMap.containsKey(VideoStreamFetcher.SD))
			return argMap.get(VideoStreamFetcher.SD);
		if(argMap.containsKey(VideoStreamFetcher.LD))
			return argMap.get(VideoStreamFetcher.LD);	
		return null;
	}



	public static List<VideoEntry> getLatestSubVideos() throws MalformedURLException, IOException, ServiceException {
		String feedUrl =  BASEURL.concat(username).concat(NEWSUBSCIPTIONS).concat(MAXRESULT).concat(nItems);
		System.out.println(feedUrl);
		VideoFeed feed = myService.getFeed(new URL(feedUrl), VideoFeed.class);
		return feed.getEntries();
	}

	public static List<VideoEntry> getUserEntries() throws MalformedURLException, IOException, ServiceException {
		VideoFeed videoFeed = null;
		String feedUrl =  BASEURL.concat(username).concat(UPLOADS);
		videoFeed = myService.getFeed(new URL(feedUrl), VideoFeed.class);
		return videoFeed.getEntries();
	}

	public static ArrayList<VideoThumb> getVideoThumbs(List<VideoEntry> argEntries) {
		ArrayList<VideoThumb> thumbArrayList = new ArrayList<VideoThumb>(Integer.parseInt(nItems));
		for(int i=0; i<argEntries.size(); i++) {
			thumbArrayList.add(new VideoThumb(argEntries.get(i)));
		}
		return thumbArrayList;
	}

	public static void addVideoThumbsProg(List<VideoEntry> argEntries) {
		VideoThumb tmp;
		for(int i=0; i<argEntries.size(); i++) {
			tmp = new VideoThumb(argEntries.get(i));
			tmp.setVisible(false);
			myPanel.add(tmp);
			myPanel.validate();
			tmp.setVisible(true);
		}
	}


	public static void latestSubVideosView(int argItems, boolean isProgressive) throws MalformedURLException, IOException, ServiceException {
		nItems = Integer.toString(argItems);
		if(isProgressive) {
			addVideoThumbsProg(getLatestSubVideos());
		} else {
			for(VideoThumb thumb:getVideoThumbs(getLatestSubVideos())) {
				myPanel.add(thumb);
			}
		}
	}
	

	public static void favouriteVideosView(int argItems) throws IOException, ServiceException {
		nItems = Integer.toString(argItems);
		URL metafeedUrl = new URL(BASEURL.concat(username).concat(FAVORITES).concat(MAXRESULT).concat(nItems));
		System.out.println("Getting favorite video entries...\n");
		VideoFeed resultFeed = myService.getFeed(metafeedUrl, VideoFeed.class);
		List<VideoEntry> entries = resultFeed.getEntries();
		for(VideoThumb thumb:getVideoThumbs(entries)) {
			myPanel.add(thumb);
		}
	}
	
	public static int[] calcMaxThumbs() {
		int[] tmpint = new int[3];
		if(isSmall) {
			int nx =  myFrame.getSize().width/VideoThumb.small.width;
			int ny = myFrame.getSize().height/VideoThumb.small.height;
			tmpint[0] = ny*nx;
			tmpint[1] = ((myFrame.getSize().width%VideoThumb.small.width))/nx;
			tmpint[2] = ((myFrame.getSize().height%VideoThumb.small.height))/ny;
		} else {
			int nx =  myFrame.getSize().width/VideoThumb.medium.width;
			int ny = myFrame.getSize().height/VideoThumb.medium.height;
			tmpint[0] = ny*nx;
			tmpint[1] = ((myFrame.getSize().width%VideoThumb.medium.width))/nx;
			tmpint[2] = ((myFrame.getSize().width%VideoThumb.medium.height))/ny;
		}
		
		return tmpint;
	}

	public static void onExit() {
		mplayer.onExit();
		myFrame.dispose();
	}
	
	public static void initNewSubPanel() throws MalformedURLException, IOException, ServiceException {
		
		newSubPanel.setBackground(globalFade);
		newSubPanel.setVisible(true);
		newSubPanel.validate();
		newSubPanel.init();
	}
	
	public static void main(String[] args) {

		try {
			//newSubPanel = new NewSubVideoPanel();
			//myFrame.add(newSubPanel);
			//UpploaderView u = new UpploaderView();
			//u.init(username);
			isSmall = true;
			JMenuBar menuBar = new JMenuBar();
			menuBar.setBackground(Color.white);
			menuBar.setVisible(true);
			menuBar.setPreferredSize(new Dimension(1024, 20));
			myFrame.setSize(1024, 600);
			myFrame.setJMenuBar(menuBar);
			myFrame.addWindowListener(new WindowClose());
			myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			myFrame.setVisible(true);
			//initNewSubPanel();
			
		}
		catch(AuthenticationException e) {
			e.printStackTrace();
		}
		catch(MalformedURLException e) {
			e.printStackTrace();
		}
		catch(ServiceException e) {
			e.printStackTrace();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}

}

class WindowClose implements WindowListener {
	@Override
	public void windowActivated(WindowEvent e) {	}
	@Override
	public void windowClosed(WindowEvent e) {
		YouTube.onExit();
	}
	@Override
	public void windowClosing(WindowEvent e) {
		YouTube.onExit();
	}
	@Override
	public void windowDeactivated(WindowEvent e) {}
	@Override
	public void windowDeiconified(WindowEvent e) {}
	@Override
	public void windowIconified(WindowEvent e) {}
	@Override
	public void windowOpened(WindowEvent e) {}
}
