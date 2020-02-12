import kotlin.random.Random

const val ATTACK : String = "A"
const val CHANGE : String = "C"
const val LEFT : String = "L"
const val RIGHT : String = "R"

class Status {

	val mineScore = mutableListOf(1, 1)
	val cpuScore = mutableListOf(1, 1)

	var isMineTurn: Boolean = false

	fun checkResult(): Boolean {
		for((idx, it) in mineScore.withIndex()) if(it>=5) mineScore[idx] = 0
		for((idx, it) in cpuScore.withIndex()) if(it>=5) cpuScore[idx] = 0

		printStatus(this)

		return mineScore.filter{it!=0}.size == 0 || cpuScore.filter{it!=0}.size == 0
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

			* type:: ${ATTACK}: attack ${CHANGE}:change number
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
			ATTACK -> attack(status, firstCommend, secondCommend)
			CHANGE -> change(status, 
				firstCommend.toIntOrNull()?:0, 
				secondCommend.toIntOrNull()?:0)
			else -> status
		}

		if (status.checkResult()) break

		status.isMineTurn = false
		println("is CPU Turn...")

		status =
			if ((0 in status.cpuScore && 1 !in status.cpuScore)|| 4 in status.cpuScore) {
				println("CPU: I'm guard...")
				change(status,
					(status.cpuScore[0] + status.cpuScore[1]) / 2,
					(status.cpuScore[0] + status.cpuScore[1]) / 2 +
						if ((status.cpuScore[0] + status.cpuScore[1]) % 2 == 1) 1 else 0
					)
			} else attack(status, returnHand(), returnHand())

	} while (!status.checkResult())

	println(if(status.mineScore.filter{it!=0}.size == 0) "you lose..." else "you win")

}

fun attack(status: Status, m: String, c: String): Status {

	val mineSide: Int = if (m.toUpperCase() == LEFT) 0 else 1
	val attackSide: Int = if (c.toUpperCase() == LEFT) 0 else 1
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

fun returnHand(): String = if (Random.nextInt(2) % 2 == 0) LEFT else RIGHT

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