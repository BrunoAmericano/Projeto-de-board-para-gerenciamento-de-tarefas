package dao;

import dao.model.Board;
import dao.model.Coluna;
import dao.model.TipoColuna;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BoardDAO {
    private Connection conn;

    public BoardDAO(Connection conn) {
        this.conn = conn;
    }

    public void salvar(Board board) throws SQLException {
        String sql = "INSERT INTO board (nome) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, board.getNome());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    board.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public void excluir(int id) throws SQLException {
    try (Connection conn = Conexao.conectar()) {
        // Excluir cards das colunas do board
        String deleteCards = "DELETE FROM card WHERE coluna_id IN (SELECT id FROM coluna WHERE board_id = ?)";
        try (PreparedStatement stmt = conn.prepareStatement(deleteCards)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }

        // Excluir colunas do board
        String deleteColunas = "DELETE FROM coluna WHERE board_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(deleteColunas)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }

        // Excluir o board
        String deleteBoard = "DELETE FROM board WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(deleteBoard)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}



    public List<Board> listar() throws SQLException {
        List<Board> boards = new ArrayList<>();
        String sql = "SELECT * FROM board";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Board board = new Board(rs.getInt("id"), rs.getString("nome"));
                boards.add(board);
            }
        }
        return boards;
    }
}
