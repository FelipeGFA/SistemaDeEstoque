/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
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
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author Lukitas
 */
public class Tela_Funcionario extends javax.swing.JFrame {
    Connection conectar = null;
    Statement St = null, St1 = null;
    PreparedStatement Pst = null;
    ResultSet Rs = null, Rs1 = null;

    public Tela_Funcionario() {
        initComponents();
        conectar = connector.Conector();
        DisplayFuncionario();
    }
     
    private void adicionar() {
        String sql = "insert into funcionarioTbl(nome_funcionario,cpf_funcionario, email_funcionario) values(?,?,?)";

        try {
            Pst = conectar.prepareStatement(sql);
            Pst.setString(1, FunNomeTb.getText());
            Pst.setString(2, FunCpfTb.getText());
            Pst.setString(3, FunEmailTb.getText());

            // checks if the textfields are empty 
            if (FunNomeTb.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "O nome do funcionário está vazio.");
            } else if (FunCpfTb.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "O CPF do funcionário está vazio.");
            } else if (FunEmailTb.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "O email do funcionário está vazio.");
            } else {
                int added = Pst.executeUpdate();

                if (added > 0) {
                    JOptionPane.showMessageDialog(null, "Funcionário adicionado com sucesso!");
                    cleanpage();
                    DisplayFuncionario();                  

                } else {
                    JOptionPane.showMessageDialog(null, "Falha ao adicionar funcionário.");
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    private void search() {
        String sql = "select * from funcionarioTbl where id_funcionario like ?";
        try {
            Pst = conectar.prepareStatement(sql);
            Pst.setString(1, ProcurarFunTb.getText() + "%");
            Rs = Pst.executeQuery();
            //this function comes from the library rs2xml
            FuncionarioLista.setModel(DbUtils.resultSetToTableModel(Rs));
        } catch (Exception e) {

        }
    }
    
    private void DisplayFuncionario(){
        try{
            conectar = (Connection) DriverManager.getConnection("jdbc:mysql://sql10.freesqldatabase.com:3306/sql10662533", "sql10662533", "9VUyNsa1k3");
            St = (Statement) conectar.createStatement();
            Rs = St.executeQuery("select * from funcionarioTbl");
            FuncionarioLista.setModel(DbUtils.resultSetToTableModel(Rs));                 
        }catch (Exception e){
    }
    }

    private void setSearchedClient() {
        int set = FuncionarioLista.getSelectedRow();
        FunIdTb.setText(FuncionarioLista.getModel().getValueAt(set, 0).toString());
        FunNomeTb.setText(FuncionarioLista.getModel().getValueAt(set, 1).toString());
        FunCpfTb.setText(FuncionarioLista.getModel().getValueAt(set, 2).toString());
        FunEmailTb.setText(FuncionarioLista.getModel().getValueAt(set, 3).toString());
        //ForTelefoneTb.setText(FuncionarioLista.getModel().getValueAt(set, 4).toString());
        //unables the add button so you dont add the same client thats already registered
        AdicionarBotao.setEnabled(false);

    }

    private void atualizar() {
        String sql = "Update funcionarioTbl set nome_funcionario = ? ,cpf_funcionario = ? ,email_funcionario = ? where id_funcionario = ?";

        try {
            Pst = conectar.prepareStatement(sql);            
            Pst.setString(1, FunNomeTb.getText());
            Pst.setString(2, FunCpfTb.getText());
            Pst.setString(3, FunEmailTb.getText());
            Pst.setString(4, FunIdTb.getText());
            
            //checks if the textfields are empty
            if (FunNomeTb.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "O nome do funcionário está vazio.");
            } else if (FunCpfTb.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "O CPF do funcionário está vazio.");}
            else if (FunEmailTb.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "O email do funcionário está vazio.");           
            }  else {
                int atualizado = Pst.executeUpdate(); 

                if (atualizado > 0) {
                    JOptionPane.showMessageDialog(null, "Funcionário atualizado com sucesso");
                    AdicionarBotao.setEnabled(true);
                    cleanpage();
                    DisplayFuncionario();
                   
                } else {
                    JOptionPane.showMessageDialog(null, "Falha ao atualizar o funcionário");
                }
            }
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    private void delete() {
        int confirm_delete = JOptionPane.showConfirmDialog(null, "Você deseja excluir esse funcionário?", "Aviso!", JOptionPane.YES_NO_OPTION);
       if (confirm_delete == JOptionPane.YES_OPTION) {
       String sql = "delete from funcionarioTbl where id_funcionario=?";
            try {
                Pst = conectar.prepareStatement(sql);
                Pst.setString(1, FunIdTb.getText());
                int deleted = Pst.executeUpdate();
                if (deleted > 0) {
                 JOptionPane.showMessageDialog(null, "Funcionário deletado!");
                   cleanpage();
                   DisplayFuncionario();
                   AdicionarBotao.setEnabled(true);
                }
           }catch(SQLIntegrityConstraintViolationException errorsql){
               JOptionPane.showMessageDialog(null, "It is not possible to delete a funcionário with active service orders");
          
            }catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
               
            }

       }
    }

    private void cleanpage() {
        FunIdTb.setText(null);
        FunNomeTb.setText(null);
        FunCpfTb.setText(null);
        FunEmailTb.setText(null);
        //ForTelefoneTb.setText(null);
        ((DefaultTableModel) FuncionarioLista.getModel()).setRowCount(0);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jPanel3 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        FunNomeTb = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        FunCpfTb = new javax.swing.JTextField();
        jSeparator6 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        FuncionarioLista = new javax.swing.JTable();
        jLabel16 = new javax.swing.JLabel();
        FunEmailTb = new javax.swing.JTextField();
        AdicionarBotao = new javax.swing.JButton();
        AtualizarBotao = new javax.swing.JButton();
        DeletarBotao = new javax.swing.JButton();
        ProcurarFunTb = new javax.swing.JTextField();
        ProcurarFunBotao = new javax.swing.JButton();
        FunIdTb = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
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

        jLabel5.setBackground(new java.awt.Color(250, 250, 250));
        jLabel5.setFont(new java.awt.Font("Segoe UI Symbol", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(250, 250, 250));
        jLabel5.setText("Sair");
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
        });

        jLabel4.setBackground(new java.awt.Color(250, 250, 250));
        jLabel4.setFont(new java.awt.Font("Segoe UI Symbol", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(250, 250, 250));
        jLabel4.setText("Logo ");

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/sistema/icons/vitas/Logo 4848.png"))); // NOI18N

        jLabel13.setBackground(new java.awt.Color(250, 0, 250));
        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(250, 0, 250));
        jLabel13.setText("Funcionários");

        jLabel14.setText("CPF");

        jLabel15.setText("Nome ");

        FunCpfTb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FunCpfTbActionPerformed(evt);
            }
        });

        FuncionarioLista.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        FuncionarioLista.setModel(new javax.swing.table.DefaultTableModel(
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
        FuncionarioLista.setRowHeight(40);
        FuncionarioLista.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                FuncionarioListaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(FuncionarioLista);

        jLabel16.setText("Email");

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

        ProcurarFunTb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProcurarFunTbActionPerformed(evt);
            }
        });
        ProcurarFunTb.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ProcurarFunTbKeyReleased(evt);
            }
        });

        ProcurarFunBotao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/sistema/icons/vitas/icon_search.png"))); // NOI18N
        ProcurarFunBotao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProcurarFunBotaoActionPerformed(evt);
            }
        });

        FunIdTb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FunIdTbActionPerformed(evt);
            }
        });

        jLabel1.setText("ID");

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
                                .addComponent(ProcurarFunBotao)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ProcurarFunTb, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(45, 45, 45)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addGap(12, 12, 12)
                                                .addComponent(jLabel1))
                                            .addComponent(FunIdTb, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(81, 81, 81)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(FunNomeTb, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(44, 44, 44)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel14)
                                            .addComponent(FunCpfTb, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(jLabel13))))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(49, 49, 49)
                                .addComponent(AdicionarBotao)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(AtualizarBotao)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(DeletarBotao))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(41, 41, 41)
                                .addComponent(FunEmailTb, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(52, 52, 52)
                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1040, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 1080, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(89, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel13)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(FunNomeTb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(FunCpfTb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(FunEmailTb, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(FunIdTb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ProcurarFunBotao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(DeletarBotao)
                                .addComponent(AtualizarBotao)
                                .addComponent(AdicionarBotao))
                            .addComponent(ProcurarFunTb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)))
                .addGap(46, 46, 46)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
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
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                                        .addComponent(jSeparator2, javax.swing.GroupLayout.DEFAULT_SIZE, 4, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(BotaoFuncionarios, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(BotaoFornecedores, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                    .addComponent(BotaoFinanceiro, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jSeparator3, javax.swing.GroupLayout.DEFAULT_SIZE, 5, Short.MAX_VALUE)
                                    .addComponent(jSeparator4)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator5)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
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
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addGap(9, 9, 9))))))
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

    private void FunCpfTbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FunCpfTbActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FunCpfTbActionPerformed

    private void FuncionarioListaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_FuncionarioListaMouseClicked
        setSearchedClient();
    }//GEN-LAST:event_FuncionarioListaMouseClicked

    private void AdicionarBotaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AdicionarBotaoActionPerformed
        adicionar();
    }//GEN-LAST:event_AdicionarBotaoActionPerformed

    private void AtualizarBotaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AtualizarBotaoActionPerformed
        atualizar();
    }//GEN-LAST:event_AtualizarBotaoActionPerformed

    private void DeletarBotaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeletarBotaoActionPerformed
        delete();        // TODO add your handling code here:
    }//GEN-LAST:event_DeletarBotaoActionPerformed

    private void ProcurarFunTbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProcurarFunTbActionPerformed
        search();
    }//GEN-LAST:event_ProcurarFunTbActionPerformed

    private void ProcurarFunTbKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ProcurarFunTbKeyReleased
        // TODO add your handling code here:
        //while is typing it keeps running the method (key released)
    }//GEN-LAST:event_ProcurarFunTbKeyReleased

    private void ProcurarFunBotaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProcurarFunBotaoActionPerformed
        search();
    }//GEN-LAST:event_ProcurarFunBotaoActionPerformed

    private void FunIdTbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FunIdTbActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FunIdTbActionPerformed

    private void BotaoItensActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotaoItensActionPerformed
        Tela_Item tela_item = new Tela_Item();
        tela_item.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_BotaoItensActionPerformed

    private void BotaoFornecedoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotaoFornecedoresActionPerformed
        Tela_Funcionario Tela_Funcionario = new Tela_Funcionario();
        Tela_Funcionario.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_BotaoFornecedoresActionPerformed

    private void BotaoFuncionariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotaoFuncionariosActionPerformed

    }//GEN-LAST:event_BotaoFuncionariosActionPerformed

    private void BotaoFinanceiroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotaoFinanceiroActionPerformed
        Tela_Financeiro tela_financeiro = new Tela_Financeiro();
        tela_financeiro.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_BotaoFinanceiroActionPerformed

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        new Tela_Login().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jLabel5MouseClicked

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
            java.util.logging.Logger.getLogger(Tela_Funcionario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Tela_Funcionario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Tela_Funcionario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Tela_Funcionario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Tela_Funcionario().setVisible(true);
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
    private javax.swing.JTextField FunCpfTb;
    private javax.swing.JTextField FunEmailTb;
    private javax.swing.JTextField FunIdTb;
    private javax.swing.JTextField FunNomeTb;
    private javax.swing.JTable FuncionarioLista;
    private javax.swing.JButton ProcurarFunBotao;
    private javax.swing.JTextField ProcurarFunTb;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
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
