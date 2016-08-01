package essent

package object credit {

  // Common
  type Amount            = Long                  // e.g. real 10.00 euro is literally 1000
  type IBAN              = String                // e.g. literally NL19ABNA0573066809
  type UUID              = java.util.UUID        // e.g. literally 47b16912-eb14-483c-86fb-4c9e4de31fbd
  type Date              = String                // e.g. literally 2016-07-24, i.e. `YYYY-MM-DD` representation

  // TODO make ERef and DRef typed and sealed.
  type Ref               = String                // e.g. literally the reference information

  object CreditDomain {
    val DateLiteral = """([0-9]{4})-(1[0-2]|0[1-9])-(3[01]|0[1-9]|[12][0-9])""".r
    def isValidDateLiteral(repr: String) = DateLiteral.findFirstIn(repr).isDefined
  }
}
