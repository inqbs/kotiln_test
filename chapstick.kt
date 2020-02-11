import kotlin.random.Random

class Status {

    val mineScore = mutableListOf(1, 1)
    val cpuScore = mutableListOf(1, 1)

    var isMineTurn: Boolean = false

    fun checkResult(): Boolean {
		val result: Boolean = (mineScore[0] >= 5 || mineScore[1] >= 5 || cpuScore[0] >= 5 || cpuScore[1] >= 5);
        return result
    }
}

fun main(args: Array<String>) {

    var status = Status()

    do {
        println("""
			* write your commend
			(type target/num target/num)

			* type:: A: attack C:change number
			* target(A):: mine L/R and cpu L/R
			* num(C):: mine L/R's Number
		""")
        val commend = readLine().toString().trimMargin().split(" ")

        println("your commend : ${commend[0]}, ${commend[1]}, ${commend[2]}")

        status.isMineTurn = true

        status = when (commend[0].toUpperCase()) {
            "A" -> attack(status, commend[1], commend[2])
            "C" -> change(status, commend[1].toInt(), commend[2].toInt())
            else -> status
        }

        println("""
		turn: ${status.isMineTurn}
		mine: ${status.mineScore[0]} : ${status.mineScore[1]}
		CPU: ${status.cpuScore[0]} : ${status.cpuScore[1]}
		""".trimMargin())
        if (status.checkResult()) return

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

        println("""
			turn: ${status.isMineTurn}
			mine: ${status.mineScore[0]} : ${status.mineScore[1]}
			CPU: ${status.cpuScore[0]} : ${status.cpuScore[1]}
		""".trimMargin())
    } while (!status.checkResult())
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

fun returnHand(): String {
    return if (Random.nextInt(2) % 2 == 0) "L" else "R"
}

fun isWrongCommend() {
    println("error!")
}
