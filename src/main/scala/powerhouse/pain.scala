package powerhouse

import powerhouse._

import java.util.{Date => JDate}

case class IncassoTransfer
(
  amount:               Amount,               // e.g. 10.99
  currency:             CurrencyCode,         // e.g. EUR
  iban:                 IBAN,                 // e.g. NL19ABNA0573066809
  bic:                  BIC,                  // e.g. ABNANL2A
  name:                 String,               // e.g. MSM IT Solutions
  country:              String,               // e.g. NL, static?
  mandateSignatureDate: JDate,                // e.g. Intended as the duration 2013-05-12
  transferDescription:  String,               // e.g. Factuur FN0001334
  endToEndID:           EndToEndID            // e.g. 033FN0001334, derived from invoice number?
)

sealed trait SequenceType
case object FRST extends SequenceType         // e.g. First
case object RCUR extends SequenceType         // e.g. Recurrence 
case object FNAL extends SequenceType         // unused?
case object OOFF extends SequenceType         // unused?


case class Incasso                            // Customer Direct Debit Initiation
(                           
  id:                   MessageID,            // e.g. ESSENTB2B-DD-FRST-201503161200
  dateTime:             JDate,                // e.g. Intended as the instant 2016-06-09T12:09:22+00:00
  iban:                 IBAN,                 // e.g. NL19ABNA0573066809
  name:                 String,               // e.g. Essent Zakelijk, static?
  country:              String,               // e.g. NL, static?
  organisationID:       String,               // e.g. ESSENTB2B, static?
  paymentInstructionID: String,               // e.g. FN0001334-ESSENTB2B-DD-150316
  sequenceType:         SequenceType,         // e.g. FRST
  transfers:            Seq[IncassoTransfer]
)

case class ExcassoTransfer
(
  instructionID:        InstructionID,        // e.g. 033FN0001334, invioce number?
  endToEndID:           EndToEndID,           // e.g. 033FN0001334, static, same?
  amount:               Amount,               // e.g. 10.99
  currency:             CurrencyCode,         // e.g. EUR
  iban:                 IBAN,                 // e.g. NL19ABNA0573066809
  bic:                  BIC,                  // e.g. ABNANL2A
  name:                 String,               // e.g. MSM IT Solutions
  country:              String,               // e.g. NL, static?
  transferDescription:  String                // e.g. Uitbetaling volgens kenmerk FN0001334
)

case class Excasso                            // Customer Credit Transfer Initiation
(
  id:                   MessageID,            // e.g. ESSENTB2B-SCT-201402021200
  dateTime:             JDate,                // e.g. Intended as the instant 2016-06-09T12:09:22+00:00
  iban:                 IBAN,                 // e.g. NL19ABNA0573066809
  name:                 String,               // e.g. Essent Zakelijk, static?
  country:              String,               // e.g. NL, static?
  organisationID:       String,               // e.g. ESSENTB2B, static?
  requestExecutionDate: JDate,                // e.g. Intended as the duration 2016-06-10
  chargeBearerType:     String,               // e.g. SLEV, static?
  transfers:            Seq[ExcassoTransfer]  
)
