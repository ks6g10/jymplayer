import java.awt.FlowLayout;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JPanel;

import com.google.gdata.data.youtube.SubscriptionEntry;
import com.google.gdata.data.youtube.SubscriptionFeed;
import com.google.gdata.data.youtube.UserProfileEntry;
import com.google.gdata.data.youtube.UserProfileFeed;
import com.google.gdata.util.ServiceException;


public class UpploaderView extends JPanel {

	
	public void getUserProfiles(ArrayList<String> argUsernames) throws MalformedURLException, IOException, ServiceException {
		String feedUrl = StatCol.BASEURL;
		for(String user: argUsernames) {
			UserProfileEntry entry = StatCol.myService.getEntry(new URL( feedUrl.concat(user)), UserProfileEntry.class);//getFeed(new URL(feedUrl.concat(user)), UserProfileFeed.class);
			this.add(new ChannelThumb(entry));
		}
		
	}
	
	public ArrayList<String> getSubsUsers() throws MalformedURLException, IOException, ServiceException {
		ArrayList<String> subs = new ArrayList<String>();//subscription usernames
		String feedUrl =  StatCol.BASEURL.concat(YouTube.username).concat(StatCol.SUBSCRIPTIONS);    
		SubscriptionFeed feed = StatCol.myService.getFeed(new URL(feedUrl), SubscriptionFeed.class);
		for(SubscriptionEntry entry : feed.getEntries()) {
			subs.add(entry.getUsername());	
		}
		return subs;
	}
	
	public void init() throws MalformedURLException, IOException, ServiceException {
		getUserProfiles(getSubsUsers());
		this.setLayout(new FlowLayout());
		this.setVisible(true);
		this.validate();
	}
	
}
