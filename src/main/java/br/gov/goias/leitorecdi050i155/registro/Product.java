package br.gov.goias.leitorecdi050i155.registro;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Product {

    private Long id;

    private String name;

    private Double price;
}
