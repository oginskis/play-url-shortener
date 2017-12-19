package scala.functions

class Encoder(alphabet: String) extends (Long => String) {

  val Base = alphabet.size

  override def apply(number: Long) = {
    def encode(current: Long): List[Int] = {
      if (current == 0) Nil
      else (current % Base).toInt :: encode(current / Base)
    }
    encode(number).reverse
      .map(current => alphabet.charAt(current)).mkString
  }
}



