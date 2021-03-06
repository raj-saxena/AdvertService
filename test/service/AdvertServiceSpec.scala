package service

import models.Advert.{Fuel, IsNew, Price, Title}
import models.{Advert, Diesel}
import org.mockito.Matchers.{eq => eqTo}
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import play.api.libs.json.Json
import repositories.AdvertRepository

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}

class AdvertServiceSpec extends Specification with Mockito {

  val mockAdvertRepository = mock[AdvertRepository]

  val advertService = new AdvertService(mockAdvertRepository)

  private val newCarTitle = "New car"
  private val newCarPrice = 1234
  val newCarAdJson = Json.obj(
    Title -> newCarTitle,
    Fuel -> "Diesel",
    Price -> newCarPrice,
    IsNew -> true
  )
  val newCarAd = Advert(None, newCarTitle, Diesel(), newCarPrice, true)
  val ads = List(newCarAdJson)

  val guid = "59438ac04a12f658004a435b"

  "AdvertService" should {
    "find sorted by id" in {
      val sortBy = Advert.Id
      mockAdvertRepository.findSortedBy(eqTo(sortBy))(any[ExecutionContext]) returns Future(ads)

      advertService.findSortedBy(sortBy)

      there was mockAdvertRepository.findSortedBy(eqTo(sortBy))(any[ExecutionContext])
    }

    "create advert" in {
      mockAdvertRepository.save(newCarAd) returns Future(true)

      advertService.save(newCarAd)

      there was mockAdvertRepository.save(newCarAd)
    }

    "update advert" in {
      mockAdvertRepository.update(guid, newCarAd) returns Future(true)

      advertService.update(guid, newCarAd)

      there was mockAdvertRepository.update(guid, newCarAd)
    }

    "delete advert" in {
      mockAdvertRepository.delete(guid) returns Future(true)

      advertService.delete(guid)

      there was mockAdvertRepository.delete(guid)
    }

    "find by id" in {
      mockAdvertRepository.select(guid) returns Future(Option(newCarAdJson))

      advertService.select(guid)

      there was mockAdvertRepository.select(guid)
    }
  }
}
