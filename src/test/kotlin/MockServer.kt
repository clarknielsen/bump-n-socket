import com.corundumstudio.socketio.Configuration
import com.corundumstudio.socketio.SocketIOServer

class MockServer(host: String, port: Int) {
    private var server : SocketIOServer

    init {
        val config = Configuration()
        config.hostname = host
        config.port = port

        server = SocketIOServer(config)
        server.start()
    }

    fun sendMenuUpdate() {
        server.broadcastOperations.sendEvent("update", object {
            val status = "menu"
            val data = arrayOf<Any>()
        })
    }

    fun sendSetupUpdate() {
        server.broadcastOperations.sendEvent("update", object {
            val status = "setup"
            val data = object {
                val blocks = arrayOf<Any>()
            }
        })
    }

    fun close() {
        server.stop()
    }
}
