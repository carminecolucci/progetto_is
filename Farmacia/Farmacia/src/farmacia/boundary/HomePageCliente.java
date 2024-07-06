package farmacia.boundary;

import farmacia.boundary.creaordine.CreaOrdinePage;

import javax.swing.*;

public class HomePageCliente extends JFrame {

	private JPanel homePanel;
	private JButton btnCercaFarmaco;
	private JButton btnCreaOrdine;
	private JButton btnVisualizzaCatalogo;
	private JButton btnVisualizzaStoricoOrdini;
	private JLabel lblScelta;
	private JLabel lblBenvenuto;

	public HomePageCliente() {
		setSize(500, 500);
		setTitle("Homepage Cliente");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setContentPane(homePanel);
		setVisible(true);

		btnVisualizzaCatalogo.addActionListener(e -> {
			JFrame visualizzaCatalogo = new VisualizzaCatalogoPage();
		});

		btnCreaOrdine.addActionListener(e -> {
			JFrame creaOrdine = new CreaOrdinePage();
		});
	}
}
