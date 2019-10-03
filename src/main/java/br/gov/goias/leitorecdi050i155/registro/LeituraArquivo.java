/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.goias.leitorecdi050i155.registro;

import br.gov.goias.leitorecdi050i155.util.HelperECD;
import br.gov.goias.leitorecdi050i155.util.HelperExcel;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author admin
 */
public class LeituraArquivo {


    public static void main(String[] args) {
        File diretorio = new File("D:\\extracao");

        long dirSizeB = FileUtils.sizeOfDirectory(diretorio);
        System.out.printf("The size of directory is: %.2f megabytes\n", (double) dirSizeB / FileUtils.ONE_MB);

        final String[] RESTRICAO_ARQUIVOS = {"txt"};
        //nivel 1 - 2 contas
        //nivel 2 - 3 contas
        //nivel 3 - 4 contas
        //nivel 4 - 4 contas
        Set<ECD000> empresas = HelperECD.extractDataJ100Empresas(diretorio, RESTRICAO_ARQUIVOS);

        // Criando o arquivo e uma planilha chamada "Product"
        HSSFWorkbook workbook = new HSSFWorkbook();
        HashMap<String, TreeSet<String>> map = HelperECD.extracDataEmpresasSheelTotalNiveisConta(workbook, empresas);
        HelperECD.extractDataEmpresasSheetNivel(workbook, empresas, "1", 2, map);
        HelperECD.extractDataEmpresasSheetNivel(workbook, empresas, "2",3, map);
        HelperECD.extractDataEmpresasSheetNivel(workbook, empresas, "3",4, map);
        HelperECD.extractDataEmpresasSheetNivel(workbook, empresas, "4",4, map);
       // HelperECD.extractDataEmpresasSheetNivel(workbook, empresas, "5",4, map);
//        HelperECD.extractDataEmpresasSheetNivel(workbook,empresas,"3", map);
//        HelperECD.extractDataEmpresasSheetNivel(workbook,empresas,"4", map);
//        HelperECD.extractDataEmpresasSheetNivel(workbook,empresas,"5", map);
//        HSSFWorkbook finalWorkbook = workbook;
//        empresas.stream().forEachOrdered(e->{
//            HelperECD.extractDataJ100EmpresasSheet(finalWorkbook,e);
//        });

        HelperExcel.createFile(workbook, UUID.randomUUID().toString().substring(0, 10));
    }
}
