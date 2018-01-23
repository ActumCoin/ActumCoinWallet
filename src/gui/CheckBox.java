package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBox;

public class CheckBox extends JCheckBox {
	private boolean checked = false;

	public CheckBox() {
		restOfConstrutor();
	}

	public CheckBox(Action arg0) {
		super(arg0);
		restOfConstrutor();
	}

	public CheckBox(Icon arg0, boolean arg1) {
		super(arg0, arg1);
		checked = arg1;
		restOfConstrutor();
	}

	public CheckBox(Icon arg0) {
		super(arg0);
		restOfConstrutor();
	}

	public CheckBox(String arg0, boolean arg1) {
		super(arg0, arg1);
		checked = arg1;
		restOfConstrutor();
	}

	public CheckBox(String arg0, Icon arg1, boolean arg2) {
		super(arg0, arg1, arg2);
		checked = arg2;
		restOfConstrutor();
	}

	public CheckBox(String arg0, Icon arg1) {
		super(arg0, arg1);
		restOfConstrutor();
	}

	public CheckBox(String arg0) {
		super(arg0);
		restOfConstrutor();
	}
	
	public boolean isChecked() {
		return checked;
	}

	private void restOfConstrutor() {
		this.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checked = !checked;
			}
		});
	}
	
}
