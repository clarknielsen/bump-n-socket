import io.socket.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import kotlinx.coroutines.*

class Client(port: Int) {
    val socket = SocketIO("http://localhost:${port}")
    var loggedIn = false
    var inGame = false

    init {
        socket.connect(object : IOCallback {
            override fun onMessage(json: JSONObject, ack: IOAcknowledge) {
                try {
                    println("Server said:" + json.toString(2))
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onMessage(data: String, ack: IOAcknowledge) {
                println("Server said: $data")
            }

            override fun onError(socketIOException: SocketIOException) {
                socketIOException.printStackTrace()
            }

            override fun onDisconnect() {
                println("Connection terminated.")
            }

            override fun onConnect() {
                println("Connection established")
            }

            override fun on(event: String, ack: IOAcknowledge?, vararg args: Any) {
                if (event == "update") {
                    val data = JSONArray(args).getJSONObject(0)

                    when (data.getString(("status"))) {
                        "menu" -> {
                            if (!loggedIn) {
                                loggedIn = true

                                socket.emit("update", JSONObject("""{
                                    "status":"menu",
                                    "data":[{"robot":${(1..6).random()},"color":${(1..7).random()},"controller":"4","online":false}]
                                }""".trimIndent()))
                            }
                        }
                        "setup" -> {
                            val subdata = data.get("data")

                            if (subdata !is String && data.getJSONObject("data").has("blocks")) {
                                inGame = true
                            }
                        }
                        "game" -> {
                            if (inGame) {
                                val hostPlayers = data.getJSONArray("data")

                                if (hostPlayers.getJSONObject(1).getInt("dead") == 0) {
                                    val guestPlayers = JSONArray()
                                    guestPlayers.put(hostPlayers[1])

                                    GlobalScope.async {
                                        dragBehind(guestPlayers.toString())
                                    }
                                }
                            }
                        }
                    }
                }
            }
        })
    }

    private suspend fun dragBehind(players: String) {
        delay(500L)

        socket.emit("update", JSONObject("""
            {"status":"game","data":${players}}
        """.trimIndent()))
    }

    fun close() {
        socket.disconnect()
    }
}