package scala.functions

class Decoder(alphabet: String) extends (String => Long) {

  val Base = alphabet.size

  override def apply(string: String) = {
    def decode(current: Long, encodedPart: String): Long = {
      if (encodedPart.size == 0) current
      else decode(current * Base + alphabet.indexOf(encodedPart.head),encodedPart.tail)
    }
    decode(0,string)
  }
}
