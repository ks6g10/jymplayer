import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Stack;

import com.google.gdata.util.ServiceException;


public class DoublePanelStack {
	private Stack<GeneralVideoPanel> prevStack;
	private Stack<GeneralVideoPanel> nextStack;
	private GeneralVideoPanel currentPanel;
	
	public DoublePanelStack(GeneralVideoPanel argPanel) throws MalformedURLException, IOException, ServiceException {
		currentPanel = argPanel;
		nextStack = new Stack<GeneralVideoPanel>();
		prevStack = new Stack<GeneralVideoPanel>();
		currentPanel.init();
	}
	
	public boolean hasPrev() {
		return !prevStack.isEmpty();
	}
	public boolean hasNext() {
		return !nextStack.isEmpty();
	}
	
	public void add(GeneralVideoPanel argPanel) throws MalformedURLException, IOException, ServiceException {
		//nextStack.add(argPanel);
		nextStack.insertElementAt(argPanel, 0);
		argPanel.init();
	}
	
	public void clear() {
		nextStack.clear();
		prevStack.clear();
	}
	
	public GeneralVideoPanel getNext() {
		prevStack.push(currentPanel);
		if(nextStack.isEmpty())
			return null;
		currentPanel = nextStack.pop();
		return currentPanel;
	}
	
	public GeneralVideoPanel getCurrentPanel() {
		return currentPanel;
	}
	
	public GeneralVideoPanel getPrev() {
		nextStack.push(currentPanel);
		if(prevStack.isEmpty())
			return null;
		currentPanel = prevStack.pop();
		return currentPanel;
	}
	

}
