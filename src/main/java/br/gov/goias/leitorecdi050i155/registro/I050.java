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
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * Plano de Contas
 * @author admin
 */
@Data
@Builder
@ToString
@EqualsAndHashCode
public class I050 {
   
    /**
     * Data da inclusão/alteração.
     */
    String dataInclusao;
    
    /**
     * Código da natureza da conta/grupo de contas, conforme tabela interna ao SPED.
     */
    String codigoNaturezaConta;
    
    /**
     * Indicador do tipo de conta: S - Sintética (grupo de contas); A - Analítica (conta).
     */
    String indTipoConta;
    
    /**
     * Nível da conta analítica/grupo de contas.
     */
    String nivelConta;
    
    /**
     * Código da conta analítica/grupo de contas.
     */
    String codigoContaAnalitica;
    
    /**
     * Código da conta sintética /grupo de contas de nível imediatamente superior.
     */
    String codigoContaSintetica;
    
    /**
     * Nome da conta analítica/grupo de contas.
     */
    String nomeConta;
   
    /**
     * 
     */
    List<I050> subContas = new ArrayList<I050>();
    
    public boolean hasSubConta(){
        return !"".equals(this.codigoContaSintetica);
    }
    
    public boolean isChild(String codigo){
        return this.codigoContaSintetica.equals(codigo);
    }
    
}
