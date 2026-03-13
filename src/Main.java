public class Main {

    public static void main(String[] args) {

        PaymentMethod paypal = new PaypalAdapter(new PaypalAPI());
        Payment payment = new DigitalPayment(paypal);

        payment.makePayment(100);

    }

}