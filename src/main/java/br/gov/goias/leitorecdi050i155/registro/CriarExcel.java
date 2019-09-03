package br.gov.goias.leitorecdi050i155.registro;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CriarExcel {

    public static void main(String[] args) {

//        // Criando o arquivo e uma planilha chamada "Product"
//        HSSFWorkbook workbook = new HSSFWorkbook();
//        HSSFSheet sheet = workbook.createSheet("Product");
//
//        // Definindo alguns padroes de layout
//        sheet.setDefaultColumnWidth(15);
//        sheet.setDefaultRowHeight((short)5000);
//
//        //Carregando os produtos
//        List<Product> products = getProducts();
//
//        int rownum = 0;
//        int cellnum = 0;
//        Cell cell;
//        Row row;
//
//
//        // Configurando Header
//        row = sheet.createRow(rownum++);
//        cell = row.createCell(cellnum++);
//        cell.setCellStyle(headerStyle);
//        cell.setCellValue("Code");
//
//        cell = row.createCell(cellnum++);
//        cell.setCellStyle(headerStyle);
//        cell.setCellValue("Name");
//
//        cell = row.createCell(cellnum++);
//        cell.setCellStyle(headerStyle);
//        cell.setCellValue("Price");
//
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
//
//        //criar arquivo

    }

    //Simulando uma listagem de produtos
    private static List<Product> getProducts() {
        List products = new ArrayList();
        products.add(Product.builder().id(1l).name("Product 1").price(200.5d).build());
        products.add(Product.builder().id(2l).name("Produto 2").price(1050.5d).build());
        products.add(Product.builder().id(3l).name("Produto 3").price(50d).build());
        products.add(Product.builder().id(4l).name("Produto 4").price(200d).build());
        products.add(Product.builder().id(5l).name("Produto 5").price(450d).build());
        products.add(Product.builder().id(6l).name("Produto 6").price(150.5d).build());
        products.add(Product.builder().id(7l).name("Produto 7").price(300.99d).build());
        products.add(Product.builder().id(8l).name("Produto 8").price(1000d).build());
        products.add(Product.builder().id(9l).name("Produto 9").price(350d).build());
        products.add(Product.builder().id(10l).name("Produto 10").price(200d).build());

        return products;

    }
}