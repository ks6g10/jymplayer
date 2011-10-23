import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.VideoFeed;
import com.google.gdata.util.ServiceException;


public class NewSubVideoPanel extends GeneralVideoPanel {
	private FlowLayout subLayout;
	private int[] size;
	public NewSubVideoPanel(Dimension argDimension){
		super(argDimension);
	}
	
	public void init() throws MalformedURLException, IOException, ServiceException {
		size =super.calcMaxThumbs();
		subLayout = new FlowLayout();
		this.setLayout(new FlowLayout(FlowLayout.LEADING, size[1], size[2]));
		//super.addPreThumbs(size[0]);
		EmptyVideoSlot tmp = new EmptyVideoSlot(0);
		EmptyVideoSlot tmp1 = new EmptyVideoSlot(1);
		//this.add(tmp);
		//this.add(tmp1);
		super.emptyVideos.add(tmp);
		super.emptyVideos.add(tmp1);
		latestSubVideosView(size[0], true);
		this.validate();
		StatCol.panelsFinished++;
		StatCol.newsubsStartIndex += size[0];
	}
	
	public void latestSubVideosView(int argItems, boolean isProgressive) throws MalformedURLException, IOException, ServiceException {
			int nItems = argItems;
			List<VideoEntry> tmpVideoList = new ArrayList<VideoEntry>(nItems);
			while(nItems>0) {
				if(nItems > 49) {
					//super.addVideoThumbsProg(super.getVideos(StatCol.NEWSUBSCIPTIONS,49,firstIndex));
					tmpVideoList.addAll(super.getVideos(StatCol.NEWSUBSCIPTIONS,49,super.startIndex));
					nItems -= 49;
					super.startIndex +=49 ;
				} else {
					tmpVideoList.addAll(super.getVideos(StatCol.NEWSUBSCIPTIONS,nItems,super.startIndex));
					nItems = 0;
				}		
			}
			super.addVideoThumbsProg(tmpVideoList);
	}
	


	@Override
	public void fetchReminingVideos(int argEmptyIndex) {
			List<VideoEntry> asd = super.getVideos(StatCol.NEWSUBSCIPTIONS, argEmptyIndex, super.thumbs.size());
			for(int i = 0; i <= argEmptyIndex; i++) {
				this.remove(super.emptyVideos.get(i));
			}
			for(int i = 0; i <= argEmptyIndex; i++) {
				super.emptyVideos.remove(i);
			}
			super.addVideoThumbs(asd);
		
	}
	
	

	
	
}
