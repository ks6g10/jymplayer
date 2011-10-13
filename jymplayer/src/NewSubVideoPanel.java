import java.awt.FlowLayout;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;



import com.google.gdata.util.ServiceException;


public class NewSubVideoPanel extends GeneralVideoPanel {

	
	public NewSubVideoPanel(){
		super();
		
	}
	
	public void init() throws MalformedURLException, IOException, ServiceException {
		int[] size =super.calcMaxThumbs();
		this.setLayout(new FlowLayout(0, size[1], size[2]));
		latestSubVideosView(size[0]-1, false);
		//new ImageIcon("icons\\addnormal.png")
		
		
		this.add(new EmptyVideoSlot());
	}
	
	public void latestSubVideosView(int argItems, boolean isProgressive) throws MalformedURLException, IOException, ServiceException {
		YouTube.nItems = Integer.toString(argItems);
		if(isProgressive) {
			super.addVideoThumbsProg(YouTube.getLatestSubVideos());
		} else {
			super.addVideoThumbs(YouTube.getLatestSubVideos());
		}
	}
	
	

	
	
}
