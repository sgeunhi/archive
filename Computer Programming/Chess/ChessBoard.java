import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.border.*;

//======================================================Don't modify below===============================================================//
enum PieceType {king, queen, bishop, knight, rook, pawn, none}

enum PlayerColor {black, white, none}

//Name: Kwon Min Gyu
//StudentID#: 2016-18160
public class ChessBoard {
    private final JPanel gui = new JPanel(new BorderLayout(3, 3));
    private JPanel chessBoard;
    private JButton[][] chessBoardSquares = new JButton[8][8];
    private Piece[][] chessBoardStatus = new Piece[8][8];
    private ImageIcon[] pieceImage_b = new ImageIcon[7];
    private ImageIcon[] pieceImage_w = new ImageIcon[7];
    private JLabel message = new JLabel("Enter Reset to Start");

    ChessBoard() {
        initPieceImages();
        initBoardStatus();
        initializeGui();
    }

    public final void initBoardStatus() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) chessBoardStatus[j][i] = new Piece();
        }
    }

    public final void initPieceImages() {
        pieceImage_b[0] = new ImageIcon(new ImageIcon("./img/king_b.png").getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH));
        pieceImage_b[1] = new ImageIcon(new ImageIcon("./img/queen_b.png").getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH));
        pieceImage_b[2] = new ImageIcon(new ImageIcon("./img/bishop_b.png").getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH));
        pieceImage_b[3] = new ImageIcon(new ImageIcon("./img/knight_b.png").getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH));
        pieceImage_b[4] = new ImageIcon(new ImageIcon("./img/rook_b.png").getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH));
        pieceImage_b[5] = new ImageIcon(new ImageIcon("./img/pawn_b.png").getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH));
        pieceImage_b[6] = new ImageIcon(new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));

        pieceImage_w[0] = new ImageIcon(new ImageIcon("./img/king_w.png").getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH));
        pieceImage_w[1] = new ImageIcon(new ImageIcon("./img/queen_w.png").getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH));
        pieceImage_w[2] = new ImageIcon(new ImageIcon("./img/bishop_w.png").getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH));
        pieceImage_w[3] = new ImageIcon(new ImageIcon("./img/knight_w.png").getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH));
        pieceImage_w[4] = new ImageIcon(new ImageIcon("./img/rook_w.png").getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH));
        pieceImage_w[5] = new ImageIcon(new ImageIcon("./img/pawn_w.png").getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH));
        pieceImage_w[6] = new ImageIcon(new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
    }

    public ImageIcon getImageIcon(Piece piece) {
        if (piece.color.equals(PlayerColor.black)) {
            if (piece.type.equals(PieceType.king)) return pieceImage_b[0];
            else if (piece.type.equals(PieceType.queen)) return pieceImage_b[1];
            else if (piece.type.equals(PieceType.bishop)) return pieceImage_b[2];
            else if (piece.type.equals(PieceType.knight)) return pieceImage_b[3];
            else if (piece.type.equals(PieceType.rook)) return pieceImage_b[4];
            else if (piece.type.equals(PieceType.pawn)) return pieceImage_b[5];
            else return pieceImage_b[6];
        } else if (piece.color.equals(PlayerColor.white)) {
            if (piece.type.equals(PieceType.king)) return pieceImage_w[0];
            else if (piece.type.equals(PieceType.queen)) return pieceImage_w[1];
            else if (piece.type.equals(PieceType.bishop)) return pieceImage_w[2];
            else if (piece.type.equals(PieceType.knight)) return pieceImage_w[3];
            else if (piece.type.equals(PieceType.rook)) return pieceImage_w[4];
            else if (piece.type.equals(PieceType.pawn)) return pieceImage_w[5];
            else return pieceImage_w[6];
        } else return pieceImage_w[6];
    }

    public final void initializeGui() {
        gui.setBorder(new EmptyBorder(5, 5, 5, 5));
        JToolBar tools = new JToolBar();
        tools.setFloatable(false);
        gui.add(tools, BorderLayout.PAGE_START);
        JButton startButton = new JButton("Reset");
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                initiateBoard();
            }
        });

        tools.add(startButton);
        tools.addSeparator();
        tools.add(message);

        chessBoard = new JPanel(new GridLayout(0, 8));
        chessBoard.setBorder(new LineBorder(Color.BLACK));
        gui.add(chessBoard);
        ImageIcon defaultIcon = new ImageIcon(new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
        Insets buttonMargin = new Insets(0, 0, 0, 0);
        for (int i = 0; i < chessBoardSquares.length; i++) {
            for (int j = 0; j < chessBoardSquares[i].length; j++) {
                JButton b = new JButton();
                b.addActionListener(new ButtonListener(i, j));
                b.setMargin(buttonMargin);
                b.setIcon(defaultIcon);
                if ((j % 2 == 1 && i % 2 == 1) || (j % 2 == 0 && i % 2 == 0)) b.setBackground(Color.WHITE);
                else b.setBackground(Color.gray);
                b.setOpaque(true);
                b.setBorderPainted(false);
                chessBoardSquares[j][i] = b;
            }
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) chessBoard.add(chessBoardSquares[j][i]);

        }
    }

    public final JComponent getGui() {
        return gui;
    }

    public static void main(String[] args) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                ChessBoard cb = new ChessBoard();
                JFrame f = new JFrame("Chess");
                f.add(cb.getGui());
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                f.setLocationByPlatform(true);
                f.setResizable(false);
                f.pack();
                f.setMinimumSize(f.getSize());
                f.setVisible(true);
            }
        };
        SwingUtilities.invokeLater(r);
    }

    //================================Utilize these functions========================================//

    class Piece {
        PlayerColor color;
        PieceType type;

        Piece() {
            color = PlayerColor.none;
            type = PieceType.none;
        }

        Piece(PlayerColor color, PieceType type) {
            this.color = color;
            this.type = type;
        }
    }

    public void setIcon(int x, int y, Piece piece) {
        chessBoardSquares[y][x].setIcon(getImageIcon(piece));
        chessBoardStatus[y][x] = piece;
    }

    public Piece getIcon(int x, int y) {
        return chessBoardStatus[y][x];
    }

    public void markPosition(int x, int y) {
        chessBoardSquares[y][x].setBackground(Color.pink);
    }

    public void unmarkPosition(int x, int y) {
        if ((y % 2 == 1 && x % 2 == 1) || (y % 2 == 0 && x % 2 == 0))
            chessBoardSquares[y][x].setBackground(Color.WHITE);
        else chessBoardSquares[y][x].setBackground(Color.gray);
    }

    public void setStatus(String inpt) {
        message.setText(inpt);
    }

    public void initiateBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) setIcon(i, j, new Piece());
        }
        setIcon(0, 0, new Piece(PlayerColor.black, PieceType.rook));
        setIcon(0, 1, new Piece(PlayerColor.black, PieceType.knight));
        setIcon(0, 2, new Piece(PlayerColor.black, PieceType.bishop));
        setIcon(0, 3, new Piece(PlayerColor.black, PieceType.queen));
        setIcon(0, 4, new Piece(PlayerColor.black, PieceType.king));
        setIcon(0, 5, new Piece(PlayerColor.black, PieceType.bishop));
        setIcon(0, 6, new Piece(PlayerColor.black, PieceType.knight));
        setIcon(0, 7, new Piece(PlayerColor.black, PieceType.rook));
        for (int i = 0; i < 8; i++) {
            setIcon(1, i, new Piece(PlayerColor.black, PieceType.pawn));
            setIcon(6, i, new Piece(PlayerColor.white, PieceType.pawn));
        }
        setIcon(7, 0, new Piece(PlayerColor.white, PieceType.rook));
        setIcon(7, 1, new Piece(PlayerColor.white, PieceType.knight));
        setIcon(7, 2, new Piece(PlayerColor.white, PieceType.bishop));
        setIcon(7, 3, new Piece(PlayerColor.white, PieceType.queen));
        setIcon(7, 4, new Piece(PlayerColor.white, PieceType.king));
        setIcon(7, 5, new Piece(PlayerColor.white, PieceType.bishop));
        setIcon(7, 6, new Piece(PlayerColor.white, PieceType.knight));
        setIcon(7, 7, new Piece(PlayerColor.white, PieceType.rook));
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) unmarkPosition(i, j);
        }
        onInitiateBoard();
    }
//======================================================Don't modify above==============================================================//	


    //======================================================Implement below=================================================================//
    enum MagicType {MARK, CHECK, CHECKMATE};
    private int selX, selY;
    private boolean blackTurn, end;
    int preX, preY;

    class ButtonListener implements ActionListener {
        int x;
        int y;

        ButtonListener(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void actionPerformed(ActionEvent e) {
            if (end) {
                return;
            }
            if (chessBoardSquares[y][x].getBackground() == Color.pink) {
                boolean isKing = (getIcon(x, y).type == PieceType.king);
                setIcon(x, y, getIcon(preX, preY));
                Piece blank = new Piece();
                setIcon(preX, preY, blank);
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        unmarkPosition(i, j);
                    }
                }
                if (isKing) {
                    if (blackTurn)
                        setStatus("BLACK WIN!");
                    else
                        setStatus("WHITE WIN!");
                    end = true;
                    return;
                }
                if (check()) {
                    if (checkmate()) {
                        if (blackTurn)
                            setStatus("WHITE's TURN / CHECKMATE");
                        else
                            setStatus("BLACK's TURN / CHECKMATE");
                        end = true;
                        return;
                    } else {
                        if (blackTurn)
                            setStatus("WHITE's TURN / CHECK");
                        else
                            setStatus("BLACK's TURN / CHECK");
                    }
                } else {
                    if (blackTurn)
                        setStatus("WHITE's TURN");
                    else
                        setStatus("BLACK's TURN");
                }
                blackTurn = !blackTurn;
            } else {
                boolean[][] marked = new boolean[8][8];
                preX = x;
                preY = y;
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        unmarkPosition(i, j);
                    }
                }
                if (blackTurn && getIcon(x, y).color == PlayerColor.black || (!blackTurn && getIcon(x, y).color == PlayerColor.white)) {
                    mark(x, y, marked);
                }
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        if (marked[i][j]) {
                            markPosition(i, j);
                        }
                    }
                }
            }
        }

        boolean check() {
            boolean[][] checked = new boolean[8][8];
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if ((blackTurn && (getIcon(i, j).color == PlayerColor.black)) || !blackTurn && (getIcon(i, j).color == PlayerColor.white))
                        mark(i, j, checked);
                }
            }
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (canMove(i, j)) {
                        if (getIcon(i, j).type == PieceType.king && ((blackTurn && (getIcon(i, j).color == PlayerColor.white)) || !blackTurn && (getIcon(i, j).color == PlayerColor.black)) && checked[i][j])
                            return true;
                    }
                }
            }
            return false;
        }

        boolean checkmate() {
            Piece[][] other = new Piece[8][8];
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++)
                    other[i][j] = getIcon(i, j);
            }
            blackTurn = !blackTurn;
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    boolean[][] tMarked = new boolean[8][8];
                    if (canMove(i, j) && ((blackTurn && getIcon(i, j).color == PlayerColor.black)
                            || (!blackTurn && getIcon(i, j).color == PlayerColor.white)))
                        mark(i, j, tMarked);
                    for (int k = 0; k < 8; k++) {
                        for (int l = 0; l < 8; l++) {
                            if (tMarked[k][l]) {
                                setIcon(k, l, getIcon(i, j));
                                Piece blank = new Piece();
                                setIcon(i, j, blank);
                                blackTurn = !blackTurn;
                                if (!check()) {
                                    for (int m = 0; m < 8; m++) {
                                        for (int n = 0; n < 8; n++) {
                                            setIcon(m, n, other[m][n]);
                                        }
                                    }
                                    return false;
                                } else {
                                    blackTurn = !blackTurn;
                                    for (int m = 0; m < 8; m++) {
                                        for (int n = 0; n < 8; n++) {
                                            setIcon(m, n, other[m][n]);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            blackTurn = !blackTurn;
            return true;
        }

        void mark(int x, int y, boolean[][] marked) {
            if (getIcon(x, y).color == PlayerColor.black) {
                if (getIcon(x, y).type == PieceType.pawn) {
                    if (canMove(x + 1, y)) {
                        if (canMove(x + 2, y)) {
                            if (getIcon(x + 1, y).color == PlayerColor.none && getIcon(x + 2, y).color == PlayerColor.none && x == 1) {
                                marked[x + 1][y] = true;
                                marked[x + 2][y] = true;
                            }
                        }
                        if (getIcon(x + 1, y).color == PlayerColor.none) {
                            marked[x + 1][y] = true;
                        }
                    }
                    if (canMove(x + 1, y - 1)) {
                        if (getIcon(x + 1, y - 1).color == PlayerColor.white) {
                            marked[x + 1][y - 1] = true;
                        }
                    }
                    if (canMove(x + 1, y + 1)) {
                        if (getIcon(x + 1, y + 1).color == PlayerColor.white) {
                            marked[x + 1][y + 1] = true;
                        }
                    }
                } else if (getIcon(x, y).type == PieceType.rook) {
                    int upOfx = 0, downOfx = 0, rightOfy = 0, leftOfy = 0;
                    for (int i = 1; i <= x; i++) {
                        if (x == 0 || x - i < 0) break;
                        if (getIcon(x - i, y).color == PlayerColor.none) upOfx++;
                        else if (getIcon(x - i, y).color == PlayerColor.white) {
                            upOfx++;
                            break;
                        } else break;
                    }
                    for (int i = 1; i <= 7 - x; i++) {
                        if (x + i > 7 || x == 7) break;
                        if (getIcon(x + i, y).color == PlayerColor.none) downOfx++;
                        else if (getIcon(x + i, y).color == PlayerColor.white) {
                            downOfx++;
                            break;
                        } else break;
                    }
                    for (int i = 1; i <= y; i++) {
                        if (y - i < 0 || y == 0) break;
                        if (getIcon(x, y - i).color == PlayerColor.none) leftOfy++;
                        else if (getIcon(x, y - i).color == PlayerColor.white) {
                            leftOfy++;
                            break;
                        } else break;
                    }
                    for (int i = 1; i <= 7 - y; i++) {
                        if (y + i > 7 || y == 7) break;
                        if (getIcon(x, y + i).color == PlayerColor.none) rightOfy++;
                        else if (getIcon(x, y + i).color == PlayerColor.white) {
                            rightOfy++;
                            break;
                        } else break;
                    }
                    for (int i = x - upOfx; i <= x + downOfx; i++) {
                        if (upOfx == 0 && downOfx == 0) break;
                        marked[i][y] = true;
                    }
                    for (int i = y - leftOfy; i <= y + rightOfy; i++) {
                        if (leftOfy == 0 && rightOfy == 0) break;
                        marked[x][i] = true;
                    }
                    marked[x][y] = false;
                } else if (getIcon(x, y).type == PieceType.knight) {
                    int digitX[] = {-2, -2, -1, -1, 1, 1, 2, 2};
                    int digitY[] = {-1, 1, -2, 2, -2, 2, -1, 1};
                    for (int i = 0; i < digitX.length; i++) {
                        int newX = x + digitX[i];
                        int newY = y + digitY[i];
                        if (canMove(newX, newY)) {
                            if (getIcon(newX, newY).color == PlayerColor.none) {
                                marked[newX][newY] = true;
                            } else if (getIcon(newX, newY).color == PlayerColor.white) {
                                marked[newX][newY] = true;
                            }
                        }
                    }
                } else if (getIcon(x, y).type == PieceType.bishop) {
                    int leftUp = 0, rightUp = 0, leftDown = 0, rightDown = 0;
                    for (int i = 1; i <= 7; i++) {
                        if (canMove(x - i, y - i)) {
                            if (getIcon(x - i, y - i).color == PlayerColor.none) leftUp++;
                            else if (getIcon(x - i, y - i).color == PlayerColor.white) {
                                leftUp++;
                                break;
                            } else break;
                        } else break;
                    }
                    for (int i = 1; i <= 7; i++) {
                        if (canMove(x - i, y + i)) {
                            if (getIcon(x - i, y + i).color == PlayerColor.none) rightUp++;
                            else if (getIcon(x - i, y + i).color == PlayerColor.white) {
                                rightUp++;
                                break;
                            } else break;
                        } else break;
                    }
                    for (int i = 1; i <= 7; i++) {
                        if (canMove(x + i, y - i)) {
                            if (getIcon(x + i, y - i).color == PlayerColor.none) leftDown++;
                            else if (getIcon(x + i, y - i).color == PlayerColor.white) {
                                leftDown++;
                                break;
                            } else break;
                        } else break;
                    }
                    for (int i = 1; i <= 7; i++) {
                        if (canMove(x + i, y + i)) {
                            if (getIcon(x + i, y + i).color == PlayerColor.none) rightDown++;
                            else if (getIcon(x + i, y + i).color == PlayerColor.white) {
                                rightDown++;
                                break;
                            } else break;
                        } else break;
                    }
                    for (int i = 1; i <= leftUp; i++) {
                        marked[x - i][y - i] = true;
                    }
                    for (int i = 1; i <= rightUp; i++) {
                        marked[x - i][y + i] = true;
                    }
                    for (int i = 1; i <= leftDown; i++) {
                        marked[x + i][y - i] = true;
                    }
                    for (int i = 1; i <= rightDown; i++) {
                        marked[x + i][y + i] = true;
                    }
                } else if (getIcon(x, y).type == PieceType.queen) {
                    int digitX[] = {-1, -1, -1, 0, 0, 1, 1, 1};
                    int digitY[] = {-1, 0, 1, -1, 1, -1, 0, 1};
                    for (int i = 0; i < digitX.length; i++) {
                        int newX = x + digitX[i];
                        int newY = y + digitY[i];
                        if (canMove(newX, newY)) {
                            if (getIcon(newX, newY).color == PlayerColor.none) {
                                marked[newX][newY] = true;
                            } else if (getIcon(newX, newY).color == PlayerColor.white) {
                                marked[newX][newY] = true;
                            }
                        }
                    }
                    int upOfx = 0, downOfx = 0, rightOfy = 0, leftOfy = 0;
                    for (int i = 1; i <= x; i++) {
                        if (x == 0 || x - i < 0) break;
                        if (getIcon(x - i, y).color == PlayerColor.none) upOfx++;
                        else if (getIcon(x - i, y).color == PlayerColor.white) {
                            upOfx++;
                            break;
                        } else break;
                    }
                    for (int i = 1; i <= 7 - x; i++) {
                        if (x + i > 7 || x == 7) break;
                        if (getIcon(x + i, y).color == PlayerColor.none) downOfx++;
                        else if (getIcon(x + i, y).color == PlayerColor.white) {
                            downOfx++;
                            break;
                        } else break;
                    }
                    for (int i = 1; i <= y; i++) {
                        if (y - i < 0 || y == 0) break;
                        if (getIcon(x, y - i).color == PlayerColor.none) leftOfy++;
                        else if (getIcon(x, y - i).color == PlayerColor.white) {
                            leftOfy++;
                            break;
                        } else break;
                    }
                    for (int i = 1; i <= 7 - y; i++) {
                        if (y + i > 7 || y == 7) break;
                        if (getIcon(x, y + i).color == PlayerColor.none) rightOfy++;
                        else if (getIcon(x, y + i).color == PlayerColor.white) {
                            rightOfy++;
                            break;
                        } else break;
                    }
                    for (int i = x - upOfx; i <= x + downOfx; i++) {
                        if (upOfx == 0 && downOfx == 0) break;
                        marked[i][y] = true;
                    }
                    for (int i = y - leftOfy; i <= y + rightOfy; i++) {
                        if (leftOfy == 0 && rightOfy == 0) break;
                        marked[x][i] = true;
                    }
                    marked[x][y] = false;
                    int leftUp = 0, rightUp = 0, leftDown = 0, rightDown = 0;
                    for (int i = 1; i <= 7; i++) {
                        if (canMove(x - i, y - i)) {
                            if (getIcon(x - i, y - i).color == PlayerColor.none) leftUp++;
                            else if (getIcon(x - i, y - i).color == PlayerColor.white) {
                                leftUp++;
                                break;
                            } else break;
                        } else break;
                    }
                    for (int i = 1; i <= 7; i++) {
                        if (canMove(x - i, y + i)) {
                            if (getIcon(x - i, y + i).color == PlayerColor.none) rightUp++;
                            else if (getIcon(x - i, y + i).color == PlayerColor.white) {
                                rightUp++;
                                break;
                            } else break;
                        } else break;
                    }
                    for (int i = 1; i <= 7; i++) {
                        if (canMove(x + i, y - i)) {
                            if (getIcon(x + i, y - i).color == PlayerColor.none) leftDown++;
                            else if (getIcon(x + i, y - i).color == PlayerColor.white) {
                                leftDown++;
                                break;
                            } else break;
                        } else break;
                    }
                    for (int i = 1; i <= 7; i++) {
                        if (canMove(x + i, y + i)) {
                            if (getIcon(x + i, y + i).color == PlayerColor.none) rightDown++;
                            else if (getIcon(x + i, y + i).color == PlayerColor.white) {
                                rightDown++;
                                break;
                            } else break;
                        } else break;
                    }
                    for (int i = 1; i <= leftUp; i++) {
                        marked[x - i][y - i] = true;
                    }
                    for (int i = 1; i <= rightUp; i++) {
                        marked[x - i][y + i] = true;
                    }
                    for (int i = 1; i <= leftDown; i++) {
                        marked[x + i][y - i] = true;
                    }
                    for (int i = 1; i <= rightDown; i++) {
                        marked[x + i][y + i] = true;
                    }
                } else if (getIcon(x, y).type == PieceType.king) {
                    int digitX[] = {-1, -1, -1, 0, 0, 1, 1, 1};
                    int digitY[] = {-1, 0, 1, -1, 1, -1, 0, 1};
                    for (int i = 0; i < digitX.length; i++) {
                        int newX = x + digitX[i];
                        int newY = y + digitY[i];
                        if (canMove(newX, newY)) {
                            if (getIcon(newX, newY).color == PlayerColor.none) {
                                marked[newX][newY] = true;
                            } else if (getIcon(newX, newY).color == PlayerColor.white && getIcon(newX, newY).type != PieceType.king) {
                                marked[newX][newY] = true;
                            }
                        }
                    }
                }
            } else if (getIcon(x, y).color == PlayerColor.white) {
                if (getIcon(x, y).type == PieceType.pawn) {
                    if (canMove(x - 1, y)) {
                        if (canMove(x - 2, y)) {
                            if (getIcon(x - 1, y).color == PlayerColor.none && getIcon(x - 2, y).color == PlayerColor.none && x == 6) {
                                marked[x - 1][y] = true;
                                marked[x - 2][y] = true;
                            }
                        }
                        if (getIcon(x - 1, y).color == PlayerColor.none) {
                            marked[x - 1][y] = true;
                        }
                    }
                    if (canMove(x - 1, y - 1)) {
                        if (getIcon(x - 1, y - 1).color == PlayerColor.black) {
                            marked[x - 1][y - 1] = true;
                        }
                    }
                    if (canMove(x - 1, y + 1)) {
                        if (getIcon(x - 1, y + 1).color == PlayerColor.black) {
                            marked[x - 1][y + 1] = true;
                        }
                    }
                } else if (getIcon(x, y).type == PieceType.rook) {
                    int upOfx = 0, downOfx = 0, rightOfy = 0, leftOfy = 0;
                    for (int i = 1; i <= x; i++) {
                        if (x == 0 || x - i < 0) break;
                        if (getIcon(x - i, y).color == PlayerColor.none) upOfx++;
                        else if (getIcon(x - i, y).color == PlayerColor.black) {
                            upOfx++;
                            break;
                        } else break;
                    }
                    for (int i = 1; i <= 7 - x; i++) {
                        if (x + i > 7 || x == 7) break;
                        if (getIcon(x + i, y).color == PlayerColor.none) downOfx++;
                        else if (getIcon(x + i, y).color == PlayerColor.black) {
                            downOfx++;
                            break;
                        } else break;
                    }
                    for (int i = 1; i <= y; i++) {
                        if (y - i < 0 || y == 0) break;
                        if (getIcon(x, y - i).color == PlayerColor.none) leftOfy++;
                        else if (getIcon(x, y - i).color == PlayerColor.black) {
                            leftOfy++;
                            break;
                        } else break;
                    }
                    for (int i = 1; i <= 7 - y; i++) {
                        if (y + i > 7 || y == 7) break;
                        if (getIcon(x, y + i).color == PlayerColor.none) rightOfy++;
                        else if (getIcon(x, y + i).color == PlayerColor.black) {
                            rightOfy++;
                            break;
                        } else break;
                    }
                    for (int i = x - upOfx; i <= x + downOfx; i++) {
                        if (upOfx == 0 && downOfx == 0) break;
                        marked[i][y] = true;
                    }
                    for (int i = y - leftOfy; i <= y + rightOfy; i++) {
                        if (leftOfy == 0 && rightOfy == 0) break;
                        marked[x][i] = true;
                    }
                    marked[x][y] = false;
                } else if (getIcon(x, y).type == PieceType.knight) {
                    int digitX[] = {-2, -2, -1, -1, 1, 1, 2, 2};
                    int digitY[] = {-1, 1, -2, 2, -2, 2, -1, 1};
                    for (int i = 0; i < digitX.length; i++) {
                        int newX = x + digitX[i];
                        int newY = y + digitY[i];
                        if (canMove(newX, newY)) {
                            if (getIcon(newX, newY).color == PlayerColor.none) {
                                marked[newX][newY] = true;
                            } else if (getIcon(newX, newY).color == PlayerColor.black) {
                                marked[newX][newY] = true;
                            }
                        }
                    }
                } else if (getIcon(x, y).type == PieceType.bishop) {
                    int leftUp = 0, rightUp = 0, leftDown = 0, rightDown = 0;
                    for (int i = 1; i <= 7; i++) {
                        if (canMove(x - i, y - i)) {
                            if (getIcon(x - i, y - i).color == PlayerColor.none) leftUp++;
                            else if (getIcon(x - i, y - i).color == PlayerColor.black) {
                                leftUp++;
                                break;
                            } else break;
                        } else break;
                    }
                    for (int i = 1; i <= 7; i++) {
                        if (canMove(x - i, y + i)) {
                            if (getIcon(x - i, y + i).color == PlayerColor.none) rightUp++;
                            else if (getIcon(x - i, y + i).color == PlayerColor.black) {
                                rightUp++;
                                break;
                            } else break;
                        } else break;
                    }
                    for (int i = 1; i <= 7; i++) {
                        if (canMove(x + i, y - i)) {
                            if (getIcon(x + i, y - i).color == PlayerColor.none) leftDown++;
                            else if (getIcon(x + i, y - i).color == PlayerColor.black) {
                                leftDown++;
                                break;
                            } else break;
                        } else break;
                    }
                    for (int i = 1; i <= 7; i++) {
                        if (canMove(x + i, y + i)) {
                            if (getIcon(x + i, y + i).color == PlayerColor.none) rightDown++;
                            else if (getIcon(x + i, y + i).color == PlayerColor.black) {
                                rightDown++;
                                break;
                            } else break;
                        } else break;
                    }
                    for (int i = 1; i <= leftUp; i++) {
                        marked[x - i][y - i] = true;
                    }
                    for (int i = 1; i <= rightUp; i++) {
                        marked[x - i][y + i] = true;
                    }
                    for (int i = 1; i <= leftDown; i++) {
                        marked[x + i][y - i] = true;
                    }
                    for (int i = 1; i <= rightDown; i++) {
                        marked[x + i][y + i] = true;
                    }
                } else if (getIcon(x, y).type == PieceType.queen) {
                    int digitX[] = {-1, -1, -1, 0, 0, 1, 1, 1};
                    int digitY[] = {-1, 0, 1, -1, 1, -1, 0, 1};
                    for (int i = 0; i < digitX.length; i++) {
                        int newX = x + digitX[i];
                        int newY = y + digitY[i];
                        if (canMove(newX, newY)) {
                            if (getIcon(newX, newY).color == PlayerColor.none) {
                                marked[newX][newY] = true;
                            } else if (getIcon(newX, newY).color == PlayerColor.black) {
                                marked[newX][newY] = true;
                            }
                        }
                    }
                    int upOfx = 0, downOfx = 0, rightOfy = 0, leftOfy = 0;
                    for (int i = 1; i <= x; i++) {
                        if (x == 0 || x - i < 0) break;
                        if (getIcon(x - i, y).color == PlayerColor.none) upOfx++;
                        else if (getIcon(x - i, y).color == PlayerColor.black) {
                            upOfx++;
                            break;
                        } else break;
                    }
                    for (int i = 1; i <= 7 - x; i++) {
                        if (x + i > 7 || x == 7) break;
                        if (getIcon(x + i, y).color == PlayerColor.none) downOfx++;
                        else if (getIcon(x + i, y).color == PlayerColor.black) {
                            downOfx++;
                            break;
                        } else break;
                    }
                    for (int i = 1; i <= y; i++) {
                        if (y - i < 0 || y == 0) break;
                        if (getIcon(x, y - i).color == PlayerColor.none) leftOfy++;
                        else if (getIcon(x, y - i).color == PlayerColor.black) {
                            leftOfy++;
                            break;
                        } else break;
                    }
                    for (int i = 1; i <= 7 - y; i++) {
                        if (y + i > 7 || y == 7) break;
                        if (getIcon(x, y + i).color == PlayerColor.none) rightOfy++;
                        else if (getIcon(x, y + i).color == PlayerColor.black) {
                            rightOfy++;
                            break;
                        } else break;
                    }
                    for (int i = x - upOfx; i <= x + downOfx; i++) {
                        if (upOfx == 0 && downOfx == 0) break;
                        marked[i][y] = true;
                    }
                    for (int i = y - leftOfy; i <= y + rightOfy; i++) {
                        if (leftOfy == 0 && rightOfy == 0) break;
                        marked[x][i] = true;
                    }
                    marked[x][y] = false;
                    int leftUp = 0, rightUp = 0, leftDown = 0, rightDown = 0;
                    for (int i = 1; i <= 7; i++) {
                        if (canMove(x - i, y - i)) {
                            if (getIcon(x - i, y - i).color == PlayerColor.none) leftUp++;
                            else if (getIcon(x - i, y - i).color == PlayerColor.black) {
                                leftUp++;
                                break;
                            } else break;
                        } else break;
                    }
                    for (int i = 1; i <= 7; i++) {
                        if (canMove(x - i, y + i)) {
                            if (getIcon(x - i, y + i).color == PlayerColor.none) rightUp++;
                            else if (getIcon(x - i, y + i).color == PlayerColor.black) {
                                rightUp++;
                                break;
                            } else break;
                        } else break;
                    }
                    for (int i = 1; i <= 7; i++) {
                        if (canMove(x + i, y - i)) {
                            if (getIcon(x + i, y - i).color == PlayerColor.none) leftDown++;
                            else if (getIcon(x + i, y - i).color == PlayerColor.black) {
                                leftDown++;
                                break;
                            } else break;
                        } else break;
                    }
                    for (int i = 1; i <= 7; i++) {
                        if (canMove(x + i, y + i)) {
                            if (getIcon(x + i, y + i).color == PlayerColor.none) rightDown++;
                            else if (getIcon(x + i, y + i).color == PlayerColor.black) {
                                rightDown++;
                                break;
                            } else break;
                        } else break;
                    }
                    for (int i = 1; i <= leftUp; i++) {
                        marked[x - i][y - i] = true;
                    }
                    for (int i = 1; i <= rightUp; i++) {
                        marked[x - i][y + i] = true;
                    }
                    for (int i = 1; i <= leftDown; i++) {
                        marked[x + i][y - i] = true;
                    }
                    for (int i = 1; i <= rightDown; i++) {
                        marked[x + i][y + i] = true;
                    }
                } else if (getIcon(x, y).type == PieceType.king) {
                    int digitX[] = {-1, -1, -1, 0, 0, 1, 1, 1};
                    int digitY[] = {-1, 0, 1, -1, 1, -1, 0, 1};
                    for (int i = 0; i < digitX.length; i++) {
                        int newX = x + digitX[i];
                        int newY = y + digitY[i];
                        if (canMove(newX, newY)) {
                            if (getIcon(newX, newY).color == PlayerColor.none) {
                                marked[newX][newY] = true;
                            } else if (getIcon(newX, newY).color == PlayerColor.black && getIcon(newX, newY).type != PieceType.king) {
                                marked[newX][newY] = true;
                            }
                        }
                    }
                }
            }
        }

        boolean canMove(int x, int y) {
            if (x >= 0 && x < 8 && y >= 0 && y < 8)
                return true;
            else
                return false;
        }
    }

    void onInitiateBoard() {
        blackTurn = true;
        end = false;
        setStatus("BLACK's TURN");
    }
}