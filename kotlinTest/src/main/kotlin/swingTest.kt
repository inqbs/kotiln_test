
import java.awt.BorderLayout
import java.awt.Canvas
import java.awt.Graphics
import javax.swing.JFrame
import kotlin.random.Random

fun main() {
	JLineFrame()
}

class MCanvas : Canvas(){
	override fun paint(g: Graphics?) {
		repeat((1..6000).count()) {
			g?.drawLine(it,500-it, Random.nextInt(50)*it,Random.nextInt(it, 6000))
			Thread.sleep(50)
		}
	}
}


class JLineFrame: JFrame("JLine"){
	init {
		setBounds(400,360,500,500)
		layout = BorderLayout()
		defaultCloseOperation = JFrame.EXIT_ON_CLOSE
		add(MCanvas(), BorderLayout.CENTER)
		isVisible = true
	}
}