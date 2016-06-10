package essent

package object credit {

  // Common
  type IBAN              = String       // e.g. NL19ABNA0573066809
  type BIC               = String       // e.g. ABNANL2A
  type Amount            = BigDecimal   // e.g. 10.00
  type CurrencyCode      = String       // e.g. EUR, length = 3

  // UFI
  type JournalID         = String       // validate domain
  type CostCenterID      = String       // validate domain, static?
  type ProfitCenterID    = Long
  type VatCode           = String

  // MT940
  type StatementNumber   = String
  type MutationCode      = String
  type TransactionTypeID = String       // guessed, validate domain
  type MT940DocumentID   = String       // guessed, validate domain
  type LedgerID          = String       // guessed, validate domain
  type ReversalCode      = String

  // PAIN
  type MessageID         = String       // e.g. ESSENTB2B-DD-FRST-201503161200, max length == 35
  type InstructionID     = String       // e.g. max length = 140
  type EndToEndID        = String
}
