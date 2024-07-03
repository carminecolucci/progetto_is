package boundary;

import javax.swing.*;

public class Prova extends JFrame {
	private JTextArea textArea1;
	private JCheckBox checkBox1;
	private JButton button1;
	private JPanel mioPannello;

	public Prova() {
		setSize(500, 500);
		setTitle("Mia Finestra");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setContentPane(mioPannello);
		setVisible(true);
	}
}
