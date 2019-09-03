package br.gov.goias.leitorecdi050i155.util;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class HelperExcel {

    public static void createFile(HSSFWorkbook workbook,String name){
        try {
            //Escrevendo o arquivo em disco
            FileOutputStream out = new FileOutputStream(new File(String.format("D:\\%s.xls",name)));
            workbook.write(out);
            out.close();
            workbook.close();
            System.out.println("Success!!");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Font getRed(HSSFWorkbook workbook){
        Font fontRed = workbook.createFont();
        fontRed.setColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
        return fontRed;
    }

    public static Font getBlue(HSSFWorkbook workbook){
        Font fontBlue = workbook.createFont();
        fontBlue.setColor(HSSFColor.HSSFColorPredefined.BLUE.getIndex());
        return fontBlue;
    }

    public static CellStyle headerStyle(HSSFWorkbook workbook){
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        return headerStyle;
    }

    public static CellStyle textStyle(HSSFWorkbook workbook){
        CellStyle textStyle = workbook.createCellStyle();
        textStyle.setAlignment(HorizontalAlignment.CENTER);
        textStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        return textStyle;
    }

    public static CellStyle numberStyle(HSSFWorkbook workbook){
        //Configurando estilos de células (Cores, alinhamento, formatação, etc..)
        HSSFDataFormat numberFormat = workbook.createDataFormat();
        CellStyle numberStyle = workbook.createCellStyle();
        numberStyle.setDataFormat(numberFormat.getFormat("#,##0.00"));
        numberStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        return numberStyle;
    }
}
