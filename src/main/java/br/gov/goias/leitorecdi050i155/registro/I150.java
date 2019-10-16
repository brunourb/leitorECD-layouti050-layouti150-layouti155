package br.gov.goias.leitorecdi050i155.registro;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@ToString
public class I150 {

    private String dataInicio;

    private String dataFinal;

    private Set<I155> i155s = new HashSet<>();
}
