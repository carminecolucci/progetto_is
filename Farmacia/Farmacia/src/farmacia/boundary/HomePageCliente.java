package farmacia.boundary;

import farmacia.boundary.creaordine.CreaOrdinePage;
import farmacia.dto.DTO;
import farmacia.exceptions.DBException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePageCliente extends JFrame {

	private JPanel homePanel;
	private JButton btnCercaFarmaco;
	private JButton btnCreaOrdine;
	private JButton btnVisualizzaCatalogo;
	private JButton btnVisualizzaStoricoOrdini;
	private JLabel lblScelta;
	private JLabel lblBenvenuto;

	public HomePageCliente(DTO dtoCliente) {
		setSize(500, 500);
		setTitle("Homepage Cliente");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setContentPane(homePanel);
		setVisible(true);
		lblBenvenuto.setText(String.format("Benvenuto %s %s", dtoCliente.get("nome"), dtoCliente.get("cognome")));

		btnVisualizzaCatalogo.addActionListener(e -> {
			JFrame visualizzaCatalogo = new VisualizzaCatalogoPage();
		});

		btnCreaOrdine.addActionListener(e -> {
			JFrame creaOrdine = new CreaOrdinePage();
		});
		btnVisualizzaStoricoOrdini.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					JFrame visualizzaStoricoOrdini = new VisualizzaStoricoOrdiniPage();
				} catch (DBException ex) {
					throw new RuntimeException(ex);
				}
			}
		});
		btnCercaFarmaco.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame cercaFarmacoPage = new CercaFarmacoPage();
			}
		});
	}
}
