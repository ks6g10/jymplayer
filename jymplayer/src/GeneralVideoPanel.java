import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.google.gdata.data.youtube.VideoEntry;


public abstract class GeneralVideoPanel extends JPanel {

	ArrayList<VideoThumb> thumbs = new ArrayList<VideoThumb>(25);
	private int xrow;
	protected int thumbCount =0;
	protected ArrayList<EmptyVideoSlot> emptyVideos = new ArrayList<EmptyVideoSlot>(10);
	protected ArrayList<VideoThumb> progThumbs;
	public GeneralVideoPanel() {
		this.setVisible(true);
		this.setBackground(StatCol.globalFade);
	}

	public abstract void fetchReminingVideos(int argEmptyIndex);


	public void addPreThumbs(int argItems) {
		progThumbs = new ArrayList<VideoThumb>(argItems);
		for(int i = 0;i < argItems; i++) {
			progThumbs.add((VideoThumb) this.add(new VideoThumb()));
		}	
	}

	public void addVideoThumbsProg1(List<VideoEntry> argEntries) {
		VideoThumb tmp;
		thumbCount += argEntries.size();
		for(int i=0; i<argEntries.size(); i++) {
			tmp = progThumbs.get(i);
			tmp.replace(argEntries.get(i));
			this.validate();
			thumbs.add(tmp);
		}
		progThumbs.clear();
	}
	public void addVideoThumbsProg(List<VideoEntry> argEntries) {
		VideoThumb tmp;
		thumbCount += argEntries.size();

		for(int i=0; i<argEntries.size(); i++) {
			tmp = new VideoThumb(argEntries.get(i));
			tmp.setVisible(false);
			this.add(tmp);
			tmp.setVisible(true);
			if(i%xrow==0)
				this.validate();
			thumbs.add(tmp);
		}
	}



	public void addVideoThumbs(List<VideoEntry> argEntries) {
		VideoThumb tmp;
		for(int i=0; i<argEntries.size(); i++) {
			tmp = new VideoThumb(argEntries.get(i));
			this.add(tmp);
			thumbs.add(tmp);
			tmp.setVisible(true);
		}
		this.validate();
	}

	public void toggleShowAuthorVideos(String argAuthor) {
		if(argAuthor == null) {
			for(VideoThumb argThumb:thumbs) {
				argThumb.toggleShowAuthorVideos(null);
			}
		} else {
			for(VideoThumb argThumb:thumbs) {
				argThumb.toggleShowAuthorVideos(argAuthor);
			}
		}
	}



	public ArrayList<VideoThumb> getVideoThumbs(List<VideoEntry> argEntries) {

		ArrayList<VideoThumb> thumbArrayList = new ArrayList<VideoThumb>(Integer.parseInt(YouTube.nItems));
		for(int i=0; i<argEntries.size(); i++) {
			thumbArrayList.add(new VideoThumb(argEntries.get(i)));
		}
		return thumbArrayList;
	}

	//nItems redundant if you call calcMaxThumbs
	public int[] calcMaxThumbs() {
		int[] tmpint = new int[3];
		int framex = this.getSize().width;
		int framey = this.getSize().height;
		if(YouTube.isSmall) {
			int nx = xrow =  framex/VideoThumb.small.width;
			int ny = framey/VideoThumb.small.height;
			tmpint[0] = ny*nx;
			tmpint[1] = ((framex%VideoThumb.small.width))/nx;
			tmpint[2] = ((framey%VideoThumb.small.height))/ny;
		} else {
			int nx =  framex/VideoThumb.medium.width;
			int ny = framey/VideoThumb.medium.height;
			tmpint[0] = ny*nx;
			tmpint[1] = ((framex%VideoThumb.medium.width))/nx;
			tmpint[2] = ((framey%VideoThumb.medium.height))/ny;
		}
		YouTube.nItems = Integer.toString(tmpint[0]);
		return tmpint;
	}
}

class detectResize implements ComponentListener {

	@Override
	public void componentHidden(ComponentEvent e) {
	}
	@Override
	public void componentMoved(ComponentEvent e) {
	}
	@Override
	public void componentResized(ComponentEvent e) {
	}
	@Override
	public void componentShown(ComponentEvent e) {
	}
}
