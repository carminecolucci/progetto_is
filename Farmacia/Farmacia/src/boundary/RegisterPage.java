package boundary;

import javax.swing.*;

public class RegisterPage extends JFrame {
	private JPanel mainPanel;
	private JTextField txtNome;
	private JTextField txtCognome;
	private JComboBox boxGiorno;
	private JComboBox boxMese;
	private JComboBox boxAnno;
	private JTextField txtUsername;
	private JPasswordField pswPassword;
	private JPasswordField pswRipetiPassword;
	private JButton btnRegistrati;

	public RegisterPage() {
		setSize(500, 300);

		//TODO: gestire mesi e giorni
		for (int i = 2024; i >= 1940; i--) {
			boxAnno.addItem(i);
		}

		setLocationRelativeTo(null);
		setContentPane(mainPanel);
		setTitle("Registrazione");
		setResizable(false);
		setVisible(true);
	}

}
