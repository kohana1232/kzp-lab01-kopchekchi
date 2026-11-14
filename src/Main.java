
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
 */
public class Main {

    private static int validCount;
    private static double totalPrice;
    private static int minDays;
    private static int prescriptionCount;

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
            resetStatistics();
            readData(input);

            if (validCount == 0) {
                System.out.println("Немає жодного коректного запису.");
                return;
            }

            String report = createReport();
            System.out.print(report);
            saveReport(output, report);

            System.out.println("Звіт збережено: " + output);

        } catch (IOException | SecurityException e) {
            System.err.println("Помилка роботи з файлом: "
                    + e.getMessage());
        }
    }

    /**
     * Скидає статистичні показники перед обробкою файлу.
     */
    private static void resetStatistics() {
        validCount = 0;
        totalPrice = 0;
        minDays = Integer.MAX_VALUE;
        prescriptionCount = 0;
    }

    /**
     * Читає CSV-файл і передає рядки на перевірку.
     *
     * @param input шлях до вхідного файлу
     * @throws IOException якщо файл неможливо прочитати
     */
    private static void readData(Path input) throws IOException {
        List<String> lines = Files.readAllLines(
                input, StandardCharsets.UTF_8);

        for (int i = 0; i < lines.size(); i++) {
            processLine(lines.get(i), i + 1);
        }
    }

    /**
     * Перевіряє один запис і оновлює показники.
     *
     * @param line текст запису
     * @param lineNumber номер рядка
     */
    private static void processLine(String line, int lineNumber) {
        if (line.isBlank()) {
            System.err.println("Рядок " + lineNumber
                    + ": порожній запис");
            return;
        }

        String[] fields = line.split(";", -1);

        if (fields.length != 5) {
            System.err.println("Рядок " + lineNumber
                    + ": очікується 5 полів");
            return;
        }

        try {
            String name = fields[0].trim();
            String form = fields[1].trim();

            if (name.isEmpty() || form.isEmpty()) {
                throw new IllegalArgumentException(
                        "Назва або форма порожня");
            }

            double price = Double.parseDouble(fields[2].trim());
            int days = Integer.parseInt(fields[3].trim());
            String value = fields[4].trim();

            if (!Double.isFinite(price) || price < 0) {
                throw new IllegalArgumentException(
                        "Ціна має бути невід'ємним числом");
            }

            if (days < 0) {
                throw new IllegalArgumentException(
                        "Кількість днів не може бути від'ємною");
            }

            if (!value.equalsIgnoreCase("true")
                    && !value.equalsIgnoreCase("false")) {
                throw new IllegalArgumentException(
                        "prescription має бути true або false");
            }

            boolean prescription = Boolean.parseBoolean(value);
            updateStatistics(price, days, prescription);

        } catch (IllegalArgumentException e) {
            System.err.println("Рядок " + lineNumber
                    + ": " + e.getMessage());
        }
    }

    /**
     * Оновлює статистику за коректним препаратом.
     *
     * @param price ціна препарату
     * @param days кількість днів до завершення терміну
     * @param prescription рецептурність
     */
    private static void updateStatistics(
            double price, int days, boolean prescription) {

        validCount++;
        totalPrice += price;
        minDays = Math.min(minDays, days);

        if (prescription) {
            prescriptionCount++;
        }
    }

    /**
     * Формує текстовий звіт.
     *
     * @return сформований звіт
     */
    private static String createReport() {
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
     * Зберігає звіт у файл UTF-8.
     *
     * @param output шлях до звіту
     * @param report текст звіту
     * @throws IOException якщо запис неможливий
     */
    private static void saveReport(
            Path output, String report) throws IOException {

        Path parent = output.toAbsolutePath().getParent();

        if (parent != null) {
            Files.createDirectories(parent);
        }

        Files.writeString(
                output, report, StandardCharsets.UTF_8);
    }

    /**
     * Виводить довідку щодо запуску.
     */
    private static void printHelp() {
        System.out.println("Лабораторна №1. Варіант 11 — Аптека.");
        System.out.println("Параметри:");
        System.out.println("  --help          Показати довідку");
        System.out.println("  --input ШЛЯХ    Вхідний CSV-файл");
        System.out.println("  --output ШЛЯХ   Файл результатів");
    }
}
