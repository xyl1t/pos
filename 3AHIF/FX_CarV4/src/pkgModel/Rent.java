package pkgModel;

import java.time.LocalDate;

public class Rent {
	private String customer;
	private LocalDate startDate;
	private LocalDate endDate;
	private double amount;

	public Rent(String customer, LocalDate startDate, LocalDate endDate, double amount) {
		this.customer = customer;
		this.startDate = startDate;
		this.endDate = endDate;
		this.amount = amount;
	}

	public String getCustomer() {
		return customer;
	}
	public double getAmount() {
		return amount;
	}
	public LocalDate getStartDate() {
		return startDate;
	}
	public LocalDate getEndDate() {
		return endDate;
	}

	@Override
	public String toString() {
		return customer + ", " + startDate + " => " + endDate + ", EUR " + amount;
	}
}
