package powerhouse

import java.util.{Date => JDate}
import essent.credit._

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
  valueDate:        JDate,                    // TODO Q: Duration or Instant ? Resolution ?
  profitCenterID:   ProfitCenterID,
  vatCode:          VatCode,
  costWBS:          String,                   // code?
  gridOperator:     String,                   // code?
  vatBase:          Amount
)

case class JournalEntry                       // line amounts total on zero, but how?
(
  entryDate:        JDate,                    // TODO Q: Duration or Instant ? Resolution ?
  creationDate:     JDate,                    // TODO Q: Duration or Instant ? Resolution ?
  lines:            Seq[JournalEntryLine]
)

case class Journal
(
  id:               JournalID,
  journalType:      JournalType,
  entries:          Seq[JournalEntry],
  relatedJournalID: JournalID
)

