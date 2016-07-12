package essent.credit

import essent.credit.Ledger._
import essent.credit.PaymentSpecification.Assumptions
import org.scalacheck.Gen
import org.scalatest.Matchers._
import org.scalatest.PropSpec
import org.scalatest.prop.PropertyChecks
import org.scalatest.prop.Tables.Table


class LedgerSpecification extends PropSpec with PropertyChecks {

  import PaymentSpecification.Assumptions._
  import LedgerSpecification._
  import Ledger._

  property("journal line; is constructable with valid parameters") {
    forAll (validJournals, validAmounts, validSides) {
      (ledger: Journal, amount: Amount, side: Side) => {
        Line(ledger, amount, side)
      }
    }
  }

  property("journal line amount; has a positive, non-zero amount") {
    forAll (invalidAmounts) { (amount: Amount) =>
      an[IllegalArgumentException] should be thrownBy {
        Line(validJournal, amount, validSide)
      }
    }
  }
}

object LedgerSpecification {

  /** valid journals are: */
  val validJournals = Gen.oneOf(BankJournal, AccountsReceivableJournal)

  /** a sampled valid journal */
  val validJournal = validJournals.sample.get

  /** valid ledger sides are: */
  val validSides = Gen.oneOf(Credit, Debit)

  /** a sampled valid side */
  val validSide = validSides.sample.get
}
