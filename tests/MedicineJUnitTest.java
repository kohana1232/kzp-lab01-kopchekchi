package src;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MedicineJUnitTest {

    @Test
    void validCsv() {
        Medicine medicine = Medicine.fromCsv(
            "Парацетамол;Таблетки;45.50;300;false"
        );

        assertEquals("Парацетамол", medicine.getName());
        assertEquals(45.50, medicine.getPrice());
        assertEquals(300, medicine.getDaysToExpire());
    }

    @Test
    void emptyName() {
        assertThrows(IllegalArgumentException.class,
            () -> new Medicine("", "Таблетки", 50, 30, false));
    }

    @Test
    void negativePrice() {
        assertThrows(IllegalArgumentException.class,
            () -> new Medicine("Ліки", "Таблетки", -10, 30, false));
    }

    @Test
    void invalidNumber() {
        assertThrows(IllegalArgumentException.class,
            () -> Medicine.fromCsv("Ліки;Таблетки;abc;30;false"));
    }

    @Test
    void validRecord() {
        ExpiryPrescription info =
            new ExpiryPrescription(30, true);

        assertEquals(30, info.daysToExpire());
        assertTrue(info.prescription());
    }

    @Test
    void negativeExpiry() {
        assertThrows(IllegalArgumentException.class,
            () -> new ExpiryPrescription(-1, false));
    }

    @Test
    void zeroValues() {
        Medicine medicine =
            new Medicine("Тест", "Таблетки", 0, 0, false);

        assertEquals(0, medicine.getPrice());
        assertEquals(0, medicine.getDaysToExpire());
    }

    @Test
    void nullName() {
        assertThrows(NullPointerException.class,
            () -> new Medicine(null, "Таблетки", 50, 30, false));
    }

    @Test
    void nanPrice() {
        assertThrows(IllegalArgumentException.class,
            () -> new Medicine(
                "Ліки", "Таблетки", Double.NaN, 30, false));
    }

    @Test
    void invalidBoolean() {
        assertThrows(IllegalArgumentException.class,
            () -> Medicine.fromCsv(
                "Ліки;Таблетки;50;30;maybe"));
    }
}
