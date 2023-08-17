import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.response.respondBytesWriter
import io.ktor.server.routing.routing
import io.ktor.utils.io.writeStringUtf8
import kotlinx.coroutines.delay

fun main() {
  println("Server running on port 4000")
  embeddedServer(CIO, 4000) { webSocketServer() }.start(wait = true)
}

private fun Application.webSocketServer() {
  routing {
    handle {
      call.response.status(HttpStatusCode.OK)
      call.respondBytesWriter {
        writeStringUtf8("Hello, ")
        flush()
        delay(1000)
        writeStringUtf8("World!")
        flush()
      }
    }
  }
}
