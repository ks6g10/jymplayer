
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
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;


public class YouTube {
	
	static mPlayerWrapper mplayer = new mPlayerWrapper();
	static HashMap<String, HashMap<String, String>> allURLMap = new HashMap<String, HashMap<String,String>>();
	static VideoStreamFetcher vSF = new VideoStreamFetcher();	
	static boolean isSmall = true;
	static int xspace = 5, yspace = 5;
	static JFrame myFrame = new JFrame();
	static JPanel myPanel = new JPanel();
	static Component[] thumbs;
	static NewSubVideoPanel newSubPanel;

	public static void playVideo(String argVideoUrl) {
		System.out.println(argVideoUrl);
		String tmpURL;
		if(!allURLMap.containsKey(argVideoUrl)) {
			//vSF = new VideoStreamFetcher();
			try {
				vSF.produceEntity(argVideoUrl);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			vSF.generateStreamURL();
			allURLMap.put(argVideoUrl, vSF.getURLMap());//puts urlmap inside allurlmap with video key 
		//	vSF.getURLMap()
		}
		tmpURL = getVideoURL(VideoStreamFetcher.HD, allURLMap.get(argVideoUrl));
		System.out.println("test"+tmpURL);
		mplayer.run(tmpURL);
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




	public static List<VideoEntry> getUserEntries() throws MalformedURLException, IOException, ServiceException {
		VideoFeed videoFeed = null;
		String feedUrl =  StatCol.BASEURL.concat(StatCol.username).concat(StatCol.UPLOADS);
		videoFeed = StatCol.myService.getFeed(new URL(feedUrl), VideoFeed.class);
		return videoFeed.getEntries();
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


//	public static void favouriteVideosView(int argItems) throws IOException, ServiceException {
//		nItems = Integer.toString(argItems);
//		URL metafeedUrl = new URL(StatCol.BASEURL.concat(StatCol.username).concat(StatCol.FAVORITES).concat(StatCol.MAXRESULT).concat(nItems));
//		System.out.println("Getting favorite video entries...\n");
//		VideoFeed resultFeed = StatCol.myService.getFeed(metafeedUrl, VideoFeed.class);
//		List<VideoEntry> entries = resultFeed.getEntries();
//		for(VideoThumb thumb:getVideoThumbs(entries)) {
//			myPanel.add(thumb);
//		}
//	}



	public static void onExit() {
		mplayer.onExit();
		myFrame.dispose();
	}

	public static void main(String[] args) {

		try {
			newSubPanel = new NewSubVideoPanel();
			//myFrame.add(newSubPanel);
			JTabbedPane tabbedPane = new JTabbedPane();
			isSmall = true;
			//tabbedPane.add(new JButton("asd"));
			JMenuBar menuBar = new JMenuBar();
			//menuBar.add(tabbedPane);
			menuBar.setBackground(Color.white);
			menuBar.setVisible(true);
			//menuBar.setPreferredSize(new Dimension(1024, 20));
			UpploaderView u = new UpploaderView();
			tabbedPane.addTab("Channel", u);
			tabbedPane.addTab("Videos", newSubPanel);
			//myFrame.add(u);
			//myFrame.add(tabbedPane);
			myFrame.add(newSubPanel);
			//	myFrame.setLayout(new FlowLayout());
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			System.out.println(toolkit.getScreenSize().width+"x"+toolkit.getScreenSize().height);
			myFrame.setSize(1900, 1080);
			//myFrame.setJMenuBar(menuBar);
			myFrame.addWindowListener(new WindowClose());
			myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			myFrame.setVisible(true);
			//u.init();
			newSubPanel.init();
		
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
