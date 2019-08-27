/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.goias.leitorecdi050i155.registro;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 *
 * REGISTRO I150: Saldos Periódicos - Identificação do Período
 * @author admin
 */
@Data
@Builder
@ToString
public class I150I155 {
    
    String periodoInicial;
    String periodoFinal;

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

    public String recursiveWalk(String tab){
        return String.format("\n\t\t%s %s %s %s %s %.2f %s %.2f %.2f %.2f %s",
                tab, periodoInicial, periodoFinal, codigoConta, codigoCentroCusto,
                valorSaldoInicial, indSituacaoSaldoInicial, valorTotalDebito,
                valorTotalCredito, valorSaldoFinal, indSituacaoSaldoFinal);
    }
}
