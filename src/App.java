import java.awt.*;
import java.awt.datatransfer.StringSelection;
import javax.swing.*;

public class App {
    
    private int tipoDeDado = 1;
    final double taxaDiaria = 0.0003305843;
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
            UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 14));
            UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 14));
        } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new App().createGUI());
    }

    private void createGUI() {
        ToolTipManager.sharedInstance().setInitialDelay(100);
        ToolTipManager.sharedInstance().setDismissDelay(10000);

        JFrame frame = new JFrame("Thaxadd");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel root = new JPanel(new GridBagLayout());
        root.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.anchor = GridBagConstraints.LINE_START;

        JTextField valorInicial = new JTextField(10);
        JTextField unid = new JTextField(10);
        JTextField taxaMes = new JTextField(10);
        JTextField multa = new JTextField(10);
        JTextField resultado = new JTextField(15);
        resultado.setEditable(false);

        JLabel labelTempo = new JLabel(tipoDeDado == 0 ? "Meses:" : "Dias:");

        String[] unidades = { "Meses", "Dias" };
        JComboBox<String> unidadeTempo = new JComboBox<>(unidades);
        unidadeTempo.setPreferredSize(new Dimension(80, 25));
        unidadeTempo.setSelectedIndex(tipoDeDado);

        JPanel painelMeses = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        painelMeses.add(unid);
        painelMeses.add(unidadeTempo);

        Dimension size = unidadeTempo.getPreferredSize();
        size.height = unid.getPreferredSize().height;
        unidadeTempo.setPreferredSize(size);

        taxaMes.setText("1");
        multa.setText("2");

        valorInicial.setToolTipText("Digite o valor base (ex: 183,84)");
        unid.setToolTipText("Quantidade de meses ou dias");
        unidadeTempo.setToolTipText("Tipo de unidade (Meses / Dias)");
        taxaMes.setToolTipText("Juros compostos ao mês (30 dias)");
        multa.setToolTipText("Multa em % sobre o valor inicial");
        resultado.setToolTipText("Valor final => valor inicial + taxa mensal + multa");

        c.gridx = 0; c.gridy = 0;
        root.add(new JLabel("Valor inicial:"), c);

        c.gridy = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL; 
        root.add(valorInicial, c);

        c.gridy = 2;
        root.add(labelTempo, c);

        c.gridy = 3;
        root.add(painelMeses, c);

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

        unidadeTempo.addActionListener(e -> {
            String selecionado = (String) unidadeTempo.getSelectedItem();
            switch (selecionado) {
                 case "Meses" -> {
                    tipoDeDado = 0;
                    labelTempo.setText("Meses:");
                }
                case "Dias" -> {
                    tipoDeDado = 1;
                    labelTempo.setText("Dias:");
                }
            }
            root.revalidate();
            root.repaint();
        });

        valorInicial.addActionListener(e -> unid.requestFocus());

        unid.addActionListener(e -> calcular.doClick());

        calcular.addActionListener(e -> {
            try {
                double valor = Double.parseDouble(valorInicial.getText().replace(',', '.'));
                int unidade = Integer.parseInt(unid.getText());
                double percentagemMes = Double.parseDouble(taxaMes.getText().replace('%', ' ')) / 100;
                double percentagemMulta = Double.parseDouble(multa.getText().replace('%', ' ')) / 100;

                if(tipoDeDado == 1){
                    percentagemMes = taxaDiaria;
                }
                
                double montante = valor * Math.pow(1 + percentagemMes, unidade);

                double totalFinal = montante + valor * percentagemMulta; //Juros acumulados + multa sobre valor inicial

                String textoFormatado = String.format("%.2f", totalFinal);
                resultado.setText("R$ " + textoFormatado);

                Toolkit.getDefaultToolkit()
                    .getSystemClipboard()
                    .setContents(new StringSelection(textoFormatado), null);

                showToast(frame, "Valor copiado: " + textoFormatado);
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

    
    public static void showToast(JFrame frame, String message) {
        JWindow toast = new JWindow(frame);
        toast.setLayout(new BorderLayout());

        JLabel label = new JLabel(message);
        label.setOpaque(true);
        label.setBackground(new Color(0, 0, 0, 200));
        label.setForeground(Color.WHITE);
        label.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        toast.add(label, BorderLayout.CENTER);
        toast.pack();

        int x = frame.getX() + (frame.getWidth() - toast.getWidth()) / 2;
        int framey = frame.getY() + frame.getHeight() - 100;
        int y = framey + framey / 2;

        toast.setLocation(x, y);
        toast.setVisible(true);

        new Timer(3500, e -> toast.setVisible(false)).start();
    }
}