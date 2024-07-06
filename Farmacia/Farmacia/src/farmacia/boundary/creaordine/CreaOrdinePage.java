package farmacia.boundary.creaordine;

import farmacia.dto.DTO;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class CreaOrdinePage extends JFrame {
	private JTable tblFarmaciOrdine;

	public CreaOrdinePage() {
		setTitle("Crea Ordine");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		pack();
		setSize(600, 400);

		CustomTableModel model = new CustomTableModel(new Object[][]{}, new String[]{"Nome", "Prezzo", "Prescrizione", "Quantità", "Possiedi la prescrizione?"});

		tblFarmaciOrdine = new JTable(model);
		tblFarmaciOrdine.getColumnModel().getColumn(4).setCellRenderer(new CheckBoxRenderer());
		tblFarmaciOrdine.getColumnModel().getColumn(4).setCellEditor(new CheckBoxEditor());

		JScrollPane scrollPane = new JScrollPane(tblFarmaciOrdine);
		getContentPane().add(scrollPane);

		tblFarmaciOrdine.getColumnModel().getColumn(3).setCellEditor(new QuantitaCellEditor());

		//ControllerCatalogo controllerCatalogo = ControllerCatalogo.getInstance();
		//List<DTO> listDTO = controllerCatalogo.visualizzaCatalogo();
		List <DTO> listDTO = new ArrayList<>();
		DTO DTOtest1 = new DTO();
		DTOtest1.set("nome", "prova");
		DTOtest1.set("prezzo", "100");
		DTOtest1.set("prescrizione", true);
		DTOtest1.set("quantita", 500);
		DTOtest1.set("possiediPrescrizione", false);

		DTO DTOtest2 = new DTO();
		DTOtest2.set("nome", "prova2");
		DTOtest2.set("prezzo", 200);
		DTOtest2.set("prescrizione", false);
		DTOtest2.set("quantita", 1000);
		DTOtest2.set("possiediPrescrizione", false);

		DTO DTOtest3 = new DTO();
		DTOtest3.set("nome", "prova3");
		DTOtest3.set("prezzo", 200);
		DTOtest3.set("prescrizione", true);
		DTOtest3.set("quantita", 1000);
		DTOtest3.set("possiediPrescrizione", false);

		DTO DTOtest4 = new DTO();
		DTOtest4.set("nome", "prova4");
		DTOtest4.set("prezzo", 200);
		DTOtest4.set("prescrizione", false);
		DTOtest4.set("quantita", 1000);
		DTOtest4.set("possiediPrescrizione", false);

		listDTO.add(DTOtest1);
		listDTO.add(DTOtest2);
		listDTO.add(DTOtest3);
		listDTO.add(DTOtest4);
		listDTO.add(DTOtest1);
		listDTO.add(DTOtest1);
		listDTO.add(DTOtest1);
		listDTO.add(DTOtest1);
		listDTO.add(DTOtest1);
		listDTO.add(DTOtest1);
		listDTO.add(DTOtest1);

		for(DTO farmaco : listDTO){
			String prescrizioneNecessaria;
			if (!((boolean) farmaco.get("prescrizione"))){
				prescrizioneNecessaria = "-";
				model.addRow(new Object[]{farmaco.get("nome"), farmaco.get("prezzo"),
					prescrizioneNecessaria, farmaco.get("quantita"),
					farmaco.get("possiediPrescrizione")});
				model.toggleCellEditability(model.getRowCount() - 1, 4);
			}
			else{
				prescrizioneNecessaria = "Necessaria";
				model.addRow(new Object[]{farmaco.get("nome"), farmaco.get("prezzo"),
						prescrizioneNecessaria, farmaco.get("quantita"),
						farmaco.get("possiediPrescrizione")});

			}
		}

		setVisible(true);
	}

	public static void main(String[] args) {
		new CreaOrdinePage();
	}

}
