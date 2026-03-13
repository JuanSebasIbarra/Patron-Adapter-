
import javax.swing.*;
import java.awt.*;

public class PaymentGUI extends JFrame {
    private JTextField txtAmount;
    private JComboBox<String> comboMethods;

    public PaymentGUI() {
        setTitle("Sistema de Suministros UCC - Pagos");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana
        setLayout(new GridLayout(5, 1, 10, 10));

        txtAmount = new JTextField();
        comboMethods = new JComboBox<>(new String[]{"PayPal", "Tarjeta de Crédito"});
        JButton btnPay = new JButton("Procesar Pago");

        add(new JLabel("Ingrese el monto a pagar:", SwingConstants.CENTER));
        add(txtAmount);
        add(new JLabel("Seleccione método:", SwingConstants.CENTER));
        add(comboMethods);
        add(btnPay);

        btnPay.addActionListener(e -> ejecutarAccionPago());
    }

    private void ejecutarAccionPago() {
        try {
            double amount = Double.parseDouble(txtAmount.getText());
            PaymentMethod method;


            if (comboMethods.getSelectedItem().equals("PayPal")) {
                method = new PaypalAdapter(new PaypalAPI());
            } else {
                method = new CardAdapter(new CardAPI());
            }

            Payment payment = new DigitalPayment(method);
            payment.makePayment(amount);

            JOptionPane.showMessageDialog(this, "Transacción procesada con éxito.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}