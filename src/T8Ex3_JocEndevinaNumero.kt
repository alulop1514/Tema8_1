import redis.clients.jedis.Jedis
import java.lang.NumberFormatException
import kotlin.math.floor

fun main() {
    val con = Jedis("89.36.214.106")
    con.connect()
    con.auth("ieselcaminas.ad")
    val numeroAdivinar = floor(Math.random() * (100 - 1 + 1) + 1).toInt()
    var numeroUsuario = 0
    val tiempoIni = System.currentTimeMillis().toDouble()
    while (numeroUsuario != numeroAdivinar) {
        val numeroAnterior = numeroUsuario
        do {
            try {
                println("Introduce un numero del 1 al 100")
                numeroUsuario = readLine()!!.toInt()
            } catch (ex: NumberFormatException) {
                println("Error: no has introducido un numero")
            }
        } while (numeroUsuario == numeroAnterior)
        when {
            numeroUsuario > numeroAdivinar -> println("El numero introducido es mayor que el numero a adivinar")
            numeroUsuario < numeroAdivinar -> println("El numero introducido es menor que el numero a adivinar")
        }
    }
    val tiempoFinal = System.currentTimeMillis().toDouble()
    val score = (tiempoFinal - tiempoIni)/1000
    println("Numero correcto, tu puntuacion: $score")
    println("Introduce tu nombre")
    var nombreUsuario = readLine().toString()
    val noms = con.zrange("joc_marques", 0 ,-1)
    var num = 0
    for (nom in noms) {
        if (nom!!.contains(nombreUsuario)) {
            num =
                if (nom[nom.length - 1].isDigit()) {
                    if (nom[nom.length - 1].digitToInt() >= num) {
                        nom[nom.length - 1].digitToInt() + 1
                    } else {
                        num
                    }
                } else {
                    if (num == 0) {
                        1
                    } else {
                        num
                    }
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
