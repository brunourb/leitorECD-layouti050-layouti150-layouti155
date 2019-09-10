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
        File diretorio = new File("D:\\ecd");

        long dirSizeB = FileUtils.sizeOfDirectory(diretorio);
        System.out.printf("The size of directory is: %.2f megabytes\n", (double) dirSizeB / FileUtils.ONE_MB);

        final String[] RESTRICAO_ARQUIVOS = {"txt"};

//        empresas = HelperECD.extractDataEmpresas(diretorio, RESTRICAO_ARQUIVOS);

        Set<ECD000> empresas = HelperECD.extractDataJ100Empresas(diretorio, RESTRICAO_ARQUIVOS);

        // Criando o arquivo e uma planilha chamada "Product"
        HSSFWorkbook workbook = new HSSFWorkbook();
        HelperECD.extractDataEmpresasSheetNivel1(workbook,empresas);
        empresas.stream().forEachOrdered(e->{

//            HelperECD.extractDataEmpresasSheetNivel2(workbook,e);
//            HelperECD.extractDataEmpresasSheetNivel3(workbook,e);
//            HelperECD.extractDataEmpresasSheetNivel4(workbook,e);
//            HelperECD.extractDataEmpresasSheetNivel5(workbook,e);
            HelperECD.extractDataJ100EmpresasSheet(workbook,e);
        });

        HelperExcel.createFile(workbook,UUID.randomUUID().toString().substring(0,10));





//        Stream<ECD000> e = empresas.parallelStream();
//        e.forEachOrdered(e1 -> {
//            System.out.println(e1.cnpj);
//            System.out.println(e1.ie);
//            System.out.println(e1.nomeEmpresa);
//            System.out.println(e1.codigoMunicipioIBGE);
//            System.out.println(e1.dataInicial);
//            System.out.println(e1.dataFinal);
//            System.out.println(e1.im);
//            System.out.println(e1.uf);
//            e1.i050s.stream().forEach(r->{
////                r.recursiveSubContasValorAcumulado();
//                System.out.printf("\n\n%s\n", r.getNomeConta());
//                BigDecimal saldo = r.recursiveWalkSubContasValues();
//                if(r.recursiveWalkSubContasValues()!=BigDecimal.ZERO){
//                    System.out.printf("%s %s %.2f \n",
//                            r.getNivelConta(),
////                        r.getCodigoContaAnalitica(),
////                        r.getCodigoContaSintetica(),
//                            r.getNomeConta(),
//                            saldo.doubleValue()
//                    );
//                }
//            });
//            });
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
