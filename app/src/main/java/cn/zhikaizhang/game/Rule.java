package cn.zhikaizhang.game;

import java.util.ArrayList;
import java.util.List;

import cn.zhikaizhang.bean.Move;
import cn.zhikaizhang.bean.Statistic;

/**
 * 规则
 */
public class Rule {

	/**
	 * 该步是否合法
	 */
	public static boolean isLegalMove(byte[][] chessBoard, Move move, byte chessColor) {

		int i, j, dirx, diry, row = move.row, col = move.col;
		if (!isLegal(row, col) || chessBoard[row][col] != Constant.NULL)
			return false;
		for (dirx = -1; dirx < 2; dirx++) {
			for (diry = -1; diry < 2; diry++) {
				if (dirx == 0 && diry == 0)
					continue;
				int x = col + dirx, y = row + diry;
				if (isLegal(y, x) && chessBoard[y][x] == (-chessColor)) {
					for (i = row + diry * 2, j = col + dirx * 2; isLegal(i, j); i += diry, j += dirx) {
						if (chessBoard[i][j] == (-chessColor)) {
							continue;
						} else if (chessBoard[i][j] == chessColor) {
							return true;
						} else {
							break;
						}
					}
				}
			}
		}
		return false;
	}

	public static boolean isLegal(int row, int col) {
		return row >= 0 && row < 8 && col >= 0 && col < 8;
	}

	/**
	 * 使用前务必先确认该步合法
	 */
	public static List<Move> move(byte[][] chessBoard, Move move, byte chessColor) {
		int row = move.row;
		int col = move.col;
		int i, j, temp, m, n, dirx, diry;
		List<Move> moves = new ArrayList<Move>();
		for (dirx = -1; dirx < 2; dirx++) {
			for (diry = -1; diry < 2; diry++) {
				if (dirx == 0 && diry == 0)
					continue;
				temp = 0;
				int x = col + dirx, y = row + diry;
				if (isLegal(y, x) && chessBoard[y][x] == (-chessColor)) {
					temp++;
					for (i = row + diry * 2, j = col + dirx * 2; isLegal(i, j); i += diry, j += dirx) {
						if (chessBoard[i][j] == (-chessColor)) {
							temp++;
							continue;
						} else if (chessBoard[i][j] == chessColor) {
							for (m = row + diry, n = col + dirx; m <= row + temp && m >= row - temp && n <= col + temp
									&& n >= col - temp; m += diry, n += dirx) {
								chessBoard[m][n] = chessColor;
								moves.add(new Move(m, n));
							}
							break;
						} else
							break;
					}
				}
			}
		}
		chessBoard[row][col] = chessColor;
		return moves;
	}

	public static List<Move> getLegalMoves(byte[][] chessBoard, byte chessColor) {
		List<Move> moves = new ArrayList<Move>();
		Move move = null;
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				move = new Move(row, col);
				if (Rule.isLegalMove(chessBoard, move, chessColor)) {
					moves.add(move);
				}
			}
		}
		return moves;
	}

	public static Statistic analyse(byte[][] chessBoard, byte playerColor) {

		int PLAYER = 0;
		int AI = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (chessBoard[i][j] == playerColor)
					PLAYER += 1;
				else if (chessBoard[i][j] == (byte)-playerColor)
					AI += 1;
			}
		}
		return new Statistic(PLAYER, AI);
	}
}
