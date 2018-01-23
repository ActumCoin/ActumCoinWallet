package gui;

import java.awt.Toolkit;
import java.awt.datatransfer.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

class PopUpMenu extends JPopupMenu {
    JMenuItem anItem;
    
    public PopUpMenu(String toCopy){
        anItem = new JMenuItem("Copy");
        anItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
		    	StringSelection stringSelection = new StringSelection(toCopy);
		    	Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
		    	clpbrd.setContents(stringSelection, null);
			}
        	
        });
        add(anItem);
    }
    
}