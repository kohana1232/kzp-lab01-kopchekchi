
package src;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

/**
 * Лабораторна робота №1.
 * Варіант 11 — Аптека.
 * Читає CSV-файл та обчислює показники препаратів.
 */
public class Main {

    /**
     * Точка входу в програму.
     *
     * @param args параметри командного рядка
     */
    public static void main(String[] args) {
        Path inputPath = Path.of("data/input.csv");
        Path outputPath = Path.of("out/report.txt");

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--help":
                    printHelp();
                    return;

                case "--input":
                    if (i + 1 >= args.length) {
                        System.err.println("Після --input потрібен шлях до файлу.");
                        return;
                    }
                    inputPath = Path.of(args[++i]);
                    break;

                case "--output":
                    if (i + 1 >= args.length) {
                        System.err.println("Після --output потрібен шлях до файлу.");
                        return;
                    }
                    outputPath = Path.of(args[++i]);
                    break;

                default:
                    System.err.println("Невідомий параметр: " + args[i]);
                    printHelp();
                    return;
            }
        }

        try {
            List<String> lines = Files.readAllLines(
                inputPath, StandardCharsets.UTF_8
            );

            int validCount = 0;
            int prescriptionCount = 0;
            int minDays = Integer.MAX_VALUE;
            double totalPrice = 0;

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                int lineNumber = i + 1;

                if (line.isBlank()) {
                    System.err.println("Рядок " + lineNumber
                        + ": порожній запис.");
                    continue;
                }

                String[] fields = line.split(";", -1);

                if (fields.length != 5) {
                    System.err.println("Рядок " + lineNumber
                        + ": очікується 5 полів, отримано "
                        + fields.length);
                    continue;
                }

                try {
                    String name = fields[0].trim();
                    String form = fields[1].trim();

                    if (name.isEmpty() || form.isEmpty()) {
                        throw new IllegalArgumentException(
                            "Назва та форма повинні бути заповнені"
                        );
                    }

                    double price = Double.parseDouble(fields[2].trim());
                    int days = Integer.parseInt(fields[3].trim());

                    if (!Double.isFinite(price) || price < 0) {
                        throw new IllegalArgumentException(
                            "Ціна має бути невід'ємним числом"
                        );
                    }

                    if (days < 0) {
                        throw new IllegalArgumentException(
                            "Кількість днів не може бути від'ємною"
                        );
                    }

                    String booleanText = fields[4].trim();

                    if (!booleanText.equalsIgnoreCase("true")
                            && !booleanText.equalsIgnoreCase("false")) {
                        throw new IllegalArgumentException(
                            "prescription має бути true або false"
                        );
                    }

                    boolean prescription =
                        Boolean.parseBoolean(booleanText);

                    validCount++;
                    totalPrice += price;
                    minDays = Math.min(minDays, days);

                    if (prescription) {
                        prescriptionCount++;
                    }

                } catch (IllegalArgumentException e) {
                    System.err.println("Рядок " + lineNumber
                        + ": " + e.getMessage());
                }
            }

            if (validCount == 0) {
                System.err.println("Немає жодного коректного запису.");
                return;
            }

            double averagePrice = totalPrice / validCount;

            String report = String.format(
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

            System.out.print(report);

            Path parent = outputPath.toAbsolutePath().getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            Files.writeString(
                outputPath, report, StandardCharsets.UTF_8
            );

            System.out.println("Звіт збережено: " + outputPath);

        } catch (IOException | SecurityException e) {
            System.err.println("Помилка роботи з файлом: "
                + e.getMessage());
        }
    }

    /**
     * Виводить довідку з параметрами запуску.
     */
    private static void printHelp() {
        System.out.println("Лабораторна №1. Варіант 11 — Аптека.");
        System.out.println("Параметри:");
        System.out.println("  --help          Показати довідку");
        System.out.println("  --input ШЛЯХ    Вхідний CSV-файл");
        System.out.println("  --output ШЛЯХ   Файл для результатів");
        System.out.println();
        System.out.println("Типові шляхи:");
        System.out.println("  data/input.csv");
        System.out.println("  out/report.txt");
    }
}
