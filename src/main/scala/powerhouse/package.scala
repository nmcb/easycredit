package powerhouse

package object powerhouse {

  // Common
  type Amount            = Long                  // e.g. real 10.00 euro is literally 1000
  type IBAN              = String                // e.g. literally NL19ABNA0573066809
  type UUID              = java.util.UUID        // e.g. literally 47b16912-eb14-483c-86fb-4c9e4de31fbd
  type Date              = String                // e.g. literally 2016-07-24, i.e. `YYYY-MM-DD` representation

  /** TODO The underlying value must support matching a JournalID */
  type Reference         = String

  // MT940 / ABNAmro
  type MT940             = Seq[MT940Transfer]

  // UFI
  type JournalID         = String       // validate domain
  type CostCenterID      = String       // validate domain, static?
  type ProfitCenterID    = Long
  type VatCode           = String

  // PAIN
  type MessageID         = String       // e.g. ESSENTB2B-DD-FRST-201503161200, max length == 35
  type InstructionID     = String       // e.g. max length = 140
  type EndToEndID        = String

  /** TODO validate required domain types below after the `mt940.scala`
    * has been minimized into `domain.Payment`
    **/

  // Common
  type BIC               = String       // e.g. ABNANL2A
  type CurrencyCode      = String       // e.g. EUR, length = 3

  // MT940
  type StatementNumber   = String
  type MutationCode      = String
  type TransactionTypeID = String       // guessed, validate domain
  type MT940DocumentID   = String       // guessed, validate domain
  type LedgerID          = String       // guessed, validate domain
  type ReversalCode      = String

  object CreditDomain {
    val DateLiteral = """([0-9]{4})-(1[0-2]|0[1-9])-(3[01]|0[1-9]|[12][0-9])""".r
    def isValidDateLiteral(repr: String) = DateLiteral.findFirstIn(repr).isDefined
  }
}
