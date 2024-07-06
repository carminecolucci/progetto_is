package farmacia.boundary;

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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setContentPane(homePanel);
	}
}
