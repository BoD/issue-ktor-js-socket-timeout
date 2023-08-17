import io.ktor.client.HttpClient
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.get
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

class MyTest {
  @Test
  fun test() = runTest {
    val client = HttpClient {
      install(HttpTimeout) {
        this.socketTimeoutMillis = 500
      }
    }
    assertFailsWith<SocketTimeoutException> {
      client.get("http://127.0.0.1:4000")
    }
  }
}

