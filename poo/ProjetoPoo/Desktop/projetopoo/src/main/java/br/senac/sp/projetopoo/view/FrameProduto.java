package br.senac.sp.projetopoo.view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Files;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import br.senac.sp.projetopoo.dao.EMFactory;
import br.senac.sp.projetopoo.dao.InterfaceDao;
import br.senac.sp.projetopoo.dao.ProdutoDaoHib;
import br.senac.sp.projetopoo.modelo.Marca;
import br.senac.sp.projetopoo.modelo.Produto;
import javax.swing.border.EmptyBorder;

public class FrameProduto extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField tfIdProduto;
    private JTextField tfNomeProduto;
    private JTextField tfPrecoProduto;
    private Produto produto;
    private InterfaceDao<Produto> produtoDao;
    private List<Produto> produtos;
    private JComboBox<String> cbMarcas;
    private JTable tbProduto;

    public static void main(String[] args) {
        FrameProduto frame = new FrameProduto();
        frame.setVisible(true);
    }

    public FrameProduto() {
        produtoDao = new ProdutoDaoHib(EMFactory.getEntityManager());

        try {
            produtos = produtoDao.listar();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(FrameProduto.this, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
            
        }

        setTitle("Cadastro de Produtos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 429, 600);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblIdProduto = new JLabel("ID:");
        lblIdProduto.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblIdProduto.setBounds(10, 11, 46, 17);
        contentPane.add(lblIdProduto);

        JLabel lblNomeProduto = new JLabel("MODELO:");
        lblNomeProduto.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblNomeProduto.setBounds(10, 69, 107, 17);
        contentPane.add(lblNomeProduto);

        JLabel lblMarca = new JLabel("MARCA:");
        lblMarca.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblMarca.setBounds(108, 11, 60, 17);
        contentPane.add(lblMarca);

        JLabel lblPrecoProduto = new JLabel("PREÇO:");
        lblPrecoProduto.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblPrecoProduto.setBounds(10, 115, 84, 17);
        contentPane.add(lblPrecoProduto);

        tfIdProduto = new JTextField();
        tfIdProduto.setEditable(false);
        tfIdProduto.setFont(new Font("Tahoma", Font.PLAIN, 12));
        tfIdProduto.setBounds(41, 10, 51, 20);
        contentPane.add(tfIdProduto);
        tfIdProduto.setColumns(10);

        tfNomeProduto = new JTextField();
        tfNomeProduto.setFont(new Font("Tahoma", Font.PLAIN, 12));
        tfNomeProduto.setColumns(10);
        tfNomeProduto.setBounds(104, 68, 267, 20);
        contentPane.add(tfNomeProduto);

        tfPrecoProduto = new JTextField();
        tfPrecoProduto.setFont(new Font("Tahoma", Font.PLAIN, 12));
        tfPrecoProduto.setBounds(104, 114, 267, 20);
        contentPane.add(tfPrecoProduto);

        cbMarcas = new JComboBox<>();
        cbMarcas.setBounds(167, 10, 177, 22);
        contentPane.add(cbMarcas);

        carregarMarcasNoComboBox();

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Verifica se o nome do produto está vazio
                if (tfNomeProduto.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(FrameProduto.this, "Informe o nome do produto", "Aviso",
                            JOptionPane.INFORMATION_MESSAGE);
                    tfNomeProduto.requestFocus();
                } else if (tfPrecoProduto.getText().trim().isEmpty()) {
                    // Verifica se o preço está vazio
                    JOptionPane.showMessageDialog(FrameProduto.this, "Informe o preço do produto", "Aviso",
                            JOptionPane.INFORMATION_MESSAGE);
                    tfPrecoProduto.requestFocus();
                } else {
                    // Cria um novo objeto Produto se ele ainda não foi instanciado
                    if (produto == null) {
                        produto = new Produto();
                    }

                    // Define os dados do produto
                    produto.setNome(tfNomeProduto.getText().trim());
                    produto.setPreco(Double.parseDouble(tfPrecoProduto.getText().trim()));
                    
                    // Verifica a seleção da marca e atribui ao produto
                    String marcaSelecionada = (String) cbMarcas.getSelectedItem();
                    if (marcaSelecionada != null && !marcaSelecionada.isEmpty()) {
                        // Aqui você deve encontrar a marca que corresponde ao nome selecionado
                        try {
                            Marca marca = ((ProdutoDaoHib) produtoDao).buscarMarcaPorNome(marcaSelecionada);
                            produto.setMarca(marca);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(FrameProduto.this, "Erro ao associar a marca: " + ex.getMessage(),
                                    "Erro", JOptionPane.ERROR_MESSAGE);
                            
                        }
                    }

                    try {
                        // Se o produto não tiver ID, significa que é um novo produto, caso contrário, é uma atualização
                        if (produto.getId() == 0) {
                            produtoDao.inserir(produto);
                        } else {
                            produtoDao.alterar(produto);
                        }

                        // Atualiza a tabela com os novos dados
                        atualizarTabela();

                        // Limpa os campos após salvar
                        limpar();
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(FrameProduto.this, e1.getMessage(), "Erro",
                                JOptionPane.ERROR_MESSAGE);
                        e1.printStackTrace();
                    }
                }
            }
        });
        btnSalvar.setBounds(41, 143, 74, 29);
        contentPane.add(btnSalvar);

        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.setBounds(125, 143, 74, 29);
        contentPane.add(btnExcluir);
        btnExcluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tbProduto.getSelectedRow();
                if (selectedRow != -1) {
                    Long produtoId = (Long) tbProduto.getValueAt(selectedRow, 0);
                    String produtoNome = (String) tbProduto.getValueAt(selectedRow, 1);
                    if (JOptionPane.showConfirmDialog(FrameProduto.this,
                            "Deseja excluir o produto " + produtoNome + "?") == JOptionPane.YES_OPTION) {
                        try {
                            produtoDao.excluir(produtoId);
                            atualizarTabela(); // Atualiza a tabela após a exclusão
                            limpar(); // Limpa os campos
                        } catch (Exception e1) {
                            JOptionPane.showMessageDialog(FrameProduto.this, e1.getMessage(), "ERRO",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(FrameProduto.this, "Nenhum produto selecionado!", "Aviso",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                limpar(); // Limpa os campos
            }
        });
        btnLimpar.setBounds(209, 143, 74, 29);
        contentPane.add(btnLimpar);

        tbProduto = new JTable(new DefaultTableModel(
        	new Object[][] {
        	},
        	new String[] {
        		"ID", "Modelo", "Pre\u00E7o", "Marca"
        	}
        ));
        tbProduto.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
     // Adicionar listener para capturar o clique na tabela
        tbProduto.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && tbProduto.getSelectedRow() != -1) {
                int selectedRow = tbProduto.getSelectedRow();
                if (selectedRow != -1) {
                    // Preencher os campos com os dados da linha selecionada
                    tfIdProduto.setText(tbProduto.getValueAt(selectedRow, 0).toString());
                    tfNomeProduto.setText(tbProduto.getValueAt(selectedRow, 1).toString());
                    tfPrecoProduto.setText(tbProduto.getValueAt(selectedRow, 2).toString());
                    
                    String nomeMarca = tbProduto.getValueAt(selectedRow, 3).toString();
                    for (int i = 0; i < cbMarcas.getItemCount(); i++) {
                        if (cbMarcas.getItemAt(i).equals(nomeMarca)) {
                            cbMarcas.setSelectedIndex(i);
                            break;
                        }
                    }

                    // Associar o objeto Produto da linha ao atributo `produto`
                    produto = produtos.get(selectedRow);
                }
            }
        });


        JScrollPane scrollPane = new JScrollPane(tbProduto);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(10, 200, 393, 300);
        contentPane.add(scrollPane);

        atualizarTabela(); // Atualiza a tabela ao iniciar

        // Botão "Buscar"
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String marcaSelecionada = (String) cbMarcas.getSelectedItem();
                try {
                    if (marcaSelecionada == null || marcaSelecionada.isEmpty()) {
                        // Se nenhuma marca for selecionada, listar todos os produtos
                        atualizarTabela();
                    } else {
                        // Caso contrário, buscar produtos pela marca selecionada
                        List<Produto> produtosBuscados = ((ProdutoDaoHib) produtoDao).buscarPorMarca(marcaSelecionada);
                        atualizarTabela(produtosBuscados);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(FrameProduto.this, "Erro ao buscar produtos: " + ex.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });
        btnBuscar.setBounds(293, 143, 78, 29);
        contentPane.add(btnBuscar);
    }

    // Carregar marcas no ComboBox
    private void carregarMarcasNoComboBox() {
        try {
            List<Marca> marcas = ((ProdutoDaoHib) produtoDao).listarMarcas();
            for (Marca marca : marcas) {
                cbMarcas.addItem(marca.getNome());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(FrameProduto.this, "Erro ao carregar as marcas", "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Atualiza a tabela com a lista de produtos
    private void atualizarTabela() {
        try {
            produtos = produtoDao.listar();
            DefaultTableModel model = (DefaultTableModel) tbProduto.getModel();
            model.setRowCount(0);
            for (Produto p : produtos) {
            	System.out.println(p.getMarca());
                model.addRow(new Object[] { p.getId(), p.getNome(), p.getPreco(), p.getMarca().getNome() });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(FrameProduto.this, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Atualiza a tabela com a lista de produtos buscados
    private void atualizarTabela(List<Produto> produtos) {
        DefaultTableModel model = (DefaultTableModel) tbProduto.getModel();
        model.setRowCount(0);
        for (Produto p : produtos) {
            model.addRow(new Object[] { p.getId(), p.getNome(), p.getPreco(), p.getMarca().getNome() });
        }
    }

    // Limpar os campos
    private void limpar() {
        tfIdProduto.setText("");
        tfNomeProduto.setText("");
        tfPrecoProduto.setText("");
        cbMarcas.setSelectedIndex(-1);
        produto = null;
    }
}