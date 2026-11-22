
package src;

/**
 * Незмінне допоміжне значення:
 * термін придатності та рецептурність препарату.
 */
public record ExpiryPrescription(
        int daysToExpire,
        boolean prescription) {

    /**
     * Перевіряє правильність значень record.
     */
    public ExpiryPrescription {
        if (daysToExpire < 0) {
            throw new IllegalArgumentException(
                "Кількість днів не може бути від'ємною"
            );
        }
    }
}
