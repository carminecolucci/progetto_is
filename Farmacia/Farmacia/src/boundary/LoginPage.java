package boundary;

import javax.swing.*;

public class LoginPage extends JFrame {
	private JTextField username;
	private JPanel loginPanel;
	private JPasswordField password;
	private JButton loginButton;

	public LoginPage() {
		setSize(500, 500);
		setTitle("Mia Finestra");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setContentPane(loginPanel);
		setVisible(true);
	}
}
