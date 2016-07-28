package cn.zhikaizhang.bean;


/**
 * 记录极小极大算法过程中的数据
 */
public class MinimaxResult {

	public int mark;
	
	public Move move;
	
	public MinimaxResult(int mark, Move move) {
		this.mark = mark;
		this.move = move;
	}
	
}
