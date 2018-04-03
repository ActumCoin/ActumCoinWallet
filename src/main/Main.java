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
		String ip   = "localhost";
		String port = "2688";
		String user = "multichainrpc";
		String pass = "";

		File conf;

		// find the os
		String os = System.getProperty("os.name");
		
		// set the conf file path based on os
		if (os.substring(0, Math.min(os.length(), 7)).equals("Windows")) {
			// windows
			conf = new File(System.getenv("APPDATA") + "\\MultiChain\\testacm\\multichain.conf");
		} else {
			// linux/mac
			conf = new File("~/.multichain/testacm/multichain.conf");
		}

		// get username and password for multichainrpc
		try {
		    Scanner scanner = new Scanner(conf);

		    int lineNum = 0;
		    while (scanner.hasNextLine()) {
		        String line = scanner.nextLine();
		        lineNum++;
		        if (line.substring(0, Math.min(line.length(), 8)).equals("rpcuser=")) {
		        	user = line.substring(8, line.length());
		        	System.out.println(user);
		        } else if (line.substring(0, Math.min(line.length(), 12)).equals("rpcpassword=")) { 
		            pass = line.substring(12, line.length());
		            System.out.println(pass);
		        }
		    }
		} catch(FileNotFoundException e) { 
		    e.printStackTrace();
		}
		
		// connect to blockchain
		try {
			Runtime.getRuntime().exec("multichaind testacm");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		MultiChainCommand m = new MultiChainCommand(ip, port, user, pass);

		GUI.createInstance(m);

	}

}
