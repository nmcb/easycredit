package essent
package credit

import java.util.Date

case class MT940Transfer
(
  amount:               Amount,
  statementNumber:      StatementNumber,
  mutationCode:         MutationCode,
  accountNumber:        IBAN,
  contraAccountNumber:  IBAN,
  transactionTypeID:    TransactionTypeID,
  date:                 Date,
  reference:            String,              // reference code or value?
  mt940DocumentID:      MT940DocumentID,     // move to containing object?
  transferFncAmount:    Amount,
  ledgerID:             LedgerID,
  name:                 String,              // which name?
  reversal:             Boolean,
  bic:                  BIC,
  reversalCode:         ReversalCode,
  paymentDescription:   String,
  dontMatch:            Boolean,             // attribute of a transfer?
  transferDate:         Date,
  description:          String
)

case class MT940Document
(
  saldoEndDate:         Date,
  transfers:            Seq[MT940Transfer]
)