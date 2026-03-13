public class CardAdapter implements PaymentMethod {
    private CardAPI card;

    public CardAdapter(CardAPI card) {
        this.card = card;
    }

    @Override
    public boolean processPayment(double amount) {
        try {
            card.makeTransaction(amount);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}