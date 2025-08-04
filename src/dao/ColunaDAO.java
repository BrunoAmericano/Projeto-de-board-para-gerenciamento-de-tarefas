package dao;

import dao.model.Coluna;
import dao.model.TipoColuna;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ColunaDAO {
    private final Connection conn;

    public ColunaDAO(Connection conn) {
        this.conn = conn;
    }

    public void salvar(Coluna coluna, int boardId) throws SQLException {
        String sql = "INSERT INTO coluna (nome, ordem, tipo, board_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, coluna.getNome());
            stmt.setInt(2, coluna.getOrdem());
            stmt.setString(3, coluna.getTipo().name());
            stmt.setInt(4, boardId);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    coluna.setId(rs.getInt(1));
                    coluna.setBoardId(boardId);  // precisa ter o setBoardId na classe Coluna
                }
            }
        }
    }

    public List<Coluna> listarPorBoard(int boardId) throws SQLException {
        String sql = "SELECT * FROM coluna WHERE board_id = ? ORDER BY ordem";
        List<Coluna> colunas = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, boardId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Coluna coluna = new Coluna(
                        rs.getString("nome"),
                        rs.getInt("ordem"),
                        TipoColuna.valueOf(rs.getString("tipo"))
                    );
                    coluna.setId(rs.getInt("id"));
                    coluna.setBoardId(boardId);
                    colunas.add(coluna);
                }
            }
        }
        return colunas;
    }

    public Coluna buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM coluna WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Coluna coluna = new Coluna(
                        rs.getString("nome"),
                        rs.getInt("ordem"),
                        TipoColuna.valueOf(rs.getString("tipo"))
                    );
                    coluna.setId(rs.getInt("id"));
                    coluna.setBoardId(rs.getInt("board_id"));
                    return coluna;
                }
            }
        }
        return null;
    }

    public Coluna buscarPorOrdem(int boardId, int ordem) throws SQLException {
        String sql = "SELECT * FROM coluna WHERE board_id = ? AND ordem = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, boardId);
            stmt.setInt(2, ordem);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Coluna coluna = new Coluna(
                        rs.getString("nome"),
                        rs.getInt("ordem"),
                        TipoColuna.valueOf(rs.getString("tipo"))
                    );
                    coluna.setId(rs.getInt("id"));
                    coluna.setBoardId(boardId);
                    return coluna;
                }
            }
        }
        return null;
    }
}
