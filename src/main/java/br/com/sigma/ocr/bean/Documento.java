package br.com.sigma.ocr.bean;


/**
 * @author adriano.dib
 * @since 11/02/2017
 */
public class Documento {

	private String resultado;
	private String caminhoLogico;
	private String armario;
	private int gaveta;
	private String pasta;
	private String numeroDocumento;
	private int numeroFolha;
	
	public String getResultado() {
		return resultado;
	}
	public void setResultado(String resultado) {
		this.resultado = resultado;
	}
	public String getCaminhoLogico() {
		return caminhoLogico;
	}
	public void setCaminhoLogico(String caminhoLogico) {
		this.caminhoLogico = caminhoLogico;
	}
	public String getArmario() {
		return armario;
	}
	public void setArmario(String armario) {
		this.armario = armario;
	}
	public int getGaveta() {
		return gaveta;
	}
	public void setGaveta(int gaveta) {
		this.gaveta = gaveta;
	}
	public String getPasta() {
		return pasta;
	}
	public void setPasta(String pasta) {
		this.pasta = pasta;
	}
	public String getNumeroDocumento() {
		return numeroDocumento;
	}
	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}
	public int getNumeroFolha() {
		return numeroFolha;
	}
	public void setNumeroFolha(int numeroFolha) {
		this.numeroFolha = numeroFolha;
	}
	

}