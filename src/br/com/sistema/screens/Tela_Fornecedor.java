package br.com.sistema.screens;

import br.com.sistema.Connection.connector;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;


public class Tela_Fornecedor extends javax.swing.JFrame {
    Connection conectar = null;
    Statement St = null, St1 = null;
    PreparedStatement Pst = null;
    ResultSet Rs = null, Rs1 = null;

    public Tela_Fornecedor() {
        initComponents();
        conectar = connector.Conector();
        DisplayFornecedores();
    }
     
    private void adicionar() {
        String sql = "insert into fornecedorTbl(nome_fornecedor,endereco_fornecedor,email_fornecedor,telefone_fornecedor) values(?,?,?,?)";

        try {
            Pst = conectar.prepareStatement(sql);
            Pst.setString(1, ForNomeTb.getText());
            Pst.setString(2, ForEnderecoTb.getText());
            Pst.setString(3, ForEmailTb.getText());
            Pst.setString(4, ForTelefoneTb.getText());

            // checks if the textfields are empty 
            if (ForNomeTb.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "The client name cannot be empty");
            } else if (ForEnderecoTb.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "The client adress cannot be empty");
            } else if (ForEmailTb.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "The client phonenumber cannot be empty");
            } else if (ForTelefoneTb.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "The client phonenumber cannot be empty");
                
            } else {
                int added = Pst.executeUpdate();

                if (added > 0) {
                    JOptionPane.showMessageDialog(null, "Fornecedor adicionado com sucesso!");
                    cleanpage();
                    DisplayFornecedores();                  

                } else {
                    JOptionPane.showMessageDialog(null, "Failled to register the client");
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    private void search() {
        String sql = "select * from fornecedorTbl where id_fornecedor like ?";
        try {
            Pst = conectar.prepareStatement(sql);
            Pst.setString(1, ProcurarForTb.getText() + "%");
            Rs = Pst.executeQuery();
            //this function comes from the library rs2xml
            FornecedoresLista.setModel(DbUtils.resultSetToTableModel(Rs));
        } catch (Exception e) {

        }
    }
    
    private void DisplayFornecedores(){
        try{
            conectar = (Connection) DriverManager.getConnection("jdbc:mysql://sql10.freesqldatabase.com:3306/sql10662533", "sql10662533", "9VUyNsa1k3");
            St = (Statement) conectar.createStatement();
            Rs = St.executeQuery("select * from fornecedorTbl");
            FornecedoresLista.setModel(DbUtils.resultSetToTableModel(Rs));                 
        }catch (Exception e){
    }
    }

    private void setSearchedClient() {
        int set = FornecedoresLista.getSelectedRow();
        ForIdTb.setText(FornecedoresLista.getModel().getValueAt(set, 0).toString());
        ForNomeTb.setText(FornecedoresLista.getModel().getValueAt(set, 1).toString());
        ForEnderecoTb.setText(FornecedoresLista.getModel().getValueAt(set, 2).toString());
        ForEmailTb.setText(FornecedoresLista.getModel().getValueAt(set, 3).toString());
        ForTelefoneTb.setText(FornecedoresLista.getModel().getValueAt(set, 4).toString());
        //unables the add button so you dont add the same client thats already registered
        AdicionarBotao.setEnabled(false);

    }

    private void atualizar() {
        String sql = "Update fornecedorTbl set nome_fornecedor=? ,endereco_fornecedor=? ,telefone_fornecedor=? ,email_fornecedor=? where id_fornecedor =?";

        try {
            Pst = conectar.prepareStatement(sql);
            Pst.setString(1, ForNomeTb.getText());
            Pst.setString(2, ForEnderecoTb.getText());
            Pst.setString(3, ForTelefoneTb.getText());
            Pst.setString(4, ForEmailTb.getText());
            Pst.setString(5, ForIdTb.getText());
            //checks if the textfields are empty
            if (ForNomeTb.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "O nome do cliente não pode ficar vazio e deve ser um número");
            } else if (ForEnderecoTb.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "O endereço não pode ficar vazio");
            } else if (ForTelefoneTb.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "O número de telefone não pode ficar vazio");
            } else if (ForEmailTb.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "O email não pode ficar vazio"); 
            } else {
                int atualizado = Pst.executeUpdate(); 

                if (atualizado > 0) {
                    JOptionPane.showMessageDialog(null, "Usuario atualizado com sucesso");
                    AdicionarBotao.setEnabled(true);
                    cleanpage();
                    DisplayFornecedores();
                   
                } else {
                    JOptionPane.showMessageDialog(null, "Falha ao atualizar o usuario");
                }
            }
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    private void delete() {
        int confirm_delete = JOptionPane.showConfirmDialog(null, "are you sure you want to delete this client", "Warning", JOptionPane.YES_NO_OPTION);
       if (confirm_delete == JOptionPane.YES_OPTION) {
       String sql = "delete from fornecedorTbl where id_fornecedor=?";
            try {
                Pst = conectar.prepareStatement(sql);
                Pst.setString(1, ForIdTb.getText());
                int deleted = Pst.executeUpdate();
                if (deleted > 0) {
                 JOptionPane.showMessageDialog(null, "user deleted");
                   cleanpage();
                   DisplayFornecedores();
                   AdicionarBotao.setEnabled(true);
                }
           }catch(SQLIntegrityConstraintViolationException errorsql){
               JOptionPane.showMessageDialog(null, "It is not possible to delete a client with active service orders");
          
            }catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
               
            }

       }
    }

    private void cleanpage() {
        ForIdTb.setText(null);
        ForNomeTb.setText(null);
        ForEnderecoTb.setText(null);
        ForEmailTb.setText(null);
        ForTelefoneTb.setText(null);
        ((DefaultTableModel) FornecedoresLista.getModel()).setRowCount(0);

    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        jPanel3 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        ForNomeTb = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        ForEnderecoTb = new javax.swing.JTextField();
        jSeparator6 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        FornecedoresLista = new javax.swing.JTable();
        jLabel16 = new javax.swing.JLabel();
        ForEmailTb = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        ForTelefoneTb = new javax.swing.JTextField();
        AdicionarBotao = new javax.swing.JButton();
        AtualizarBotao = new javax.swing.JButton();
        DeletarBotao = new javax.swing.JButton();
        ProcurarForTb = new javax.swing.JTextField();
        ProcurarBotao = new javax.swing.JButton();
        ForIdTb = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(40, 0), new java.awt.Dimension(40, 0), new java.awt.Dimension(40, 32767));
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        BotaoItens = new javax.swing.JButton();
        BotaoFornecedores = new javax.swing.JButton();
        BotaoFuncionarios = new javax.swing.JButton();
        BotaoFinanceiro = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(2, 0, 217));

        jLabel4.setBackground(new java.awt.Color(250, 250, 250));
        jLabel4.setFont(new java.awt.Font("Segoe UI Symbol", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(250, 250, 250));
        jLabel4.setText("Logo ");

        jLabel5.setBackground(new java.awt.Color(250, 250, 250));
        jLabel5.setFont(new java.awt.Font("Segoe UI Symbol", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(250, 250, 250));
        jLabel5.setText("Sair");

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/sistema/icons/vitas/Logo 4848.png"))); // NOI18N

        jLabel13.setBackground(new java.awt.Color(250, 0, 250));
        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(250, 0, 250));
        jLabel13.setText("Fornecedores");

        jLabel14.setText("Endereço");

        jLabel15.setText("Nome ");

        FornecedoresLista.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        FornecedoresLista.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        FornecedoresLista.setRowHeight(40);
        FornecedoresLista.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                FornecedoresListaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(FornecedoresLista);

        jLabel16.setText("Email");

        jLabel17.setText("Telefone");

        AdicionarBotao.setText("Adicionar");
        AdicionarBotao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AdicionarBotaoActionPerformed(evt);
            }
        });

        AtualizarBotao.setText("Atualizar");
        AtualizarBotao.setActionCommand("Update");
        AtualizarBotao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AtualizarBotaoActionPerformed(evt);
            }
        });

        DeletarBotao.setText("Deletar");
        DeletarBotao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeletarBotaoActionPerformed(evt);
            }
        });

        ProcurarForTb.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ProcurarForTbKeyReleased(evt);
            }
        });

        ProcurarBotao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/sistema/icons/vitas/icon_search.png"))); // NOI18N
        ProcurarBotao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProcurarBotaoActionPerformed(evt);
            }
        });

        jLabel1.setText("Id Fornecedor");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(ProcurarBotao)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ProcurarForTb, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(45, 45, 45)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel13)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(ForIdTb, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING))
                                        .addGap(48, 48, 48)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addComponent(ForNomeTb, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(49, 49, 49)
                                .addComponent(AdicionarBotao)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(AtualizarBotao)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(DeletarBotao))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel14)
                                    .addComponent(ForEnderecoTb, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(40, 40, 40)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(ForEmailTb, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(40, 40, 40)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ForTelefoneTb, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1040, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 1080, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel13)
                .addGap(14, 14, 14)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(ForTelefoneTb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(ForNomeTb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(ForIdTb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ForEmailTb, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ProcurarBotao, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(DeletarBotao)
                                        .addComponent(AtualizarBotao)
                                        .addComponent(AdicionarBotao))
                                    .addComponent(ProcurarForTb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(24, 24, 24)))
                        .addGap(46, 46, 46)
                        .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ForEnderecoTb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/sistema/icons/vitas/Box-Open-icon.png"))); // NOI18N

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/sistema/icons/vitas/icons8-caminhão-48.png"))); // NOI18N

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/sistema/icons/vitas/icons8-usuário-48.png"))); // NOI18N

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/sistema/icons/vitas/icons8-saco-de-dinheiro-48.png"))); // NOI18N

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/sistema/icons/vitas/exit-to-app-icon.png"))); // NOI18N

        BotaoItens.setBackground(new java.awt.Color(2, 0, 217));
        BotaoItens.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        BotaoItens.setForeground(new java.awt.Color(255, 255, 255));
        BotaoItens.setText("Itens");
        BotaoItens.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotaoItensActionPerformed(evt);
            }
        });

        BotaoFornecedores.setBackground(new java.awt.Color(2, 0, 217));
        BotaoFornecedores.setFont(new java.awt.Font("Segoe UI Semibold", 0, 16)); // NOI18N
        BotaoFornecedores.setForeground(new java.awt.Color(255, 255, 255));
        BotaoFornecedores.setText("Fornecedores");
        BotaoFornecedores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotaoFornecedoresActionPerformed(evt);
            }
        });

        BotaoFuncionarios.setBackground(new java.awt.Color(2, 0, 217));
        BotaoFuncionarios.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        BotaoFuncionarios.setForeground(new java.awt.Color(255, 255, 255));
        BotaoFuncionarios.setText("Funcionários");
        BotaoFuncionarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotaoFuncionariosActionPerformed(evt);
            }
        });

        BotaoFinanceiro.setBackground(new java.awt.Color(2, 0, 217));
        BotaoFinanceiro.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        BotaoFinanceiro.setForeground(new java.awt.Color(255, 255, 255));
        BotaoFinanceiro.setText("Financeiro");
        BotaoFinanceiro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotaoFinanceiroActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jSeparator1)
                                .addGap(59, 59, 59))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(BotaoItens, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jSeparator2, javax.swing.GroupLayout.DEFAULT_SIZE, 6, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(BotaoFuncionarios, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                                            .addComponent(BotaoFornecedores, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                    .addComponent(BotaoFinanceiro, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jSeparator3, javax.swing.GroupLayout.DEFAULT_SIZE, 6, Short.MAX_VALUE)
                                    .addComponent(jSeparator4)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator5)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addGap(164, 164, 164)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(BotaoItens, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9)
                            .addComponent(BotaoFornecedores, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(BotaoFuncionarios, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(45, 45, 45)
                                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(BotaoFinanceiro, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel12))))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BotaoItensActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotaoItensActionPerformed
       Tela_Item tela_item = new Tela_Item();
       tela_item.setVisible(true);
       this.dispose();

    }//GEN-LAST:event_BotaoItensActionPerformed

    private void BotaoFinanceiroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotaoFinanceiroActionPerformed
        Tela_Financeiro tela_financeiro = new Tela_Financeiro();
        tela_financeiro.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_BotaoFinanceiroActionPerformed

    private void BotaoFornecedoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotaoFornecedoresActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BotaoFornecedoresActionPerformed

    private void BotaoFuncionariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotaoFuncionariosActionPerformed
        Tela_Funcionario tela_funcionario = new Tela_Funcionario();
        tela_funcionario.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_BotaoFuncionariosActionPerformed

    private void ProcurarBotaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProcurarBotaoActionPerformed
        search();
    }//GEN-LAST:event_ProcurarBotaoActionPerformed

    private void ProcurarForTbKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ProcurarForTbKeyReleased
        
    }//GEN-LAST:event_ProcurarForTbKeyReleased

    private void DeletarBotaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeletarBotaoActionPerformed
        delete();
    }//GEN-LAST:event_DeletarBotaoActionPerformed

    private void AtualizarBotaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AtualizarBotaoActionPerformed
        atualizar();
    }//GEN-LAST:event_AtualizarBotaoActionPerformed

    private void AdicionarBotaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AdicionarBotaoActionPerformed
        adicionar();
    }//GEN-LAST:event_AdicionarBotaoActionPerformed

    private void FornecedoresListaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_FornecedoresListaMouseClicked
        setSearchedClient();
    }//GEN-LAST:event_FornecedoresListaMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Tela_Fornecedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Tela_Fornecedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Tela_Fornecedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Tela_Fornecedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Tela_Fornecedor().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AdicionarBotao;
    private javax.swing.JButton AtualizarBotao;
    private javax.swing.JButton BotaoFinanceiro;
    private javax.swing.JButton BotaoFornecedores;
    private javax.swing.JButton BotaoFuncionarios;
    private javax.swing.JButton BotaoItens;
    private javax.swing.JButton DeletarBotao;
    private javax.swing.JTextField ForEmailTb;
    private javax.swing.JTextField ForEnderecoTb;
    private javax.swing.JTextField ForIdTb;
    private javax.swing.JTextField ForNomeTb;
    private javax.swing.JTextField ForTelefoneTb;
    private javax.swing.JTable FornecedoresLista;
    private javax.swing.JButton ProcurarBotao;
    private javax.swing.JTextField ProcurarForTb;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    // End of variables declaration//GEN-END:variables
}
