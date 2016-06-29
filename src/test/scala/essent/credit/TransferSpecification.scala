package essent.credit

import org.scalacheck.Gen
import org.scalatest.PropSpec
import org.scalatest.prop.PropertyChecks
import org.scalatest.Matchers._
import org.scalatest.prop.Tables.Table

class TransferSpecification extends PropSpec with PropertyChecks {

  import TransferSpecification.Assumptions._
  import Transfer._

  property("transfer should be constructable with valid parameters") {
    forAll (validValues, validIBANs, validIBANs, validValueDates) {
      (value: Amount, source: IBAN, target: IBAN, valueDate: Date) => {
        whenever(source != target) {
          val transfer = Transfer(value, source, target, valueDate, "reference")
          transfer.value should be > zeroValue
          transfer.source should not be transfer.target
          isValidValueDate(transfer.valueDate) should be (true)
        }
      }
    }
  }

  property("value should be non-zero, positive") {
    forAll (invalidValues) { (value: Amount) =>
      an[IllegalArgumentException] should be thrownBy {
        Transfer(value, validSourceIBAN, validTargetIBAN, validValueDate, "reference")
      }
    }
  }

  property("source and target should not be equal") {
    forAll (validIBANs) { (target: IBAN) =>
      an[IllegalArgumentException] should be thrownBy {
        val source = target
        Transfer(validValue, source, target, "0000-01-01", "reference")
      }
    }
  }

  property("value date must be represented in `YYYY-MM-DD` format") {
    forAll (invalidValueDates) { (valueDate: Date) =>
      an[IllegalArgumentException] should be thrownBy {
        Transfer(unitValue, validSourceIBAN, validTargetIBAN, valueDate, "reference")
      }
    }
  }
}

object TransferSpecification {

  object Assumptions {

    /** assume zero transfer value as an amount of 0.00 euro */
    val zeroValue: Amount = 0

    /** assume unit transfer value as an amount of 0.01 euro */
    val unitValue: Amount = 1

    /** assume valid transfer values in the range of 0,01 to 1.000.000,00 euro */
    val validValues = Gen.choose[Amount](unitValue, 100000000)

    /** assume a valid transfer value sample from validValues */
    val validValue  = validValues.sample.get

    /** assume typically known invalid transfer values to be: */
    val invalidValues = Table[Amount]("value", 0, -1, Long.MinValue)

    /** assume transfers between bank accounts located in either the BE, NL or DE */
    val bankAccountCountries: Seq[IBAN] = Array("BE", "NL", "DE")

    // TODO generate valid IBANs instead of the IBAN's country code.
    val validIBANs      = Gen.oneOf(bankAccountCountries)

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
