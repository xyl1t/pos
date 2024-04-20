package pkgModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Repair implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	private LocalDate date;
	private String text;
	private BigDecimal amount;
	
	public Repair(int id, LocalDate date, String text, String amount) {
		this(id, date, text, new BigDecimal(amount));
	}

	public Repair(int id, LocalDate date, String text, BigDecimal amount) {
		this.id = id;
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
}
