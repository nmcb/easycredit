package powerhouse

import java.util.{Date => JDate}

import essent.credit._

case class MT940Transfer
(
  amount:               Amount,             // TODO Q: Currency ? Rounding Rules ? Resolution ?

  accountNumber:        IBAN,               // TODO Q: Source / Target ?
  contraAccountNumber:  IBAN,               // TODO Q: Source / Target ?

  name:                 String,             // TODO Q: Of which IBAN identified entity ?

  mutationCode:         MutationCode,       // TODO Q: Meaning ?

  statementNumber:      StatementNumber,    // TODO Q: Sequence of Events (event ordering)
  date:                 Date,               // TODO Q: Resolution ? Instance/Duration ? (event ordering)

  dontMatch:            Boolean,            // TODO Q: Is this input ? (seems to be a DF marker)
  transactionTypeID:    TransactionTypeID,  // TODO Q: Reference Match ? (or event ordering)
  reference:            String,             // TODO Q: Reference Match ? (also some description)
  mt940DocumentID:      MT940DocumentID,    // TODO Q: Reference Match ? (traceability)
  paymentDescription:   String,             // TODO Q: Reference Match ?

  transferFncAmount:    Amount,             // TODO Q: Meaning ?

  reversal:             Boolean,            // TODO Q: Dual ?
  reversalCode:         ReversalCode,       // TODO Q: Dual ?

  ledgerID:             LedgerID,           // TODO Q: ledger :: S -> IBAN -> IBAN -> Ledger ?
  bic:                  BIC,                // TODO Q: bic :: IBAN -> BIC ?

  transferDate:         Date,               // TODO Q: Resolution ? Instance/Duration ?
  description:          String              // TODO Q: Reference Match ? (see reference)
)

case class MT940Document
(
  saldoEndDate:         JDate,              // TODO Q: Resolution ? What is saldo ?
  transfers:            Seq[MT940Transfer]  // TODO Q: Ordered ? Indexed ?
)