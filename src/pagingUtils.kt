import kotlin.test.assertEquals

//  For Paging

const val DEFAULT_PAGE : Int = 1

//  Run for test
fun main(){
	
	val pagingDto: PagingUtils = PagingUtils.calculate(
		nowPage=1,
		listSize=35,
		pageSize = 10,
		rangePage = 10
	).apply{
		this.calculate()
	}

	assertEquals(4, pagingDto.end)
	
	println("begin : ${pagingDto.begin}, end: ${pagingDto.end}")
}
//  Main logic
class PagingUtils{

	var nowPage: Int = DEFAULT_PAGE
		set(value){
			field = if(value > 0) value else DEFAULT_PAGE
		}

	var listSize: Int = 0	//	All post count
	var pageSize: Int = 10	//	post per 1page
	var rangePage: Int = 0	//	page count for pager

	//	nowPage is first/last Page?
	val firstPage: Boolean
		get() = listSize == 0 || nowPage == DEFAULT_PAGE
	val lastPage: Boolean
		get() = listSize == 0 || nowPage == last

	//	link to prev next on pager
	val prev: Int
		get() = if(begin==DEFAULT_PAGE) 1 else begin-1
	val next: Int
		get() = if(end==last) last else end+1

	var begin: Int = 0	//	first pageNum on pager
	var end: Int = 0	//	last pageNum on pager
	var last: Int = 0	//	last pageNum on this boardList

	var startItemNum: Int = 0
	var endItemNum: Int = 0

	fun calculate(): PagingUtils{

		println("""
			* listSize : $listSize
			* pageSize : $pageSize
		""")

		//  calculate last pageNum
		last = listSize / pageSize + if(listSize % pageSize == 0) 0 else 1

		println("""
			* last: $last
		""")

		//	if page count for pager > last pageNum, it will be last pageNum
		if(rangePage>last) rangePage = last

		println("""
			* rangePage : $rangePage
		""")

		//	calculate first page on pager
		begin = nowPage - rangePage/2
		begin = when{
			(begin < 1) -> DEFAULT_PAGE
			(nowPage > last - rangePage/2) -> last - rangePage + 1
			else -> begin
		}

		//	calculate last page on pager
		end = nowPage + rangePage/2
		end = when{
			listSize==0 -> DEFAULT_PAGE
			(last in 1..end) -> last
			(end < rangePage) -> rangePage
			else -> end
		}

		//	for on SQL
		startItemNum = (nowPage - 1) * pageSize
		endItemNum = startItemNum + pageSize

		return this
	}

	companion object{
		fun calculate(nowPage:Int?, listSize:Int?, pageSize:Int?, rangePage:Int?):PagingUtils{
			return PagingUtils().apply{
				this.nowPage = nowPage ?: 1
				this.listSize = listSize ?: 0
				this.pageSize = pageSize ?: 10
				this.rangePage = rangePage ?: 0
			}
		}
		
	}
	
}