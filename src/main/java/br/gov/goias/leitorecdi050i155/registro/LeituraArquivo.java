/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.goias.leitorecdi050i155.registro;

import br.gov.goias.leitorecdi050i155.FXMLController;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author admin
 */
public class LeituraArquivo {


    public static void main(String[] args){

        File myDir = new File("D:\\ecd");

        List<ECD000> empresas = new ArrayList<ECD000>();

        long dirSizeB = FileUtils.sizeOfDirectory(myDir);
        System.out.printf("The size of directory is: %.2f megabytes\n", (double) dirSizeB / FileUtils.ONE_MB);

        final String[] RESTRICAO_ARQUIVOS = {"txt"};
        example(myDir, RESTRICAO_ARQUIVOS);

    }

    static String[] formatData(String s){
        return s.split("\\|");
    }

    static boolean filterData(String[] s, String value){
        return value.equals(s[5]);
    }

    static I050 extract(String[] i, List<I050> lista){
        List<I050> subContas = lista.stream().filter(n->n.isChild(i[6])).collect(Collectors.toList());
            return I050.builder()
                    .dataInclusao(i[2])
                    .codigoNaturezaConta(i[3])
                    .indTipoConta(i[4])
                    .nivelConta(i[5])
                    .codigoContaAnalitica(i[6])
                    .codigoContaSintetica(i[7])
                    .nomeConta(i[8])
                    .subContas(subContas).build();
    }

    public static void example(File myDir, String[] RESTRICAO_ARQUIVOS){
        Collection<File> files = FileUtils.listFiles(myDir, RESTRICAO_ARQUIVOS, true);
        Stream<File> stream = files.parallelStream();
        stream.forEachOrdered(file-> {
            try {
                List<String> lines = FileUtils.readLines(file, StandardCharsets.ISO_8859_1.name());

                String[] data = lines.get(0).split("\\|");
                ECD000 empresa = ECD000.builder()
                        .dataInicial(data[3])
                        .dataFinal(data[4])
                        .nomeEmpresa(data[5])
                        .cnpj(data[6])
                        .uf(data[7])
                        .ie(data[8])
                        .codigoMunicipioIBGE(data[9])
                        .im(data[10]).build();


                List<I050> nivel4 = lines.stream().filter(l-> l.contains("I050"))
                        .map(s -> formatData(s)).filter(s1-> filterData(s1,"4"))
                        .map(i-> I050.builder()
                                .dataInclusao(i[2])
                                .codigoNaturezaConta(i[3])
                                .indTipoConta(i[4])
                                .nivelConta(i[5])
                                .codigoContaAnalitica(i[6])
                                .codigoContaSintetica(i[7])
                                .nomeConta(i[8])
                                .build())
                        .collect(Collectors.toList());


                List<I050> nivel3 = lines.stream().filter(l-> l.contains("I050"))
                        .map(s -> formatData(s)).filter(s1-> filterData(s1,"3"))
                        .map(i-> extract(i,nivel4)).collect(Collectors.toList());

                List<I050> nivel2 = lines.stream().filter(l-> l.contains("I050"))
                        .map(s -> formatData(s)).filter(s1-> filterData(s1,"2"))
                        .map(i-> extract(i, nivel3)).collect(Collectors.toList());


                List<I050> nivel1 = lines.stream().filter(l-> l.contains("I050"))
                        .map(s ->formatData(s)).filter(s1-> filterData(s1,"1"))
                        .map(i-> extract(i,nivel2)).collect(Collectors.toList());

                nivel1.forEach(i-> {
                            System.out.printf("%s %s %s %s %s \n",
                                    i.getNivelConta(),
                                    i.getCodigoContaAnalitica(),
                                    i.getCodigoContaSintetica(),
                                    i.getNomeConta(),
                                    i.recursiveWalk("\t"));
                });

            } catch (IOException ex) {
                Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }


    public static void loadFiles(File myDir, String[] RESTRICAO_ARQUIVOS){

        Collection<File> files = FileUtils.listFiles(myDir, RESTRICAO_ARQUIVOS, true);
        Stream<File> stream = files.parallelStream();
        stream.forEachOrdered(new Consumer<File>() {

            public void accept(File file) {
                try {

                    List<String> lines = FileUtils.readLines(file, StandardCharsets.ISO_8859_1.name());
                    String[] data = lines.get(0).split("\\|");
                    ECD000 empresa = ECD000.builder()
                            .dataInicial(data[3])
                            .dataFinal(data[4])
                            .nomeEmpresa(data[5])
                            .cnpj(data[6])
                            .uf(data[7])
                            .ie(data[8])
                            .codigoMunicipioIBGE(data[9])
                            .im(data[10]).build();


                    List<I050> nivel1 = lines.stream().filter(l-> l.contains("I050"))
                            .map(s -> s.split("\\|")).filter(s1->"1".equals(s1[5]))
                            .map(i-> I050.builder()
                                    .dataInclusao(i[2])
                                    .codigoNaturezaConta(i[3])
                                    .indTipoConta(i[4])
                                    .nivelConta(i[5])
                                    .codigoContaAnalitica(i[6])
                                    .codigoContaSintetica(i[7])
                                    .nomeConta(i[8])
                                    .subContas(new ArrayList<I050>())
                                    .build())
                            .collect(Collectors.toList());

                    List<I050> nivel2 = lines.stream().filter(l-> l.contains("I050"))
                            .map(s -> s.split("\\|")).filter(s1->"2".equals(s1[5]))
                            .map(i-> I050.builder()
                                    .dataInclusao(i[2])
                                    .codigoNaturezaConta(i[3])
                                    .indTipoConta(i[4])
                                    .nivelConta(i[5])
                                    .codigoContaAnalitica(i[6])
                                    .codigoContaSintetica(i[7])
                                    .nomeConta(i[8])
                                    .subContas(new ArrayList<I050>())
                                    .build())
                            .collect(Collectors.toList());

                    List<I050> nivel3 = lines.stream().filter(l-> l.contains("I050"))
                            .map(s -> s.split("\\|")).filter(s1->"3".equals(s1[5]))
                            .map(i-> I050.builder()
                                    .dataInclusao(i[2])
                                    .codigoNaturezaConta(i[3])
                                    .indTipoConta(i[4])
                                    .nivelConta(i[5])
                                    .codigoContaAnalitica(i[6])
                                    .codigoContaSintetica(i[7])
                                    .nomeConta(i[8])
                                    .subContas(new ArrayList<I050>())
                                    .build())
                            .collect(Collectors.toList());

                    List<I050> nivel4 = lines.stream().filter(l-> l.contains("I050"))
                            .map(s -> s.split("\\|")).filter(s1->"4".equals(s1[5]))
                            .map(i-> I050.builder()
                                    .dataInclusao(i[2])
                                    .codigoNaturezaConta(i[3])
                                    .indTipoConta(i[4])
                                    .nivelConta(i[5])
                                    .codigoContaAnalitica(i[6])
                                    .codigoContaSintetica(i[7])
                                    .nomeConta(i[8])
                                    .subContas(new ArrayList<I050>())
                                    .build())
                            .collect(Collectors.toList());

//                 nivel3.forEach(i->{
//                     System.out.printf("%s %s %s %s\n",i.getCodigoContaAnalitica(), i.getCodigoContaSintetica(),i.getNomeConta(), i.getSubContas());
//                });
                    List<List<I050>> x = nivel3.stream().map(st3 ->
                            nivel4.stream()
                                    .filter(n4->n4.getCodigoContaSintetica().equals(st3.getCodigoContaAnalitica()))
                                    .collect(Collectors.toList())).filter(x1 -> x1.size()>0)
                            .collect(Collectors.toList());


                    List<List<I050>> y = nivel2.stream().map(st2 ->
                            nivel3.stream()
                                    .filter(n3->n3.getCodigoContaSintetica().equals(st2.getCodigoContaAnalitica()))
                                    .collect(Collectors.toList())).filter(x1 -> x1.size()>0)
                            .collect(Collectors.toList());


                    List<List<I050>> z = nivel1.stream().map(st1 ->
                            nivel2.stream()
                                    .filter(n2->n2.getCodigoContaSintetica().equals(st1.getCodigoContaAnalitica()))
                                    .collect(Collectors.toList())).filter(x1 -> x1.size()>0)
                            .collect(Collectors.toList());





                    /*.collect(Collectors.toList());*/
//                 List<I050> i050 = lines.stream().filter(l-> l.contains("I050"))
//                         .map(s -> s.split("\\|"))
//                         .map(i-> I050.builder()
//                                .dataInclusao(i[2])
//                                .codigoNaturezaConta(i[3])
//                                .indTipoConta(i[4])
//                                .nivelConta(i[5])
//                                .codigoContaAnalitica(i[6])
//                                .codigoContaSintetica(i[7])
//                                .nomeConta(i[8])
//                                .build())
//                        .collect(Collectors.toList());
//                 i050.forEach(i->{
//                     System.out.printf("%s %s %s\n",i.getCodigoContaAnalitica(), i.getCodigoContaSintetica(),i.getNomeConta());
//                });

                    return;

                } catch (IOException ex) {
                    Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

//            try {
//                List<String> lines = FileUtils.readLines(file, StandardCharsets.ISO_8859_1.name());
//                String[] data = lines.get(0).split("\\|");
//                ECD000 empresa = ECD000.builder()
//                        .dataInicial(data[3])
//                        .dataFinal(data[4])
//                        .nomeEmpresa(data[5])
//                        .cnpj(data[6])
//                        .uf(data[7])
//                        .ie(data[8])
//                        .codigoMunicipioIBGE(data[9])
//                        .im(data[10]).build();
//
//                List<I050> i050 = lines.stream().filter(l-> l.contains("I050"))
//                        .map(s -> s.split("\\|"))
//                        .map(i->
//                                I050.builder()
//                                .dataInclusao(i[2])
//                                .codigoNaturezaConta(i[3])
//                                .indTipoConta(i[4])
//                                .nivelConta(i[5])
//                                .codigoContaAnalitica(i[6])
//                                .codigoContaSintetica(i[7])
//                                .nomeConta(i[8])
//                                .build())
//                        .collect(Collectors.toList());
//               
//                i050.forEach(i->{
//                    System.out.printf("%s %s %s\n",i.getCodigoContaAnalitica(), i.getCodigoContaSintetica(),i.getNomeConta());
//                });
//                

//                Stream<String[]> i050 = lines.stream().filter(l-> l.contains("I050"))
//                        .map(s -> s.split("\\|"));
//                i050.forEach(s->System.out.println(s[1]));
        //.map(a -> I050.builder().dataInclusao(a[0]));
        //.collect(Collectors.toList());

//                List<String> i155 = lines.stream().filter(l-> l.contains("I155")).collect(Collectors.toList());
//                
//                System.out.printf("There are %d lines in the file\n", lines.size());
//                
//                double dirSizeKB = file.length() / FileUtils.ONE_KB;
//                double dirSizeMB = file.length() / FileUtils.ONE_MB;
//
//                System.out.printf("The size of file is: %.2f kilobytes\n", dirSizeKB);
//                System.out.printf("The size of file is: %.2f megabytes\n", dirSizeMB);  

//            } catch (IOException ex) {
//                Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        });

        //Here creating a parallel stream
//        Stream<Integer> stream = list.parallelStream();
//        Integer[] evenNumbersArr = stream.filter(i -> i % 2 == 0).toArray(Integer[]::new);
//        System.out.print(evenNumbersArr);

//        long fileSizeB = FileUtils.sizeOf(file);
//        System.out.printf("The size of file is: %d bytes\n", fileSizeB);
//        
//        myDir = new File("D:\\ecd");
//        
//        long dirSizeB = FileUtils.sizeOfDirectory(mydir);
//        double dirSizeKB = (double) dirSizeB / FileUtils.ONE_KB;
//        double dirSizeMB = (double) dirSizeB / FileUtils.ONE_MB;
//        
//        System.out.printf("The size of directory is: %d bytes\n", dirSizeB);
//        System.out.printf("The size of file is: %.2f kilobytes\n", dirSizeKB);
//        System.out.printf("The size of file is: %.2f megabytes\n", dirSizeMB);  
//                
//        });    
    }
}
