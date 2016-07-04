package essent.credit

import java.lang.System._
import java.util.UUID._

abstract class Event {
  /** The factual period in which this event was constructed, resolution in millis */
  val timestamp: Long = currentTimeMillis()

  /** Identifies this event, universally unique with respect to timestamp and version. */
  val uuid: UUID = randomUUID()

  /** The event version */
  val version: String = "0.0.1"
}

case class Transfer
(
  /** The transferred value; positive.
    *
    * TODO Q: Do we need to register currency, e.g `EUR` ?
    * TODO Q: Do we require floating point arithmetic ?
    * TODO Q: Do we require decimal or binary semantics ?
    */
  value: Amount,

  /** Source and target bank account numbers of the transfer.
    * Either the source or the target must be owned by Essent,
    * thus providing for bi-directional transfers.  Source and
    * target may not equal each other.
    *
    * TODO Q: What is the meaning of the underlying string rep ?
    * TODO Q: What are constraints on the underlying string rep ?
    * TODO Q: Can a BIC number be computed from an IBAN ?
    */
  source: IBAN,
  target: IBAN,

  /** The date the value was transferred, resolution in days. The
    * date is to be represented in `YYYY-MM-DD` format according
    * to GMT time, date literals are specified by ISO-8601.    *
    *
    * TODO Q: Which date is this in MT940: value, entry or book ?
    * TODO Q: Assuming ISO-8601 specification, OK ?
    * TODO Q: Do we need to consider range rules of the underlying clock ?
    *
    * TODO A: Assuming GMT, but currently implemented using sysdate.
    */
  valueDate: Date,

  /** Payment description, employed if this transfer requires a new ledger
    * entry, either when automatically matched or manually, by a DM.
    *
    * A reference can be either a:
    *  - Description of bank statement.
    *  - Code by bank to indicate type of transfer.
    *
    * TODO Q: On what system owned data matches this reference automatically ?
    * TODO Q: What is the mapping from MT940 to Reference ?
    */
  reference: Reference

) extends Event {
  import Transfer._
  require (value > 0, s"must be non-zero and positive, value: '${value}'")
  require (source != target, s"may not be equal, source == target: '${source}'")
  require (isValidDateLiteral(valueDate), s"must match YYYY-MM-DD, valueDate: '${valueDate}'")
}

object Transfer {
  val DateLiteral = """([0-9]{4})-(1[0-2]|0[1-9])-(3[01]|0[1-9]|[12][0-9])""".r
  def isValidDateLiteral(date: Date) = DateLiteral.findFirstIn(date).isDefined
}
