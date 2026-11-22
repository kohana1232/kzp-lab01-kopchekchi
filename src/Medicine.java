
package src;

import java.util.Locale;
import java.util.Objects;

/**
 * Описує лікарський препарат аптеки.
 * Лабораторна робота №2, варіант 11.
 */
public final class Medicine {

    private final String name;
    private final String form;
    private final double price;
    private final int daysToExpire;
    private final boolean prescription;

    /**
     * Створює препарат і перевіряє правильність даних.
     *
     * @param name назва препарату
     * @param form форма випуску
     * @param price ціна
     * @param daysToExpire днів до завершення терміну
     * @param prescription чи потрібен рецепт
     */
    public Medicine(String name, String form, double price,
                    int daysToExpire, boolean prescription) {

        Objects.requireNonNull(name, "Назва не може бути null");
        Objects.requireNonNull(form, "Форма не може бути null");

        if (name.isBlank() || form.isBlank()) {
            throw new IllegalArgumentException(
                "Назва та форма не можуть бути порожніми"
            );
        }

        if (!Double.isFinite(price) || price < 0) {
            throw new IllegalArgumentException(
                "Ціна має бути невід'ємним скінченним числом"
            );
        }

        if (daysToExpire < 0) {
            throw new IllegalArgumentException(
                "Термін придатності не може бути від'ємним"
            );
        }

        this.name = name.trim();
        this.form = form.trim();
        this.price = price;
        this.daysToExpire = daysToExpire;
        this.prescription = prescription;
    }

    /**
     * Створює препарат із CSV-рядка.
     *
     * @param line CSV-рядок із п'ятьма полями
     * @return об'єкт Medicine
     */
    public static Medicine fromCsv(String line) {
        Objects.requireNonNull(line, "Рядок не може бути null");

        String[] fields = line.split(";", -1);

        if (fields.length != 5) {
            throw new IllegalArgumentException(
                "Очікується рівно 5 полів"
            );
        }

        String prescriptionText = fields[4].trim();

        if (!prescriptionText.equalsIgnoreCase("true")
                && !prescriptionText.equalsIgnoreCase("false")) {
            throw new IllegalArgumentException(
                "prescription має бути true або false"
            );
        }

        try {
            return new Medicine(
                fields[0].trim(),
                fields[1].trim(),
                Double.parseDouble(fields[2].trim()),
                Integer.parseInt(fields[3].trim()),
                Boolean.parseBoolean(prescriptionText)
            );
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "Некоректний формат числа", e
            );
        }
    }

    /** Повертає назву препарату. */
    public String getName() {
        return name;
    }

    /** Повертає форму випуску. */
    public String getForm() {
        return form;
    }

    /** Повертає ціну. */
    public double getPrice() {
        return price;
    }

    /** Повертає кількість днів до завершення терміну. */
    public int getDaysToExpire() {
        return daysToExpire;
    }

    /** Повертає ознаку рецептурності. */
    public boolean isPrescription() {
        return prescription;
    }

    /**
     * Повертає текстове представлення препарату.
     *
     * @return інформація про препарат
     */
    @Override
    public String toString() {
        return String.format(
            Locale.ROOT,
            "%s (%s): %.2f грн, %d днів, рецепт: %s",
            name, form, price, daysToExpire, prescription
        );
    }
}
