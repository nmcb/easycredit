package essent
package credit

import java.util.UUID._
import java.lang.System._

case class MT940Transfer
(
  amount:    Amount,
  source:    IBAN,
  target:    IBAN,
  date:      Date,
  ledgerID:  LedgerID,
  timestamp: Long       = currentTimeMillis,
  uuid:      UUID       = randomUUID,
  version:   String     = "0.0.1"   
)