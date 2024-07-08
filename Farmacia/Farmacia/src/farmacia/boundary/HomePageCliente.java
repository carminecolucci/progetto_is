package farmacia.boundary;

import farmacia.boundary.creaordine.CreaOrdinePage;
import farmacia.dto.DTO;
import farmacia.exceptions.DBException;

import javax.swing.*;

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
		lblBenvenuto.setText(String.format("Benvenuto %s %s", cliente.get("nome"), cliente.get("cognome")));

		btnVisualizzaCatalogo.addActionListener(e -> new VisualizzaCatalogoPage());

		btnCreaOrdine.addActionListener(e -> new CreaOrdinePage());

		btnVisualizzaStoricoOrdini.addActionListener(e -> {
			try {
				new VisualizzaStoricoOrdiniPage();
			} catch (DBException ex) {
				throw new RuntimeException(ex);
			}
		});

		btnCercaFarmaco.addActionListener(e -> new CercaFarmacoPage());
	}
}
