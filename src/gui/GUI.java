package gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Enumeration;

import javax.imageio.ImageIO;
import javax.swing.*;

public class GUI extends JFrame {
	private JFrame f;

	public GUI() {
		// init
		setUIFont(new javax.swing.plaf.FontUIResource("1234", Font.PLAIN, 26));
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		// logo
		JLabel logo = new JLabel(new ImageIcon("res/logo.png"));
		logo.setBounds(274, 0, 171, 119);// gah! not quite centered, should be right another 0.5 pixels

		// window close listener
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});

		add(logo);
		// icon
		try {
			setIconImage(ImageIO.read(new File("res/logo.png")));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		setTitle("ActumCoinWallet");
		setSize(720, 576);
		setLayout(null);
		setResizable(false);
		setVisible(true);
	}

	public static void setUIFont(javax.swing.plaf.FontUIResource f) {
		Enumeration keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof javax.swing.plaf.FontUIResource)
				UIManager.put(key, f);
		}
		UIManager.put("ToolTip.font", new javax.swing.plaf.FontUIResource("1234", Font.ITALIC, 14));
	}

}
