package farmacia.boundary;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LoginPage extends JFrame {
	private JTextField username;
	private JPanel loginPanel;
	private JPasswordField password;
	private JButton loginButton;
	private JButton registratiButton;

	public LoginPage() {
		setSize(500, 500);
		setTitle("Mia Finestra");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setContentPane(loginPanel);
		setVisible(true);
		registratiButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame registerPage = new RegisterPage();
				setVisible(false);
				registerPage.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						setVisible(true);
					}
				});
			}
		});
	}
}
