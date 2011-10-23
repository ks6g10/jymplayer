import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;


public class ResolutionButton extends JButton {
	private int state = 0;
	
	public ResolutionButton() {
		this.setText("1080P");
		this.setVisible(true);
		this.addActionListener(new switchResolution());
	}
	
	public void toggleResolution() {
		switch (state = ++state%3) {
		case 0:
			this.setText("1080P");
			StatCol.currentResolution = StatCol.FULLHD;
			break;
		case 1:
			this.setText("720P");
			StatCol.currentResolution = StatCol.HD;
			break;
		default:
			this.setText("360P");
			StatCol.currentResolution = StatCol.SD;
			break;
		}
	}
}

class switchResolution implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		((ResolutionButton) e.getSource()).toggleResolution();
	}
	
}
