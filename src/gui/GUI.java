package gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Enumeration;

import javax.imageio.ImageIO;
import javax.swing.*;

import util.Preferences;
import util.TransactionCode;

public class GUI extends JFrame {
	private JFrame f;
	private boolean isPreferences;

	public GUI() {
		// init
		setUIFont(new javax.swing.plaf.FontUIResource("Arial", Font.PLAIN, 26));
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		// logo
		JLabel logo = new JLabel(new ImageIcon("res/logo.png"));
		logo.setBounds(274, 0, 171, 119);// gah! not quite centered, should be right another 0.5 pixels

		// transaction code button
		JButton codeButton = new JButton("Enter Code");
		codeButton.setBounds(10, 10, 210, 40);
		codeButton.setBackground(Color.WHITE);

		// send button
		JButton sendButton = new JButton("Send ActumCoin");
		sendButton.setBounds(260, 139, 210, 40);
		sendButton.setBackground(Color.WHITE);

		// preferences button
		JButton preferencesButton = new JButton("Preferences");
		preferencesButton.setBounds(480, 10, 210, 40);
		preferencesButton.setBackground(Color.WHITE);

		// preferences stuff
		CheckBox linkCheckBox = new CheckBox("Link with miner", Preferences.isLink());
		linkCheckBox.setBounds(480, 60, 200, 30);
		linkCheckBox.setVisible(false);
		linkCheckBox.setToolTipText(
				"This allows ActumCoinWallet to automatically sync with your ActumMiner, if it's on this PC.");

		// balance
		JLabel balanceLabel = new JLabel("<html>&#164; " + /* placeholder >*/"1000.00000");
		balanceLabel.setBounds(10, 189, 700, 60);
		balanceLabel.setFont(new javax.swing.plaf.FontUIResource("1234", Font.PLAIN, 60));
		
		// address
		JLabel addressLabel = new JLabel(/* placeholder >*/"80084bf2fba02475726feb2cab2d8215eab14bc6bdd8bfb2c8151257032ecd8b");
		addressLabel.setBounds(10, 306, 700, 26);
		addressLabel.setFont(new javax.swing.plaf.FontUIResource("1234", Font.PLAIN, 16));
		addressLabel.addMouseListener(new PopClickListener(/* placeholder >*/"80084bf2fba02475726feb2cab2d8215eab14bc6bdd8bfb2c8151257032ecd8b"));

		// button listeners
		codeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String s = (String) JOptionPane.showInputDialog(f, "Enter a transaction code",
						"Enter a transaction code", JOptionPane.PLAIN_MESSAGE, null, null, "");

				if ((s != null) && (s.length() > 0)) {
					TransactionCode tc = new TransactionCode(s);

					int result = JOptionPane.showConfirmDialog(f, "<html>Confirm that you would like to send "
							+ (tc.getToken().equals("acm") ? "&#164;" : tc.getToken()) + tc.getAmount() + ".</html>",
							"Confirm transaction", JOptionPane.CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

					if (result == JOptionPane.OK_OPTION) {
						// send
					}
				}
			}
		});

		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SendDialog s = new SendDialog();

				while (s.repeat) {
					s = new SendDialog();
				}

				System.out.println(s.amount);
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
					// if already open\
					System.out.println(linkCheckBox.isChecked());
					Preferences.setLink(linkCheckBox.isChecked());
					preferencesButton.setText("Preferences");
				}

			}
		});

		// window close listener
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});

		add(logo);
		add(codeButton);
		add(sendButton);
		add(preferencesButton);
		add(linkCheckBox);
		add(balanceLabel);
		add(addressLabel);

		// icon
		try {
			setIconImage(ImageIO.read(new File("res/logo.png")));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		setTitle("ActumCoinWallet");
		setSize(720, 376);
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
