import org.junit.jupiter.api.Test
import org.junit.After
import org.junit.Assert.assertTrue

class SocketTest {
    companion object {
        private const val PORT = 8080
        val server = MockServer("localhost", PORT)
        val client = Client(PORT)
    }

    @Test
    fun `The client receives a menu update`() {
        Thread.sleep(1000)
        server.sendMenuUpdate()
        Thread.sleep(1000)
        assertTrue(client.loggedIn)
    }

    @Test
    fun `The client receives a setup update`() {
        Thread.sleep(1000)
        server.sendSetupUpdate()
        Thread.sleep(1000)
        assertTrue(client.inGame)
    }

    @After
    fun teardown() {
        client.close()
        server.close()
    }
}