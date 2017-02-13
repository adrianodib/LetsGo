package br.com.sigma.ocr.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import br.com.sigma.ocr.bean.Documento;

/**
 * @author adriano.dib
 * @since 11/02/2017
 */
public class OcrDAO {
	
	Connection db;
	Statement st; 
	
	public void openConn() throws ClassNotFoundException, FileNotFoundException, IOException, SQLException {
		
		String url = "jdbc:postgresql://localhost/OCR?charSet=UTF-8";
        String usr = "postgres";
        String pwd = "onairdaa";		
		
        Class.forName("org.postgresql.Driver");
        db = DriverManager.getConnection(url, usr, pwd);
        //st = db.createStatement();
        //st.close();
        //db.close();	
        //st.executeQuery(sql)
        
	}
	
	public void persisteOCR(List<Documento> documentos){
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
			
			System.out.println("Gravação realizada!");
			ps.close(); 
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				db.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
}
