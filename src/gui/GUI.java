package gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

import balance.Balance;
import balance.Balances;

public class GUI extends JFrame {
	private JFrame f;
	private String address;
	private Balances balances;

	public GUI(Balances b, String a) {
		// variables
		address = a;
		balances = b;
		
		// init
		setUIFont(new javax.swing.plaf.FontUIResource("Arial", Font.PLAIN, 26));
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		// logo
		JLabel logo = new JLabel(new ImageIcon("res/logo.png"));
		logo.setBounds(75, 65, 100, 100);// gah! not quite centered, should be right another 0.5 pixels

		// send button
		JButton sendButton = new JButton("Send");
		sendButton.setBounds(260, 10, 210, 40);
		sendButton.setBackground(Color.WHITE);

		// info
		JLabel info = new JLabel("ActumWallet v1.0.0");
		info.setBounds(30, 10, 210, 40);
		info.setFont(new javax.swing.plaf.FontUIResource("Arial", Font.PLAIN, 22));
		
		// help button
		JButton helpButton = new JButton("Help");
		helpButton.setBounds(480, 10, 210, 40);
		helpButton.setBackground(Color.WHITE);

		// balances
		JLabel balanceLabel = new JLabel(balances.getBalances().get(0).toString());
		balanceLabel.setBounds(10, 189, 700, 60);
		balanceLabel.setFont(new javax.swing.plaf.FontUIResource("Arial", Font.PLAIN, 60));
		
		int currentIndex = 0;
		List<JLabel> balanceLabels = new ArrayList<JLabel>();
		for (Balance balance : balances.getBalances()) {
			if (currentIndex != 0 && currentIndex <= 5) {
				JLabel label = new JLabel(balance.toString());
				label.setBounds(col(currentIndex),  row(currentIndex), 700, 60);
				balanceLabels.add(label);
			}
			currentIndex++;
		}
		
		// all button
		if (balances.getBalances().size() > 6) {
			// button
			JButton allButton = new JButton("See All");
			allButton.setBounds(470, 272, 130, 30);
			allButton.setBackground(Color.WHITE);
			
			// action listener and all window
			String bs = "";
			for (Balance balance : balances.getBalances()) {
				bs += balance.toString() + "\n";
			}
			final String balanceString = bs;
			allButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JOptionPane.showMessageDialog(f, balanceString, "Balances", JOptionPane.PLAIN_MESSAGE);
				}
			});
			
			// add
			add(allButton);
		}
		
		// address
		JLabel addressLabel = new JLabel(address);
		addressLabel.setBounds(10, 306, 700, 26);
		addressLabel.setFont(new javax.swing.plaf.FontUIResource("Arial", Font.PLAIN, 16));
		addressLabel.addMouseListener(new PopClickListener(address));

		// button listeners
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SendDialog s = new SendDialog(balances);

				while (s.isRepeat()) {
					s = new SendDialog(balances);
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
		add(sendButton);
		add(info);
		add(helpButton);
		add(balanceLabel);
		add(addressLabel);
		
		// balances
		for (JLabel label : balanceLabels) {
			add(label);
		}

		// icon
		try {
			setIconImage(ImageIO.read(new File("res/logo.png")));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		setTitle("ActumWallet");
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
	
	private int row(int i) {
		// returns row y coordinate
		return 199 + ((i%2)==0 ? 2 : 1)*30;
	}
	
	private int col(int i) {
		// returns col x coordinate
		float half = i/2f;
		return (int) (10 + (Math.round(half)-1)*230);
	}
	
	public void message(String title, String message, int type) {
		JOptionPane.showMessageDialog(f, message, title, type);
	}

}
