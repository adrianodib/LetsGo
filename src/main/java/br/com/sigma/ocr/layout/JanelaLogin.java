package br.com.sigma.ocr.layout;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import javax.swing.JFrame;

public class JanelaLogin implements ActionListener {

    private JPanel mainPanel;
    private JLabel mainLabel;

    public JanelaLogin(){
        
        adicionaCamposLogin();
    }

    private void adicionaCamposLogin(){
        JTextField campoLogin = new JTextField("Login");
        JTextField campoSenha = new JTextField("Senha");
        JButton verificar = new JButton("OK");

        verificar.addActionListener(this);

        mainPanel = new JPanel();
        mainPanel.add(campoLogin);
        mainPanel.add(campoSenha);
        mainPanel.add(verificar);
    }

    private static void criaExibeJanelaLogin() {
        //Seta decoração DEFAULT
        JFrame.setDefaultLookAndFeelDecorated(true);

        //Cria uma nova instancia da janela
        JanelaLogin carregarJanela = new JanelaLogin();

        //Cria e organiza a janela.
        JFrame carregarMainFrame = new JFrame("");
        carregarMainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        carregarMainFrame.setContentPane(carregarJanela.mainPanel);

        //Exibe janela.
        carregarMainFrame.pack();
        carregarMainFrame.setSize(800, 600);
        carregarMainFrame.setVisible(true);

    }

    public static void main(String[] args) {
        //Agenda um trabalho para o event-dispatching thread:
        //cria e exibe uma aplicacao GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                criaExibeJanelaLogin();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        // TODO Auto-generated method stub
    }

}