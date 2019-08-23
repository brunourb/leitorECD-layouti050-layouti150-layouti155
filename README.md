# Escrituração Contábil Digital - ECD



A Escrituração Contábil Digital - ECD - foi instituída para fins fiscais e previdenciários e deverá ser 
transmitida pelas pessoas jurídicas a ela obrigadas, ao Sistema Público de Escrituração Digital (Sped), 
e será considerada válida após a confirmação de recebimento do arquivo que a contém e, quando for o caso, 
após a autenticação pelos órgãos de registro.

# Digital Accounting Bookkeeping

Bookkeeping is the recording of financial transactions, and is part of the process of accounting in business.
Transactions include purchases, sales, receipts, and payments by an individual person or an organization/corporation.
There are several standard methods of bookkeeping, including the single-entry and double-entry bookkeeping systems.
While these may be viewed as "real" bookkeeping, any process for recording financial transactions is a bookkeeping process.[Wikipedia]

The Digital accounting bookkeeping in Brazil as ECD was instituted for tax and social security purposes and should be 
transmitted by the legal persons obliged to it, to the public system of Digital bookkeeping (SPED), 
and will be deemed valid after the confirmation of Receipt of the file containing it and,
 where appropriate, after authentication by the registration bodies.
 
 #Propose
 This project read file in layout i050, i150, i155 
  https://documentacao.senior.com.br/goup/5.8.9/menu_controladoria/sped/contabil-ecd/capa.htm
  
  # Bloco I: Lançamentos Contábeis
  | Registro | Descrição |
  | ---  | --- |
  | I050 | Plano de contas |
  | I150 | Saldos periódicos – Identificação do período |
  | I155 | Detalhes dos saldos periódicos |
  
  
  # Configuração de Ambiente
  VM Options
  
  Use absolute path or environment variable 
  export PATH_TO_FX=C:\Program Files\Java\javafx-sdk-11.0.2\lib
  
  Ex.: $PATH_TO_FX or "C:\Program Files\Java\javafx-sdk-11.0.2\lib"
  
  * --module-path $PATH_TO_FX
  * --add-modules javafx.controls,javafx.fxml
  * --add-exports javafx.graphics/com.sun.javafx.util=ALL-UNNAMED
  * --add-exports javafx.base/com.sun.javafx.reflect=ALL-UNNAMED
  * --add-exports javafx.base/com.sun.javafx.beans=ALL-UNNAMED
  * --add-exports javafx.graphics/com.sun.glass.utils=ALL-UNNAMED
  * --add-exports javafx.graphics/com.sun.javafx.tk=ALL-UNNAMED