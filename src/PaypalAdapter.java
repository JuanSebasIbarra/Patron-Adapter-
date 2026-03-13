public class PaypalAdapter implements PaymentMethod {
    private PaypalAPI paypal;

    public PaypalAdapter(PaypalAPI paypal) {
        this.paypal = paypal;
    }

    @Override
    public boolean processPayment(double amount) {
        try {
            paypal.sendPayment(amount);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}