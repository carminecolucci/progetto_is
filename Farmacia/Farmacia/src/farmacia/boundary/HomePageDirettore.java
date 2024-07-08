package farmacia.boundary;

import farmacia.dto.DTO;

import javax.swing.*;

public class HomePageDirettore extends JFrame {
	private JButton btnVisualizzaCatalogo;
	private JPanel mainPanel;
	private JButton btnCercaFarmaco;
	private JLabel lblBenvenuto;
	private JButton btnGeneraReport;

	public HomePageDirettore(DTO direttore) {
		setTitle("Homepage Direttore");
		setSize(600, 400);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setContentPane(mainPanel);
		setResizable(false);
		setVisible(true);
		lblBenvenuto.setText(String.format("Benvenuto, direttore %s %s.", direttore.get("nome"), direttore.get("cognome")));

		btnCercaFarmaco.addActionListener(e -> new CercaFarmacoPage());
		btnVisualizzaCatalogo.addActionListener(e -> new VisualizzaCatalogoPage());
		btnGeneraReport.addActionListener(e -> new GeneraReportPage());
	}
}
