package gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.*;

import balance.Balance;
import balance.BalanceManager;
import balance.Balances;
import balance.SendManager;
import multichain.command.MultiChainCommand;
import multichain.command.MultichainException;
import multichain.object.BalanceAsset;

public class GUI extends JFrame {
	private JFrame f;
	private String address;
	private Balances balances;
	private static GUI instance;

	public GUI(MultiChainCommand m) {
		// get address
		List<String> addresses = null;
		try {
			addresses = m.getAddressCommand().getAddresses();
		} catch (MultichainException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(f, "ActumWallet could not connect to the Actum blockchain. Please check your internet connection and try again.", "Error!", JOptionPane.WARNING_MESSAGE);
		}

		String address = null;

		for (String a : addresses) {
			address = a;
		}

		SendManager.createInstance(m, address);

		/* balances */
		// get balances
		List<BalanceAsset> balancesList = null;
		try {
			balancesList = m.getBalanceCommand().getTotalBalances();
		} catch (MultichainException e) {
			e.printStackTrace();
		}

		List<Balance> b = new ArrayList<Balance>();
		for (BalanceAsset ba : balancesList) {
			b.add(new Balance(ba.getName(), new BigDecimal(ba.getQty())));
		}

		Balances balances = new Balances(b);
		
		final String la = address;
		
		Thread recievedListener = new Thread() {
			public void run() {
				try {
					while(!BalanceManager.getInstance().checkReceivedGreater(new Balance("acm",  BigDecimal.ZERO), la)) {
						TimeUnit.MINUTES.sleep(1);
					}
					GUI.getInstance().destroy();
					GUI.createInstance(m);
				} catch (MultichainException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};

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
		JLabel balanceLabel;
		try {
			balanceLabel = new JLabel(balances.getBalances().get(0).toString());
		} catch (IndexOutOfBoundsException e) {
			balanceLabel = new JLabel("No Balances");
		}
		balanceLabel.setBounds(10, 189, 700, 60);
		balanceLabel.setFont(new javax.swing.plaf.FontUIResource("Arial", Font.PLAIN, 60));

		int currentIndex = 0;
		List<JLabel> balanceLabels = new ArrayList<JLabel>();
		for (Balance balance : balances.getBalances()) {
			if (currentIndex != 0 && currentIndex <= 5) {
				JLabel label = new JLabel(balance.toString());
				label.setBounds(col(currentIndex), row(currentIndex), 700, 60);
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
				SendDialog s = new SendDialog(balances, m);

				while (s.isRepeat()) {
					s = new SendDialog(balances, m);
				}

			}
		});

		helpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					openWebpage(new URL("https://actumcrypto.org/help/actum-wallet"));
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
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
		return 199 + ((i % 2) == 0 ? 2 : 1) * 30;
	}

	private int col(int i) {
		// returns col x coordinate
		float half = i / 2f;
		return (int) (10 + (Math.round(half) - 1) * 230);
	}

	public static boolean openWebpage(URI uri) {
		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
			try {
				desktop.browse(uri);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public static boolean openWebpage(URL url) {
		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
			try {
				desktop.browse(url.toURI());
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public static GUI getInstance() {
		return instance;
	}

	public static void createInstance(MultiChainCommand m) {
		instance = new GUI(m);
	}

	public void destroy() {
		dispose();
	}

}
