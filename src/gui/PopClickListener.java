package gui;

import java.awt.event.*;

class PopClickListener extends MouseAdapter {
	
	private String toCopy;

	public PopClickListener(String toCopy) {
		this.toCopy = toCopy;
	}
	
    public void mousePressed(MouseEvent e){
        if (e.isPopupTrigger())
            doPop(e);
    }

    public void mouseReleased(MouseEvent e){
        if (e.isPopupTrigger())
            doPop(e);
    }

    private void doPop(MouseEvent e){
        PopUpMenu menu = new PopUpMenu(toCopy);
        menu.show(e.getComponent(), e.getX(), e.getY());
    }
    
}
