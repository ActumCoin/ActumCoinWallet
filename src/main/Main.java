package main;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
			Runtime.getRuntime().exec("multichaind e -daemon");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		String ip = "localhost";
		String port = "2688";
		String user = "multichainrpc";
		String pass = "6gPTcFXitC617RxEucprgxJXYwyiL2bQYUqi3cs4PAtM";
		MultiChainCommand m = new MultiChainCommand(ip, port, user, pass);

		String warning = null;
		
		

		GUI.createInstance(m);

	}

}
