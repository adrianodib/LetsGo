package br.com.sigma.ocr;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.awt.Desktop;

import javax.print.attribute.TextSyntax;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.BoxLayout;

import com.asprise.ocr.Ocr;

import br.com.sigma.ocr.bean.Documento;
import br.com.sigma.ocr.dao.OcrDAO;
import br.com.sigma.ocr.layout.NegociosTableModel;

/**
 * @author adriano.dib
 * @since 11/02/2017
 */
public class LetsGo extends JFrame{
	
	private static final long serialVersionUID = 1L;
	private File diretorio;
	private File[] arquivos;
	private JFrame janela;
	private JButton btExecutarOCR;
	private JButton btAbrirArquivo;
	private JButton btPesquisa;
	private JPanel painel;
	private JPanel painelBotoes;
	private JPanel painelLog;
	private JLabel labelPath;
	private JLabel labelResultado;
	private JLabel labelArmario;
	private JLabel labelGaveta;
	private JLabel labelPasta;
	private JTextField textFieldPesquisa;
	private JTextField textFieldLog;
	private List<Documento> listaResultado;
	private JProgressBar jProgressBar; 
	private JTable table; 	
	private Desktop desktop;
    private Desktop.Action action = Desktop.Action.OPEN;
    private Documento doc;
	
	public static void main(String[] args){
		Locale.setDefault(new Locale("pt", "BR"));
		LetsGo lets = new LetsGo();
		lets.preparaJanela();
		lets.inicializaTable();
		lets.inicializaListeners();
	}
	
	private void preparaJanela() {

		//jProgressBar = new JProgressBar(); 
		desktop = Desktop.getDesktop();
		doc = new Documento();
		
		labelPath = new JLabel();

		labelArmario = new JLabel();
		labelGaveta = new JLabel();
		labelPasta = new JLabel();
		textFieldPesquisa = new JTextField(20);
		labelResultado = new JLabel();
		textFieldLog = new JTextField();
		listaResultado = new ArrayList<Documento>();
		
		btExecutarOCR = new JButton("Executar OCR");
		btAbrirArquivo = new JButton("Selecionar Pasta");
		btPesquisa = new JButton("Pesquisar");
		
		painel = new JPanel();
		painel.add(labelPath);
		painel.add(textFieldPesquisa);
		painel.add(btPesquisa);
		painel.add(labelResultado);
		//painel.add(jProgressBar);

		painelBotoes = new JPanel(new GridLayout());
		painelBotoes.add(btExecutarOCR);
		painelBotoes.add(btAbrirArquivo);
		
		painel.add(painelBotoes);
		
		//painelLog = new JPanel(new GridLayout());
		//painelLog.add(textFieldLog);
		//painel.add(painelLog);
		
		janela = new JFrame("Ferramenta de OCR da Sigma");
		janela.add(painel);
		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		janela.setSize(800, 500);
		janela.setLocationRelativeTo(null);
		janela.setVisible(true);
	}

	private void inicializaListeners(){

		btAbrirArquivo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				//JFileChooser fileChooser = new JFileChooser("/Users/adriano.dib/Documents/Scanned Documents/TesteSplit");
				JFileChooser fileChooser = new JFileChooser("/TesteSplit");
				int retorno = fileChooser.showOpenDialog(null);				
				if (retorno == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getCurrentDirectory();
					diretorio = new File(file.getAbsolutePath());
					labelPath.setText("Caminho escolhido: " + file.getAbsolutePath());
					arquivos = diretorio.listFiles();
				} 				
			}
		});
		
		btExecutarOCR.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				performOCR();
			}
		});
		
		btPesquisa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OcrDAO ocrDao = new OcrDAO();
				String textoPesquisa = textFieldPesquisa.getText();
				listaResultado = ocrDao.pesquisaDocumento(textoPesquisa);
				NegociosTableModel ntm = new NegociosTableModel(listaResultado);
			    table.setModel(ntm);				
			}
		});
		
		table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
		        JTable table =(JTable) me.getSource();
		        Point p = me.getPoint();
		        int row = table.rowAtPoint(p);
		        if (me.getClickCount() == 2) {
		        	doc = (Documento) listaResultado.get(row);
		        	onOpenAction(me);
		        	onLaunchDefaultApplication(me);
		        }
		    }
		});
	}
	
	private void performOCR(){
		String resultado;
		List<Documento> listaDoc;
		Ocr.setUp();
		Ocr ocr = new Ocr();
		ocr.startEngine("por", Ocr.SPEED_FASTEST);
		listaDoc = new ArrayList<Documento>();
		
		//configuraBarraProgresso(arquivos.length);
		//configuraBarraProgresso(100);
		/*
		for (int i=0; i<100; i++){
			atualizaBarraProgresso(i);
		}*/
		
		try {
			
			if (arquivos != null){
				for (File file : arquivos) {
					String s = ocr.recognize(new File[] { new File(file.getAbsolutePath()) }, Ocr.RECOGNIZE_TYPE_ALL, Ocr.OUTPUT_FORMAT_PLAINTEXT);
					Documento doc = populaDocumento(file, s);
					listaDoc.add(doc);
					//atualizaBarraProgresso(jProgressBar.getValue()+1 );
				}
				
				OcrDAO ocrDao = new OcrDAO();
				resultado = ocrDao.persisteOCR(listaDoc);
				labelPath.setText(resultado);
			}
			 
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			//atualizaBarraProgresso(0);
			ocr.stopEngine();	
		}
	}

	private Documento populaDocumento(File file, String s) {
		
		Documento doc = new Documento();
		doc.setResultado(s);
		doc.setCaminhoLogico(file.getAbsolutePath());
		doc.setArmario("1");
		doc.setGaveta(1);
		doc.setPasta("1");

		//System.out.println("Iniciando a conversão do número da folha");
		
		String folha = file.getName();
		folha = folha.substring(0, folha.indexOf("."));
		folha = folha.contains("-") ? folha.substring(folha.indexOf("-") + 1) : "0"; 
		
		//Conversao para integer
		Integer numFolha = 0;
		if (!folha.isEmpty()) {
			numFolha = Integer.valueOf(folha);
			System.out.println("Número da folha: " + numFolha);
		}
		//System.out.println("Encerrando a conversão do número da folha");
		
		String [] fileName = file.getName().split("-");
		if (fileName[0].contains(".jpg") || fileName[0].contains(".tif")){
			String numDoc = fileName[0].substring(0, fileName[0].indexOf("."));
			doc.setNumeroDocumento(numDoc);
		} else {
			doc.setNumeroDocumento(fileName[0]);
		}
		
		doc.setNumeroFolha(numFolha);
		return doc;
	}

	private void inicializaTable(){
		
		table = new JTable();
		table.setBorder(new LineBorder(Color.black));
		table.setGridColor(Color.black);
		table.setShowGrid(true);		
		//table.setSize(750, 450);
		table.setSize(1000, 1000);
		
		JScrollPane scroll = new JScrollPane(); 
		scroll.getViewport().setBorder(null);
		scroll.getViewport().add(table); 
		//scroll.setSize(450, 450);	
		scroll.setSize(1000, 1000);	
		painel.add(scroll);

	}

    /**
     * Set the Desktop.Action to OPEN before invoking
     * the default application.
     */
    private void onOpenAction(MouseEvent me) {
        action = Desktop.Action.OPEN;
    }	
	
	private void onLaunchDefaultApplication(MouseEvent me) {
		
		String fileName = doc.getCaminhoLogico().trim();
        File file = new File(fileName);
        
        try {
            switch(action) {
                case OPEN:
                    desktop.open(file);
                    break;
                case EDIT:
                    desktop.edit(file);
                    break;
                case PRINT:
                    desktop.print(file);
                    break;
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Cannot perform the given operation to the " + file + " file");
        }
    }
	/*
	public String path(){
		
		//JFileChooser fileChooser = new JFileChooser();
		JFileChooser fileChooser = new JFileChooser("/Users/adriano.dib/Documents/Scanned Documents/TesteSplit");
		//fileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);
		int retorno = fileChooser.showOpenDialog(null);

		if (retorno == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getCurrentDirectory();
			return file.getAbsolutePath();
		} else {
			return "dialogo cancelado"; 
		}
	}
	*/		
	
	/*
	private void configuraBarraProgresso(int maxValue){
		jProgressBar.setBounds(new Rectangle(20, 20, 200, 20));
		jProgressBar.setMinimum(0);
		jProgressBar.setMaximum(maxValue);
		jProgressBar.setStringPainted(true);
	}
	
	private void atualizaBarraProgresso(int valor) {
		jProgressBar.setValue(valor);
	}	
	*/	
}