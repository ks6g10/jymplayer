
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

	static mPlayerWrapper mplayer;// = new mPlayerWrapper();
	static HashMap<String, HashMap<String, String>> allURLMap = new HashMap<String, HashMap<String,String>>();
	static VideoStreamFetcher vSF = new VideoStreamFetcher();	
	static boolean isSmall = true;
	static int xspace = 5, yspace = 5;
	static JFrame myFrame = new JFrame();
	static Component[] thumbs;
	static NewSubVideoPanel newSubPanel;
	static NewSubVideoPanel sub2;
	static ArrayList<NewSubVideoPanel> newSubPanels = new ArrayList<NewSubVideoPanel>(5);
	static DoublePanelStack newSubs;
	static DoublePanelStack currentStack = newSubs;

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
		tmpURL = getVideoURL(StatCol.currentResolution, allURLMap.get(argVideoUrl));
		System.out.println("test"+tmpURL);
		mplayer.run(tmpURL);
	}

	public static String getVideoURL(String argResolution, HashMap<String, String> argMap) {
		if(argResolution.equals(StatCol.FULLHD) ) {
			if(argMap.containsKey(StatCol.FULLHD))
				return argMap.get(StatCol.FULLHD);
		}
		if(argResolution.equals(StatCol.FULLHD) || argResolution.equals(StatCol.HD))  { //if full hd is not avaliable
			if(argMap.containsKey(StatCol.HD))
				return argMap.get(StatCol.HD);
		} 
		if(argMap.containsKey(StatCol.SD))
			return argMap.get(StatCol.SD);
		if(argMap.containsKey(StatCol.LD))
			return argMap.get(StatCol.LD);	
		return null;
	}




	public static List<VideoEntry> getUserEntries() throws MalformedURLException, IOException, ServiceException {
		VideoFeed videoFeed = null;
		String feedUrl =  StatCol.BASEURL.concat(StatCol.username).concat(StatCol.UPLOADS);
		videoFeed = StatCol.myService.getFeed(new URL(feedUrl), VideoFeed.class);
		return videoFeed.getEntries();
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
		if(mplayer != null)
			mplayer.onExit();	
		myFrame.dispose();
	}

	public static void nextPanel() {
		if(currentStack.hasNext()) {
			myFrame.remove(currentStack.getCurrentPanel());
			myFrame.add(currentStack.getNext());
			currentStack.getCurrentPanel().setVisible(true);
			myFrame.validate();
			myFrame.repaint();
		}
	}
	public static void prevPanel() {
		if(currentStack.hasPrev()) {
			myFrame.remove(currentStack.getCurrentPanel());
			myFrame.add(currentStack.getPrev());
			currentStack.getCurrentPanel().setVisible(true);
			myFrame.validate();
			myFrame.repaint();
		}
	}


	public static void main(String[] args) {

		try {
			//myFrame.add(newSubPanel);

			JTabbedPane tabbedPane = new JTabbedPane();
			isSmall = true;
			//tabbedPane.add(new JButton("asd"));
			JMenuBar menuBar = new JMenuBar();
			//menuBar.add(tabbedPane);
			menuBar.setBackground(Color.white);
			menuBar.setVisible(true);
			menuBar.add(new ResolutionButton());
			menuBar.add(new NextPanelbutton(false));
			menuBar.add(new NextPanelbutton(true));
			//menuBar.setPreferredSize(new Dimension(1024, 20));
			UpploaderView u = new UpploaderView();
			tabbedPane.addTab("Channel", u);
			tabbedPane.addTab("Videos", newSubPanel);
			//	menuBar.add(tabbedPane);
			//tabbedPane.setTabComponentAt(0,menuBar);

			//myFrame.add(u);
			//myFrame.add(tabbedPane);
			//	myFrame.setLayout(new FlowLayout());
			//Toolkit toolkit = Toolkit.getDefaultToolkit();
			//System.out.println(toolkit.getScreenSize().width+"x"+toolkit.getScreenSize().height);
			myFrame.setSize(1024, 600);
			myFrame.setJMenuBar(menuBar);
			myFrame.addWindowListener(new WindowClose());
			myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			myFrame.setVisible(true);
			Dimension panelDim = myFrame.getRootPane().getSize();
			//myFrame.add(newSubPanel);
			for(int i =0; i < 3;i++) {
				if(i == 0) {
					newSubs = new DoublePanelStack(new NewSubVideoPanel(panelDim));
					myFrame.add(newSubs.getCurrentPanel());
					myFrame.validate();
					myFrame.repaint();
				}
				if(StatCol.newsubsStartIndex < StatCol.MAXNEWSUBVIDEOS)
					newSubs.add(new NewSubVideoPanel(panelDim));
			}
			//u.init();
			//sub2 = new NewSubVideoPanel(newSubPanel.init(),panelDim);
			myFrame.validate();
			//myFrame.pack();

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
