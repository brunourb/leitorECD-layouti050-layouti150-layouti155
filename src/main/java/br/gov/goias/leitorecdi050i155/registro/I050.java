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

    List<I150I155> lancamentos = new ArrayList<I150I155>();
    
    public boolean hasSubConta(){
        return !"".equals(this.getCodigoContaSintetica());
    }
    
    public boolean isChild(String codigoContaAnaliticaParent){
        return this.getCodigoContaSintetica().equals(codigoContaAnaliticaParent);
    }

    public String recursiveWalkSubContas(String tab){
        StringBuilder sb = new StringBuilder();
        if (this.getSubContas()!=null && this.getSubContas().size() > 0) {
            this.getSubContas().forEach(i1 -> sb.append(String.format("\n\t%s %s %s %s %s %s", tab, i1.getCodigoContaAnalitica(), i1.getCodigoContaSintetica(), i1.getNomeConta(), i1.recursiveWalkSubContas("\t\t"), i1.recursiveWalkLancamentos("\t\t\t"))));
        }
        return sb.toString();
    }

    public String recursiveWalkLancamentos(String tab){
        StringBuilder sb = new StringBuilder();
        if (this.getLancamentos()!=null && this.getLancamentos().size() > 0) {
            this.getLancamentos().forEach(i1 -> sb.append(i1.recursiveWalk(tab)));
        }

        return sb.toString();
    }

}
