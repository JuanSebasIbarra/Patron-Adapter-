import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.io.OutputStream;

/**
 * SWING UI — Payment Module
 * Demonstrates both patterns in one clean window:
 *   • BRIDGE : Order + Processor (PayPal / CreditCard)
 *   • ADAPTER: BankTransferAdapter wrapping LegacyBankTransfer
 */
public class PaymentUI extends JFrame {

    // ── UI Components ────────────────────────────────────────────────────────
    private final JComboBox<String> methodCombo;
    private final JTextField        amountField;
    private final JTextField        detailsField;
    private final JLabel            detailsLabel;
    private final JTextArea         logArea;
    private final JButton           payButton;
    private final JButton           refundButton;
    private final JLabel            statusLabel;

    // ── State ────────────────────────────────────────────────────────────────
    private Order           lastOrder   = null;  // Bridge orders
    private BankTransferAdapter bankAdapter = null; // Adapter orders
    private boolean         lastWasBank = false;

    // ── Colors ───────────────────────────────────────────────────────────────
    private static final Color BG_DARK    = new Color(30, 30, 35);
    private static final Color BG_PANEL   = new Color(40, 40, 48);
    private static final Color BG_INPUT   = new Color(55, 55, 65);
    private static final Color ACCENT     = new Color(99, 179, 237);
    private static final Color GREEN      = new Color(72, 199, 142);
    private static final Color RED        = new Color(252, 129, 129);
    private static final Color TEXT_MAIN  = new Color(230, 230, 240);
    private static final Color TEXT_MUTED = new Color(140, 140, 160);

    // ── Constructor ──────────────────────────────────────────────────────────
    public PaymentUI() {
        super("Payment Module · Bridge & Adapter Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(720, 600);
        setMinimumSize(new Dimension(600, 520));
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_DARK);

        // ── Header ───────────────────────────────────────────────────────────
        JLabel title = makeLabel("Payment Module", 20, Font.BOLD, TEXT_MAIN);
        JLabel subtitle = makeLabel("Bridge Pattern (PayPal / Credit Card) + Adapter Pattern (Bank Wire)", 12, Font.PLAIN, TEXT_MUTED);
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(BG_DARK);
        header.setBorder(new EmptyBorder(20, 24, 12, 24));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitle);

        // ── Form panel ───────────────────────────────────────────────────────
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(BG_PANEL);
        form.setBorder(new CompoundBorder(
                new LineBorder(new Color(70, 70, 85), 1, true),
                new EmptyBorder(18, 20, 18, 20)));

        GridBagConstraints lc = new GridBagConstraints();
        lc.anchor = GridBagConstraints.WEST;
        lc.insets = new Insets(6, 0, 6, 12);
        lc.gridx = 0;

        GridBagConstraints fc = new GridBagConstraints();
        fc.fill = GridBagConstraints.HORIZONTAL;
        fc.weightx = 1.0;
        fc.insets = new Insets(6, 0, 6, 0);
        fc.gridx = 1;

        // Row 1 – Payment method
        lc.gridy = fc.gridy = 0;
        form.add(makeLabel("Payment method", 13, Font.PLAIN, TEXT_MUTED), lc);
        methodCombo = new JComboBox<>(new String[]{"PayPal", "Credit Card", "Bank Wire (Adapter)"});
        styleCombo(methodCombo);
        methodCombo.addActionListener(e -> onMethodChanged());
        form.add(methodCombo, fc);

        // Row 2 – Amount
        lc.gridy = fc.gridy = 1;
        form.add(makeLabel("Amount (USD)", 13, Font.PLAIN, TEXT_MUTED), lc);
        amountField = makeTextField("50.00");
        form.add(amountField, fc);

        // Row 3 – Details (dynamic label)
        lc.gridy = fc.gridy = 2;
        detailsLabel = makeLabel("PayPal email", 13, Font.PLAIN, TEXT_MUTED);
        form.add(detailsLabel, lc);
        detailsField = makeTextField("user@example.com");
        form.add(detailsField, fc);

        // Row 4 – Buttons
        lc.gridy = fc.gridy = 3;
        lc.gridwidth = 2;
        lc.insets = new Insets(14, 0, 0, 0);
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnRow.setBackground(BG_PANEL);
        payButton    = makeButton("Pay Now",     ACCENT,  Color.WHITE);
        refundButton = makeButton("Refund / Cancel", new Color(80,80,95), TEXT_MUTED);
        refundButton.setEnabled(false);
        btnRow.add(payButton);
        btnRow.add(refundButton);
        form.add(btnRow, lc);

        // ── Status bar ───────────────────────────────────────────────────────
        statusLabel = makeLabel("Ready", 13, Font.PLAIN, TEXT_MUTED);
        statusLabel.setBorder(new EmptyBorder(6, 4, 0, 0));

        // ── Log area ─────────────────────────────────────────────────────────
        logArea = new JTextArea(10, 50);
        logArea.setEditable(false);
        logArea.setBackground(new Color(22, 22, 28));
        logArea.setForeground(new Color(180, 200, 200));
        logArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        logArea.setBorder(new EmptyBorder(8, 10, 8, 10));
        JScrollPane scroll = new JScrollPane(logArea);
        scroll.setBorder(new LineBorder(new Color(60, 60, 75), 1, true));

        JLabel logTitle = makeLabel("Console output", 12, Font.PLAIN, TEXT_MUTED);
        logTitle.setBorder(new EmptyBorder(10, 0, 4, 0));

        // ── Layout assembly ──────────────────────────────────────────────────
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(BG_DARK);
        center.setBorder(new EmptyBorder(0, 24, 20, 24));

        form.setAlignmentX(Component.LEFT_ALIGNMENT);
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        logTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);

        center.add(form);
        center.add(statusLabel);
        center.add(logTitle);
        center.add(scroll);

        add(header, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);

        // ── Wire actions ─────────────────────────────────────────────────────
        payButton.addActionListener(e -> onPay());
        refundButton.addActionListener(e -> onRefund());

        // Redirect System.out to the log area
        redirectConsole();

        setVisible(true);
    }

    // ── Event Handlers ────────────────────────────────────────────────────────

    private void onMethodChanged() {
        String m = (String) methodCombo.getSelectedItem();
        switch (m) {
            case "PayPal":
                detailsLabel.setText("PayPal email");
                detailsField.setText("user@example.com");
                break;
            case "Credit Card":
                detailsLabel.setText("Card number (masked)");
                detailsField.setText("****-****-****-1234");
                break;
            case "Bank Wire (Adapter)":
                detailsLabel.setText("Destination account #");
                detailsField.setText("ACC-987654321");
                break;
        }
    }

    private void onPay() {
        double amount;
        try {
            amount = Double.parseDouble(amountField.getText().trim());
            if (amount <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            setStatus("Invalid amount — enter a positive number.", RED);
            return;
        }

        String details = detailsField.getText().trim();
        String method  = (String) methodCombo.getSelectedItem();

        payButton.setEnabled(false);
        setStatus("Processing...", ACCENT);

        // Run payment off the EDT so the UI stays responsive
        new Thread(() -> {
            String txId = null;
            lastWasBank = false;

            if ("Bank Wire (Adapter)".equals(method)) {
                // ADAPTER PATTERN: wrap legacy system
                lastWasBank = true;
                bankAdapter = new BankTransferAdapter();
                boolean ok = bankAdapter.pay(amount, details);
                txId = ok ? "WIRE-OK" : null;

            } else {
                // BRIDGE PATTERN: Order + Processor
                Processor proc = "PayPal".equals(method)
                        ? new PayPalProcessor()
                        : new CreditCardProcessor();

                String orderId = "ORD-" + System.currentTimeMillis();
                lastOrder = new Order(orderId, amount, details, proc);
                txId = lastOrder.pay();
            }

            final String finalTx = txId;
            SwingUtilities.invokeLater(() -> {
                if (finalTx != null) {
                    setStatus("✓ Payment approved  ·  TX: " + finalTx, GREEN);
                    refundButton.setEnabled(true);
                } else {
                    setStatus("✗ Payment failed — see console.", RED);
                    refundButton.setEnabled(false);
                }
                payButton.setEnabled(true);
            });
        }).start();
    }

    private void onRefund() {
        refundButton.setEnabled(false);
        setStatus("Refunding...", ACCENT);

        new Thread(() -> {
            boolean ok;
            if (lastWasBank && bankAdapter != null) {
                ok = bankAdapter.cancel();
            } else if (lastOrder != null) {
                ok = lastOrder.refund();
            } else {
                ok = false;
            }
            final boolean result = ok;
            SwingUtilities.invokeLater(() -> {
                setStatus(result ? "✓ Refund complete." : "✗ Refund failed.", result ? GREEN : RED);
            });
        }).start();
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void setStatus(String msg, Color c) {
        statusLabel.setText(msg);
        statusLabel.setForeground(c);
    }

    private JLabel makeLabel(String text, int size, int style, Color fg) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", style, size));
        l.setForeground(fg);
        return l;
    }

    private JTextField makeTextField(String placeholder) {
        JTextField f = new JTextField(placeholder, 20);
        f.setBackground(BG_INPUT);
        f.setForeground(TEXT_MAIN);
        f.setCaretColor(TEXT_MAIN);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setBorder(new CompoundBorder(
                new LineBorder(new Color(80, 80, 100), 1, true),
                new EmptyBorder(5, 8, 5, 8)));
        return f;
    }

    private JButton makeButton(String text, Color bg, Color fg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setBorder(new EmptyBorder(8, 20, 8, 20));
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setOpaque(true);
        return b;
    }

    private void styleCombo(JComboBox<String> c) {
        c.setBackground(BG_INPUT);
        c.setForeground(TEXT_MAIN);
        c.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    }

    /** Redirects System.out to the JTextArea log. */
    private void redirectConsole() {
        PrintStream ps = new PrintStream(new OutputStream() {
            private final StringBuilder sb = new StringBuilder();
            @Override public void write(int b) {
                if (b == '\n') {
                    String line = sb.toString();
                    sb.setLength(0);
                    SwingUtilities.invokeLater(() -> {
                        logArea.append(line + "\n");
                        logArea.setCaretPosition(logArea.getDocument().getLength());
                    });
                } else {
                    sb.append((char) b);
                }
            }
        });
        System.setOut(ps);
    }

    // ── Entry Point ──────────────────────────────────────────────────────────

    public static void main(String[] args) {
        // Use the system look-and-feel for native widgets, then override colors
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(PaymentUI::new);
    }
}
