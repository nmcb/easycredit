package easy.credit

import org.scalatest.propspec._
import org.scalatest.matchers.should.Matchers._
import org.scalatest.prop.Tables._
import org.scalatestplus.scalacheck._
import org.scalacheck._

class PaymentSpecification extends AnyPropSpec with ScalaCheckPropertyChecks {

  import PaymentSpecification._
  import CreditDomain._

  property("payment; is valid on construction") {
    forAll { payment: Payment => {
        payment.amount should be > Amount.zero
        payment.source should not be payment.target
        isValidDateLiteral(payment.valueDate) shouldBe true
      }
    }
  }

  property("payment; has a positive and non-zero amount") {
    forAll (invalidAmounts) { (amount: Amount) =>
      an[IllegalArgumentException] should be thrownBy {
        Payment(amount, someSourceIBAN, someTargetIBAN, someDateLiteral, someRef)
      }
    }
  }

  property("payment; source and target are not equal") {
    forAll (validIBANs) { (target: IBAN) =>
      an[IllegalArgumentException] should be thrownBy {
        val source = target
        Payment(someAmount, source, target, someDateLiteral, someRef)
      }
    }
  }

  property("payment; value date is represented in `YYYY-MM-DD` format") {
    forAll (invalidDateLiterals) { (valueDate: Date) =>
      an[IllegalArgumentException] should be thrownBy {
        Payment(Amount.unit, someSourceIBAN, someTargetIBAN, valueDate, someRef)
      }
    }
  }
}

object PaymentSpecification {

  /** assume payments between bank accounts located in either BE, NL or DE */
  val bankAccountCountries: Seq[String] = Array("BE", "NL", "DE")

  /** assume valid payment amounts in the range of 0.01 to 1,000,000.00 euro */
  val validAmounts = Gen.choose[Amount](Amount.unit, 100000000)

  /** assume typically known invalid payment amounts to be: */
  val invalidAmounts = Table[Amount]("amount", Amount.zero, -1, Long.MinValue)

  // TODO generate valid IBANs instead of the IBAN's country code.
  val validIBANs = for {
    countryCode    <- Gen.oneOf(bankAccountCountries)
    sequenceNumber <- Gen.choose[Int](0, 99)
  } yield f"$countryCode%s$sequenceNumber%02d"

  /** assume valid date literals to match ISO-8601's YYYY-MM-DD format */
  val validDateLiterals: Gen[Date] = for {
    year  <- Gen.choose(0, 9999)
    month <- Gen.choose(1, 12)
    day   <- Gen.choose(1, 31)
  } yield f"$year%04d-$month%02d-$day%02d"

  /** known invalid value literals violate ISO-8601's YYYY-MM-DD format */
  val invalidDateLiterals = Table[Date]("valueDate",
    "0000-00-00", "0000-13-01", "0000-01-32", "2016-1-1", "-1601-01", "2016-31-12",
    "02/27/2013", "02/27/13", "27/02/2013", "27/02/13", "20130227", "2013.02.27",
    "27.02.13", "27-02-13", "27.2.13", "2013.II.27", "27/2-13", "2013.158904109",
    "MMXIII-II-XXVII", "MMXIII LVII/CCCLXV", "1330300800", "", "0", "1234567890"
  )

  /** assume valid references to be electronic, literal numeric strings */
  // TODO generate DRef's
  val validRefs: Gen[Ref] = for {
    ref <- Gen.numStr
  } yield ERef(ref)

  val validPayments: Gen[Payment] = for {
    amount    <- validAmounts
    source    <- validIBANs
    target    <- validIBANs.retryUntil(t => t != source)
    valueDate <- validDateLiterals
    ref       <- validRefs
  } yield Payment(amount, source, target, valueDate, ref)

  implicit val arbitraryPayment: Arbitrary[Payment] = Arbitrary(validPayments)

  /** assume some valid payment amount to sample from validAmounts */
  val someAmount = validAmounts.sample.get

  /** some date literal, sampled from valid date literals */
  val someDateLiteral = validDateLiterals.sample.get

  /** assume some valid source IBAN sample from validIBANs */
  val someSourceIBAN = validIBANs.sample.get

  /** assume some valid target IBAN sample from validIBANs other than some source IBAN */
  val someTargetIBAN = validIBANs.retryUntil(_ != someSourceIBAN).sample.get

  /** some reference, sampled from valid references */
  val someRef = validRefs.sample.get

}
