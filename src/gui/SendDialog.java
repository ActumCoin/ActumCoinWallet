package gui;

import java.math.BigDecimal;

import javax.swing.*;

import balance.Balance;
import balance.Balances;
import balance.SendManager;
import multichain.command.MultichainException;
import net.miginfocom.swing.MigLayout;

public class SendDialog {

	private BigDecimal amount;
	private String token;
	private String address;
	private boolean repeat;
	private SendManager sm;

	public SendDialog(Balances balances, SendManager s) {
		sm = s;
		
		String[] options = { "Next", "Cancel" };

		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout());

		String[] tokens = balances.getTokens();
		JComboBox dropdown = new JComboBox(tokens);

		panel.add(new JLabel("Set the amount and token you want to send."), "cell 0 0");
		panel.add(dropdown, "cell 0 1, align center");
		JTextField amountField = new JTextField(20);
		panel.add(amountField, "cell 0 1, align center");

		int result = JOptionPane.showOptionDialog(null, panel, "Enter amount and token",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, null);

		if (result == JOptionPane.YES_OPTION && !amountField.getText().isEmpty() && isNumeric(amountField.getText())
				&& new BigDecimal(amountField.getText()).compareTo(BigDecimal.ZERO) == 1) {
			amount = new BigDecimal(amountField.getText());
			token = (String) dropdown.getSelectedItem();

			// check if enough balance
			if (balances.getBalanceFor(token).compareTo(amount) == 1
					|| balances.getBalanceFor(token).compareTo(amount) == 0) {
				address = (String) JOptionPane.showInputDialog(panel,
						"Paste address to send " + token + " " + amountField.getText() + " to.",
						"Paste destination wallet address", JOptionPane.PLAIN_MESSAGE);
				
				// send
				try {
					s.send(new Balance(token, amount), address);
				} catch (MultichainException e) {
					e.printStackTrace();
				}
			} else {
				JOptionPane.showMessageDialog(panel, "You do not have enough " + token + " to complete the transaction.\nYou need " + amount.subtract(balances.getBalanceFor(token)) + " more " + token + ".", "Insufficient " + token, JOptionPane.WARNING_MESSAGE);
				repeat = true;
			}
		} else if (result == JOptionPane.YES_OPTION) {
			JOptionPane.showMessageDialog(panel, "<html>Enter a <u>number</u> above 0</html>", "Error",
					JOptionPane.WARNING_MESSAGE);
			repeat = true;
		}
	}

	private static boolean isNumeric(String str) {
		return str.matches("-?\\d+(\\.\\d+)?");
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public String getToken() {
		return token;
	}

	public String getAddress() {
		return address;
	}

	public boolean isRepeat() {
		return repeat;
	}

}
