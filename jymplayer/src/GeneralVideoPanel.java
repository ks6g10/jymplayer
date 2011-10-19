import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;

import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.VideoFeed;
import com.google.gdata.util.ServiceException;


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
	
	public static List<VideoEntry> getVideos(String argFeed, int argNItems, int argStartIndex){
		String nItems = Integer.toString(argNItems+1);//plus 1 for index correction
		String index = Integer.toString(argStartIndex+1);//plus one for index correction
		String feedUrl = StatCol.BASEURL.concat(StatCol.username);
		feedUrl = feedUrl.concat(argFeed); // what feed it is
		feedUrl = feedUrl.concat(StatCol.MAXRESULT).concat(nItems); // max results
		feedUrl = feedUrl.concat(StatCol.FIRSTINDEX).concat(index);; // first index
		VideoFeed feed = null;
		try {
			feed = StatCol.myService.getFeed(new URL(feedUrl), VideoFeed.class);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return feed.getEntries();
	}

	public void addPreThumbs(int argItems) {
		progThumbs = new ArrayList<VideoThumb>(argItems);
		for(int i = 0;i <= argItems; i++) {
			progThumbs.add((VideoThumb) this.add(new VideoThumb()));
		}	
		System.out.println(progThumbs.size());
	}

	public void addVideoThumbsProg1(List<VideoEntry> argEntries) {
		VideoThumb tmp;
		thumbCount += argEntries.size();
		Iterator<VideoThumb> iter = progThumbs.iterator();
		for(VideoEntry entry: argEntries) {
			tmp = iter.next();
			iter.remove();
			tmp.replace(entry);
			thumbs.add(tmp);
		}
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
		System.out.println(thumbs.size());
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
			for(VideoThumb argThumb:thumbs) {
				argThumb.toggleShowAuthorVideos(argAuthor);
			}
			this.validate();
			this.repaint();//increase performance with like 100%;
	}



	public ArrayList<VideoThumb> getVideoThumbs(List<VideoEntry> argEntries) {

		ArrayList<VideoThumb> thumbArrayList = new ArrayList<VideoThumb>(Integer.parseInt("32"));
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
			int nx = xrow =  framex/StatCol.small.width;
			int ny = framey/StatCol.small.height;
			tmpint[0] = ny*nx;
			tmpint[1] = ((framex%StatCol.small.width))/nx;
			tmpint[2] = ((framey%StatCol.small.height))/ny;
		} else {
			int nx =  framex/StatCol.medium.width;
			int ny = framey/StatCol.medium.height;
			tmpint[0] = ny*nx;
			tmpint[1] = ((framex%StatCol.medium.width))/nx;
			tmpint[2] = ((framey%StatCol.medium.height))/ny;
		}
		
		
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
