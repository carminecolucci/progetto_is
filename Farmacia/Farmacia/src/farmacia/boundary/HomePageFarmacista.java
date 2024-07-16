package farmacia.boundary;

import farmacia.dto.DTO;

import javax.swing.*;
import java.util.Locale;

public class HomePageFarmacista extends JFrame {
	private JPanel mainPanel;
	private JTabbedPane tpnFarmacista;
	private JPanel pnlClienti;
	private JPanel pnlForniture;
	private JPanel pnlCatalogo;
	private JLabel lblBenvenuto;
	private JButton btnGeneraOrdineAcquisto;
	private JButton btnVisualizzaOrdiniClienti;
	private JButton btnVisualizzaOrdiniAcquisto;
	private JButton btnModificaFarmaco;
	private JButton btnRegistraRitiro;
	private JButton btnRegistraConsegnaOrdineAcquisto;
	private JButton btnAggiungiFarmaco;
	private JButton btnEliminaFarmaco;
	private JButton btnVisualizzaCatalogo;
	private JPanel pnlFarmaci;
	private JButton btnCercaFarmaco;
	private JPanel pnlBenvenuto;

	public HomePageFarmacista(DTO farmacista) {
		setTitle("Homepage Farmacista");
		setSize(500, 320);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setContentPane(mainPanel);
		setResizable(false);
		setVisible(true);
		lblBenvenuto.setText(String.format("Benvenuto, farmacista %s %s.", ((String)farmacista.get("nome")).toUpperCase(Locale.ITALY), ((String)farmacista.get("cognome")).toUpperCase(Locale.ITALY)));

		btnGeneraOrdineAcquisto.addActionListener(e -> new GeneraOrdineAcquistoPage());
		btnVisualizzaOrdiniClienti.addActionListener(e -> new VisualizzaOrdiniClientiPage());
		btnVisualizzaOrdiniAcquisto.addActionListener(e -> new VisualizzaOrdiniAcquistoPage());
		btnModificaFarmaco.addActionListener(e -> new ScegliFarmacoModificaPage());
		btnRegistraRitiro.addActionListener(e -> new RegistraRitiroOrdineClientePage());
		btnRegistraConsegnaOrdineAcquisto.addActionListener(e -> new RegistraConsegnaOrdineAcquistoPage());
		btnCercaFarmaco.addActionListener(e -> new CercaFarmacoPage());
		btnVisualizzaCatalogo.addActionListener(e -> new VisualizzaCatalogoFarmacistaPage());
		btnAggiungiFarmaco.addActionListener(e -> new AggiungiFarmacoPage());
		btnEliminaFarmaco.addActionListener(e -> new EliminaFarmacoPage());
	}
}
