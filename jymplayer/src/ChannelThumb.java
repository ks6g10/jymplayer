import java.awt.Color;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.google.gdata.data.media.mediarss.MediaThumbnail;
import com.google.gdata.data.youtube.UserProfileEntry;


public class ChannelThumb extends JPanel {
	private final String author;
	private final JLabel imageLabel;
	private final URL imageURL;
	
	public ChannelThumb(UserProfileEntry argProfile) throws MalformedURLException {
		author = argProfile.getUsername();
		imageURL = new URL(argProfile.getThumbnail().getUrl());
		this.setPreferredSize(VideoThumb.small);
		imageLabel = new JLabel(new ImageIcon(imageURL));
		this.add(new JLabel(author));
		this.setBorder(BorderFactory.createLineBorder(Color.pink));
		this.add(imageLabel);
		this.setVisible(true);
	}
	
	
}
