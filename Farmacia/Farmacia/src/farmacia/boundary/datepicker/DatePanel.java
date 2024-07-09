package farmacia.boundary.datepicker;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * <code>DatePanel</code> è il popup che si apre quando si tenta di inserire una data in un <code>DateTextField</code>.
 */
public class DatePanel extends JPanel implements ChangeListener {
	private final DateTextField dateTextField;
	int startYear = 1940;
	int lastYear = 2050;

	Color backGroundColor = Color.WHITE;
	Color palletTableColor = Color.WHITE;
	Color todayBackColor = Color.orange;
	Color weekFontColor = Color.black;
	Color dateFontColor = Color.black;
	Color sundayColor = Color.red;

	Color controlLineColor = Color.WHITE;
	Color controlTextColor = Color.black;

	JSpinner yearSpin;
	JSpinner monthSpin;
	JButton[][] daysButton = new JButton[6][7];

	public DatePanel(DateTextField dateTextField) {
		this.dateTextField = dateTextField;
		setLayout(new BorderLayout());
		setBorder(new LineBorder(backGroundColor, 2));
		setBackground(backGroundColor);

		JPanel topYearAndMonth = createYearAndMonthPanel();
		add(topYearAndMonth, BorderLayout.NORTH);
		JPanel centerWeekAndDay = createWeekAndDayPanel();
		add(centerWeekAndDay, BorderLayout.CENTER);

		reflushWeekAndDay();
	}

	private JPanel createYearAndMonthPanel() {
		Calendar cal = getCalendar();
		int currentYear = cal.get(Calendar.YEAR);
		int currentMonth = cal.get(Calendar.MONTH) + 1;

		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		panel.setBackground(controlLineColor);

		yearSpin = new JSpinner(new SpinnerNumberModel(currentYear, startYear, lastYear, 1));
		yearSpin.setPreferredSize(new Dimension(56, 20));
		yearSpin.setName("Year");
		yearSpin.setEditor(new JSpinner.NumberEditor(yearSpin, "####"));
		yearSpin.addChangeListener(this);
		panel.add(yearSpin);

		JLabel yearLabel = new JLabel("Year");
		yearLabel.setForeground(controlTextColor);
		panel.add(yearLabel);

		monthSpin = new JSpinner(new SpinnerNumberModel(currentMonth, 1, 12, 1));
		monthSpin.setPreferredSize(new Dimension(35, 20));
		monthSpin.setName("Month");
		monthSpin.addChangeListener(this);
		panel.add(monthSpin);

		JLabel monthLabel = new JLabel("Month");
		monthLabel.setForeground(controlTextColor);
		panel.add(monthLabel);

		return panel;
	}

	/**
	 * Creates a <code>JPanel</code> with the names of the days
	 * @return <code>JPanel</code>
	 */
	private JPanel createWeekAndDayPanel() {
		String[] days = {"Lun", "Mar", "Mer", "Gio", "Ven", "Sab", "Dom"};
		JPanel panel = new JPanel();
		panel.setFont(new Font("Arial", Font.PLAIN, 10));
		panel.setLayout(new GridLayout(7, 7));
		panel.setBackground(Color.white);

		for (int i = 0; i < 7; i++) {
			JLabel cell = new JLabel(days[i]);
			cell.setHorizontalAlignment(SwingConstants.RIGHT);
			if (i == 6) {
				cell.setForeground(sundayColor);
			} else {
				cell.setForeground(weekFontColor);
			}
			panel.add(cell);
		}

		int actionCommandId = 0;
		for (int row = 0; row < 6; row++)
			for (int column = 0; column < 7; column++) {
				JButton button = getDayButton(actionCommandId, column);
				daysButton[row][column] = button;
				panel.add(button);
				actionCommandId++;
			}

		return panel;
	}

	private JButton getDayButton(int actionCommandId, int column) {
		JButton day = new JButton();
		day.setBorder(null);
		day.setHorizontalAlignment(SwingConstants.RIGHT);
		day.setActionCommand(String.valueOf(actionCommandId));
		day.setBackground(palletTableColor);
		day.setForeground(dateFontColor);
		day.addActionListener(event -> {
			JButton source = (JButton) event.getSource();
			if (source.getText().isEmpty()) {
				dateTextField.setVisible(false);
				return;
			}
			dayColorUpdate(true);
			source.setForeground(todayBackColor);

			// update calendar with new selected date
			int newDay = Integer.parseInt(source.getText());
			Calendar cal = getCalendar();
			cal.set(Calendar.DAY_OF_MONTH, newDay);
			dateTextField.setDate(cal.getTime());
			dateTextField.setVisible(false);
		});

		if (column == 6)
			day.setForeground(sundayColor);
		else
			day.setForeground(dateFontColor);
		return day;
	}

	private Calendar getCalendar() {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"), Locale.ITALY);
		calendar.setTime(dateTextField.getDate());
		return calendar;
	}

	private int getSelectedYear() {
		return (Integer) yearSpin.getValue();
	}

	private int getSelectedMonth() {
		return (Integer) monthSpin.getValue();
	}

	private void dayColorUpdate(boolean isOldDay) {
		Calendar cal = getCalendar();
		int day = cal.get(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		int actionCommandId = day + cal.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY - 1;

		int i = actionCommandId / 7;
		int j = actionCommandId % 7;
		if (isOldDay) {
			Color color = j == 6 ? sundayColor : weekFontColor;
			daysButton[i][j].setForeground(color);
		} else {
			daysButton[i][j].setForeground(todayBackColor);
		}
	}

	private void reflushWeekAndDay() {
		Calendar cal = getCalendar();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		int maxDayNo = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		int dayNo = Calendar.MONDAY + 1 - cal.get(Calendar.DAY_OF_WEEK);

		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {
				String s = "";
				if (dayNo >= 1 && dayNo <= maxDayNo) {
					s = String.valueOf(dayNo);
				}
				daysButton[i][j].setText(s);
				dayNo++;
			}
		}
		dayColorUpdate(false);
	}

	public void stateChanged(ChangeEvent e) {
		dayColorUpdate(true);

		JSpinner source = (JSpinner) e.getSource();
		Calendar cal = getCalendar();
		if (source.getName().equals("Year")) {
			cal.set(Calendar.YEAR, getSelectedYear());
		} else {
			cal.set(Calendar.MONTH, getSelectedMonth() - 1);
		}
		dateTextField.setDate(cal.getTime());
		reflushWeekAndDay();
	}
}
