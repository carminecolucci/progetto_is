package farmacia.boundary;

import farmacia.boundary.datepicker.DateTextField;
import farmacia.controller.ControllerOrdini;
import farmacia.controller.ControllerReport;
import farmacia.dto.DTO;
import farmacia.exceptions.DBException;
import farmacia.exceptions.ReportException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class GeneraReportPage extends JFrame {
	private JPanel mainPanel;
	private JButton btnGeneraReport;
	private JPanel pnlConferma;
	private JLabel lblInizio;
	private JPanel pnlDataInizio;
	private JPanel pnlDataFine;
	private JPanel pnlDate;
	private JLabel lblFine;
	private JPanel pnlTable;
	private JTable tblReport;
	private JPanel pnlFarmaciVenduti;
	private JLabel lblBanco;
	private JLabel lblNomeBanco;
	private JLabel lblBancoVenduto;
	private JLabel lblPrescrizione;
	private JLabel lblPrescrizioneVenduto;
	private JLabel lblNomePrescrizione;
	private JLabel lblVendutePrescrizione;
	private JLabel lblVenduteBanco;
	private JScrollPane srlTable;
	private DateTextField txtDataInizio;
	private DateTextField txtDataFine;

	public GeneraReportPage() {
		setSize(700, 400);
		setLocationRelativeTo(null);
		setContentPane(mainPanel);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setTitle("Genera report");
		setResizable(true);

		txtDataInizio = new DateTextField();
		txtDataFine = new DateTextField();
		pnlDataInizio.add(txtDataInizio);
		pnlDataFine.add(txtDataFine);

		DefaultTableModel model = new DefaultTableModel(new Object[][]{}, new String[]{"Farmaci da banco venduti", "Incassi da farmaci da banco", "Farmaci con prescrizione venduti", "Incassi da farmaci con prescrizione"}){
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tblReport.setModel(model);

		btnGeneraReport.addActionListener(e -> {
			model.setRowCount(0);
			Date dataInizio = txtDataInizio.getDate();
			Date dataFine = txtDataFine.getDate();
			if (dataInizio.after(dataFine)) {
				JOptionPane.showMessageDialog(null, "Il mondo reale è strettamente causale...", "Errore", JOptionPane.ERROR_MESSAGE);
				txtDataFine = new DateTextField();
				txtDataInizio = new DateTextField();
				lblNomeBanco.setText("-");
				lblNomePrescrizione.setText("-");
				lblVendutePrescrizione.setText("0");
				lblVenduteBanco.setText("0");
				return;
			}
			ControllerReport controllerReport = ControllerReport.getInstance();
			try {
				DTO report = controllerReport.generaReport(dataInizio, dataFine);
				model.addRow(new Object[]{report.get("venditeBanco"), report.get("incassoBanco"), report.get("venditePrescrizione"), report.get("incassoPrescrizione")});
				lblNomeBanco.setText((String)report.get("nomeBanco"));
				lblNomePrescrizione.setText((String)report.get("nomePrescrizione"));
				lblVenduteBanco.setText(String.valueOf(report.get("venditeBanco")));
				lblVendutePrescrizione.setText(String.valueOf(report.get("venditePrescrizione")));
			} catch (ReportException ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
			}


		});

		setVisible(true);
	}
}
