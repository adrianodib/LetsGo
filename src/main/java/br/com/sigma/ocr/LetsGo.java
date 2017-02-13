package br.com.sigma.ocr;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;

import com.asprise.ocr.Ocr;

import br.com.sigma.ocr.bean.Documento;
import br.com.sigma.ocr.dao.OcrDAO;

/**
 * @author adriano.dib
 * @since 11/02/2017
 */
public class LetsGo {
	
	public static void main(String[] args){
		new LetsGo().lerArquivos();
	}
	
	public void lerArquivos(){

		List<Documento> listaDoc;
		File arquivos[];
		//File diretorio = new File("/Users/adriano.dib/Documents/Scanned Documents/Amostras atestados");
		//File diretorio = new File("/Users/adriano.dib/Documents/Scanned Documents/TesteSplit");

		File diretorio = new File(path());
		arquivos = diretorio.listFiles();

		Ocr.setUp();
		Ocr ocr = new Ocr();
		ocr.startEngine("por", Ocr.SPEED_FASTEST);
		listaDoc = new ArrayList<Documento>();
		
		try {

			for (File file : arquivos) {
				String s = ocr.recognize(new File[] { new File(file.getAbsolutePath()) }, Ocr.RECOGNIZE_TYPE_ALL, Ocr.OUTPUT_FORMAT_PLAINTEXT);
				Documento doc = populaDocumento(file, s);
				listaDoc.add(doc);
				OcrDAO ocrDao = new OcrDAO();
				ocrDao.persisteOCR(listaDoc);
			} 
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			ocr.stopEngine();	
		}
	}
	
	public String path(){
		
		//JFileChooser fileChooser = new JFileChooser();
		JFileChooser fileChooser = new JFileChooser("/Users/adriano.dib/Documents/Scanned Documents/TesteSplit");
		fileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);
		int retorno = fileChooser.showOpenDialog(null);

		if (retorno == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getCurrentDirectory();
			return file.getAbsolutePath();
		} else {
			return "dialogo cancelado"; 
		}
	}	

	private Documento populaDocumento(File file, String s) {
		Documento doc = new Documento();
		doc.setResultado(s);
		doc.setCaminhoLogico(file.getAbsolutePath());
		doc.setArmario("1");
		doc.setGaveta(1);
		doc.setPasta("1");

		System.out.println("Iniciando a conversão do número da folha");
		String folha = file.getName();
		
		folha = folha.substring(0, folha.indexOf("."));
		folha = folha.contains("-") ? folha.substring(folha.indexOf("-") + 1) : "0"; 
		
		//Conversao para integer
		Integer numFolha = 0;
		if (!folha.isEmpty()) {
			numFolha = Integer.valueOf(folha);
			System.out.println("Número da folha: " + numFolha);
		}
		System.out.println("Encerrando a conversão do número da folha");
		
		String [] fileName = file.getName().split("-");
		if (fileName[0].contains(".jpg")){
			String numDoc = fileName[0].substring(0, fileName[0].indexOf("."));
			doc.setNumeroDocumento(numDoc);
		} else {
			doc.setNumeroDocumento(fileName[0]);
		}
		
		doc.setNumeroFolha(numFolha);
		return doc;
	}

}
