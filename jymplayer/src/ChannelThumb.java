import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class ChannelThumb extends JPanel {
	private final String author = "talesoflumin";
	private final JLabel imageLabel;
	
	public ChannelThumb() {
		
		this.setPreferredSize(VideoThumb.small);
		imageLabel = new JLabel(new ImageIcon("/home/ks6g10/1.png"));
		this.add(new JLabel(author));
		this.setBorder(BorderFactory.createLineBorder(Color.pink));
		this.add(imageLabel);
		this.setVisible(true);
	}
	
	
}
