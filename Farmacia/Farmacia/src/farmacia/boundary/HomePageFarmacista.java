package farmacia.boundary;

import farmacia.dto.DTO;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePageFarmacista extends JFrame {
	private JPanel mainPanel;
	private JLabel lblBenvenuto;
	private JLabel lblScelta;
	private JButton btnGeneraOrdineAcquisto;
	private JButton btnVisualizzaOrdiniClienti;
	private JButton btnVisualizzaOrdiniAcquisto;
	private JButton btnModificaFarmaco;
	private JButton btnRegistraRitiro;
	private JButton btnRegistraConsegnaOrdineAcquisto;
	private JButton btnAggiungiFarmaco;
	private JButton btnEliminaFarmaco;
	private JButton btnVisualizzaCatalogo;
	private JButton btnCercaFarmaco;

	public HomePageFarmacista(DTO farmacista) {
		setTitle("Homepage Farmacista");
		setSize(400, 400);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setContentPane(mainPanel);
		setResizable(false);
		setVisible(true);
		lblBenvenuto.setText(String.format("Benvenuto, farmacista %s %s.", farmacista.get("nome"), farmacista.get("cognome")));

		btnGeneraOrdineAcquisto.addActionListener(e -> new GeneraOrdineAcquistoFarmacistaPage());
		btnVisualizzaOrdiniClienti.addActionListener(e -> new VisualizzaOrdiniClientiFarmacistaPage());
		btnVisualizzaOrdiniAcquisto.addActionListener(e -> new VisualizzaOrdiniAcquistoPage());
		btnModificaFarmaco.addActionListener(e -> new ModificaFarmacoPage());
		btnRegistraRitiro.addActionListener(e -> new RegistraRitiroOrdineClientePage());
		btnRegistraConsegnaOrdineAcquisto.addActionListener(e -> new RegistraConsegnaOrdineAcquistoPage());
		btnCercaFarmaco.addActionListener(e -> new CercaFarmacoPage());
		btnVisualizzaCatalogo.addActionListener(e -> new VisualizzaCatalogoPage());
		btnAggiungiFarmaco.addActionListener(e -> new AggiungiFarmacoPage());
		btnEliminaFarmaco.addActionListener(e -> new EliminaFarmacoPage());
	}
}
