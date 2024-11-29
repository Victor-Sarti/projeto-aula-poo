package br.senac.sp.projetopoo.view;


import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.sql.RowSetListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;

import br.senac.sp.projetopoo.dao.ConnectionFactory;
import br.senac.sp.projetopoo.dao.EMFactory;
import br.senac.sp.projetopoo.dao.InterfaceDao;
import br.senac.sp.projetopoo.dao.MarcaDAO;
import br.senac.sp.projetopoo.dao.MarcaDaoHib;
import br.senac.sp.projetopoo.modelo.Marca;
import br.senac.sp.projetopoo.tablemodel.MarcaTableModel;

public class FrameMarca extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField tfId;
	private JTextField tfNome;
	private Marca marca;
	private InterfaceDao<Marca> dao;
	private JFileChooser chooser;
	private FileFilter imageFilter;
	private JLabel lbLogo;
	private File selecionado;
	private JTable tbMarca;
	private List<Marca> marcas;
	private MarcaTableModel marcaTableModel;

	public static void main(String[] args) {
		FrameMarca frame = new FrameMarca();
		frame.setVisible(true);
	}

	public FrameMarca() {
		dao = new MarcaDaoHib(EMFactory.getEntityManager());
		
		try {
			marcas = dao.listar();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(FrameMarca.this, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
		}
		
		marcaTableModel = new MarcaTableModel(marcas);
		
		chooser = new JFileChooser();
		imageFilter = new FileNameExtensionFilter("Imagens", ImageIO.getReaderFileSuffixes());

		setTitle("Cadastro de Marcas");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 424, 516);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("ID:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel.setBounds(10, 11, 46, 17);
		contentPane.add(lblNewLabel);

		JLabel lblNome = new JLabel("NOME:");
		lblNome.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNome.setBounds(10, 45, 46, 17);
		contentPane.add(lblNome);

		tfId = new JTextField();
		tfId.setEditable(false);
		tfId.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tfId.setBounds(66, 11, 51, 20);
		contentPane.add(tfId);
		tfId.setColumns(10);

		tfNome = new JTextField();
		tfNome.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tfNome.setColumns(10);
		tfNome.setBounds(66, 44, 267, 20);
		contentPane.add(tfNome);

		lbLogo = new JLabel("");
		lbLogo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					chooser.setFileFilter(imageFilter);
					if (chooser.showOpenDialog(FrameMarca.this) == JFileChooser.APPROVE_OPTION) {
						selecionado = chooser.getSelectedFile();
						try {
							BufferedImage bufImg = ImageIO.read(selecionado);
							Image imagem = bufImg.getScaledInstance(lbLogo.getWidth(), lbLogo.getHeight(),
									Image.SCALE_SMOOTH);
							ImageIcon imgLabel = new ImageIcon(imagem);
							lbLogo.setIcon(imgLabel);
						} catch (IOException e1) {
							e1.printStackTrace();
						}

					}
				}
			}
		});
		lbLogo.setBackground(Color.LIGHT_GRAY);
		lbLogo.setBounds(343, 11, 51, 53);
		lbLogo.setOpaque(true);
		contentPane.add(lbLogo);

		JButton btnSalvar = new JButton("Salvar");
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tfNome.getText().trim().isEmpty()) {
					JOptionPane.showMessageDialog(FrameMarca.this, "Informe o nome", "Aviso",
							JOptionPane.INFORMATION_MESSAGE);
					tfNome.requestFocus();
				} else {
					if(marca == null) {
						marca = new Marca();
					}
					
					marca.setNome(tfNome.getText().trim());
					try {
						if (selecionado != null) {
							byte[] imagemBytes = Files.readAllBytes(selecionado.toPath());
							marca.setLogo(imagemBytes);
						}
						if(marca.getId() == 0) {
							dao.inserir(marca);
						}else {
							dao.alterar(marca);
						}
						
						atualizarTabela();

						limpar();
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(FrameMarca.this, e1.getMessage(), "Erro",
								JOptionPane.ERROR_MESSAGE);
						e1.printStackTrace();
					}
				}
			}
		});
		btnSalvar.setMnemonic('s');
		btnSalvar.setBounds(10, 72, 74, 29);
		contentPane.add(btnSalvar);

		JButton btnExcluir = new JButton("Excluir");
		btnExcluir.setMnemonic('e');
		btnExcluir.setBounds(94, 72, 74, 29);
		contentPane.add(btnExcluir);
		btnExcluir.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (marca != null) {
					if (JOptionPane.showConfirmDialog(FrameMarca.this,
							"Deseja excluir a marca" + marca.getNome() + "?") == JOptionPane.YES_OPTION) {
						try {
							dao.excluir(Integer.parseInt(tfId.getText()));
							atualizarTabela();
							limpar();
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(FrameMarca.this, e1.getMessage(), "ERRO",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				}
				
			}
		});
		
		
		JButton btnLimpar = new JButton("Limpar");
		btnLimpar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				limpar();
			}
		});
		btnLimpar.setMnemonic('l');
		btnLimpar.setBounds(178, 72, 74, 29);
		contentPane.add(btnLimpar);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 124, 388, 342);
		contentPane.add(scrollPane);
		
		
		tbMarca = new JTable(marcaTableModel);
		scrollPane.setViewportView(tbMarca);
		tbMarca.setToolTipText("Selecione um item para aletrar ou excluir");
		tbMarca.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		tbMarca.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int linha = tbMarca.getSelectedRow();
				
				if(linha >= 0) {
					marca = marcas.get(linha);
					tfId.setText(""+marca.getId());
					tfNome.setText(marca.getNome());
//					ImageIcon icon = new ImageIcon(marca.getLogo()
				}
				
			}
		});
	}

	private void limpar() {
		tfId.setText("");
		tfNome.setText("");
		marca = null;
		tfNome.requestFocus();
	}
	
	private void atualizarTabela() throws Exception {
		marcas = dao.listar();
		marcaTableModel.setLista(marcas);
		marcaTableModel.fireTableDataChanged();
	}
}
