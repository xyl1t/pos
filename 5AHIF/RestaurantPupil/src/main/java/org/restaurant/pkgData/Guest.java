package org.restaurant.pkgData;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Guest {
    private String name;
    private LocalDate birth;
    private String location;
    private BigDecimal totalAmount;
    public static DateTimeFormatter DTF = DateTimeFormatter.ofPattern("d.M.y");

    public Guest(String name, String birth, String location, String totalAmount) {
        this.name = name;

        this.birth = LocalDate.parse(birth, DTF);
        this.location = location;
        this.totalAmount = new BigDecimal(totalAmount);
        System.out.println(this);
    }

    public Guest(String name, LocalDate birth, String location, BigDecimal totalAmount) {
        this.name = name;
        this.birth = birth;
        this.location = location;
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return name + ", birth=" + birth + ", location='" + location + "'" + ", totalAmount=" + totalAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirth() {
        return birth;
    }

    public void setBirth(LocalDate birth) {
        this.birth = birth;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
