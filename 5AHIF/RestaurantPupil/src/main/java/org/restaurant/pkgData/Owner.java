package org.restaurant.pkgData;

import org.bson.types.ObjectId;
import org.restaurant.pkgMisc.DateFormatConverter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Owner {
    ObjectId _id;
    String name;
    String cv;
    LocalDate birthday;

    public Owner(String name, String cv, LocalDate birthday) {
        this.name = name;
        this.cv = cv;
        this.birthday = birthday;
    }

    public Owner(String name, String cv, String birthday) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        formatter = formatter.withLocale(Locale.GERMAN);  // Locale specifies human language for translating, and cultural norms for lowercase/uppercase and abbreviations and such. Example: Locale.US or Locale.CANADA_FRENCH
        LocalDate date = LocalDate.parse(birthday, formatter);
        this.name = name;
        this.cv = cv;
        this.birthday = date;
    }

    @Override
    public String toString() {
        return name + " (" + birthday.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + ")";
    }

    public ObjectId getId() {
        return _id;
    }

    public void setId(ObjectId id) {
        this._id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCv() {
        return cv;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
}
