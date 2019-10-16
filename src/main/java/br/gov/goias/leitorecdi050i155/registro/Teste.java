package br.gov.goias.leitorecdi050i155.registro;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class Teste {

    public static void main(String[] args) {
        File diretorio = new File("D:\\ecd");
        final String[] RESTRICAO_ARQUIVOS = {"txt"};

        Set<ECD000> empresas = ExtractData.extractFileData(diretorio, RESTRICAO_ARQUIVOS);



    }
}
