package gui;

import java.math.BigDecimal;

import javax.swing.*;

import net.miginfocom.swing.MigLayout;

public class SendDialog {

	public BigDecimal amount;
	public String address;
	public boolean repeat;

	public SendDialog() {
		Object[] options = { "Next", "Cancel" };

		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout());

		panel.add(new JLabel("Set the amount of ActumCoin you want to send."), "cell 0 0");
		panel.add(new JLabel("<html>&#164;</html>"), "cell 0 1, align center");
		JTextField amountField = new JTextField(20);
		panel.add(amountField, "cell 0 1, align center");

		int result = JOptionPane.showOptionDialog(null, panel, "Enter amount of ActumCoin",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, null);

		if (result == JOptionPane.YES_OPTION && !amountField.getText().isEmpty() && isNumeric(amountField.getText())
				&& new BigDecimal(amountField.getText()).compareTo(BigDecimal.ZERO) == 1) {
			amount = new BigDecimal(amountField.getText());
			address = (String) JOptionPane.showInputDialog(panel,
					"<html>Paste address to send &#164;" + amountField.getText() + "to.</html>",
					"Paste destination wallet address", JOptionPane.PLAIN_MESSAGE, null, null, "");
		} else if (result == JOptionPane.YES_OPTION) {
			JOptionPane.showMessageDialog(panel, "<html>Enter a <u>number</u> above 0</html>", "Error",
					JOptionPane.WARNING_MESSAGE);
			repeat = true;
		}
	}

	private static boolean isNumeric(String str) {
		return str.matches("-?\\d+(\\.\\d+)?");
	}

}
