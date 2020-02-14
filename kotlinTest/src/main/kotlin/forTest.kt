fun main(){
	val mutableList = mutableListOf(1, 2, 3)
	val list: List<Int> = mutableList
	
	mutableList.add(9)
	
	println(mutableList)
	println(list)
}