import redis.clients.jedis.Jedis
import java.lang.NumberFormatException

fun main() {
    // Sentencies per a la conexio en redis
    val con = Jedis("89.36.214.106")
    con.connect()
    con.auth("ieselcaminas.ad")

    var contador = 1
    // Agarrem totes les claus y les mostrem
    val keys = con.keys("*")
    for (key in keys) {
        println("${contador}.- $key (${con.type(key)})")
        contador++
    }
    while (contador < 0 || contador > keys.size) {
        try {
            println("Introdueix un numero (0 per a eixir)")
            contador = readLine()!!.toInt()
        } catch (ex: NumberFormatException) {
            println("Error: no has introducido un numero")
        }
    }
    // Bucle principal mentre el contador no sea 0
    while (contador != 0) {
        // Agafem la key que ha seleccionat el usuari
        val key = keys.elementAt(contador -1)
        // Imprirem tots els valors de la clau, tenin en conter per a ferlo el tipus
        when (con.type(key)) {
            "string" -> {
                println("$key: ${con.get(key)}")
            }
            "hash" -> {
                println(key)
                val subcamps = con.hkeys(key)
                for (subcamp in subcamps) {
                    println("\t$subcamp --> ${con.hget(key, subcamp)}")
                }
            }
            "list" -> {
                println(key)
                val ll = con.lrange(key, 0, -1)
                for (valor in ll) {
                    println("\t$valor")
                }
            }
            "set" -> {
                println(key)
                val s = con.smembers(key)
                for (valor in s) {
                    println("\t$valor")
                }
            }
            else -> {
                println(key)
                val s = con.zrangeWithScores(key, 0, -1)
                for (valor in s) {
                    println("\t${valor.element} --> ${valor.score}")
                }
            }
        }
        contador = -20
        // Demanem un nou numero al usuari
        while (contador < 0 || contador >= keys.size) {
            try {
                println("Introdueix un numero (0 per a eixir)")
                contador = readLine()!!.toInt()
            } catch (ex: NumberFormatException) {
                println("Error: no has introducido un numero")
            }
        }
    }
    // Tanquem la conexio
    con.close()
}

