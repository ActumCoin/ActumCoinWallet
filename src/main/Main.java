package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;

import balance.Balance;
import balance.Balances;
import balance.SendManager;
import gui.GUI;
import multichain.command.MultiChainCommand;
import multichain.command.MultichainException;
import multichain.object.BalanceAsset;

public class Main {

	public static void main(String[] args) {
		// connect to blockchain
		try {
			Runtime.getRuntime().exec("multichaind testacm");
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		String ip = "localhost";
		String port = "2688";
		String user = "multichainrpc";
		String pass = "";

		File conf;
		File params;

		// find the os
		String os = System.getProperty("os.name");

		// set the conf file path based on os
		if (os.substring(0, Math.min(os.length(), 7)).equals("Windows")) {
			// windows
			conf = new File(System.getenv("APPDATA") + "\\MultiChain\\testacm\\multichain.conf");
			params = new File(System.getenv("APPDATA") + "\\MultiChain\\testacm\\params.dat");
		} else {
			// linux/mac
			conf = new File("~/.multichain/testacm/multichain.conf");
			params = new File("~/.multichain/testacm/params.dat");
		}

		try {
			// get username and password for multichainrpc
			Scanner scanner = new Scanner(conf);

			int lineNum = 0;
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				lineNum++;
				if (line.startsWith("rpcuser=")) {
					user = line.substring(8, line.length());
				} else if (line.startsWith("rpcpassword=")) {
					pass = line.substring(12, line.length());
				}
			}
			
			// get port
			scanner = new Scanner(params);
			
			lineNum = 0;
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				lineNum++;
				if (line.startsWith("default-rpc-port = ")) {
					// allows port to be 2-4 characters and have a comment
					String portAndComment = line.substring(18, line.length()).trim();
					port = portAndComment.split(" ")[0];
					System.out.println(port + " " + portAndComment);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		MultiChainCommand m = new MultiChainCommand(ip, port, user, pass);
		
		// so the wallet doesnt mine
		try {
			m.getMiningCommand().pauseMining();
		} catch (MultichainException e) {
			e.printStackTrace();
		}

		GUI.createInstance(m);

	}

}
