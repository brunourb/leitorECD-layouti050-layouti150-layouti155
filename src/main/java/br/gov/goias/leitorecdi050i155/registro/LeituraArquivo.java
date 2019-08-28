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
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author admin
 */
public class LeituraArquivo {


    public static void main(String[] args) {

        File diretorio = new File("D:\\ecd");

        Set<ECD000> empresas = new HashSet<>();

        long dirSizeB = FileUtils.sizeOfDirectory(diretorio);
        System.out.printf("The size of directory is: %.2f megabytes\n", (double) dirSizeB / FileUtils.ONE_MB);

        final String[] RESTRICAO_ARQUIVOS = {"txt"};
        empresas = extractDataEmpresas(diretorio, RESTRICAO_ARQUIVOS);

        Stream<ECD000> e = empresas.parallelStream();
        e.forEachOrdered(e1 -> {
            System.out.printf("\n %s", e1.toString());
        });


//        System.out.println(empresas.toString());

        //                nivel1.forEach(i -> {
//                    System.out.printf("%s %s %s %s %s \n",
//                            i.getNivelConta(),
//                            i.getCodigoContaAnalitica(),
//                            i.getCodigoContaSintetica(),
//                            i.getNomeConta(),
//                            i.recursiveWalkSubContas("\t"),
//                            i.recursiveWalkLancamentos("\t\t")
//                    );
//                });

    }

    static String[] formatData(String s) {
        return s.split("\\|");
    }

    static boolean filterDataI050(String[] s, String value) {
        return value.equals(s[5]);
    }

    static BigDecimal convert2BigDecimal(String value) {
        return new BigDecimal(value.replace(",", "."));
    }

    static Set<I150I155> extractI150I155(String[] i, List<String> lines) {
        final String FILTER_I150 = "I150";
        final String FILTER_I155 = "I155";

        Comparator<I150I155> comparator = (c1, c2) -> c1.getCodigoConta().compareTo(c2.getCodigoConta());

        Object[] p1 = lines.stream()
                .filter(f -> f.contains(FILTER_I150))
                .map(m -> formatData(m)).toArray();

        String[] periodo = (String[]) p1[0];

        Set<I150I155> registroI150 = lines.stream()
                .filter(f -> f.contains(FILTER_I155)).filter(f1 -> f1.contains(i[6])) //CODIGO ANALITICO
                .map(m -> formatData(m))
                .map(m1 -> I150I155.builder()
                        .periodoInicial(String.valueOf(periodo[2]))
                        .periodoFinal(String.valueOf(periodo[3]))
                        .codigoConta(m1[2])
                        .codigoCentroCusto(m1[3])
                        .valorSaldoFinal(convert2BigDecimal(m1[4]))
                        .indSituacaoSaldoInicial(m1[5])
                        .valorTotalDebito(convert2BigDecimal(m1[6]))
                        .valorTotalCredito(convert2BigDecimal(m1[7]))
                        .valorSaldoFinal(convert2BigDecimal(m1[8]))
                        .indSituacaoSaldoFinal(m1[9])
                        .build())
                .collect(Collectors.toCollection(() -> new TreeSet<I150I155>(comparator)));

        return registroI150;
    }

    static I050 builderI050(String[] i, List<String> lines) {
        final String FILTER_I150 = "I150";
        final String FILTER_I155 = "I155";

        Set<I150I155> registroI150I155 = extractI150I155(i, lines);

        return I050.builder()
                .dataInclusao(i[2])
                .codigoNaturezaConta(i[3])
                .indTipoConta(i[4])
                .nivelConta(i[5])
                .codigoContaAnalitica(i[6])
                .codigoContaSintetica(i[7])
                .nomeConta(i[8])
                .lancamentos(registroI150I155)
                .build();
    }

    static I050 extract(String[] i, Set<I050> lista) {
        List<I050> subContas = lista.stream().filter(n -> n.isChild(i[6])).collect(Collectors.toList());
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

    static ECD000 extract(String[] data) {
        return ECD000.builder()
                .dataInicial(data[3])
                .dataFinal(data[4])
                .nomeEmpresa(data[5])
                .cnpj(data[6])
                .uf(data[7])
                .ie(data[8])
                .codigoMunicipioIBGE(data[9])
                .registros(new LinkedList<>())
                .im(data[10]).build();
    }

    static Set<I150I155> extract(List<String> lines) {
        final String FILTER_I150 = "I150";
        final String FILTER_I155 = "I155";

        Comparator<I150I155> comparator = (c1, c2) -> c1.getCodigoConta().compareTo(c2.getCodigoConta());

        return lines.stream().filter(l -> l.contains(FILTER_I150))
                .map(s -> formatData(s))
                .map(i -> builderI150(i))
                .collect(Collectors.toCollection(() -> new TreeSet<I150I155>(comparator)));
    }

    static I150I155 builderI150(String[] i) {
        return I150I155.builder()
                .periodoInicial(i[2])
                .periodoFinal(i[3]).build();
    }

    static List<I150I155> extractI150155(I050 i, List<String> lines) {

        return new ArrayList<>();
    }

    static Set<I050> extract(List<String> lines, String nivelConta, Set<I050> subLista) {
        final String FILTER_I050 = "I050";
//        LI050 registro = new I050();

        Comparator<I050> comparator = (c1, c2) -> c1.getCodigoContaAnalitica().compareTo(c2.getCodigoContaAnalitica());

        if (subLista == null) {
            return lines.stream().filter(l -> l.contains(FILTER_I050))
                    .map(s -> formatData(s)).filter(s1 -> filterDataI050(s1, nivelConta))
                    .map(i -> builderI050(i, lines))
                    .collect(Collectors.toCollection(() -> new TreeSet<I050>(comparator)));
        } else {
            return lines.stream().filter(l -> l.contains(FILTER_I050))
                    .map(s -> formatData(s)).filter(s1 -> filterDataI050(s1, nivelConta))
                    .map(i -> extract(i, subLista))
                    .collect(Collectors.toCollection(() -> new TreeSet<I050>(comparator)));
        }


    }

    public static Set<ECD000> extractDataEmpresas(File diretorio, String[] RESTRICAO_ARQUIVOS) {

        File[] files1 = diretorio.listFiles();
        Set<ECD000> empresas = new HashSet<ECD000>();

        Arrays.stream(files1).forEach(f -> {
            List<String> lines = new ArrayList<String>();
            Set<I050> nivel4 = new HashSet<>();
            Set<I050> nivel3 = new HashSet<>();
            Set<I050> nivel2 = new HashSet<>();
            Set<I050> nivel1 = new HashSet<>();


            Collection<File> files = FileUtils.listFiles(f, RESTRICAO_ARQUIVOS, true);
            Stream<File> stream = files.parallelStream();

            stream.forEachOrdered(file -> {
                try {

                    lines.addAll(FileUtils.readLines(file, StandardCharsets.ISO_8859_1.name()));

                } catch (IOException ex) {
                    Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            String[] data = lines.get(0).split("\\|");
            ECD000 empresa = extract(data);

            nivel4 = extract(lines, "4", null);

            nivel3 = extract(lines, "3", nivel4);

            nivel2 = extract(lines, "2", nivel3);

            nivel1 = extract(lines, "1", nivel2);

            empresa.getRegistros().addAll(nivel1);

            empresas.add(empresa);
        });

        return empresas;
    }
}


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
//    }
//}
