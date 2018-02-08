package balance;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Balances {

	private List<Balance> balances = new ArrayList<Balance>();
	private Comparator<Balance> comparator = new Comparator<Balance>() {

		@Override
		public int compare(Balance arg0, Balance arg1) {
			// sort acm to first
			if (arg0.getToken().equals("ACM")) {
				return -1;
			}
			
			if (arg1.getToken().equals("ACM")) {
				return 1;
			}
			
			// if values are not equal, sort by value
			if (arg0.getValue().compareTo(arg1.getValue()) != 0) {
				return -(arg0.getValue().compareTo(arg1.getValue()));
			}
			
			// sort rest by alphabetical order
			return arg0.getToken().compareTo(arg1.getToken());
		}
		
	};

	public Balances(List<Balance> b) {
		balances = b;
		Collections.sort(balances, comparator);
	}

	public List<Balance> getBalances() {
		return balances;
	}
	
	public String[] getTokens() {
		List<String> tokenList = new ArrayList<String>();
		for (Balance balance : balances) {
			tokenList.add(balance.getToken());
		}		
		return tokenList.toArray(new String[tokenList.size()]);
	}
	
	public BigDecimal getBalanceFor(String token) {
		for (Balance balance : balances) {
			if (balance.getToken() == token) {
				return balance.getValue();
			}
		}
		return BigDecimal.ZERO;
	}
	
}
