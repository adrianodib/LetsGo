package br.com.sigma.ocr.util;

import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerOcr {

	private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		
	public LoggerOcr(){
		inicializaLog();
	}
	
	public void inicializaLog() {

        Handler console = new ConsoleHandler();
        Handler file;
        
		try {
			file = new FileHandler("C:\\temp\\scanner.txt");
 	        //Define que na consola apenas aparece log com nível superior ou o warning e no ficheiro deve aparecer o log de qualquer nível
	        console.setLevel(Level.WARNING);
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
	
}
