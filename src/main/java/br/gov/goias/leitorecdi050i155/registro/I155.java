/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.goias.leitorecdi050i155.registro;

import lombok.Data;

import java.math.BigDecimal;

/**
 *
 * @author admin
 */
@Data
public class I155 {
    
    /**
     * Código da conta analítica.
     */
    
    String codigoConta;
    /**
     * Código do centro de custos.
     */
    String codigoCentroCusto;
    
    /**
     * Valor do saldo inicial do período
     */
    BigDecimal valorSaldoInicial;
    
    /**
     * Indicador da situação do saldo inicial: 
     * D - Devedor; 
     * C - Credor.
     */
    String indSituacaoSaldoInicial;
    
    /**
     * Valor total dos débitos no período.
     */
    BigDecimal valorTotalDebito;
    
    /**
     * Valor total dos créditos no período.
     */
    BigDecimal valorTotalCredito;
    
    /**
     * Valor do saldo final do período.
     */
    BigDecimal valorSaldoFinal;
    
    /**
     * Indicador da situação do saldo final: 
     * D - Devedor; 
     * C - Credor.
     */
    String indSituacaoSaldoFinal;
    
    
}
