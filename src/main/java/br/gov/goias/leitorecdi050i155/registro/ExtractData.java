package br.gov.goias.leitorecdi050i155.registro;

import br.gov.goias.leitorecdi050i155.FXMLController;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExtractData {

    static final Logger logger = LogManager.getLogger(ExtractData.class);
    static final String FILTER_I050 = "I050";
    static final String FILTER_I150 = "I150";
    static final String FILTER_I155 = "I155";

    public static Set<ECD000> extractFileData(File diretorio, String[] RESTRICAO_ARQUIVOS) {

        Set<ECD000> empresas = new HashSet<>();

        try{

            File[] f1 = diretorio.listFiles();
            logger.debug(String.format("Arquivos carregados: %d",f1.length));
            final int[] index = {0};
            Arrays.stream(f1).forEach(f -> {
                List<String> lines = new ArrayList<String>();
                Collection<File> files = FileUtils.listFiles(f, RESTRICAO_ARQUIVOS, true);
                Stream<File> stream = files.parallelStream();
                extractLines(lines, stream);
                ECD000 empresa = extractECD000(formatData(lines.get(0)));
                Set<I050> nivel4 = new HashSet<>();
                nivel4 = ExtractData.extractI150(lines,"4",null);
//                empresa.getI050s().addAll(extractI050(lines));
//                empresa.getI100s().addAll(extractJ100(lines));
//                empresa.getI150s().addAll(extractI150(lines));

                empresas.add(empresa);
            });
        }catch (Exception e){
            logger.error(String.format("Erro ao extrair arquivo: %s",e.getMessage()));
        }
        return empresas;
    }

    private static void extractLines(List<String> lines, Stream<File> stream) {
        stream.forEachOrdered(file -> {
            try {
                lines.addAll(FileUtils.readLines(file, StandardCharsets.ISO_8859_1.name()));
            } catch (IOException ex) {
                logger.error(FXMLController.class.getName(),ex.getMessage());
            }
        });
    }

    static ECD000 extractECD000(String[] data) {
        try {
            return ECD000.builder()
                    .dataInicial(data[3])
                    .dataFinal(data[4])
                    .nomeEmpresa(data[5])
                    .cnpj(data[6])
                    .uf(data[7])
                    .ie(data[8])
                    .codigoMunicipioIBGE(data[9])
                    .i050s(new HashSet<>())
                    .i100s(new HashSet<>())
                    .i150s(new HashSet<>())
                    .im(data[10]).build();

        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }

    static Set<I050> extractI150(List<String> lines, Set<I050> subLista) {
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
                    .map(i -> extractI150(i, subLista))
                    .collect(Collectors.toCollection(() -> new TreeSet<I050>(comparator)));
        }
    }

    static I050 extractI150(String[] i, Set<I050> lista) {
        List<I050> subContas = lista.stream().filter(n -> n.isChild(i[6])).collect(Collectors.toList());
        return I050.builder()
                .dataInclusao(i[2])
                .codigoNaturezaConta(i[3])
                .indTipoConta(i[4])
                .nivelConta(i[5])
                .codigoContaAnalitica(i[6])
                .codigoContaSintetica(i[7])
                .nomeConta(i[8])
                .saldo(BigDecimal.ZERO)
                .subContas(subContas).build();
    }

    static I050 builderI050(String[] i, List<String> lines) {
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
                .saldo(BigDecimal.ZERO)
                .build();
    }

    static Set<I150I155> extractI150I155(String[] i, List<String> lines) {
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

    static BigDecimal convert2BigDecimal(String value) {
        return new BigDecimal(value.replace(",", "."));
    }

    static String[] formatData(String s) {
        return s.split("\\|");
    }

    static boolean filterDataI050(String[] s, String value) {
        return value.equals(s[5]);
    }
}
