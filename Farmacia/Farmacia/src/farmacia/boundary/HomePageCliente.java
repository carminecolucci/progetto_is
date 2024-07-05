package farmacia.boundary;

import javax.swing.*;

public class HomePageCliente extends JFrame {
	private JPanel mainPanel;
	private JLabel strBenvenuto;
	private JButton effettuaUnOrdineButton;
	private JButton visualizzaIlCatalogoButton;
	private JButton cercaUnFarmacoButton;
	private JButton visualizzaITuoiOrdiniButton;

	public HomePageCliente() {
		setTitle("Farmacia");
		setSize(500, 500);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setContentPane(mainPanel);
		setVisible(true);
	}
}
