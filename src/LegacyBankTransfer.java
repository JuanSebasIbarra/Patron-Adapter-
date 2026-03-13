
/**
 * ADAPTER PATTERN - Adaptee (Legacy System)
 * This class simulates an old bank-transfer library with a completely different API.
 * We cannot modify it (it's a third-party or legacy dependency), so we wrap it.
 */
public class LegacyBankTransfer {

    /**
     * Opens a bank wire session using routing credentials.
     * This API is incompatible with our PaymentHandler interface.
     */
    public void openSession(String bankCode, String accountNumber) {
        System.out.printf("[LegacyBank] Opening session → bank: %s, acct: %s%n",
                bankCode, accountNumber);
    }

    /**
     * Transfers funds.  Note: amount comes first, destination last — reversed from our interface.
     * @param cents integer cents (not dollars!) to avoid floating-point issues
     * @param destinationAccount the target bank account number
     * @return a raw bank reference string
     */
    public String transferFunds(int cents, String destinationAccount) {
        System.out.printf("[LegacyBank] Wiring %d cents to account %s%n",
                cents, destinationAccount);

        // Simulate network delay
        try { Thread.sleep(400); } catch (InterruptedException ignored) {}

        return "WIRE-" + System.currentTimeMillis();
    }

    /**
     * Reverses a previous wire using the bank's internal reference.
     * @param bankRef the reference returned by transferFunds()
     */
    public void reverseTransfer(String bankRef) {
        System.out.println("[LegacyBank] Reversing wire: " + bankRef);
    }

    /**
     * Closes the bank session.
     */
    public void closeSession() {
        System.out.println("[LegacyBank] Session closed.");
    }
}
