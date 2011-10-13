import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.google.gdata.data.youtube.VideoEntry;


public class GeneralVideoPanel extends JPanel {

	ArrayList<VideoThumb> thumbs = new ArrayList<VideoThumb>(25);
	private int xrow;
	public GeneralVideoPanel() {
		this.setVisible(true);
	}

	public void addVideoThumbsProg(List<VideoEntry> argEntries) {
		VideoThumb tmp;
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

	public void showAuthorVideos(String argAuthor) {

		for(VideoThumb argThumb:thumbs) {
			argThumb.checkAuthorShow(argAuthor);
		}
	}
	public void unshowAuthorVideos() {
		for(VideoThumb argThumb:thumbs) {
			argThumb.unshowAuthorVideos();
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
