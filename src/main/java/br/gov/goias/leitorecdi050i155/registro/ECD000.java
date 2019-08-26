/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.goias.leitorecdi050i155.registro;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.MaskFormatter;
import lombok.Builder;
import lombok.Data;

/**
 *
 * @author admin
 */
@Data
@Builder
public class ECD000 {
    
    String dataInicial;
    String dataFinal;
    String nomeEmpresa;
    String cnpj;
    String uf;
    String ie;
    String codigoMunicipioIBGE;
    String im; 
    
    List<I050> registros = new ArrayList<I050>();

    
    public String getCnpj(){
        try {
            MaskFormatter formatter;
            formatter = new MaskFormatter("AA.AAA.AAA/AAA-AA");
            formatter.setValueContainsLiteralCharacters(false);
            
            return formatter.valueToString(this.cnpj);
        } catch (ParseException ex) {
            Logger.getLogger(ECD000.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
    
    
}