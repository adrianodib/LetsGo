package br.com.sigma.ocr;

import java.io.File;

public class TesteSplit {

	public static void main(String[] args) {
		
		try {
			File arquivos[];
			File diretorio = new File("/Users/adriano.dib/Documents/Scanned Documents/TesteSplit");
			arquivos = diretorio.listFiles();
			
			for (File file : arquivos) {
				
				String nomeArquivo = file.getName();
				nomeArquivo = nomeArquivo.substring(nomeArquivo.indexOf("-") + 1);
				nomeArquivo = nomeArquivo.substring(0, nomeArquivo.indexOf("."));				
				
				System.out.println(nomeArquivo);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//int numFlolha = Integer.valueOf(file.getName().split("-")); 
		
		//doc.setNumeroFolha(file.getName().split("-"));
		
		

	}

}
