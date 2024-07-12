package farmacia.boundary;

import farmacia.boundary.creaordine.CreaOrdinePage;
import farmacia.dto.DTO;

import javax.swing.*;
import java.util.Locale;

public class HomePageCliente extends JFrame {

	private JPanel homePanel;
	private JButton btnCercaFarmaco;
	private JButton btnCreaOrdine;
	private JButton btnVisualizzaCatalogo;
	private JButton btnVisualizzaStoricoOrdini;
	private JLabel lblScelta;
	private JLabel lblBenvenuto;

	public HomePageCliente(DTO cliente) {
		setSize(500, 500);
		setTitle("Homepage Cliente");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setContentPane(homePanel);
		setVisible(true);
		lblBenvenuto.setText(String.format("Benvenuto %s %s", ((String)cliente.get("nome")).toUpperCase(Locale.ITALY), ((String)cliente.get("cognome")).toUpperCase(Locale.ITALY)));

		btnVisualizzaCatalogo.addActionListener(e -> new VisualizzaCatalogoPage());

		btnCreaOrdine.addActionListener(e -> new CreaOrdinePage());

		btnVisualizzaStoricoOrdini.addActionListener(e -> new VisualizzaStoricoOrdiniPage());

		btnCercaFarmaco.addActionListener(e -> new CercaFarmacoPage());

	}
}
