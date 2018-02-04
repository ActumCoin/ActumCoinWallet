package main;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import balance.Balance;
import balance.Balances;
import gui.GUI;
import multichain.command.MultiChainCommand;
import multichain.command.MultichainException;
import util.Preferences;

public class Main {

	public static void main(String[] args) {
		// init
		new Preferences();
		// connect to blockchain
		String ip = "192.168.56.1";
		String port = "6716";
		String user = "multichainrpc";
		String pass = "G8LRqDQNtdSRsDiaoZ2wZ3tps24LDMzUq1Ygww9g4YGy";
		MultiChainCommand m = new MultiChainCommand(ip, port, user, pass);
		
		// get addresses
		List<String> addresses = null;
		try {
			addresses = m.getAddressCommand().getAddresses();
		} catch (MultichainException e) {
			e.printStackTrace();
		}

		// balances
		List<Balance> b = new ArrayList<Balance>();
		b.add(new Balance("tst", createBigD(154, 0)));
		b.add(new Balance("lol", createBigD(23, 2342)));
		b.add(new Balance("fun", createBigD(69, 420)));
		b.add(new Balance("wtf", createBigD(666, 666)));
		b.add(new Balance("fun", createBigD(69, 420)));
		b.add(new Balance("acm", createBigD(1, 19823)));

		Balances balances = new Balances(b);

		new GUI(balances);

	}

	private static BigDecimal /* (¬‿¬) */ createBigD(int whole, int dec) {
		BigDecimal decimals = new BigDecimal(dec < 99999 ? dec : 99999);
		decimals = decimals.divide(new BigDecimal(100000));
		BigDecimal amount = new BigDecimal(whole);
		amount = amount.add(decimals);
		return amount;
	}

}
