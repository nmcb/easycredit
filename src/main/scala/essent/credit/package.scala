package essent

import essent.credit.external.Transfer

package object credit {

  // Common
  type Amount            = BigDecimal            // e.g. 10.00
  type IBAN              = String                // e.g. NL19ABNA0573066809
  type UUID              = java.util.UUID        // e.g. 47b16912-eb14-483c-86fb-4c9e4de31fbd
  type Date              = String                // e.g. 2016-07-24, i.e. `YYYY-MM-DD` representation

  // Shared

  /** TODO The underlying value must support matching a JournalID */
  type Reference         = String

  // MT940 / ABNAmro
  type MT940             = Seq[Transfer]

  // UFI
  type JournalID         = String       // validate domain
  type CostCenterID      = String       // validate domain, static?
  type ProfitCenterID    = Long
  type VatCode           = String

  // PAIN
  type MessageID         = String       // e.g. ESSENTB2B-DD-FRST-201503161200, max length == 35
  type InstructionID     = String       // e.g. max length = 140
  type EndToEndID        = String
}
