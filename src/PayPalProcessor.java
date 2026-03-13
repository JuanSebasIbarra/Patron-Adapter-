
/**
 * BRIDGE PATTERN - Concrete Implementor A
 * Simulates a PayPal REST API integration.
 */
public class PayPalProcessor implements Processor {

    private boolean connected = false;

    @Override
    public boolean connect() {
        // Simulate OAuth handshake with PayPal sandbox
        System.out.println("[PayPal] Authenticating with OAuth2...");
        connected = true;
        System.out.println("[PayPal] Session opened.");
        return true;
    }

    @Override
    public String charge(double amount, String details) {
        if (!connected) {
            System.out.println("[PayPal] ERROR: Not connected.");
            return null;
        }
        // 'details' is expected to be the payer's PayPal email
        System.out.printf("[PayPal] Charging $%.2f to account: %s%n", amount, details);

        // Simulate a network round-trip (200ms)
        sleep(200);

        String txId = "PP-" + System.currentTimeMillis();
        System.out.println("[PayPal] Transaction approved: " + txId);
        return txId;
    }

    @Override
    public boolean refund(String transactionId) {
        System.out.println("[PayPal] Requesting refund for: " + transactionId);
        sleep(150);
        System.out.println("[PayPal] Refund issued.");
        return true;
    }

    @Override
    public void disconnect() {
        connected = false;
        System.out.println("[PayPal] Session closed.");
    }

    @Override
    public String getName() {
        return "PayPal";
    }

    private void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }
}
