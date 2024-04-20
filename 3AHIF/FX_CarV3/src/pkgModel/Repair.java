package pkgModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Repair implements Serializable {
	private static final long serialVersionUID = 1L;

	private static int nextRepairId = 0;

	private int id;
	private LocalDate date;
	private String text;
	private BigDecimal amount;
	
	public Repair(LocalDate date, String text, String amount) {
		this(date, text, new BigDecimal(amount));
	}

	public Repair(LocalDate date, String text, BigDecimal amount) {
		this.id = nextRepairId;
		this.date = date;
		this.text = text;
		this.amount = amount;
	}

	public int getId() {
		return id;
	}
	public LocalDate getDate() {
		return date;
	}
	public String getDateAsString() {
		return date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
	}
	public String getText() {
		return text;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	
	public static int getRepairId() {
		return nextRepairId;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "Repair [id=" + id + ", date=" + date + ", text=" + text + ", amount=" + amount + "]";
	}

	public static void setRepairId(int id) {
		nextRepairId = id;
	}
	public static void incrementRepairId() {
		nextRepairId++;
	}
	
}
