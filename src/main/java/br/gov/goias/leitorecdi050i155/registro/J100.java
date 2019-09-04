package br.gov.goias.leitorecdi050i155.registro;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Registro J100: Balanço Patrimonial
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class J100 {

    public String codigoAglutinacao;

    public String nivelAglutinacao;

    public String indiGrupoBalanco;

    public String descricao;

    public BigDecimal valorTotal;

    public String indiSituacaoSaldo;

}
