import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;


public class NextPanelbutton extends JButton {
	
	private boolean isNext;
	
	public NextPanelbutton(boolean argNext) {
		isNext = argNext;
		if(isNext) {
			this.setText("Next Videos");
		} else {
			this.setText("Previus Videos");
		}
		this.addActionListener(new nextPanelHandler(argNext));
		
	}
	
	
}

class nextPanelHandler implements ActionListener {
	private boolean isNext;
	public nextPanelHandler(boolean argNext) {
		isNext = argNext;
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(isNext) 
			YouTube.nextPanel();
		else
			YouTube.prevPanel();
	}
	
	
}
