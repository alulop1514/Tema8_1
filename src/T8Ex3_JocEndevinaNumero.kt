import redis.clients.jedis.Jedis
import kotlin.math.floor

fun main() {
    val con = Jedis("89.36.214.106")
    con.connect()
    con.auth("ieselcaminas.ad")

    val numeroAdivinar = floor(Math.random() * (100 - 1 + 1) + 1).toInt()
    var numeroUsuario = 0
    val tiempoIni = System.currentTimeMillis()
    while (numeroUsuario != numeroAdivinar) {
        println("Introduce un numero del 1 al 100")
        numeroUsuario = readLine()!!.toInt()
        when {
            numeroUsuario > numeroAdivinar -> println("")
        }
    }
}
