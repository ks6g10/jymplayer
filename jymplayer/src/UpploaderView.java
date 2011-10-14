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
		String feedUrl = YouTube.BASEURL;
		ArrayList<UserProfileEntry> tmpUserProfiles = new ArrayList<UserProfileEntry>(argUsernames.size());
		for(String user: argUsernames) {
			UserProfileEntry entry = YouTube.myService.getEntry(new URL( feedUrl.concat(user)), UserProfileEntry.class);//getFeed(new URL(feedUrl.concat(user)), UserProfileFeed.class);
			System.out.println(entry.getThumbnail().getUrl());
		}
		
	}
	
	public ArrayList<String> getSubsUsers(String argUser) throws MalformedURLException, IOException, ServiceException {
		ArrayList<String> subs = new ArrayList<String>();//subscription usernames
		String feedUrl =  YouTube.BASEURL.concat(argUser).concat(YouTube.SUBSCRIPTIONS);    
		SubscriptionFeed feed = YouTube.myService.getFeed(new URL(feedUrl), SubscriptionFeed.class);
		for(SubscriptionEntry entry : feed.getEntries()) {
			subs.add(entry.getUsername());	
		}
		return subs;
	}
	
	public void init(String argUser) throws MalformedURLException, IOException, ServiceException {
		getUserProfiles(getSubsUsers(argUser));
	}
	
}
