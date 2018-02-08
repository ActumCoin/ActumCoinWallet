package main;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import balance.Balance;
import balance.Balances;
import balance.SendManager;
import gui.GUI;
import gui.QR;
import multichain.command.MultiChainCommand;
import multichain.command.MultichainException;
import multichain.object.BalanceAsset;
import util.Preferences;

public class Main {

	public static void main(String[] args) {
		// init
		new Preferences();

		// connect to blockchain
		String ip = "localhost"; // <-will change lol
		String port = "6462";
		String user = "multichainrpc";
		String pass = "HdEvYTGhbHo457TmmN2FZWZfHbMegeFehPVEdKy8VJGi";
		MultiChainCommand m = new MultiChainCommand(ip, port, user, pass);


		// get address
		List<String> addresses = null;
		try {
			addresses = m.getAddressCommand().getAddresses();
		} catch (MultichainException e) {
			e.printStackTrace();
		}

		String address = null;

		for (String a : addresses) {
			address = a;
		}
		
		SendManager sm = new SendManager(m, address);

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

		new GUI(balances, address, sm);

	}

	private static BigDecimal /* (¬‿¬) */ createBigD(int whole, int dec) {
		BigDecimal decimals = new BigDecimal(dec < 99999 ? dec : 99999);
		decimals = decimals.divide(new BigDecimal(100000));
		BigDecimal amount = new BigDecimal(whole);
		amount = amount.add(decimals);
		return amount;
	}

}
