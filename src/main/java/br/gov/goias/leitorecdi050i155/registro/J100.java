package br.gov.goias.leitorecdi050i155.registro;

import lombok.*;

import java.math.BigDecimal;

/**
 * Registro J100: Balanço Patrimonial
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class J100 {

    public String codigoAglutinacao;

    public String nivelAglutinacao;

    public String indiGrupoBalanco;

    public String descricao;

    public BigDecimal valorTotal;

    public String indiSituacaoSaldo;

}
