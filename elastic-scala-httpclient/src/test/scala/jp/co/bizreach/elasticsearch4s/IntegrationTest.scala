package jp.co.bizreach.elasticsearch4s

import java.io.File

import org.apache.commons.io.FileUtils
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.node.{NodeBuilder, Node}
import org.scalatest._
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.io._
import IntegrationTest._
import scala.concurrent.ExecutionContext.Implicits.global

class IntegrationTest extends FunSuite with BeforeAndAfter {

  var node: Node = null

  before {
    val settings = ImmutableSettings.settingsBuilder()
      .put("http.enabled", "true")
      .put("http.port", "9200")
      .put("path.data", "elasticsearch-test-data")
    node = NodeBuilder.nodeBuilder().settings(settings).node()

    val client = HttpUtils.createHttpClient()
    HttpUtils.post(client, "http://localhost:9200/my_index",
      Source.fromFile("src/test/resources/schema.json")(Codec("UTF-8")).toString())
    client.close()

    ESClient.init()
    AsyncESClient.init()
  }

  after {
    node.stop()
    FileUtils.forceDelete(new File("elasticsearch-test-data"))

    ESClient.shutdown()
    AsyncESClient.shutdown()
  }

  test("Error response"){
    val client = HttpUtils.createHttpClient()
    intercept[HttpResponseException] {
      // Create existing index to cause HttpResponseException
      HttpUtils.post(client, "http://localhost:9200/my_index",
        Source.fromFile("src/test/resources/schema.json")(Codec("UTF-8")).toString())
    }
    client.close()
  }

  test("Error response in async API"){
    val client = HttpUtils.createHttpClient()
    // Create existing index to cause HttpResponseException
    val f = HttpUtils.postAsync(client, "http://localhost:9200/my_index",
      Source.fromFile("src/test/resources/schema.json")(Codec("UTF-8")).toString())

    intercept[HttpResponseException] {
      Await.result(f, Duration.Inf)
    }
    client.close()
  }

  test("Sync client"){
    val config = ESConfig("my_index", "my_type")
    val client = ESClient("http://localhost:9200")

    // Register 100 docs
    (1 to 100).foreach { num =>
      client.insert(config, Map(
        "subject" -> s"[$num]Hello World!",
        "content" -> "This is a first registration test!"
      ))
    }
    client.refresh(config)

    // Check doc count
    val count1 = client.countAsInt(config){ searcher =>
      searcher.setQuery(matchAllQuery)
    }
    assert(count1 == 100)

    // Check doc exists
    val result1 = client.find[Blog](config){ searcher =>
      searcher.setQuery(matchPhraseQuery("subject", "10"))
    }
    assert(result1.get._2.subject == "[10]Hello World!")
    assert(result1.get._2.content == "This is a first registration test!")

    // Delete 1 doc
    client.deleteByQuery(config){ searcher =>
      searcher.setQuery(matchPhraseQuery("subject", "10"))
    }
    client.refresh(config)

    // Check doc doesn't exist
    val result2 = client.find[Blog](config){ searcher =>
      searcher.setQuery(matchPhraseQuery("subject", "10"))
    }
    assert(result2.isEmpty)

    // Check doc count
    val count2 = client.countAsInt(config){ searcher =>
      searcher.setQuery(matchAllQuery)
    }
    assert(count2 == 99)

    // Scroll search
    val sum = client.scroll[Blog, Int](config){ searcher =>
      searcher.setQuery(matchPhraseQuery("subject", "Hello"))
    }{ case (id, blog) =>
      assert(blog.content == "This is a first registration test!")
      1
    }.sum
    assert(sum == 99)
  }

  test("Async client"){
    val config = ESConfig("my_index", "my_type")
    val client = AsyncESClient("http://localhost:9200")

    val seqf = (1 to 100).map { num =>
      client.insertAsync(config, Map(
        "subject" -> s"[$num]Hello World!",
        "content" -> "This is a first registration test!"
      ))
    }

    val f = for {
      _ <- Future.sequence(seqf)
      _ <- client.refreshAsync(config)
      count <- client.countAsIntAsync(config) { searcher =>
        searcher.setQuery(matchAllQuery)
      }
    } yield count

    val count = Await.result(f, Duration.Inf)
    assert(count == 100)
  }

}

object IntegrationTest {
  case class Blog(subject: String, content: String)
}