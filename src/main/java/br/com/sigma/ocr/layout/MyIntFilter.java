package br.com.sigma.ocr.layout;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.DocumentFilter.FilterBypass;

public class MyIntFilter extends DocumentFilter {
   @Override
   public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {

      Document doc = fb.getDocument();
      StringBuilder sb = new StringBuilder();
      sb.append(doc.getText(0, doc.getLength()));
      sb.insert(offset, string);

      if (test(sb.toString())) {
         super.insertString(fb, offset, string, attr);
      } else {
         // warn the user and don't allow the insert
      }
   }

   private boolean test(String text) {
      try {
         Integer.parseInt(text);
         return true;
      } catch (NumberFormatException e) {
         return false;
      }
   }}
