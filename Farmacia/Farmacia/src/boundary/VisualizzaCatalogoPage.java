package boundary;

import com.sun.tools.javac.Main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class VisualizzaCatalogoPage extends JFrame {
	private JPanel mainPanel;
	private JTable tblFarmaci;

	public VisualizzaCatalogoPage() {

		DefaultTableModel model = new DefaultTableModel(
				new Object[][]{
						{"John", "Doe", 29},
						{"Anna", "Smith", 35},
						{"Peter", "Jones", 40}
				},
				new String[]{
						"First Name", "Last Name", "Age"
				}
		){
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};



		tblFarmaci.setModel(model);

		setVisible(true);
		setSize(500, 500);
		setContentPane(mainPanel);
	}

	public static void main(String[] args) {
		VisualizzaCatalogoPage catalogo = new VisualizzaCatalogoPage();

	}
}
