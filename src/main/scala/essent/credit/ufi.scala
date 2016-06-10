package essent
package credit

import java.util.Date

sealed trait JournalType
case object BankEntry    extends JournalType
case object Reversal     extends JournalType
case object Correction   extends JournalType
case object InvoiceEntry extends JournalType
case object WriteOff     extends JournalType
case object Provision    extends JournalType

case class JournalEntryLine
(
  amount:           Amount,
  description:      String,
  costCenterID:     CostCenterID,
  accountNumber:    Long,                     // not an IBAN/String?
  valueDate:        Date,
  profitCenterID:   ProfitCenterID,
  vatCode:          VatCode,
  costWBS:          String,                   // code?
  gridOperator:     String,                   // code?
  vatBase:          Amount
)

case class JournalEntry                       // line amounts total on zero, but how?
(
  entryDate:        Date,
  creationDate:     Date,
  lines:            Seq[JournalEntryLine]
)

case class Journal
(
  id:               JournalID,
  journalType:      JournalType,
  entries:          Seq[JournalEntry],
  relatedJournalID: JournalID
)

