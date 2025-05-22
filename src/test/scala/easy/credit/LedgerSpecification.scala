package easy.credit

import easy.credit.Ledger._

import org.scalatest.propspec._
import org.scalatest.matchers.should.Matchers._
import org.scalatest.prop.Tables._
import org.scalatestplus.scalacheck._
import org.scalacheck._

class LedgerSpecification extends AnyPropSpec with ScalaCheckPropertyChecks {

  import PaymentSpecification._
  import LedgerSpecification._
  import Ledger._

  property("journal line; is validly constructable from valid parameters only") {
    forAll (journals, validAmounts, sides) {
      (ledger: Journal, amount: Amount, side: Side) => {
        val line = Line(ledger, amount, side)
        line.amount should be > Amount.zero
      }
    }
  }

  property("journal line; has a positive and non-zero amount") {
    forAll (invalidAmounts) { (amount: Amount) =>
      an[IllegalArgumentException] should be thrownBy {
        Line(someJournal, amount, someSide)
      }
    }
  }

  property("journal entry; is validly constructable from valid parameters only") {
    forAll (someBalancedLineSeqs) { (lines: Seq[Line]) => {
        forAll (validDateLiterals) { (date: Date) =>
          val entry = Entry(lines, date)
          entry.lines.isEmpty shouldBe false
          entry.debit shouldBe entry.credit
          CreditDomain.isValidDateLiteral(entry.date) shouldBe true
        }
      }
    }
  }

  property("journal entry; has balanced journal lines") {
    forAll (someUnbalancedLineSeqs) { (lines: Seq[Line]) =>
        an[IllegalArgumentException] should be thrownBy {
          Entry(lines, someDateLiteral)
        }
    }
  }

  property("journal entry; has at least one journal line") {
    an[IllegalArgumentException] should be thrownBy {
      Entry(emptyLineSeq, someDateLiteral)
    }
  }

  property("journal entry; entry date is represented in `YYYY-MM-DD` format") {
    forAll (invalidDateLiterals) { (date: Date) =>
      an[IllegalArgumentException] should be thrownBy {
        Entry(someBalancedLineSeq, date)
      }
    }
  }
}

object LedgerSpecification {

  import PaymentSpecification._

  /** valid journals are: */
  val journals = Gen.oneOf(BankJournal, AccountsReceivableJournal)

  /** a sampled valid journal */
  val someJournal = journals.sample.get

  /** valid ledger sides are: */
  val sides = Gen.oneOf(Credit, Debit)

  /** a sampled valid side */
  val someSide = sides.sample.get

  /** valid journal lines are composed from valid amounts */
  val validLines = for {
    journal <- journals
    amount  <- validAmounts
    side    <- sides
  } yield Line(journal, amount, side)

  /** empty line sequence */
  val emptyLineSeq = Seq.empty[Line]

  /** some balanced line sequences are: */
  val someBalancedLineSeqs = Table("lines",
    Seq(
      Line( BankJournal,               100    , Debit  ),
      Line( AccountsReceivableJournal,     100, Credit )
    ),
    Seq(
      Line( BankJournal,               200    , Debit  ),
      Line( AccountsReceivableJournal,     100, Credit ),
      Line( AccountsReceivableJournal,     100, Credit )
    ),
    Seq(
      Line( BankJournal,               100    , Debit  ),
      Line( BankJournal,               100    , Debit  ),
      Line( AccountsReceivableJournal,     200, Credit )
    )
  )

  /** some balanced line sequence, the first taken from balanced line sequences */
  val someBalancedLineSeq = someBalancedLineSeqs.head

  /** some known unbalanced line sequences are: */
  val someUnbalancedLineSeqs = Table("lines",
    Seq(
      Line( BankJournal,               100    , Debit  ),
      Line( AccountsReceivableJournal,     101, Credit )
    ),
    Seq(
      Line( BankJournal,               100    , Debit  ),
      Line( AccountsReceivableJournal,      50, Credit ),
      Line( AccountsReceivableJournal,      51, Credit )
    ),
    Seq(
      Line( BankJournal,               100    , Debit  ),
      Line( BankJournal,               100    , Credit ),
      Line( AccountsReceivableJournal,     201, Credit )
    )
  )

  /** some unbalanced line sequence, the first taken from unbalanced line sequences */
  val someUnbalancedLineSeq = someUnbalancedLineSeqs.head
}
