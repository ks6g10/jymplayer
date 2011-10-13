import java.awt.FlowLayout;
import java.util.List;
import java.io.IOException;
import java.net.MalformedURLException;
import com.google.gdata.data.youtube.VideoEntry;
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
		
		EmptyVideoSlot tmp = new EmptyVideoSlot(1);
		this.add(tmp);
		super.emptyVideos.add(tmp);
		this.validate();
	}
	
	public void latestSubVideosView(int argItems, boolean isProgressive) throws MalformedURLException, IOException, ServiceException {
		YouTube.nItems = Integer.toString(argItems);
		if(isProgressive) {
			super.addVideoThumbsProg(YouTube.getLatestSubVideos());
		} else {
			super.addVideoThumbs(YouTube.getLatestSubVideos());
		}
	}

	@Override
	public void fetchReminingVideos(int argEmptyIndex) {
			List<VideoEntry> asd = YouTube.getNextVideos(YouTube.NEWSUBSCIPTIONS, argEmptyIndex, super.thumbCount);
			for(int i = 1; i <= argEmptyIndex; i++) {
				this.remove(super.emptyVideos.get(i));
			}
		
			super.addVideoThumbs(asd);
		
	}
	
	

	
	
}
