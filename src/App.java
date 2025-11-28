import java.awt.*;
import javax.swing.*;

public class App {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
            UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 14));
            UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 14));
        } catch (Exception e) {}
        SwingUtilities.invokeLater(App::createGUI);
    }

    private static void createGUI() {
        ToolTipManager.sharedInstance().setInitialDelay(100);
        ToolTipManager.sharedInstance().setDismissDelay(10000);

        JFrame frame = new JFrame("Thaxadd");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel root = new JPanel(new GridBagLayout());
        root.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.anchor = GridBagConstraints.LINE_START;

        // Campos
        JTextField valorInicial = new JTextField(10);
        JTextField meses = new JTextField(10);
        JTextField taxaMes = new JTextField(10);
        JTextField multa = new JTextField(10);
        JTextField resultado = new JTextField(15);
        resultado.setEditable(false);

        taxaMes.setText("1");
        multa.setText("2");

        valorInicial.setToolTipText("Digite o valor base (ex: 183,84)");
        meses.setToolTipText("Quantidade de meses");
        taxaMes.setToolTipText("Juros compostos ao mês");
        multa.setToolTipText("Multa em % sobre o valor inicial");

        c.gridx = 0; c.gridy = 0;
        root.add(new JLabel("Valor inicial:"), c);

        c.gridy = 1;
        root.add(valorInicial, c);

        c.gridy = 2;
        root.add(new JLabel("Meses:"), c);

        c.gridy = 3;
        root.add(meses, c);

        c.gridx = 1; c.gridy = 0;
        root.add(new JLabel("Taxa ao mês (%):"), c);

        c.gridy = 1;
        root.add(taxaMes, c);

        c.gridy = 2;
        root.add(new JLabel("Taxa de multa (%):"), c);

        c.gridy = 3;
        root.add(multa, c);

        JButton calcular = new JButton("Calcular");
        calcular.setBackground(new Color(245, 73, 39));
        calcular.setForeground(Color.WHITE);
        calcular.setOpaque(true);
        calcular.setBorderPainted(false);

        c.gridx = 0; c.gridy = 4;
        c.gridwidth = 2;
        root.add(calcular, c);

        JLabel lblRes = new JLabel("Resultado:");
        c.gridy = 5;
        root.add(lblRes, c);

        c.gridy = 6;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        root.add(resultado, c);

        calcular.addActionListener(e -> {
            try {
                double valor = Double.parseDouble(valorInicial.getText().replace(',', '.'));
                int m = Integer.parseInt(meses.getText());
                double percentagemMes = Double.parseDouble(taxaMes.getText().replace('%', ' ')) / 100;
                double percentagemMulta = Double.parseDouble(multa.getText().replace('%', ' ')) / 100;

                double montante = valor * Math.pow(1 + percentagemMes, m);

                double totalFinal = montante + valor * percentagemMulta; //Juros acumulados + multa sobre valor inicial

                resultado.setText(String.format("R$ %.2f", totalFinal));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Valores inválidos!");
            }
        });

        /*Image img = Toolkit.getDefaultToolkit().getImage(App.class.getResource("logo.jpg"));
        frame.setIconImage(img);*/
        frame.setContentPane(root);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}