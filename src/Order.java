
/**
 * BRIDGE PATTERN - Abstraction
 * An Order knows WHAT to pay; the Processor knows HOW to pay.
 * The bridge decouples these two hierarchies so each can vary independently.
 */
public class Order {

    private final String orderId;
    private final double amount;
    private final String recipient;  // email or masked card depending on processor
    private final Processor processor; // ← the bridge to the implementation

    private String lastTransactionId = null;

    public Order(String orderId, double amount, String recipient, Processor processor) {
        this.orderId   = orderId;
        this.amount    = amount;
        this.recipient = recipient;
        this.processor = processor;
    }

    /**
     * Executes the full payment lifecycle: connect → charge → disconnect.
     * @return the transaction ID, or null if payment failed
     */
    public String pay() {
        System.out.printf("%n[Order %s] Starting payment of $%.2f via %s%n",
                orderId, amount, processor.getName());

        if (!processor.connect()) {
            return null;
        }

        lastTransactionId = processor.charge(amount, recipient);
        processor.disconnect();

        if (lastTransactionId != null) {
            System.out.printf("[Order %s] Payment SUCCESS → TX: %s%n", orderId, lastTransactionId);
        } else {
            System.out.printf("[Order %s] Payment FAILED%n", orderId);
        }

        return lastTransactionId;
    }

    /**
     * Refunds the last successful transaction for this order.
     * @return true if refund was accepted
     */
    public boolean refund() {
        if (lastTransactionId == null) {
            System.out.println("[Order " + orderId + "] Nothing to refund.");
            return false;
        }
        processor.connect();
        boolean ok = processor.refund(lastTransactionId);
        processor.disconnect();
        return ok;
    }

    // ── Getters used by the UI ──────────────────────────────────────────────

    public String getOrderId()            { return orderId; }
    public double getAmount()             { return amount; }
    public String getRecipient()          { return recipient; }
    public Processor getProcessor()       { return processor; }
    public String getLastTransactionId()  { return lastTransactionId; }
}
