package essent.credit

import java.lang.System._
import java.util.UUID._

abstract class Event {
  /** The factual instant in time that the event was constructed */
  val timestamp: Long = currentTimeMillis()

  /** Uniquely identifies the event */
  val uuid: UUID = randomUUID()

  /** The event version */
  val version: String = "0.0.1"
}

case class Transfer private
(
  /** The transferred value; non-zero and positive.
    *
    * TODO Q: Do we need to register currency, e.g `EUR` ?
    */
  value: Amount,

  /** Source and target bank number for the transferred value.
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
    * date is to be represented in `yyyy-MM-dd` format according
    * to GMT time as specified by ISO-8601.    *
    *
    * TODO Q: Which date is this in MT940, value, entry or book ?
    * TODO Q: Is the representation calculated correct ?
    * TODO Q: Which timezone must be used; static or dynamic ?
    * TODO Q: What are the range rules; inclusive, exclusive ?
    */
  valueDate: Date,

  /** Payment description, used for matching with a Ledger ID
    * which can be done either automated or manually by a DM.
    *
    * A reference can be either a:
    *  - Description of bank statement.
    *  - Code by bank to indicate type of transfer.
    *
    * TODO Q: On what data does this reference match ?
    * TODO Q: What is the mapping from MT940 to Reference ?
    */
  reference: Reference
) extends Event {
  import Transfer._
  require (value > 0, s"should have non-zero, positive value: '${value}'")
  require (source != target, s"source and target should not be equal: '${source}'")
  require (isValidValueDate(valueDate), s"valueDate should match YYYY-MM-DD: '${valueDate}'")
}

object Transfer {
  val REG_YYYY_MM_DD = """([0-9]{4})-(1[0-2]|0[1-9])-(3[01]|0[1-9]|[12][0-9])""".r
  def isValidValueDate(date: Date) = REG_YYYY_MM_DD.findFirstIn(date).isDefined
}
