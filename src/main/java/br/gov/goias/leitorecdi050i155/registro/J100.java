package br.gov.goias.leitorecdi050i155.registro;

import lombok.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

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

    public boolean isPresent(Integer quantidade){
        return codigoAglutinacao.length() == 2;
    }

    /**
     * nivel 1 - 2 contas
     * nivel 2 - 3 contas
     * nivel 3 - 4 contas
     * nivel 4 - 5 contas
     *
     * @param nivel
     * @return
     */
    public boolean limitNivel(String nivel) {
        switch (Integer.valueOf(nivel)){
            case 1:
                return nivelAglutinacao.equals(nivel) && codigoAglutinacao.length() == 1;
            case 2:
                return nivelAglutinacao.equals(nivel) && codigoAglutinacao.length() == 2;
            case 3:
                return nivelAglutinacao.equals(nivel) && codigoAglutinacao.length() == 3;
            case 4:
                return nivelAglutinacao.equals(nivel) && codigoAglutinacao.length() == 4;
            case 5:
                return nivelAglutinacao.equals(nivel) && codigoAglutinacao.length() == 5;
            default:
                return false;
        }
    }
}
