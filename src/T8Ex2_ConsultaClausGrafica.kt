import redis.clients.jedis.Jedis
import java.awt.Color
import java.awt.EventQueue
import java.awt.FlowLayout
import javax.swing.*

class EstadisticaRD : JFrame() {

    val etTipClau= JLabel("Tipus:")
    val tipClau= JTextField(8)
    val contClau = JTextArea(8,15)
    val con = Jedis("89.36.214.106")
    val listModel = DefaultListModel<String>()
    val llClaus = JList(listModel)

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        setBounds(100, 100, 450, 450)
        setLayout(FlowLayout())


        llClaus.setForeground(Color.blue)
        val scroll = JScrollPane(llClaus)
        llClaus.setVisibleRowCount(20)

        val scroll2 = JScrollPane(contClau)

        add(scroll)
        add(etTipClau)
        add(tipClau)
        add(scroll2)

        setSize(600, 400)
        setVisible(true)

        inicialitzar()

        llClaus.addListSelectionListener{valorCanviat()}
        llClaus.selectedIndex = 0
    }
    fun inicialitzar(){
        con.connect()
        con.auth("ieselcaminas.ad")
        val keys = con.keys("*").toMutableList()
        for (valor in keys.sorted())
            listModel.addElement(valor)
    }

    fun valorCanviat() {
        contClau.text = ""
        val key = llClaus.selectedValue
        tipClau.text = con.type(key)
        when (con.type(key)) {
            "string" -> {
                contClau.append(con.get(key))
            }
            "hash" -> {
                val subcamps = con.hkeys(key)
                for (subcamp in subcamps) {
                    contClau.append("$subcamp --> ${con.hget(key, subcamp)}\n")
                }
            }
            "list" -> {
                val ll = con.lrange(key, 0, -1)
                for (valor in ll) {
                    contClau.append("$valor\n")
                }
            }
            "set" -> {
                val s = con.smembers(key)
                for (valor in s) {
                    contClau.append("$valor\n")
                }
            }
            else -> {
                val s = con.zrangeWithScores(key, 0, -1)
                for (valor in s) {
                    contClau.append("${valor.element} --> ${valor.score}\n")
                }
            }
        }
    }
}

fun main() {
    EventQueue.invokeLater {
        EstadisticaRD().isVisible = true
    }
}