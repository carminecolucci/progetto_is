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

		btnGeneraOrdineAcquisto.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new GeneraOrdineAcquistoFarmacistaPage();
			}
		});
		btnVisualizzaOrdiniClienti.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new VisualizzaOrdiniClientiFarmacistaPage();
			}
		});
		btnVisualizzaOrdiniAcquisto.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new VisualizzaOrdiniAcquistoPage();
			}
		});
		btnModificaFarmaco.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new ModificaFarmacoPage();
			}
		});
		btnRegistraRitiro.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new RegistraRitiroOrdineClientePage();
			}
		});
		btnRegistraConsegnaOrdineAcquisto.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});
		btnCercaFarmaco.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new CercaFarmacoPage();
			}
		});
		btnVisualizzaCatalogo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new VisualizzaCatalogoPage();
			}
		});
		btnAggiungiFarmaco.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new AggiungiFarmacoPage();
			}
		});
		btnEliminaFarmaco.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new EliminaFarmacoPage();
			}
		});
	}
}
