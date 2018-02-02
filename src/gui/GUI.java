package gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;

import javax.imageio.ImageIO;
import javax.swing.*;

import mining.MiningManager;
import util.Preferences;

public class GUI extends JFrame {
	private JFrame f;
	private boolean isPreferences = false;

	public GUI() {
		// init
		setUIFont(new javax.swing.plaf.FontUIResource("Arial", Font.PLAIN, 26));
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		// logo
		JLabel logo = new JLabel(new ImageIcon("res/logo.png"));
		logo.setBounds(274, 0, 171, 119);// gah! not quite centered, should be right another 0.5 pixels

		// mine button
		JButton mineButton = new JButton("Start Mining");
		mineButton.setBounds(260, 139, 210, 40);
		mineButton.setBackground(Color.WHITE);
		mineButton.setEnabled(Preferences.getAddress() != null);

		// preferences button
		JButton preferencesButton = new JButton("Preferences");
		preferencesButton.setBounds(480, 10, 210, 40);
		preferencesButton.setBackground(Color.WHITE);

		// preferences stuff
		CheckBox linkCheckBox = new CheckBox("Link with wallet", Preferences.isLink());
		linkCheckBox.setBounds(480, 60, 200, 30);
		linkCheckBox.setVisible(false);
		linkCheckBox.setToolTipText(
				"This allows ActumMiner to automatically sync with your ActumWallet, if it's on this PC.");

		// address button
		JButton addressButton = new JButton("Set Address");
		addressButton.setBounds(10, 10, 210, 40);
		addressButton.setBackground(Color.WHITE);

		// display current address
		JLabel currentAddress = new JLabel(Preferences.getAddress());
		currentAddress.setBounds(10, 10, 210, 100);
		currentAddress.setFont(new javax.swing.plaf.FontUIResource("1234", Font.ITALIC, 14));

		// log
		LiveLog log = new LiveLog("Log", new String[7], 20, 220, 660, 276,
				new String[] { "Ready to mine...", "Currently mining..." }, 0);
		DateFormat df = DateFormat.getTimeInstance();

		// button listeners
		mineButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Calendar cal = Calendar.getInstance();
				Timestamp time = new Timestamp(cal.getTimeInMillis());
				// toggle mining
				if (MiningManager.isCurrentlyMining()) {
					mineButton.setText("Start Mining");
					log.log("Stopped mining: " + df.format(new Date(time.getTime())), 0);
					MiningManager.stopMining();
				} else {
					mineButton.setText("Stop Mining");
					log.log("Started mining: " + df.format(new Date(time.getTime())), 1);
					MiningManager.mine();
				}
			}
		});

		preferencesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isPreferences = !isPreferences;
				linkCheckBox.setVisible(isPreferences);
				if (isPreferences) {
					// if already closed
					preferencesButton.setText("Save");
				} else {
					// if already open
					Preferences.setLink(linkCheckBox.isChecked());
					preferencesButton.setText("Preferences");
					
					// log
					Calendar cal = Calendar.getInstance();
					Timestamp time = new Timestamp(cal.getTimeInMillis());
					log.log("Preferences updated: " + df.format(new Date(time.getTime())), log.getStatus());
				}

			}
		});

		addressButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String s = (String) JOptionPane.showInputDialog(f, "Paste your wallet address",
						"Paste your wallet address", JOptionPane.PLAIN_MESSAGE, null, null, "");

				if ((s != null) && (s.length() > 0)) {
					Preferences.setAddress(s);
					currentAddress.setText(s);
					
					// update mine button enabled
					mineButton.setEnabled(Preferences.getAddress() != null);
					
					// log
					Calendar cal = Calendar.getInstance();
					Timestamp time = new Timestamp(cal.getTimeInMillis());
					log.log("Address updated: " + df.format(new Date(time.getTime())), log.getStatus());
				}
			}
		});

		// window close listener
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (MiningManager.isCurrentlyMining()) {
					int n = JOptionPane.showConfirmDialog(f,
							"You are currently mining, would you like to stop and exit?", "Currently mining",
							JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					if (n == JOptionPane.YES_OPTION) {
						MiningManager.stopMining();
						dispose();
					}
				} else {
					dispose();
				}
			}
		});

		add(logo);
		add(mineButton);
		add(preferencesButton);
		add(linkCheckBox);
		add(addressButton);
		add(currentAddress);
		add(log.getLabel());

		// icon
		try {
			setIconImage(ImageIO.read(new File("res/logo.png")));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		setTitle("ActumMiner");
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
		UIManager.put("ToolTip.font", new javax.swing.plaf.FontUIResource(f.getFontName(), Font.ITALIC, 14));
	}

}
