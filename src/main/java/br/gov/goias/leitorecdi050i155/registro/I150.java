/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.goias.leitorecdi050i155.registro;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 *
 * REGISTRO I150: Saldos Periódicos - Identificação do Período
 * @author admin
 */
@Data
@Builder
public class I150 {
    
    String periodoInicial;
    String periodoFinal;    
    List<I155> registros = new ArrayList<I155>();
}
