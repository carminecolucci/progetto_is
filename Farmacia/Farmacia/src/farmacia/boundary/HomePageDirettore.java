package farmacia.boundary;

import farmacia.dto.DTO;

import javax.swing.*;
import java.util.Locale;

public class HomePageDirettore extends JFrame {
	private JButton btnVisualizzaCatalogo;
	private JPanel mainPanel;
	private JButton btnCercaFarmaco;
	private JLabel lblBenvenuto;
	private JButton btnGeneraReport;

	public HomePageDirettore(DTO direttore) {
		setTitle("Homepage Direttore");
		setSize(500, 400);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setContentPane(mainPanel);
		setResizable(false);
		setVisible(true);
		lblBenvenuto.setText(String.format("Benvenuto, direttore %s %s", ((String)direttore.get("nome")).toUpperCase(Locale.ITALY), ((String)direttore.get("cognome")).toUpperCase(Locale.ITALY)));

		btnCercaFarmaco.addActionListener(e -> new CercaFarmacoPage());
		btnVisualizzaCatalogo.addActionListener(e -> new VisualizzaCatalogoFarmacistaPage());
		btnGeneraReport.addActionListener(e -> new GeneraReportPage());
	}
}
