package application;

public class AI {
    private static final char AI = 'O';
    private static final char PLAYER = 'X';

    public int[] findBestMove(char[][] board) {
        // Chiến thuật đơn giản: ưu tiên đánh trung tâm, hoặc ô trống đầu tiên tìm thấy
        for (int d = 0; d < board.length; d++) {
            int mid = board.length / 2;
            if (board[mid][mid] == ' ') return new int[]{mid, mid}; // Ưu tiên trung tâm

            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board.length; j++) {
                    if (board[i][j] == ' ') return new int[]{i, j};
                }
            }
        }
        return new int[]{-1, -1};
    }
}