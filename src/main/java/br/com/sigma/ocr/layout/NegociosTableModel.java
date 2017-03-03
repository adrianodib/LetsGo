package br.com.sigma.ocr.layout;

import java.util.List;

import javax.swing.table.AbstractTableModel;
import br.com.sigma.ocr.bean.Documento;

public class NegociosTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private final List<Documento> documentos;

	public NegociosTableModel(List<Documento> documentos) {
		this.documentos = documentos;
	}

	@Override
	public int getColumnCount() {
		return 6;
	}

	@Override
	public int getRowCount() {
		return documentos.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Documento n = documentos.get(rowIndex);
     
		switch (columnIndex) {
			case 0:
				return n.getArmario();
			case 1:
				return n.getGaveta();
			case 2:
				return n.getPasta();
			case 3:
				return n.getNumeroDocumento();
			case 4:
				return n.getNumeroFolha();
			case 5:
				return n.getResultado();
		}
		return "";
	}
	
	@Override
	public String getColumnName(int column){
		switch (column) {
			case 0:
				return "Armário";
			case 1:
				return "Gaveta";
			case 2:
				return "Pasta";
			case 3:
				return "Nº Documento";
			case 4:
				return "Folha";
			case 5:
				return "Texto";
		}
		return "";		
	}
}
	
	
	
	