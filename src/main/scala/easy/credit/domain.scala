package easy.credit

import java.lang.System._
import java.util.UUID._

trait Event
{
  /** The factual datum in which this event was constructed, resolution in millis. */
  val timestamp: Long = currentTimeMillis()

  /** Identifies this event, universally unique with respect to version. */
  val uuid: UUID = randomUUID()

  /** The event's version, identifies the informational structure that is captured. */
  val version: String = "0.0.1"
}

case class Payment
(
  /** The paid amount; positive and non-zero.
    *
    * TODO Q: Do we need to maintain currency, e.g `EUR` ?
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

  /** Payment information intended to match this payment with one or more
    * future bookable events.  A payment is matched to these events auto-
    * matically, employing the referential information provided in this
    * payment, e.g. the amount or source and target account numbers, but
    * only if the payment is accompanied with enough information to render
    * future bookable event values and book entries for it.  Matching, and
    * subsequent booking is known as 'the automated process'.  A payment
    * reference models all information required to match, an electronic
    * payment reference all information required to match automatically,
    * a descriptive payment reference all payment information intended to
    * match automatically, but minimally all payment information required
    * to match by human decision.
    *
    * A reference can be either a:
    *
    *  - Description of one or more future bookable events, a [[DRef]].
    *  This type of reference contains an informative description of the
    *  reason the payment was made.  References of the descriptive type
    *  should be matched to future events automatically, but this is not
    *  guaranteed, sometimes an end-user action or additional information
    *  is required for a match.  The information contained in a descriptive
    *  reference originates from within a system controlled by the source
    *  of the payment.
    *
    *  - Code that uniquely identifies one or more future bookable events,
    *  an [[ERef]].  An electronic references originates from the target
    *  of a payment, it was provided by the target of this payment to the
    *  bank previously, specifically intended to be able to match a future
    *  payment (this one) to a future bookable event.  That is, an [[ERef]]
    *  literal originates from within the credit system runtime boundaries.
    *
    * TODO Q: What is the mapping from MT940 to a reference ?  (ERef = EREF)
    */
  ref: Ref
)
extends Event
{
  import CreditDomain._
  require (amount > 0, s"must be non-zero and positive, amount is '$amount'")
  require (source != target, s"may not be equal, source and target are both '$source'")
  require (isValidDateLiteral(valueDate), s"must have YYYY-MM-DD format, valueDate is '$valueDate'")
}

object Ledger
{
  sealed trait Side
  case object Debit  extends Side
  case object Credit extends Side

  trait Bookable {
    def debit: Amount  = amount(Debit)
    def credit: Amount = amount(Credit)
    def amount(side: Side): Amount
  }

  sealed trait Journal
  case object BankJournal               extends Journal
  case object AccountsReceivableJournal extends Journal

  case class Line(journal: Journal, amount: Amount, side: Side) {
    require (amount > 0, s"must be non-zero and positive, amount is '$amount'")
  }

  object Line {
    def amountsTo(lines: Seq[Line])(side: Side): Amount =
      lines.filter(_.side == side).map(l => l.amount).sum[Amount]
  }

  case class Entry(lines: Seq[Line], date: Date) extends Event with Bookable {
    import CreditDomain._
    import Line._
    require (lines.nonEmpty , s"must contain entry lines")
    require (debit == credit , s"must be balanced, debit is '$debit', credit is '$credit'")
    require (isValidDateLiteral(date), s"must have YYYY-MM-DD format, date is '$date'")
    override def amount(side: Side): Amount = amountsTo(lines)(side)
  }
}
