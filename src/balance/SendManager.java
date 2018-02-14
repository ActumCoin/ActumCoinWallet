package balance;

import java.util.ArrayList;
import java.util.List;

import multichain.command.MultiChainCommand;
import multichain.command.MultichainException;
import multichain.object.BalanceAssetBase;

public class SendManager {
	
	private MultiChainCommand mCommand;
	private String address;
	private static SendManager instance = null;

	public SendManager(MultiChainCommand m, String a) {
		mCommand = m;
		address = a;
	}
	
	public void send(Balance balance, String rxAddress) throws MultichainException {
		List<BalanceAssetBase> babList = new ArrayList<BalanceAssetBase>();
		BalanceAssetBase bab = new BalanceAssetBase();
		bab.setName(balance.getToken());
		bab.setQty(balance.getValue().doubleValue());
		System.out.println(bab);
		babList.add(bab);
		mCommand.getWalletTransactionCommand().sendFromAddress(address, rxAddress, babList);
	}

	public static SendManager getInstance() {
		return instance;
	}
	
	public static void createInstance (MultiChainCommand m, String a) {
		instance = new SendManager(m, a);
	}

	public MultiChainCommand getmCommand() {
		return mCommand;
	}

}
