package essent
package credit

case class IncassoTransfer
(
  amount:               Amount,
  iban:                 IBAN
)

case class Incasso                            // Customer Direct Debit Initiation
(                           
  id:                   MessageID,            // e.g. ESSENTB2B-DD-FRST-201503161200
  dateTime:             Date,                 // e.g. 2016-06-09T12:09:22+00:00
  iban:                 IBAN,                 // e.g. NL19ABNA0573066809
  name:                 String,               // e.g. Essent Zakelijk, static?
  country:              String,               // e.g. NL, static?
  organisationID:       String,               // e.g. ESSENTB2B, static?
  paymentInstructionID: String,               // e.g. FN0001334-ESSENTB2B-DD-150316
  transfers:            Seq[IncassoTransfer]
)

case class ExcassoTransfer
(
  instructionID:        InstructionID,        // e.g. 033FN0001334, invioce number?
  endToEndID:           EndToEndID,           // e.g. 033FN0001334, static, same?
  amount:               Amount,               // e.g. 10.99
  iban:                 IBAN,                 // e.g. NL19ABNA0573066809
  name:                 String,               // e.g. MSM IT Solutions
  country:              String,               // e.g. NL, static?
  transferDescription:  String                // e.g. Uitbetaling volgens kenmerk FN0001334
)

case class Excasso                            // Customer Credit Transfer Initiation
(
  id:                   MessageID,            // e.g. ESSENTB2B-SCT-201402021200
  dateTime:             Date,                 // e.g. 2016-06-09T12:09:22+00:00
  iban:                 IBAN,                 // e.g. NL19ABNA0573066809
  name:                 String,               // e.g. Essent Zakelijk, static?
  country:              String,               // e.g. NL, static?
  organisationID:       String,               // e.g. ESSENTB2B, static?
  requestExecutionDate: Date,                 // e.g. 2016-06-10
  chargeBearerType:     String,               // e.g. SLEV, static?
  transfers:            Seq[ExcassoTransfer]  
)
