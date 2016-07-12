package essent.credit

import java.lang.System._
import java.util.UUID._

trait Event
{
  /** The factual period in which this event was constructed, resolution in millis. */
  val timestamp: Long = currentTimeMillis()

  /** Identifies this event, universally unique with respect to timestamp and version. */
  val uuid: UUID = randomUUID()

  /** The event's version, identifies the informational structure that is captured. */
  val version: String = "0.0.1"
}

case class Payment
(
  /** The paid amount; positive.
    *
    * TODO Q: Do we need to register currency, e.g `EUR` ?
    * TODO Q: Do we require floating point arithmetic ?
    * TODO Q: Do we require decimal or binary semantics ?
    */
  amount: Amount,

  /** Source and target bank account numbers of the payment.
    * Either the source or the target must be owned by Essent,
    * thus providing for bi-directional payments.  Source and
    * target may not equal each other.
    *
    * TODO Q: What is the meaning of the underlying string rep ?
    * TODO Q: What are constraints on the underlying string rep ?
    * TODO Q: Can a BIC number be computed from an IBAN ?
    */
  source: IBAN,
  target: IBAN,

  /** The date the amount was paid, resolution in days. The date is
    * to be represented in `YYYY-MM-DD` format as specified by ISO-8601.
    * As payment events are universally identifiable we require payments
    * to agree on Greenwich Mean Time (GMT) as the underlying clock from
    * which the value date is calculated.
    *
    * TODO Q: Which date is this in MT940: value, entry or book ?
    * TODO Q: Assuming ISO-8601 specification, OK ?
    * TODO Q: Do we need to consider range rules of the underlying clock ?
    *
    * TODO A: Assuming GMT, but currently implemented using sysdate.
    */
  valueDate: Date,

  /** Payment description, employed if this payment requires a new ledger
    * entry, either when automatically matched or manually, by a DM.
    *
    * A reference can be either a:
    *  - Description of bank statement.
    *  - Code by bank to indicate type of payment.
    *
    * TODO Q: On what system owned data matches this reference automatically ?
    * TODO Q: What is the mapping from MT940 to Reference ?
    * TODO A: Is there usability in the the `EREF` attribute in the MT940 specification ?
    */
  reference: Reference
)
extends Event
{
  import CreditDomain._
  require (amount > 0, s"must be non-zero and positive, amount is '${amount}'")
  require (source != target, s"may not be equal, source and target are both '${source}'")
  require (isValidDateLiteral(valueDate), s"must have YYYY-MM-DD format, valueDate is '${valueDate}'")
}

object Ledger
{
  trait Bookable {
    def debit: Amount  = amount(Debit)
    def credit: Amount = amount(Credit)
    def amount(side: Side): Amount
  }

  sealed trait Journal
  case object BankJournal               extends Journal
  case object AccountsReceivableJournal extends Journal

  sealed trait Side
  case object Debit  extends Side
  case object Credit extends Side

  case class Line(journal: Journal, amount: Amount, side: Side) {
    require (amount > 0, s"must be non-zero and positive, amount is '${amount}'")
  }

  object Line {
    def amountsTo(lines: Seq[Line])(side: Side): Amount =
      lines.filter(_.side == side).map(l => l.amount).sum[Amount]
  }

  case class Entry(lines: Seq[Line], date: Date) extends Event with Bookable {
    import CreditDomain._
    import Line._
    require (debit == credit , s"should be balanced, debit is '${debit}', credit is '${credit}'")
    require (isValidDateLiteral(date), s"must have YYYY-MM-DD format, valueDate is '${date}'")
    override def amount(side: Side): Amount = amountsTo(lines)(side)
  }
}
