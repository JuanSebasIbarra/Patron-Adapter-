import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Swing debe ejecutarse en el Event Dispatch Thread para evitar errores visuales
        SwingUtilities.invokeLater(() -> {
            PaymentGUI ventana = new PaymentGUI();
            ventana.setVisible(true); // Esto hace que la ventana sea visible
        });
    }
}