
/**
 * BRIDGE PATTERN - Concrete Implementor B
 * Simulates a credit-card gateway (Stripe-like API).
 */
public class CreditCardProcessor implements Processor {

    private boolean connected = false;

    @Override
    public boolean connect() {
        System.out.println("[CreditCard] Opening TLS connection to gateway...");
        sleep(100);
        connected = true;
        System.out.println("[CreditCard] Gateway ready.");
        return true;
    }

    @Override
    public String charge(double amount, String details) {
        if (!connected) {
            System.out.println("[CreditCard] ERROR: Not connected.");
            return null;
        }
        // 'details' is expected to be a masked card number like "****-1234"
        System.out.printf("[CreditCard] Charging $%.2f to card: %s%n", amount, details);

        // Simulate card authorization check
        sleep(300);

        // Reject cards ending in 0000 to simulate a declined card
        if (details.endsWith("0000")) {
            System.out.println("[CreditCard] Transaction DECLINED: insufficient funds.");
            return null;
        }

        String txId = "CC-" + System.currentTimeMillis();
        System.out.println("[CreditCard] Authorization approved: " + txId);
        return txId;
    }

    @Override
    public boolean refund(String transactionId) {
        System.out.println("[CreditCard] Reversing charge: " + transactionId);
        sleep(200);
        System.out.println("[CreditCard] Reversal complete.");
        return true;
    }

    @Override
    public void disconnect() {
        connected = false;
        System.out.println("[CreditCard] Gateway connection closed.");
    }

    @Override
    public String getName() {
        return "Credit Card";
    }

    private void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }
}
