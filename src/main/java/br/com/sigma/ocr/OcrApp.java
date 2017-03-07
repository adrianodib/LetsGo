package br.com.sigma.ocr;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.text.PlainDocument;

import com.asprise.ocr.Ocr;

import br.com.sigma.ocr.bean.Documento;
import br.com.sigma.ocr.dao.OcrDAO;
import br.com.sigma.ocr.layout.MyIntFilter;
import br.com.sigma.ocr.layout.NegociosTableModel;

/**
 * @author adriano.dib
 * @since 11/02/2017
 */
public class OcrApp extends JFrame{
	
	private static final long serialVersionUID = 1L;
	private File diretorio;
	private File[] arquivos;
	private JFrame janela;
	private JButton btExecutarOCR;
	private JButton btAbrirArquivo;
	private JButton btPesquisa;
	
	private JPanel painelPrincipal;
	private JPanel painelBotoes;
	//private JPanel painelLog;
	private JLabel labelPath;
	private JLabel labelResultado;
	private JLabel labelArmario;
	private JLabel labelGaveta;
	private JLabel labelPasta;
	
	private JTextField textFieldPesquisa;
	//private JTextField textFieldLog;
	private List<Documento> listaResultado;
	//private JProgressBar jProgressBar; 
	private JTable table; 	
	private Desktop desktop;
    private Documento doc;
    private JPanel painelLabels;
	
    private JTextField textFieldArmario;
	private JTextField textFieldGaveta;
	private JTextField textFieldPasta;
	private Desktop.Action action;
	
	private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public static void main(String[] args){
		Locale.setDefault(new Locale("pt", "BR"));
		OcrApp lets = new OcrApp();
		lets.inicializaLog();
		lets.preparaJanela();
		lets.inicializaTable();
		lets.inicializaListeners();
	}
	
	private void preparaJanela() {
		
		action = Desktop.Action.OPEN;

		//jProgressBar = new JProgressBar(); 
		desktop = Desktop.getDesktop();
		doc = new Documento();
		
		labelPath = new JLabel();
		
		textFieldPesquisa = new JTextField(20);
		labelResultado = new JLabel();
		//textFieldLog = new JTextField();
		listaResultado = new ArrayList<Documento>();
		
		btAbrirArquivo = new JButton("Selecionar Pasta");
		btExecutarOCR = new JButton("Executar OCR");
		btPesquisa = new JButton("Pesquisar");
		
		labelArmario = new JLabel("Armário:");
		labelGaveta = new JLabel("Gaveta:");
		labelPasta = new JLabel("Pasta:");
		
		textFieldArmario = new JTextField(3);
		textFieldGaveta = new JTextField(3);
		textFieldPasta = new JTextField(3);
		
		painelLabels = new JPanel();
		painelLabels.add(labelArmario);
		painelLabels.add(textFieldArmario);
		painelLabels.add(labelPasta);
		painelLabels.add(textFieldPasta);
		painelLabels.add(labelGaveta);
		painelLabels.add(textFieldGaveta);
		
		painelPrincipal = new JPanel();
		
		painelPrincipal.add(labelPath);
		painelPrincipal.add(textFieldPesquisa);
		painelPrincipal.add(btPesquisa);
		painelPrincipal.add(labelResultado);
		//painel.add(jProgressBar);

		painelBotoes = new JPanel(new GridLayout());
		painelBotoes.add(btExecutarOCR);
		painelBotoes.add(btAbrirArquivo);
		
		painelPrincipal.add(painelBotoes);
		painelPrincipal.add(painelLabels);
		
		//painelLog = new JPanel(new GridLayout());
		//painelLog.add(textFieldLog);
		//painel.add(painelLog);
		
		janela = new JFrame("Ferramenta de OCR da Sigma");
		janela.add(painelPrincipal);
		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		janela.setSize(900, 500);
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
					logger.log(Level.INFO, "Caminho escolhido: " + file.getAbsolutePath(), e);
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
				try {
					listaResultado = ocrDao.pesquisaDocumento(textoPesquisa);
				} catch (Exception e1) {
					logger.log(Level.SEVERE, e1.getMessage(), e1);
				}
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
					logger.log(Level.INFO, "Documento processado: " + doc.getNumeroDocumento() + " - Folha: " + doc.getNumeroFolha());
					System.out.println("Documento processado: " + doc.getNumeroDocumento() + " - Folha: " + doc.getNumeroFolha());
					//atualizaBarraProgresso(jProgressBar.getValue()+1 );
				}
				OcrDAO ocrDao = new OcrDAO();
				resultado = ocrDao.persisteOCR(listaDoc);
				labelPath.setText(resultado);
			}
			 
		} catch (Exception e){
			e.printStackTrace();
			logger.log(Level.SEVERE, e.getMessage(), e);
		} finally {
			//atualizaBarraProgresso(0);
			ocr.stopEngine();	
		}
	}

	private Documento populaDocumento(File file, String s) {
		
		Documento doc = new Documento();
		doc.setResultado(s);
		doc.setCaminhoLogico(file.getAbsolutePath());
		
		doc.setArmario(textFieldArmario.getText());
		
		PlainDocument plainDoc = (PlainDocument) textFieldGaveta.getDocument();
		plainDoc.setDocumentFilter(new MyIntFilter());
		
		doc.setGaveta(Integer.valueOf(textFieldGaveta.getText()));
		doc.setPasta(textFieldPasta.getText());

		//System.out.println("Iniciando a conversão do número da folha");
		
		String folha = file.getName();
		folha = folha.substring(0, folha.indexOf("."));
		folha = folha.contains("-") ? folha.substring(folha.indexOf("-") + 1) : "0"; 
		
		//Conversao para integer
		Integer numFolha = 0;
		if (!folha.isEmpty()) {
			numFolha = Integer.valueOf(folha);
			//System.out.println("Número da folha: " + numFolha);
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
		table.setSize(750, 450);
				
		JScrollPane scroll = new JScrollPane(); 
		scroll.getViewport().setBorder(null);
		scroll.getViewport().add(table); 
		scroll.setSize(450, 450);	
		painelPrincipal.add(scroll);
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
			logger.log(Level.SEVERE, "Não deu certo a operação para o " + file + " arquivo", ioe); 
        }
    }
	
	public void inicializaLog() {

        Handler console = new ConsoleHandler();
        Handler file;
        
		try {
			file = new FileHandler("C:\\temp\\scanner.txt");
 	        //Define que no console aparece apenas log com nível superior à warning
	        console.setLevel(Level.WARNING);
	        
	        //No arquivo deve aparecer o log de qualquer nível
	        file.setLevel(Level.ALL);
	        
	        //Define o formato de output do ficheiro como XML
	        file.setFormatter(new SimpleFormatter());
	        //Adiciona os handlers para ficheiro e console
	        logger.addHandler(file);
	        logger.addHandler(console);
	        //Ignora os Handlers definidos no Logger Global
	        logger.setUseParentHandlers(false);
	        
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e); 
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