package essent.credit

import org.scalacheck.Gen
import org.scalatest.PropSpec
import org.scalatest.prop.PropertyChecks
import org.scalatest.Matchers._
import org.scalatest.prop.Tables.Table

class PaymentSpecification extends PropSpec with PropertyChecks {

  import PaymentSpecification.Assumptions._
  import Payment._

  property("payment should be constructable with valid parameters") {
    forAll (validAmounts, validIBANs, validIBANs, validValueDates) {
      (amount: Amount, source: IBAN, target: IBAN, valueDate: Date) => {
        whenever(source != target) {
          val payment = Payment(amount, source, target, valueDate, "reference")
          payment.amount should be > zeroAmount
          payment.source should not be payment.target
          isValidDateLiteral(payment.valueDate) should be (true)
        }
      }
    }
  }

  property("amounts must be positive and non-zero") {
    forAll (invalidAmounts) { (amount: Amount) =>
      an[IllegalArgumentException] should be thrownBy {
        Payment(amount, validSourceIBAN, validTargetIBAN, validValueDate, "reference")
      }
    }
  }

  property("source and target may not be equal") {
    forAll (validIBANs) { (target: IBAN) =>
      an[IllegalArgumentException] should be thrownBy {
        val source = target
        Payment(validAmount, source, target, "0000-01-01", "reference")
      }
    }
  }

  property("value date must be represented in `YYYY-MM-DD` format") {
    forAll (invalidValueDates) { (valueDate: Date) =>
      an[IllegalArgumentException] should be thrownBy {
        Payment(unitAmount, validSourceIBAN, validTargetIBAN, valueDate, "reference")
      }
    }
  }
}

object PaymentSpecification {

  object Assumptions {

    /** assume a zero payment amount to equal 0.00 euro */
    val zeroAmount: Amount = 0

    /** assume a unit payment amount to equal 0.01 euro */
    val unitAmount: Amount = 1

    /** assume valid payment amounts in the range of 0.01 to 1,000,000.00 euro */
    val validAmounts = Gen.choose[Amount](unitAmount, 100000000)

    /** assume a valid payment amount to sample from validAmounts */
    val validAmount = validAmounts.sample.get

    /** assume typically known invalid payment amounts to be: */
    val invalidAmounts = Table[Amount]("amount", 0, -1, Long.MinValue)

    /** assume payments between bank accounts located in either BE, NL or DE */
    val bankAccountCountries: Seq[String] = Array("BE", "NL", "DE")

    // TODO generate valid IBANs instead of the IBAN's country code.
    val validIBANs = for {
      countryCode    <- Gen.oneOf(bankAccountCountries)
      sequenceNumber <- Gen.choose[Int](0, 99)
    } yield f"$countryCode%s$sequenceNumber%02d"

    /** assume a valid source IBAN sample from validIBANs */
    val validSourceIBAN = validIBANs.sample.get

    /** assume a valid target IBAN sample from validIBANs */
    val validTargetIBAN = validIBANs.retryUntil(_ != validSourceIBAN).sample.get

    /** assume valid value dates to match ISO-8601's YYYY-MM-DD format */
    val validValueDates: Gen[Date] = for {
      year  <- Gen.choose(0, 9999)
      month <- Gen.choose(1, 12)
      day   <- Gen.choose(1, 31)
    } yield f"$year%04d-$month%02d-$day%02d"

    /** assume a valid value date sample from validValueDates */
    val validValueDate = validValueDates.sample.get

    /** assume invalid value dates to violate ISO-8601's YYY-MM-DD format */
    val invalidValueDates = Table[Date]("valueDate",
      "0000-00-00", "0000-13-01", "0000-01-32", "2016-1-1", "-1601-01", "2016-31-12",
      "02/27/2013", "02/27/13", "27/02/2013", "27/02/13", "20130227", "2013.02.27",
      "27.02.13", "27-02-13", "27.2.13", "2013.II.27", "27/2-13", "2013.158904109",
      "MMXIII-II-XXVII", "MMXIII LVII/CCCLXV", "1330300800", "", "0", "1234567890"
    )
  }
}
