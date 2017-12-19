package scala.functions

import org.scalatest.{FlatSpec, Matchers}

class DecoderAndEncoderTest extends FlatSpec with Matchers {

  val Alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"

  "A number with base 10" should "be correctly encoded into base 62 string" in {
    val encoder = new Encoder(Alphabet)
    encoder(127) should be ("cd")
    encoder(543513414) should be ("KWGPy")
  }

  "A base 62 string" should "be correctly decoded into a number with base 10" in {
    val decoder = new Decoder(Alphabet)
    decoder("cd") should be (127)
    decoder("KWGPy") should be (543513414)
  }

}
