import java.awt.FlowLayout;
import java.util.List;
import java.io.IOException;
import java.net.MalformedURLException;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.util.ServiceException;


public class NewSubVideoPanel extends GeneralVideoPanel {
	private FlowLayout subLayout;
	
	public NewSubVideoPanel(){
		super();
		
	}
	
	public void init() throws MalformedURLException, IOException, ServiceException {
		
		int[] size =super.calcMaxThumbs();
		this.setLayout(new FlowLayout(0, size[1], size[2]));
		super.addPreThumbs(size[0]-1);
		EmptyVideoSlot tmp = new EmptyVideoSlot(0);
		this.add(tmp);
		super.emptyVideos.add(tmp);
		latestSubVideosView(size[0]-1, true);
		this.validate();
		
	}
	
	public void latestSubVideosView(int argItems, boolean isProgressive) throws MalformedURLException, IOException, ServiceException {
		YouTube.nItems = Integer.toString(argItems);
		if(isProgressive) {
			super.addVideoThumbsProg1(YouTube.getLatestSubVideos());
		} else {
			super.addVideoThumbs(YouTube.getLatestSubVideos());
		}
	}

	@Override
	public void fetchReminingVideos(int argEmptyIndex) {
			List<VideoEntry> asd = YouTube.getNextVideos(StatCol.NEWSUBSCIPTIONS, argEmptyIndex, super.thumbs.size());
			for(int i = 0; i <= argEmptyIndex; i++) {
				this.remove(super.emptyVideos.get(i));
			}
			for(int i = 0; i <= argEmptyIndex; i++) {
				super.emptyVideos.remove(i);
			}
			super.addVideoThumbs(asd);
		
	}
	
	

	
	
}
