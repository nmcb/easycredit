package essent.credit

import java.lang.System._
import java.util.UUID._

case class Transfer
(
/** The transferred value; non-zero and positive.
  *
  * TODO Q: Do we need to register currency, e.g `EUR`.
  */
value: Amount,

/** Source and target bank number of the transferred amount.
  * Either the source or the target must be owned by Essent,
  * thus providing for bi-directional transfers.
  *
  * TODO Q: What is the meaning of the underlying string rep?
  * TODO Q: What are constraints on the underlying string rep?
  * TODO Q: Can a BIC number be computed from an IBAN.
  */
source: IBAN,
target: IBAN,

/** The date the value was transferred, resolution in days,
  *
  * TODO Q: Which date is this in MT940, value, entry or book?
  * TODO Q: Is the resolution correct?
  * TODO Q: Which timezone must be used; static or dynamic?
  * TODO Q: What are the range rules; inclusive, exclusive?
  */
valueDate: Date,

/** Payment description, used for matching with a Ledger ID
  * which can be done either automated or manually by a DM.
  *
  * A reference can be either a:
  *  - Description of bank statement.
  *  - Code by bank to indicate type of transfer.
  *
  * TODO Q: On what data does this reference match?
  * TODO Q: What is the mapping from MT940 to Reference?
  */
reference: Reference,

/** The factual instant in time that the transfer was first known. */
timestamp: Long       = currentTimeMillis,

/** Uniquely identifies the transfer fact. */
uuid:      UUID       = randomUUID,

/** The transfer version */
version:   String     = "0.0.1"
)


