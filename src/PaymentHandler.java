
/**
 * ADAPTER PATTERN - Target Interface
 * This is the interface our application expects every payment handler to expose.
 * The Adapter will make the legacy system conform to this contract.
 */
public interface PaymentHandler {

    /**
     * Initiates a payment.
     * @param amount  amount in USD
     * @param details destination identifier (email, card number, etc.)
     * @return true if the payment was accepted
     */
    boolean pay(double amount, String details);

    /**
     * Cancels / refunds the most recent payment.
     * @return true if the cancellation was successful
     */
    boolean cancel();

    /**
     * Returns a short description of this handler for logging and UI display.
     */
    String describe();
}
