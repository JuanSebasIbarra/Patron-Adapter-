

/**
 * Entry point for the Payment Module demo.
 *
 * Run from the project root:
 *   javac -d out payment/*.java
 *   java  -cp out payment.Main
 *
 * The program launches the Swing UI.  All console output from the
 * processors is redirected into the in-window log panel automatically.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("=== Payment Module — Bridge & Adapter Demo ===");
        System.out.println("Opening UI...");
        PaymentUI.main(args);
    }
}