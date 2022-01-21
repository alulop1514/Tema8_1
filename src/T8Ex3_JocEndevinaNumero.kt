import redis.clients.jedis.Jedis
import kotlin.math.floor

fun main() {
    val con = Jedis("89.36.214.106")
    con.connect()
    con.auth("ieselcaminas.ad")
    val numeroAdivinar = floor(Math.random() * (100 - 1 + 1) + 1).toInt()
    var numeroUsuario = 0
    val tiempoIni = System.currentTimeMillis().toDouble()
    while (numeroUsuario != numeroAdivinar) {
        println("Introduce un numero del 1 al 100")
        numeroUsuario = readLine()!!.toInt()
        when {
            numeroUsuario > numeroAdivinar -> println("El numero introducido es mayor")
            numeroUsuario < numeroAdivinar -> println("El numero introducido es menor")
        }
    }
    val tiempoFinal = System.currentTimeMillis().toDouble()
    println("Introduce tu nombre")
    var nombreUsuario = readLine().toString()
    val score = (tiempoFinal - tiempoIni)/1000
    val noms = con.zrange("joc_marques", 0 ,-1)
    var num = 0
    for (nom in noms) {
        if (nom!!.contains(nombreUsuario)) {
            num =
                if (nom[nom.length - 1].isDigit()) {
                nom[nom.length - 1].digitToInt() + 1
            } else {
                1
            }
        }
    }
    if (num != 0) {
        nombreUsuario += "_$num"
    }
    con.zadd("joc_marques", score, nombreUsuario)
    val puntuacions = con.zrangeWithScores("joc_marques", 0, 9)
    for (puntuacion in puntuacions) {
        println("${puntuacion.element} ---> ${puntuacion.score}")
    }
}
