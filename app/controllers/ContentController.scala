package controllers

import java.util.UUID

import cats.instances.future._
import com.aaabramov.encoded.core.entity.SecureRequest
import com.aaabramov.encoded.core.persistence.SecretsRepository
import com.aaabramov.encoded.core.util.json.AnyAsJson
import javax.inject._
import views.{html => Render}

import scala.concurrent.ExecutionContext
import scala.language.implicitConversions
import scala.util.Try

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class ContentController @Inject()(
                                   repo: SecretsRepository
                                 )(implicit ec: ExecutionContext)
  extends RestController {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def generate() = Action.async(parse.json[SecureRequest]) { implicit request =>
    repo
      .insert(request.body)
      .map(Created(_))
  }

  def list(uuid: Option[String]) = Action.async { implicit request =>

    val filter = SecureFilter(uuid.flatMap(s => Try(UUID.fromString(s)).toOption))

    repo
      .list(Some(filter))
      .map(_.asJson)
      .map(Ok(_))
  }

  def view(uuid: UUID) = Action.async { implicit request =>
    for {
      found <- repo.findOne(uuid)
      updated <- repo.update(found.uuid)(_.copy(viewed = true))
    } yield {
      if (found.viewed) {
        NotFound(Render.index("Error", "Page not found"))
      } else {
        Ok(Render.index("Secret data", updated.data))
      }
    }
  }

}
