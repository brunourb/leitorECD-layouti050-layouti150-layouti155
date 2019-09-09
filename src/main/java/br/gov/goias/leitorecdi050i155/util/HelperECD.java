package br.gov.goias.leitorecdi050i155.util;

import br.gov.goias.leitorecdi050i155.FXMLController;
import br.gov.goias.leitorecdi050i155.registro.ECD000;
import br.gov.goias.leitorecdi050i155.registro.I050;
import br.gov.goias.leitorecdi050i155.registro.I150I155;
import br.gov.goias.leitorecdi050i155.registro.J100;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HelperECD {

    public static HSSFWorkbook extract2Excel(Set<ECD000> empresas) {

        // Criando o arquivo e uma planilha chamada "Product"
        HSSFWorkbook workbook = new HSSFWorkbook();

        CellStyle style = HelperExcel.headerStyle(workbook);

        Stream<ECD000> e = empresas.parallelStream();
        e.forEachOrdered(e1 -> {
            HSSFSheet sheet = workbook.createSheet(e1.getNomeEmpresa().replaceAll("[^A-Za-z0-9 ]", "")); //nome da planilha (CNPJ)
            // Definindo alguns padroes de layout
            sheet.setDefaultColumnWidth(15);
            sheet.setDefaultRowHeight((short) 400);

            int rownum = 0;
            int cellnum = 0;
            Cell cell;
            Row row;

            // Configurando Header
            row = sheet.createRow(rownum++);
            cell = row.createCell(cellnum);
            cell.setCellStyle(style);
            cell.setCellValue("Empresa");

            cell = row.createCell(1);
            cell.setCellStyle(style);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 5));
            cell.setCellValue(e1.getNomeEmpresa());

            row = sheet.createRow(rownum++);
            cell = row.createCell(cellnum);
            cell.setCellStyle(style);
            cell.setCellValue("CNPJ");

            cell = row.createCell(1);
            cell.setCellStyle(style);
            cell.setCellValue(e1.getCnpj());

            row = sheet.createRow(rownum++);
            cell = row.createCell(cellnum);
            cell.setCellStyle(style);
            cell.setCellValue("IE");

            cell = row.createCell(1);
            cell.setCellStyle(style);
            cell.setCellValue(e1.getIe());

            row = sheet.createRow(rownum++);
            cell = row.createCell(cellnum);
            cell.setCellStyle(style);
            cell.setCellValue("IM");

            cell = row.createCell(1);
            cell.setCellStyle(style);
            cell.setCellValue(e1.getIm());

            row = sheet.createRow(rownum++);
            cell = row.createCell(cellnum);
            cell.setCellStyle(style);
            cell.setCellValue("UF");

            cell = row.createCell(1);
            cell.setCellStyle(style);
            cell.setCellValue(e1.getUf());

//            System.out.println(e1.getCnpj());
//            System.out.println(e1.getIe());
//            System.out.println(e1.getNomeEmpresa());
//            System.out.println(e1.getCodigoMunicipioIBGE());
//            System.out.println(e1.getDataInicial());
//            System.out.println(e1.getDataFinal());
//            System.out.println(e1.getIm());
//            System.out.println(e1.getUf());

            int finalRownum = 6;

            for (I050 r : e1.getI050s()) {

                row = sheet.createRow(finalRownum);

                cell = row.createCell(cellnum);
                cell.setCellStyle(style);
                cell.setCellValue(r.getCodigoContaAnalitica());

                cell = row.createCell(1);
                cell.setCellStyle(style);
                cell.setCellValue(r.getCodigoContaSintetica());

                cell = row.createCell(2);
                cell.setCellStyle(style);
                cell.setCellValue(r.getNomeConta());

//                cell = row.createCell(cellnum);
//                cell.setCellStyle(HelperExcel.textStyle(workbook));
//                cell.setCellValue(r.getNomeConta());

                finalRownum++;

                recursiveWalkSubContas(workbook, sheet, row, finalRownum, r.getSubContas());


//                System.out.printf("%s %s %s %s %s \n",
//                        r.getNivelConta(),
//                        r.getCodigoContaAnalitica(),
//                        r.getCodigoContaSintetica(),
//                        r.getNomeConta(),
//                        r.recursiveWalkSubContas("\t"),
//                        r.recursiveWalkLancamentos("\t\t")
//                );
            }

            //        // Adicionando os dados dos produtos na planilha
//        for (Product product : products) {
//            row = sheet.createRow(rownum++);
//            cellnum = 0;
//
//            cell = row.createCell(cellnum++);
//            cell.setCellStyle(textStyle);
//            cell.setCellValue(product.getId());
//
//            cell = row.createCell(cellnum++);
//            cell.setCellStyle(textStyle);
//            cell.setCellValue(product.getName());
//
//            cell = row.createCell(cellnum++);
//            cell.setCellStyle(numberStyle);
//            cell.setCellValue(product.getPrice());
//        }


        });
        return workbook;
    }

    public static String recursiveWalkSubContas(HSSFWorkbook workbook, HSSFSheet sheet, Row row, int finalRownum, List<I050> registros){
        StringBuilder sb = new StringBuilder();
        int cellnum = 0;
//        CellStyle style = HelperExcel.headerStyle(workbook);
        if(registros!=null && registros.size()>0){
            for (I050 i050 : registros) {
                Cell cell;
                row = sheet.createRow(finalRownum);
                cell = row.createCell(0);
//                cell.setCellStyle(style);
                cell.setCellValue(i050.getCodigoContaAnalitica());

                cell = row.createCell(1);
//                cell.setCellStyle(style);
                cell.setCellValue(i050.getCodigoContaSintetica());

                cell = row.createCell(2);
//                cell.setCellStyle(style);
                cell.setCellValue(i050.getNomeConta());

                finalRownum++;
                recursiveWalkSubContas(workbook, sheet, row, finalRownum, registros);

//                cell = row.createCell(3);
//                cell.setCellStyle(HelperExcel.textStyle(workbook));
//                cell.setCellValue(i050.getNomeConta());
            }
        }

//        if (this.getSubContas()!=null && this.getSubContas().size() > 0) {
//            this.getSubContas().forEach(i1 ->
//                    sb.append(String.format("\n\t%s %s %s %s %s %s",
//                            tab,
//                            i1.getCodigoContaAnalitica(),
//                            i1.getCodigoContaSintetica(),
//                            i1.getNomeConta(),
//                            i1.recursiveWalkSubContas("\t\t"),
//                            i1.recursiveWalkLancamentos("\t\t\t"))));
//
//            return sb.toString();
//        }

        return null;
    }

//    public String recursiveWalkLancamentos(String tab){
//        StringBuilder sb = new StringBuilder();
//        if (this.getLancamentos()!=null && this.getLancamentos().size() > 0) {
//            this.getLancamentos().forEach(i1 -> sb.append(i1.recursiveWalk(tab)));
//            return sb.toString();
//        }
//        return null;
//    }

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
                .saldo(BigDecimal.ZERO)
                .build();
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

    static ECD000 extractI150(String[] data) {
        return ECD000.builder()
                .dataInicial(data[3])
                .dataFinal(data[4])
                .nomeEmpresa(data[5])
                .cnpj(data[6])
                .uf(data[7])
                .ie(data[8])
                .codigoMunicipioIBGE(data[9])
                .i050s(new LinkedList<>())
                .im(data[10]).build();
    }

    static Set<I150I155> extractI150(List<String> lines) {
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

    static Set<I050> extractI150(List<String> lines, String nivelConta, Set<I050> subLista) {
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

    public static HSSFWorkbook extractDataJ100Empresas(File diretorio, String[] RESTRICAO_ARQUIVOS) {

        HSSFWorkbook workbook = new HSSFWorkbook();

        File[] f1 = diretorio.listFiles();
        final int[] index = {0};
        Arrays.stream(f1).forEach(f -> {

            List<String> lines = new ArrayList<String>();
            Collection<File> files = FileUtils.listFiles(f, RESTRICAO_ARQUIVOS, true);
            Stream<File> stream = files.parallelStream();
            extractLines(lines, stream);
            String[] data = lines.get(0).split("\\|");
            ECD000 empresa = extractI150(data);
            Set<J100> j100 = extractJ100(lines);

            // Criando o arquivo e uma planilha chamada "Product"
            HSSFSheet sheet = workbook.createSheet(UUID.randomUUID().toString());
            // Definindo alguns padroes de layout
            sheet.setDefaultColumnWidth(10);
            sheet.setDefaultRowHeight((short)500);

            int rownum = 0;
            int cellnum = 0;
            Cell cell = null;
            Row row;

            // Configurando Header
            sheet.createFreezePane(0, 1); // this will freeze first row
            sheet.autoSizeColumn(rownum);

            row = sheet.createRow(rownum++);
            cell = row.createCell(cellnum++);
            cell.setCellStyle(HelperExcel.headerStyle(workbook));
            cell.setCellValue("codigoAglutinacao");

            cell = row.createCell(cellnum++);
            cell.setCellStyle(HelperExcel.headerStyle(workbook));
            cell.setCellValue("nivelAglutinacao");

            cell = row.createCell(cellnum++);
            cell.setCellStyle(HelperExcel.headerStyle(workbook));
            cell.setCellValue("indiGrupoBalanco");

            cell = row.createCell(cellnum++);
            cell.setCellStyle(HelperExcel.headerStyle(workbook));
            cell.setCellValue("descricao");

            cell = row.createCell(cellnum++);
            cell.setCellStyle(HelperExcel.headerStyle(workbook));
            cell.setCellValue("valorTotal");

            cell = row.createCell(cellnum++);
            cell.setCellStyle(HelperExcel.headerStyle(workbook));
            cell.setCellValue("indiSituacaoSaldo");

            int rownum1 = 1;
            Iterator<J100> itr = j100.iterator();
            int para = 0;
            while(itr.hasNext()){
                int cellnum1 = 0;
                J100 element = itr.next();

                sheet.autoSizeColumn(rownum1);
                row = sheet.createRow(rownum1++);
                cell = row.createCell(cellnum1++);
                cell.setCellStyle(HelperExcel.textStyle(workbook));
                cell.setCellValue(element.getCodigoAglutinacao());

                cell = row.createCell(cellnum1++);
                cell.setCellStyle(HelperExcel.textStyle(workbook));
                cell.setCellValue(element.getNivelAglutinacao());

                cell = row.createCell(cellnum1++);
                cell.setCellStyle(HelperExcel.textStyle(workbook));
                cell.setCellValue(element.getIndiGrupoBalanco());

                cell = row.createCell(cellnum1++);
                cell.setCellStyle(HelperExcel.textStyle(workbook));
                cell.setCellValue(element.getDescricao());

                cell = row.createCell(cellnum1++);
                cell.setCellStyle(HelperExcel.numberStyle(workbook));
                cell.setCellValue(element.getValorTotal().doubleValue());

                cell = row.createCell(cellnum1++);
                cell.setCellStyle(HelperExcel.textStyle(workbook));
                cell.setCellValue(element.getIndiSituacaoSaldo());
            }
        });
        return workbook;
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

            extractLines(lines, stream);

            String[] data = lines.get(0).split("\\|");
            ECD000 empresa = extractI150(data);

            nivel4 = extractI150(lines, "4", null);

            nivel3 = extractI150(lines, "3", nivel4);

            nivel2 = extractI150(lines, "2", nivel3);

            nivel1 = extractI150(lines, "1", nivel2);

            Set<J100> j100 = extractJ100(lines);

            empresa.getI050s().addAll(nivel1);

            empresas.add(empresa);
        });

        return empresas;
    }

    private static void extractLines(List<String> lines, Stream<File> stream) {
        stream.forEachOrdered(file -> {
            try {

                lines.addAll(FileUtils.readLines(file, StandardCharsets.ISO_8859_1.name()));

            } catch (IOException ex) {
                Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private static Set<J100> extractJ100(List<String> lines) {
        final String FILTER_J100 = "J100";
        Comparator<J100> comparator = (c1, c2) -> c1.getCodigoAglutinacao().compareTo(c2.getCodigoAglutinacao());

        return lines.stream().filter(l -> l.contains(FILTER_J100))
                .map(s -> formatData(s))
                .map(i -> builderJ100(i))
                .collect(Collectors.toCollection(() -> new TreeSet<J100>(comparator)));
    }

    private static J100 builderJ100(String[] i) {
        return J100.builder()
                .codigoAglutinacao(i[2])
                .nivelAglutinacao(i[3])
                .indiGrupoBalanco(i[4])
                .descricao(i[5])
                .valorTotal(new BigDecimal(i[6].replace(",",".")))
                .indiSituacaoSaldo(i[7])
                .build();
    }


    static void createSheet(List<ECD000> empresas) {

        // Criando o arquivo e uma planilha chamada "Product"
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Product");

        // Definindo alguns padroes de layout
        sheet.setDefaultColumnWidth(15);
        sheet.setDefaultRowHeight((short) 400);

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
