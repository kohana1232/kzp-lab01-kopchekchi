
package src;


public class MedicineTest {

    public static void main(String[] args) {
        int passed = 0;

        Medicine medicine = Medicine.fromCsv(
            "Парацетамол;Таблетки;45.50;300;false"
        );

        if (medicine.getName().equals("Парацетамол")
                && medicine.getPrice() == 45.50
                && medicine.getDaysToExpire() == 300) {
            passed++;
            System.out.println("Тест 1: коректний CSV — OK");
        }

        try {
            new Medicine("", "Таблетки", 50, 30, false);
            System.out.println("Тест 2: FAIL");
        } catch (IllegalArgumentException e) {
            passed++;
            System.out.println("Тест 2: порожня назва — OK");
        }

        try {
            new Medicine("Ліки", "Таблетки", -10, 30, false);
            System.out.println("Тест 3: FAIL");
        } catch (IllegalArgumentException e) {
            passed++;
            System.out.println("Тест 3: від'ємна ціна — OK");
        }

        try {
            Medicine.fromCsv("Ліки;Таблетки;abc;30;false");
            System.out.println("Тест 4: FAIL");
        } catch (IllegalArgumentException e) {
            passed++;
            System.out.println("Тест 4: неправильне число — OK");
        }

        ExpiryPrescription info =
            new ExpiryPrescription(30, true);

        if (info.daysToExpire() == 30 && info.prescription()) {
            passed++;
            System.out.println("Тест 5: record — OK");
        }

        try {
            new ExpiryPrescription(-1, false);
            System.out.println("Тест 6: FAIL");
        } catch (IllegalArgumentException e) {
            passed++;
            System.out.println("Тест 6: від'ємний термін — OK");
        }

       
        Medicine freeMedicine =
            new Medicine("Тест", "Таблетки", 0, 0, false);

        if (freeMedicine.getPrice() == 0
                && freeMedicine.getDaysToExpire() == 0) {
            passed++;
            System.out.println("Тест 7: нульові значення — OK");
        }

        try {
            new Medicine(null, "Таблетки", 50, 30, false);
            System.out.println("Тест 8: FAIL");
        } catch (NullPointerException e) {
            passed++;
            System.out.println("Тест 8: null назва — OK");
        }

        try {
            new Medicine("Ліки", "Таблетки",
                    Double.NaN, 30, false);
            System.out.println("Тест 9: FAIL");
        } catch (IllegalArgumentException e) {
            passed++;
            System.out.println("Тест 9: NaN ціна — OK");
        }

        try {
            Medicine.fromCsv("Ліки;Таблетки;50;30;maybe");
            System.out.println("Тест 10: FAIL");
        } catch (IllegalArgumentException e) {
            passed++;
            System.out.println("Тест 10: неправильний boolean — OK");
        }

        System.out.println("Пройдено перевірок: " + passed + "/10");

    }
}
