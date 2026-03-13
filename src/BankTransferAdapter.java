
/**
 * ADAPTER PATTERN - Adapter (Object Adapter via composition)
 *
 * Wraps the incompatible LegacyBankTransfer and exposes it as a PaymentHandler.
 * The adapter translates:
 *   - dollars  →  cents
 *   - "details" string  →  bankCode + accountNumber split
 *   - cancel()  →  reverseTransfer(lastRef)
 *
 * Neither the application code nor LegacyBankTransfer needs to change.
 */
public class BankTransferAdapter implements PaymentHandler {

    private static final String BANK_CODE = "BK-0042"; // hardcoded routing code for the demo

    private final LegacyBankTransfer legacy;  // ← adaptee held by composition
    private String lastBankRef = null;

    public BankTransferAdapter() {
        this.legacy = new LegacyBankTransfer();
    }

    /**
     * Adapts our standard pay(amount, details) call to the legacy wire API.
     * @param amount  payment amount in USD (we convert to cents internally)
     * @param details the destination account number
     */
    @Override
    public boolean pay(double amount, String details) {
        System.out.println("[Adapter] Translating PaymentHandler.pay() → LegacyBankTransfer API");

        // 1. Open the legacy session
        legacy.openSession(BANK_CODE, details);

        // 2. Convert dollars → cents (legacy API requires integer cents)
        int cents = (int) Math.round(amount * 100);

        // 3. Execute the wire transfer
        lastBankRef = legacy.transferFunds(cents, details);

        // 4. Close the session
        legacy.closeSession();

        boolean success = lastBankRef != null;
        System.out.println("[Adapter] Wire result: " + (success ? lastBankRef : "FAILED"));
        return success;
    }

    /**
     * Adapts cancel() to the legacy reverseTransfer() call.
     */
    @Override
    public boolean cancel() {
        if (lastBankRef == null) {
            System.out.println("[Adapter] No wire reference stored — nothing to cancel.");
            return false;
        }
        System.out.println("[Adapter] Translating PaymentHandler.cancel() → LegacyBankTransfer.reverseTransfer()");
        legacy.openSession(BANK_CODE, "REVERSE");
        legacy.reverseTransfer(lastBankRef);
        legacy.closeSession();
        lastBankRef = null;
        return true;
    }

    @Override
    public String describe() {
        return "Bank Wire Transfer (via legacy adapter)";
    }
}
