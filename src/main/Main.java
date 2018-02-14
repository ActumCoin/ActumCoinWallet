package main;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import balance.Balance;
import balance.Balances;
import balance.SendManager;
import gui.GUI;
import gui.QR;
import multichain.command.MultiChainCommand;
import multichain.command.MultichainException;
import multichain.object.BalanceAsset;
import util.LinkManager;
import util.Preferences;

public class Main {

	public static void main(String[] args) {
		// init
		new Preferences();

		// connect to blockchain
		String ip = "localhost"; // <-will change lol
		String port = "4796";
		String user = "multichainrpc";
		String pass = "DzLbpqFWStpVajzyokHeYUmuYcVq5fUuPweV8fzW11Pf";
		MultiChainCommand m = new MultiChainCommand(ip, port, user, pass);

		String warning = null;
		
		// get address
		List<String> addresses = null;
		try {
			addresses = m.getAddressCommand().getAddresses();
		} catch (MultichainException e) {
			e.printStackTrace();
			warning = "ActumWallet could not connect to the Actum blockchain. Please check your internet connection and try again.";
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

		GUI gui = new GUI(balances, address);
		
		if (warning != null) {
			gui.message(warning, "", JOptionPane.WARNING_MESSAGE);
		}
		
		// check if link is already set
		if (Preferences.isLink()) {
			// if so, link
			LinkManager.link();
		}

	}

}
