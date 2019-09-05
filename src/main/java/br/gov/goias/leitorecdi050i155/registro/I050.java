/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.goias.leitorecdi050i155.registro;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import lombok.*;

/**
 *
 * Plano de Contas
 * @author admin
 */
@Data
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class I050 {

    final String DEBIDO ="D";
    final String CREDITO ="C";

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

    Set<I150I155> lancamentos = new HashSet<>();

    BigDecimal saldo = BigDecimal.ZERO;

    public boolean hasSubConta(){
        return !"".equals(this.getCodigoContaSintetica());
    }

    public boolean isChild(String codigoContaAnaliticaParent){
        return this.getCodigoContaSintetica().equals(codigoContaAnaliticaParent);
    }

    public boolean hasSubcontas(){
        return this.getSubContas()!=null && this.getSubContas().size()>0;
    }

    public void recursiveSubContasValorAcumulado(){

//        Map<String, Long> counting = this.getSubContas().stream().collect(Collectors.groupingBy(I050::getNomeConta, Collectors.counting()));

        if (hasSubcontas()) {
            this.getSubContas().stream().forEach(i1 ->
            {
                i1.recursiveSubContasValorAcumulado();
                if(i1.getLancamentos()!=null && i1.getLancamentos().size()>0){
                    BigDecimal saldo = i1.getLancamentos().stream().map(i150I155 -> i150I155.getValorSaldoFinal()).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
                    System.out.printf("\n%s %.2f",i1.getNomeConta(),saldo.doubleValue());
                }
            });
        }


//        Map<String, Integer> sum = this.getSubContas().stream().collect(Collectors.groupingBy(I050::getNomeConta, Collectors.summingDouble(sumLancamentos);

//        Map<String, Integer> sum = this.getSubContas().stream().collect(Collectors.groupingBy(I150I155::getCodigoConta, Collectors.summingInt(I150I155::getValorSaldoFinal)));
//
//        //group by price
//        Map<BigDecimal, List<I150I155>> groupByPriceMap = (Map<BigDecimal, List<I150I155>>) this.getSubContas().stream().collect(Collectors.groupingBy(I150I155::getValorSaldoFinal));
//
       // System.out.println(sumLancamentos);
//
//        // group by price, uses 'mapping' to convert List<Item> to Set<String>
//        Map<BigDecimal, Set<String>> result = (Map<BigDecimal, Set<String>>) this.getSubContas().stream().collect(Collectors.groupingBy(I150I155::getValorSaldoFinal, Collectors.mapping(I150I155::getCodigoConta, Collectors.toSet())));
//
       // System.out.println(counting);
    }

    public BigDecimal recursiveWalkSubContasValues(){
        BigDecimal saldo = new BigDecimal(0);
        if (hasSubcontas()) {
            this.getSubContas().stream().forEach(i1 ->
            {
                i1.recursiveWalkSubContasValues();
                saldo.add(i1.recursiveWalkLancamentosValues());
            });
        }

        return saldo;
    }

    public String recursiveWalkSubContas(String tab){
        StringBuilder sb = new StringBuilder();
        if (hasSubcontas()) {
            this.getSubContas().stream().forEach(i1 ->
            {
                i1.recursiveWalkSubContas("\t\t");
               i1.recursiveWalkLancamentos("\t\t\t");
//                sb.append(String.format("\n\t%s %s %s",
//                            tab,contas,lancamentos));
////                            i1.getCodigoContaAnalitica(),
////                            i1.getCodigoContaSintetica(),
////                            i1.getNivelConta(),
////                            i1.getNomeConta(),
////                            if(),
////                            ));
        });

            return sb.toString();
        }

        return null;
    }

    public boolean hasLancamentos(){
        return this.getLancamentos()!=null && this.getLancamentos().size() > 0;
    }

    public BigDecimal recursiveWalkLancamentosValues(){
        if (hasLancamentos()) {
            BigDecimal saldo = this.getLancamentos()
                    .stream()
                    .map(i150I155 ->
                            convertCreditoDebito(i150I155.getValorSaldoFinal(),i150I155.getIndSituacaoSaldoFinal()).multiply(i150I155.getValorSaldoFinal()))
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);

            if(saldo.doubleValue()>0){
                System.out.printf("\nCódigo Natureza: %s %s %.2f",this.getCodigoNaturezaConta(), this.getNomeConta(),saldo.doubleValue());
            }
            return saldo;
        }
        return BigDecimal.ZERO;
    }

    public String recursiveWalkLancamentos(String tab){
        StringBuilder sb = new StringBuilder();
        if (this.getLancamentos()!=null && this.getLancamentos().size() > 0) {

            BigDecimal saldo = this.getLancamentos().stream().map(i150I155 -> convertCreditoDebito(i150I155.getValorSaldoFinal(),i150I155.getIndSituacaoSaldoFinal()).multiply(i150I155.getValorSaldoFinal())).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
            System.out.printf("\nCódigo Natureza: %s %s %.2f",this.getCodigoNaturezaConta(), this.getNomeConta(),saldo.doubleValue());

            this.getLancamentos().forEach(i1 -> sb.append(i1.recursiveWalk(tab)));
//            System.out.printf("\n%s ",sb.toString());
            return sb.toString();
        }
        return null;
    }

    public BigDecimal convertCreditoDebito(BigDecimal valor, String indicadorConta){

        GrupoContas grupo = GrupoContas.fromString(this.getCodigoNaturezaConta());

        BigDecimal saldo = new BigDecimal(1);

        switch (grupo){
            case ATIVO:
            case CONTAS_RESULTADO:
                saldo = indicadorConta.equals(DEBIDO) ? saldo : saldo.negate();
                break;
            case PASSIVO:
            case PATRIMONIO_LIQUIDO:
                saldo = indicadorConta.equals(DEBIDO) ? saldo.negate() : saldo;
                break;
            case CONTAS_COMPENSACAO:
                break;
            case OUTRAS:
                break;
        }
        return saldo;
    }

    public boolean isNotNull(String s){
        return s!=null;
    }

}
