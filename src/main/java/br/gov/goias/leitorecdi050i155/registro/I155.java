/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.goias.leitorecdi050i155.registro;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 *
 * @author admin
 */
@Data
@Builder
public class I155 {

    /**
     * Código da conta analítica/grupo de contas.
     */
    String codigoContaAnalitica;

    String codigoCentroCusto;

    BigDecimal saldoInicial;

    String indSituacaoSaldoInicial;

    BigDecimal valorTotalDebito;

    BigDecimal valorTotalCredito;

    BigDecimal saldoFinal;

    String indSituacaoSaldoFinal;

}
