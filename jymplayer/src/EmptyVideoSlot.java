import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class EmptyVideoSlot extends JLabel {
	private final ImageIcon normal,hover,click;
	//private final JLabel	lNormal,lHover,lClick;
	private final int index;
	
	public EmptyVideoSlot(int argIndex) {
		index = argIndex;
		normal = new ImageIcon(getClass().getResource("icons/addnormal.png"));
		hover = new ImageIcon(getClass().getResource("icons/addhover.png"));
		click = new ImageIcon(getClass().getResource("icons/addclick.png"));
		this.setIcon(normal);
		this.setPreferredSize(StatCol.small);
		this.setOpaque(true);
		this.setBackground(StatCol.globalFade);
		this.addMouseListener(new SlotMouseHandler());
	}
	
	
	public void setHover() {
		this.setIcon(hover);
	}
	public void setNormal() {
		this.setIcon(normal);
	}
	public void setClick() {
		this.setIcon(click);
	}
	public void clicked() {
		setHover();
		((GeneralVideoPanel) this.getParent()).fetchReminingVideos(index);
	}
}

class SlotMouseHandler implements MouseListener {

	@Override
	public void mouseClicked(MouseEvent e) {
		((EmptyVideoSlot) e.getSource()).clicked();
		e.consume();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		((EmptyVideoSlot) e.getSource()).setHover();
		e.consume();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		((EmptyVideoSlot) e.getSource()).setNormal();
		e.consume();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		((EmptyVideoSlot) e.getSource()).setClick();
		e.consume();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	
	}
	
}
