package br.gov.goias.leitorecdi050i155.registro;

public enum GrupoContas {

    ATIVO("01"),
    PASSIVO("02"),
    PATRIMONIO_LIQUIDO("03"),
    CONTAS_RESULTADO("04"),
    CONTAS_COMPENSACAO("05"),
    OUTRAS("09");

    private String numVal;

    GrupoContas(String numVal) {
        this.numVal = numVal;
    }

    public static GrupoContas fromString(String text) {
        for (GrupoContas b : GrupoContas.values()) {
            if (b.numVal.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
