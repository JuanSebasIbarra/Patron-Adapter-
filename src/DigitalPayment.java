public class DigitalPayment extends Payment {

    public DigitalPayment(PaymentMethod paymentMethod) {
        super(paymentMethod);
    }

    @Override
    public void makePayment(double amount) {
        paymentMethod.processPayment(amount);
    }

}