public abstract class Payment {
    protected PaymentMethod paymentMethod; // Uso de composición

    public Payment(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public abstract void makePayment(double amount);
}


