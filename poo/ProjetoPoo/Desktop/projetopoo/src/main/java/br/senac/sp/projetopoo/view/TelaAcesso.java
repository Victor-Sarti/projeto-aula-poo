package br.senac.sp.projetopoo.view;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

public class TelaAcesso extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    TelaAcesso frame = new TelaAcesso();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public TelaAcesso() {
        setTitle("Tela De Acesso");

        // Alteração do comportamento de fechamento da janela para apenas esconder a tela.
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        
        setBounds(100, 100, 369, 246);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        // Botão que abre a FrameMarca
        JButton btnMarcas = new JButton("MARCAS");
        btnMarcas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Criando e exibindo a tela FrameMarca
                FrameMarca frameMarca = new FrameMarca();
                frameMarca.setVisible(true);
            }
        });
        
        // Botão que abre a FrameProduto
        JButton btnProdutos = new JButton("PRODUTOS");
        btnProdutos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Criando e exibindo a tela FrameProduto
                FrameProduto frameProduto = new FrameProduto();
                frameProduto.setVisible(true);
            }
        });
        
        btnMarcas.setBounds(20, 81, 115, 62);
        contentPane.add(btnMarcas);
        
        btnProdutos.setBounds(214, 81, 115, 62);
        contentPane.add(btnProdutos);
        
        JLabel lblNewLabel = new JLabel("ESCOLHA A OPÇÃO DESEJADA");
        lblNewLabel.setBounds(92, 11, 190, 40);
        contentPane.add(lblNewLabel);
    }
}

