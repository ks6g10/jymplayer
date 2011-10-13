import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.YouTubeMediaGroup;

public class VideoThumb extends JPanel implements Comparable<VideoThumb> {

	/**
	 * @param args
	 */
	private int x,y;
	private final String title;
	private final String description;
	private final String author;
	private final String lenght;
	private final String views;
	private final DateTime date;
	private final VideoEntry entry;
	private final YouTubeMediaGroup mediaGroup;
	private URL urlImage;
	private JLabel imageLabel,titleLabel,authLabel,lengthLabel,dateLabel;
	private JTextArea descArea;
	private JTextArea smallTitle;
	private final SpringLayout layout = new SpringLayout();
	private final String videoURL;
	public static final Dimension small = new Dimension(122, 122);
	public static final Dimension medium = new Dimension(338, 107);
	public static HashMap<String, Color> colormap = new HashMap<String, Color>();
	private final Color bkgColor;
	private Color titleColor;
	private final int colorBreak = 125; //when to change the color of title
	private Border border;

	public static Color getColor(String argUser) {
		if(colormap.containsKey(argUser))
			return colormap.get(argUser);
		int h = argUser.hashCode();
		if(h<0)
			h= -h;
		int r = h %255;
		h >>=8;
		int g = h %255;
		h >>=8;
		int b = h %255;
		colormap.put(argUser,new Color(r,g,b));

		return colormap.get(argUser);
	}

	@Override
	public boolean equals(Object argThumb) {
		if(!(argThumb instanceof VideoThumb))
			return false;
		return getVideoURL().equals(((VideoThumb) argThumb).getVideoURL());
	}

	public DateTime getDate() {
		return date;
	}

	private String durationToString(long argTime) {
		String returnString = (argTime/60)+":";
		if(argTime%60 < 10)
			returnString += "0";
		returnString += argTime%60;
		return returnString;
	}

	public String getVideoURL() {
		return videoURL;
	}
	public int compareTo(VideoThumb argObj) {
		return ((VideoThumb)argObj).getDate().compareTo(date);
	}

	public VideoThumb(VideoEntry argVideo) {
		entry = argVideo;
		mediaGroup = entry.getMediaGroup();
		views = "Views: 1261";
		lenght = durationToString(mediaGroup.getDuration());
		date = mediaGroup.getUploaded();
		title = entry.getTitle().getPlainText();
		description = mediaGroup.getDescription().getPlainTextContent();
		author = mediaGroup.getUploader();
		videoURL =entry.getLinks().get(0).getHref().split("&")[0];
		this.setLayout(layout);
		this.addMouseListener(new clickHandler());
		try {
			createJObjects();
		} catch (MalformedURLException e) {e.printStackTrace();}
		initLayouts();
		bkgColor = getColor(author);
		adjustTitleColor(bkgColor);
		this.setBackground(bkgColor);
		border = BorderFactory.createLineBorder(bkgColor);
		this.setBorder(border);
		//this.setBorder(BorderFactory.createBevelBorder(0));
		this.validate();
		this.setVisible(true);
	}


	private void initLayouts() {
		if(YouTube.isSmall) {
			initSpringSmall();
		} else {
			initSpringMedium();
		}
	}

	private void adjustTitleColor(Color argColor) {
		titleColor = Color.black;
		int r = argColor.getRed();
		int g = argColor.getGreen();
		int b = argColor.getBlue();
		int colorBrightness = (((r*299) + (g*587) + (b*114)) / 1000);
		if(colorBrightness < colorBreak) {
			titleColor = Color.white;
			titleLabel.setForeground(titleColor);
			titleLabel.validate();
			smallTitle.setForeground(titleColor);
			smallTitle.validate();
		}
	}

	private void createJObjects() throws MalformedURLException {
		//title area
		titleLabel = new JLabel(title);
		titleLabel.setToolTipText(title);
		titleLabel.setForeground(Color.white);
		smallTitle = new JTextArea(title);
		smallTitle.setEditable(false);  
		smallTitle.setCursor(null);
		smallTitle.setOpaque(false);  
		smallTitle.setFocusable(false);
		smallTitle.setLineWrap(true);
		smallTitle.setWrapStyleWord(false);
		smallTitle.setFont(getFont().deriveFont((float) 10));
		smallTitle.setRows(2);

		//author,date length and description area
		urlImage = new URL(mediaGroup.getThumbnails().get(0).getUrl());
		imageLabel = new JLabel(new ImageIcon(urlImage));
		authLabel = new JLabel(author);
		authLabel.addMouseListener(new mouseOverAuthor(this));
		dateLabel = new JLabel(date.toUiString());
		lengthLabel = new JLabel(lenght);
		descArea = new JTextArea(description.replaceAll("[\n\t]","" ));
		String tmpString = "<html>"+description.replaceAll("[\n]","<br>" )+"</html>";
		//descArea.get
		descArea.setToolTipText(tmpString);
		descArea.setEditable(false);  
		descArea.setCursor(null);  
		descArea.setOpaque(false);  
		descArea.setFocusable(false);
		descArea.setWrapStyleWord(true);
		descArea.setLineWrap(true);
	}

	public void initSpringSmall() {
		this.removeAll();
		this.setPreferredSize(small);
		//title =title.replaceAll("[ \t\n\f\r]","");
		//titleLabel.setText(title);
		//titleLabel.setFont(getFont().deriveFont((float) 7.5));
		//this.add(titleLabel);

		this.add(smallTitle);
		this.add(lengthLabel);
		this.add(authLabel);
		this.add(imageLabel);
		Color myBck = new Color(45, 45, 45);
		layout.putConstraint(SpringLayout.NORTH, this, -10, SpringLayout.NORTH, smallTitle);
		layout.putConstraint(SpringLayout.WEST, this, 0, SpringLayout.WEST, smallTitle);
		//layout.putConstraint(SpringLayout.NORTH, imageLabel, 0, SpringLayout.SOUTH, smallTitle);
		layout.putConstraint(SpringLayout.SOUTH, imageLabel, 0, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.SOUTH, lengthLabel, 0, SpringLayout.SOUTH, imageLabel);
		layout.putConstraint(SpringLayout.NORTH, authLabel, 0, SpringLayout.NORTH, imageLabel);
		//layout.putConstraint(SpringLayout.EAST, authLabel, 0, SpringLayout.EAST, imageLabel);

		//layout.putConstraint(SpringLayout.EAST, lengthLabel, 0, SpringLayout.EAST, this);
		LineBorder line = new LineBorder(myBck, 1,true);
		lengthLabel.setFont(getFont().deriveFont((float) 7.5));
		lengthLabel.setBorder(line);
		lengthLabel.setOpaque(true);
		lengthLabel.setHorizontalAlignment(JLabel.CENTER);
		lengthLabel.setBackground(myBck);
		lengthLabel.setForeground(Color.WHITE);
		authLabel.setFont(getFont().deriveFont((float) 7.5));
		authLabel.setBorder(line);
		authLabel.setOpaque(true);
		authLabel.setHorizontalAlignment(JLabel.CENTER);
		authLabel.setBackground(myBck);
		authLabel.setForeground(Color.WHITE);
		int tmpint = small.height - imageLabel.getPreferredSize().height-4;
		Dimension tmpD = new Dimension(small.width-2, tmpint);
		smallTitle.setPreferredSize(tmpD);
		this.validate();
	}

	public void initSpringMedium() {
		this.removeAll();
		this.setPreferredSize(medium);
		this.add(titleLabel);
		authLabel.setForeground(Color.BLUE);
		this.add(authLabel);
		this.add(imageLabel);
		//this.add(viewsLabel);
		this.add(dateLabel);
		this.add(lengthLabel);
		this.add(descArea);

		layout.putConstraint(SpringLayout.NORTH, this, 0, SpringLayout.NORTH, titleLabel);
		layout.putConstraint(SpringLayout.WEST, this, 0, SpringLayout.WEST, titleLabel);
		layout.putConstraint(SpringLayout.EAST, authLabel, 0, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.WEST, this, 0, SpringLayout.WEST, imageLabel);
		layout.putConstraint(SpringLayout.NORTH, this, 0, SpringLayout.NORTH, authLabel);
		//	layout.putConstraint(SpringLayout.EAST, titleLabel,0, SpringLayout.WEST, authLabel);
		layout.putConstraint(SpringLayout.SOUTH, imageLabel, 0, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.SOUTH, lengthLabel, 0, SpringLayout.NORTH, imageLabel);
		layout.putConstraint(SpringLayout.WEST, descArea, 0, SpringLayout.EAST, imageLabel);
		layout.putConstraint(SpringLayout.NORTH, descArea, 0, SpringLayout.SOUTH, titleLabel);
		layout.putConstraint(SpringLayout.EAST, descArea, 0, SpringLayout.EAST, this);

		layout.putConstraint(SpringLayout.SOUTH, dateLabel, 0, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.SOUTH, lengthLabel, 0, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.EAST, dateLabel, 0, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.WEST, lengthLabel, 0, SpringLayout.EAST, imageLabel);
		int tmpintx = medium.width - authLabel.getPreferredSize().width -5;
		titleLabel.setPreferredSize(new Dimension(tmpintx,titleLabel.getPreferredSize().height));
		int tmpintY = medium.height - dateLabel.getPreferredSize().height-authLabel.getPreferredSize().height;
		descArea.setPreferredSize(new Dimension(descArea.getPreferredSize().width, tmpintY));
		this.validate();
	}

	public void checkAuthorShow(String argAuthor) {
		if(!author.equals(argAuthor)) {
			this.setBackground(YouTube.globalFade);
			smallTitle.setForeground(YouTube.globalFade);
			this.setBorder(BorderFactory.createLineBorder(YouTube.globalFade));
			this.setOpaque(!isOpaque());
			imageLabel.setOpaque(!isOpaque());
			//this.validate();
		}
	}
	public void unshowAuthorVideos() {
		if(!this.getBackground().equals(bkgColor)) {
			this.setBackground(bkgColor);
			smallTitle.setForeground(titleColor);
			this.setBorder(border);
			this.setOpaque(!isOpaque());
			imageLabel.setOpaque(!isOpaque());
		}
	}

	public String getAuthor() {
		return author;
	}
	
	public GeneralVideoPanel getParentPanel() {
		return (GeneralVideoPanel) this.getParent();
	}
}

class clickHandler implements MouseListener {
	@Override
	public void mouseClicked(MouseEvent e) {
		YouTube.playVideo(((VideoThumb)e.getSource()).getVideoURL());
	}
	@Override
	public void mouseEntered(MouseEvent e){e.consume();}
	@Override
	public void mouseExited(MouseEvent e) {e.consume();}
	@Override
	public void mousePressed(MouseEvent e) {e.consume();}
	@Override
	public void mouseReleased(MouseEvent e) {e.consume();}
}

class mouseOverAuthor implements MouseListener {
	VideoThumb parentThumb;
	public mouseOverAuthor(VideoThumb argParent) {
		parentThumb = argParent;
	}
	@Override
	public void mouseClicked(MouseEvent e) {e.consume();}
	@Override
	public void mouseEntered(MouseEvent e) {
		
		parentThumb.getParentPanel().showAuthorVideos(((JLabel) e.getSource()).getText());
		//YouTube.showAuthorVideos(((JLabel) e.getSource()).getText());
		e.consume();
	}
	@Override
	public void mouseExited(MouseEvent e) {
		parentThumb.getParentPanel().unshowAuthorVideos();
		e.consume();
	}
	@Override
	public void mousePressed(MouseEvent e) {e.consume();}
	@Override
	public void mouseReleased(MouseEvent e) {e.consume();}
}