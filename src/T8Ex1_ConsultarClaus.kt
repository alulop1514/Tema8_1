import redis.clients.jedis.Jedis
import java.lang.NumberFormatException

fun main() {
    val con = Jedis("89.36.214.106")
    con.connect()
    con.auth("ieselcaminas.ad")

    var contador = 1
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
    while (contador != 0) {
        val key = keys.elementAt(contador -1)
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
        while (contador < 0 || contador >= keys.size) {
            try {
                println("Introdueix un numero (0 per a eixir)")
                contador = readLine()!!.toInt()
            } catch (ex: NumberFormatException) {
                println("Error: no has introducido un numero")
            }
        }
    }
    con.close()
}

