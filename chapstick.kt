import kotlin.random.Random

import java.util.ArrayList

class Status {

	val mineScore = mutableListOf(1, 1)
	val cpuScore = mutableListOf(1, 1)

	var isMineTurn: Boolean = false

	fun checkResult(): Boolean {
		val result: Boolean = 
			listOf(mineScore[0], mineScore[1], cpuScore[0], cpuScore[1])
				.filter{it>=5}.size > 0
		return result
	}
}

fun main(args: Array<String>) {

	var status = Status()

	printStatus(status)

	do {
		println("""
			* write your commend
			(type target/num target/num)
			ex) A L R, C 2 3

			* type:: A: attack C:change number
			* target(A):: mine L/R and cpu L/R
			* num(C):: mine L/R's Number
		""".trimMargin())
		
		val commend = readLine().toString().trimMargin().split(" ")
		if(commend.size!=3) continue

		val type:String = commend[0]
		val firstCommend:String = commend[1]
		val secondCommend:String = commend[2]

		println("your commend :${type} ${firstCommend} ${secondCommend}")

		status.isMineTurn = true

		status = when (type.toUpperCase()) {
			"A" -> attack(status, firstCommend, secondCommend)
			"C" -> change(status, firstCommend.toInt(), secondCommend.toInt())
			else -> status
		}

		printStatus(status)

		if (status.checkResult()) break

		status.isMineTurn = false
		println("is CPU Turn...")

		status =
			if (4 in status.cpuScore) {
				println("gaurd...")
				change(status,
					(status.cpuScore[0] + status.cpuScore[1]) / 2,
					(status.cpuScore[0] + status.cpuScore[1]) / 2 +
						if ((status.cpuScore[0] + status.cpuScore[1]) % 2 == 1) 1 else 0
					)
			} else attack(status, returnHand(), returnHand())

		printStatus(status)
		
	} while (!status.checkResult())

	println(if(5 in status.mineScore) "you lose..." else "you win")

}

fun attack(status: Status, m: String, c: String): Status {

	val mineSide: Int = if (m.toUpperCase() == "L") 0 else 1
	val attackSide: Int = if (c.toUpperCase() == "L") 0 else 1
	val count: Int = if (status.isMineTurn) status.mineScore[mineSide] else status.cpuScore[mineSide]

	if (status.isMineTurn) status.cpuScore[attackSide] += count
	else status.mineScore[attackSide] += count

	return status
}

fun change(status: Status, l: Int, r: Int): Status {

	val total: Int = 
		if (status.isMineTurn) status.mineScore[0] + status.mineScore[1] 
		else status.cpuScore[0] + status.cpuScore[1];

	if (l in 0..5 && r in 0..5 && total == l + r){
		if (status.isMineTurn) status.mineScore[0] = l else status.cpuScore[0] = l
		if (status.isMineTurn) status.mineScore[1] = r else status.cpuScore[1] = r
	}

	return status
}

fun returnHand(): String = if (Random.nextInt(2) % 2 == 0) "L" else "R"

fun isWrongCommend() {
	println("error!")
}

fun printStatus(status:Status){
	println("""
		turn: ${status.isMineTurn}
		mine: ${status.mineScore[0]} : ${status.mineScore[1]}
		CPU: ${status.cpuScore[0]} : ${status.cpuScore[1]}
	""".trimMargin())
}