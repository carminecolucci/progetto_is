package farmacia.boundary.datepicker;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;

/**
 * <code>DateTextField</code> è un <code>JTextField</code> utilizzato per rappresentare le date
 */
public class DateTextField extends JTextField {

	private static String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";
	private static final int DIALOG_WIDTH = 200;
	private static final int DIALOG_HEIGHT = 200;

	private SimpleDateFormat dateFormat;
	private DatePanel datePanel = null;
	private JDialog dateDialog = null;

	/**
	 * Crea un nuovo <code>DateTextField</code>
	 */
	public DateTextField() {
		this(new Date());
	}

	/**
	 * Crea un nuovo <code>DateTextField</code>
	 * @param date data iniziale
	 */
	public DateTextField(Date date) {
		setDate(date);
		setEditable(false);
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		addListeners();
	}

	private void addListeners() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent paramMouseEvent) {
				if (datePanel == null) {
					datePanel = new DatePanel(DateTextField.this);
				}
				Point point = getLocationOnScreen();
				point.y = point.y + 30;
				showDateDialog(datePanel, point);
			}
		});
	}

	private void showDateDialog(DatePanel dateChooser, Point position) {
		Frame owner = (Frame) SwingUtilities.getWindowAncestor(DateTextField.this);
		if (dateDialog == null || dateDialog.getOwner() != owner) {
			dateDialog = createDateDialog(owner, dateChooser);
		}
		dateDialog.setLocation(getAppropriateLocation(owner, position));
		dateDialog.setVisible(true);
	}

	private JDialog createDateDialog(Frame owner, JPanel contentPanel) {
		JDialog dialog = new JDialog(owner, "Date Selected", true);
		dialog.setUndecorated(true);
		dialog.getContentPane().add(contentPanel, BorderLayout.CENTER);
		dialog.pack();
		dialog.setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
		return dialog;
	}

	private Point getAppropriateLocation(Frame owner, Point position) {
		Point result = new Point(position);
		Point p = owner.getLocation();
		int offsetX = (position.x + DIALOG_WIDTH) - (p.x + owner.getWidth());
		int offsetY = (position.y + DIALOG_HEIGHT) - (p.y + owner.getHeight());

		if (offsetX > 0) {
			result.x -= offsetX;
		}

		if (offsetY > 0) {
			result.y -= offsetY;
		}

		return result;
	}

	private SimpleDateFormat getDefaultDateFormat() {
		if (dateFormat == null) {
			dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
		}
		return dateFormat;
	}

	/**
	 * Same as <code>setDate(Date)</code>
	 * @param date date to set
	 */
	public void setText(Date date) {
		setDate(date);
	}

	/**
	 * Sets a new date
	 * @param date date to set
	 */
	public void setDate(Date date) {
		super.setText(getDefaultDateFormat().format(date));
	}

	/**
	 * Gets the selected date
	 * @return selected date
	 */
	public Date getDate() {
		try {
			return getDefaultDateFormat().parse(getText());
		} catch (ParseException e) {
			return new Date();
		}
	}

	@Override
	public void setVisible(boolean b) {
		this.dateDialog.setVisible(b);
	}
}