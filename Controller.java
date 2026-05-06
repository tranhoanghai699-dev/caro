package application;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private GridPane gridPane;
    @FXML private Button btnReset;
    @FXML private Button btnExit;
    @FXML private Label lblStatus;
    
    private static final int SIZE = 9;
    private Button[][] buttons = new Button[SIZE][SIZE];
    private char[][] logicBoard = new char[SIZE][SIZE];
    private boolean playerTurn = true;
    private AI ai = new AI();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        createBoard();
   

       // Xử lý nút chơi lại
         btnReset.setOnAction(e -> {
               createBoard();
               lblStatus.setText("Đang chờ lượt...");
            });

            // Xử lý nút thoát
            btnExit.setOnAction(e -> {
                System.exit(0);
            });
        }
    

    private void createBoard() {
        gridPane.getChildren().clear();

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Button btn = new Button();
                btn.setPrefSize(50, 50);
                btn.setStyle("-fx-font-size: 16;");
                final int r = row, c = col;
                btn.setOnAction(e -> handleMove(r, c));
                gridPane.add(btn, col, row);
                buttons[row][col] = btn;
                logicBoard[row][col] = ' ';
            }
        }

        playerTurn = true;
    }

    private void handleMove(int row, int col) {
        if (!playerTurn || logicBoard[row][col] != ' ') return;

        logicBoard[row][col] = 'X';
        buttons[row][col].setText("X");
        playerTurn = false;

        lblStatus.setText("Đang chờ AI...");

        if (checkGameOver('X')) return;

        int[] move = ai.findBestMove(logicBoard);
        if (move[0] != -1) {
            logicBoard[move[0]][move[1]] = 'O';
            buttons[move[0]][move[1]].setText("O");
        }

        if (!checkGameOver('O')) {
            lblStatus.setText("Lượt của bạn...");
            playerTurn = true;
        }
    }
    private boolean checkGameOver(char symbol) {
        if (checkWin(symbol)) {
            showGameOverAlert((symbol == 'X') ? "Bạn thắng!" : "AI thắng!");
            return true;
        }

        // Kiểm tra hòa
        boolean full = true;
        for (char[] row : logicBoard)
            for (char cell : row)
                if (cell == ' ') full = false;

        if (full) {
            showGameOverAlert("Hòa rồi!");
            return true;
        }
        return false;
    }

    // Kiểm tra thắng 5 ô liên tiếp
    private boolean checkWin(char symbol) {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (checkDirection(r, c, 1, 0, symbol) ||
                    checkDirection(r, c, 0, 1, symbol) ||
                    checkDirection(r, c, 1, 1, symbol) ||
                    checkDirection(r, c, 1, -1, symbol))
                    return true;
            }
        }
        return false;
    }

    private boolean checkDirection(int row, int col, int dRow, int dCol, char symbol) {
        int count = 0;
        for (int i = 0; i < 5; i++) {
            int r = row + dRow * i;
            int c = col + dCol * i;
            if (r < 0 || r >= SIZE || c < 0 || c >= SIZE || logicBoard[r][c] != symbol) return false;
            count++;
        }
        return count == 5;
    }

    private void showGameOverAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION, message + "\nBạn có muốn chơi lại?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Kết thúc trò chơi");
        alert.setHeaderText(null);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                createBoard();
            } else {
                System.exit(0);
            }
        });
    }
}