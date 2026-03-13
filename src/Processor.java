
/**
 * BRIDGE PATTERN - Implementor Interface
 * Defines the operations that concrete payment processors must implement.
 * This is the "implementation" side of the Bridge.
 */
public interface Processor {

    /**
     * Connects to the payment gateway and opens a session.
     * @return true if connection was established successfully
     */
    boolean connect();

    /**
     * Sends a charge request to the payment provider.
     * @param amount  the amount to charge (in USD)
     * @param details payment-specific details (card token, PayPal email, etc.)
     * @return a transaction ID string on success, or null on failure
     */
    String charge(double amount, String details);

    /**
     * Requests a refund for a previous transaction.
     * @param transactionId the ID returned by a prior charge() call
     * @return true if the refund was accepted
     */
    boolean refund(String transactionId);

    /**
     * Closes the connection / session with the payment provider.
     */
    void disconnect();

    /**
     * Returns a human-readable name for this processor (shown in the UI).
     */
    String getName();
}
