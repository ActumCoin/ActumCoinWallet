package util;

import java.math.BigDecimal;

public class TransactionCode {

	/*
	 * syntax:
	 * first 3 are token ID ex.:
	 * acm
	 * next 4 are hex of whole number ex.: 
	 * 0001 next 5
	 * are hex of decimal ex.: 
	 * 0a0a0
	 * last 64 are address ex.:
	 * 80084bf2fba02475726feb2cab2d8215eab14bc6bdd8bfb2c8151257032ecd8b
	 * full code ex.:
	 * acm00010000180084bf2fba02475726feb2cab2d8215eab14bc6bdd8bfb2c8151257032ecd8b
	 */

	private String code;

	private String token;
	private BigDecimal amount;
	private String address;

	public TransactionCode(String c) {
		code = c;
		
		// break into parts
		String[] s = chunk(code);
		
		// token
		token = s[0];
		
		// amount
		int wholeNums = Integer.parseInt(s[1], 16) < 10000 ? Integer.parseInt(s[1], 16) : 10000;
		BigDecimal decimals = new BigDecimal(Integer.parseInt(s[2], 16) < 99999 ? Integer.parseInt(s[2], 16) : 99999);
		decimals = decimals.divide(new BigDecimal(100000));
		amount = new BigDecimal(wholeNums);
		amount = amount.add(decimals);
		
		// address
		address = s[3];
		
		System.out.println(token + "\n" + amount + "\n" + address);
	}

	public String getToken() {
		return token;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public String getAddress() {
		return address;
	}

	private String[] chunk(String c) {
		String[] s = new String[4];
		s[0] = c.substring(0, 3);
		s[1] = c.substring(3, 7);
		s[2] = c.substring(7, 12);
		s[3] = c.substring(12, 76);
		return s;
	}

}
