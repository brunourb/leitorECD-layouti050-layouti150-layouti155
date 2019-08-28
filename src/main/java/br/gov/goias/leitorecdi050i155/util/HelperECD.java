package br.gov.goias.leitorecdi050i155.util;

import br.gov.goias.leitorecdi050i155.FXMLController;
import br.gov.goias.leitorecdi050i155.registro.ECD000;
import br.gov.goias.leitorecdi050i155.registro.I050;
import br.gov.goias.leitorecdi050i155.registro.I150I155;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HelperECD {

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

        File[] f1 = diretorio.listFiles();
        Set<ECD000> empresas = new HashSet<ECD000>();

        Arrays.stream(f1).forEach(f -> {
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


    static void createSheet(List<ECD000> empresas){

        // Criando o arquivo e uma planilha chamada "Product"
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Product");

        // Definindo alguns padroes de layout
        sheet.setDefaultColumnWidth(15);
        sheet.setDefaultRowHeight((short)400);

        int rownum = 0;
        int cellnum = 0;
        Cell cell;
        Row row;

        //Configurando estilos de células (Cores, alinhamento, formatação, etc..)
        HSSFDataFormat numberFormat = workbook.createDataFormat();

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        CellStyle textStyle = workbook.createCellStyle();
        textStyle.setAlignment(HorizontalAlignment.CENTER);
        textStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        CellStyle numberStyle = workbook.createCellStyle();
        numberStyle.setDataFormat(numberFormat.getFormat("#,##0.00"));
        numberStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // Configurando Header
        row = sheet.createRow(rownum++);
        cell = row.createCell(cellnum++);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Code");

        cell = row.createCell(cellnum++);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Name");

        cell = row.createCell(cellnum++);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Price");

    }
}
