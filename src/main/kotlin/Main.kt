import kotlin.system.exitProcess

class Main {
    companion object {
        @JvmStatic fun main(args: Array<String>) {
            val client = Client(8080)

            println("Press ENTER to close connection.")
            System.`in`.read()

            client.close()
            exitProcess(0)
        }
    }
}