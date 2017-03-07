package br.com.sigma.ocr.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import br.com.sigma.ocr.bean.Documento;

/**
 * @author adriano.dib
 * @since 11/02/2017
 */
public class OcrDAO {
	
	private Connection db;
	private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public void openConn() throws ClassNotFoundException, FileNotFoundException, IOException, SQLException {
		
		String url = "jdbc:postgresql://localhost/OCR?charSet=UTF-8";
		//String url = "jdbc:postgresql://192.168.40.97/OCR?charSet=UTF-8";
        String usr = "postgres";
        String pwd = "senha123";		
        		
		
        Class.forName("org.postgresql.Driver");
        db = DriverManager.getConnection(url, usr, pwd);
        
        inicializaLog();

	}
	
	public String persisteOCR(List<Documento> documentos) throws Exception {
		
		String resultado = "";
		try {
			openConn();
			PreparedStatement ps = db.prepareStatement("insert into documento values (default,?,?,?,?,?,?,?)");
			for (Documento documento : documentos) {

				ps.setString(1, documento.getResultado());
				ps.setString(2, documento.getCaminhoLogico());
				ps.setString(3, documento.getArmario());
				ps.setInt(4, documento.getGaveta());
				ps.setString(5, documento.getPasta());
				ps.setString(6, documento.getNumeroDocumento());
				ps.setInt(7, documento.getNumeroFolha());
				ps.executeUpdate();
			} 
			ps.close();
			resultado = getDataHora() + " - Gravação finalizada \n";
			logger.log(Level.INFO, getDataHora() + " - Gravação finalizada");
			System.out.println(resultado);
			
		} catch (Exception e) {
			resultado = getDataHora() + " - Houve uma falha na gravação. Verifique o Log";
			logger.log(Level.SEVERE, getDataHora() + " - Houve uma falha na gravação. Verifique o Log");
			System.out.println(resultado);
			e.printStackTrace();
			throw e;
		} finally {
			try {
				db.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return resultado;
	}
	
	
	public List<Documento> pesquisaDocumento(String palavra) throws Exception {
		
		List<Documento> documentoList = new ArrayList<Documento>();

		try {
			openConn();
			PreparedStatement ps = db.prepareStatement("select * from documento where resultado like ?");
			ps.setString(1, "%"+ palavra +"%");
						
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()){
		      Documento doc = new Documento();
		      doc.setArmario(rs.getString("armario"));
		      doc.setCaminhoLogico(rs.getString("caminho_logico"));
		      doc.setGaveta(rs.getInt("gaveta"));
		      doc.setNumeroDocumento(rs.getString("numero_documento"));
		      doc.setNumeroFolha(rs.getInt("numero_folha"));
		      doc.setPasta(rs.getString("pasta"));
		      doc.setResultado(rs.getString("resultado"));
		      documentoList.add(doc);
		    }
			ps.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally{
			try {
				db.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}
		}
		return documentoList;
	}
	
	private String getDataHora()  {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return(sdf.format(d));
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
	
}