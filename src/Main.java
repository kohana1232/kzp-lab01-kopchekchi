
package src;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Лабораторна робота №2.
 * Варіант 11 — Аптека.
 * Обробка препаратів за допомогою класів та record.
 */
public class Main {

    /**
     * Головний метод програми.
     *
     * @param args аргументи командного рядка
     */
    public static void main(String[] args) {
        Path input = Path.of("data", "input.csv");
        Path output = Path.of("out", "report.txt");

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--help":
                    printHelp();
                    return;

                case "--input":
                    if (i + 1 >= args.length) {
                        System.err.println("Не вказано шлях після --input");
                        return;
                    }
                    input = Path.of(args[++i]);
                    break;

                case "--output":
                    if (i + 1 >= args.length) {
                        System.err.println("Не вказано шлях після --output");
                        return;
                    }
                    output = Path.of(args[++i]);
                    break;

                default:
                    System.err.println("Невідомий параметр: " + args[i]);
                    printHelp();
                    return;
            }
        }

        try {
            List<Medicine> medicines = readData(input);

            if (medicines.isEmpty()) {
                System.out.println("Немає жодного коректного запису.");
                return;
            }

            String report = createReport(medicines);

            System.out.print(report);
            saveReport(output, report);

            System.out.println("Звіт збережено: " + output);

        } catch (IOException | SecurityException e) {
            System.err.println("Помилка роботи з файлом: "
                    + e.getMessage());
        }
    }

    /**
     * Читає CSV-файл і створює список препаратів.
     *
     * @param input шлях до вхідного файлу
     * @return список коректних препаратів
     * @throws IOException якщо файл неможливо прочитати
     */
    private static List<Medicine> readData(Path input)
            throws IOException {

        List<String> lines = Files.readAllLines(
                input, StandardCharsets.UTF_8);

        List<Medicine> medicines = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            try {
                Medicine medicine = Medicine.fromCsv(lines.get(i));
                medicines.add(medicine);

            } catch (IllegalArgumentException e) {
                System.err.println("Рядок " + (i + 1)
                        + ": " + e.getMessage());
            }
        }

        return medicines;
    }

    /**
     * Обчислює статистику та формує текст звіту.
     *
     * @param medicines список препаратів
     * @return текстовий звіт
     */
    private static String createReport(List<Medicine> medicines) {

        int validCount = medicines.size();
        double totalPrice = 0;
        int minDays = Integer.MAX_VALUE;
        int prescriptionCount = 0;

        for (Medicine medicine : medicines) {
            totalPrice += medicine.getPrice();

            ExpiryPrescription info = new ExpiryPrescription(
                    medicine.getDaysToExpire(),
                    medicine.isPrescription()
            );

            minDays = Math.min(minDays, info.daysToExpire());

            if (info.prescription()) {
                prescriptionCount++;
            }
        }

        double averagePrice = totalPrice / validCount;

        return String.format(
                Locale.ROOT,
                "=== РЕЗУЛЬТАТИ: АПТЕКА ===%n"
                + "Кількість коректних записів: %d%n"
                + "Середня ціна: %.2f грн%n"
                + "Мінімум днів до завершення терміну: %d%n"
                + "Рецептурних препаратів: %d%n",
                validCount,
                averagePrice,
                minDays,
                prescriptionCount
        );
    }

    /**
     * Зберігає звіт у файл.
     *
     * @param output шлях до файлу
     * @param report текст звіту
     * @throws IOException якщо запис неможливий
     */
    private static void saveReport(Path output, String report)
            throws IOException {

        Path parent = output.toAbsolutePath().getParent();

        if (parent != null) {
            Files.createDirectories(parent);
        }

        Files.writeString(
                output, report, StandardCharsets.UTF_8
        );
    }

    /**
     * Виводить довідку про запуск програми.
     */
    private static void printHelp() {
        System.out.println("Лабораторна №2. Варіант 11 — Аптека.");
        System.out.println("Параметри:");
        System.out.println("  --help          Показати довідку");
        System.out.println("  --input ШЛЯХ    Вхідний CSV-файл");
        System.out.println("  --output ШЛЯХ   Файл результатів");
    }
}
