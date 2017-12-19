package controllers

import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject

import forms.UrlShorteningForm
import play.api.data.Form
import play.api.data.Forms.{mapping, nonEmptyText}
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.functions.Encoder
import scala.functions.Decoder
import scala.collection.concurrent.TrieMap
import HomeController._
import play.api.Configuration

class HomeController @Inject()(cc: ControllerComponents, configuration: Configuration)
                               extends AbstractController(cc) with I18nSupport {

  val sequence = new AtomicLong
  val urls = TrieMap[Long,String]()
  val encoder = new Encoder(configuration.get[String](Alphabet))
  val decoder = new Decoder(configuration.get[String](Alphabet))

  def listUrls = Action { implicit request => {
    val sortedPairs = urls.toList.sortBy(_._1).reverse
    Ok(views.html.listUrls(sortedPairs, encoder, HomeController.urlForm))
    }
  }

  def goHome = Action { implicit request => {
      Redirect(routes.HomeController.listUrls)
    }
  }

  def addUrl = Action { implicit request => {
    urlForm.bindFromRequest.fold(
        formWithErrors => {
          val sortedPairs = urls.toList.sortBy(_._1).reverse
          BadRequest(views.html.listUrls(sortedPairs,encoder,formWithErrors))
        },
        body => {
          urls.put(sequence.addAndGet(1),body.url)
          Redirect(routes.HomeController.listUrls)
        }
      )
    }
  }

  def deleteUrl(id: String) = Action { implicit request => {
      val decodedUrl = decoder(id)
      urls.get(decodedUrl) match {
        case Some(value) => {
          urls.remove(decodedUrl)
          Redirect(routes.HomeController.listUrls)
        }
        case None => NotFound("URL has not been found by the ID given")
      }
    }
  }

  def goto(id: String) = Action { implicit request => {
      urls.get(decoder(id)) match {
        case Some(value) => Redirect(value)
        case None => NotFound("Unknown shortened URL")
      }
    }
  }
}

object HomeController {
  val Alphabet = "alphabet"
  val urlForm = Form(
    mapping(
      "url" -> nonEmptyText.verifying("Must be valid URL", url => url.isEmpty || url
        .matches("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"))
    )(UrlShorteningForm.apply)(UrlShorteningForm.unapply)
  )
}
