package br.com.sigma.ocr.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
        
	}
	
	public String persisteOCR(List<Documento> documentos){
		
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
			System.out.println(resultado);
			
		} catch (Exception e) {
			resultado = getDataHora() + " - Houve uma falha na gravação. Verifique o Log";
			System.out.println(resultado);
			e.printStackTrace();
		}finally{
			try {
				db.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return resultado;
	}
	
	
	public List<Documento> pesquisaDocumento(String palavra){
		
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
		}finally{
			try {
				db.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return documentoList;
	}
	
	private String getDataHora()  {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return(sdf.format(d));
	}
		
	
}
