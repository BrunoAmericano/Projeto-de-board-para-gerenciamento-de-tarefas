package dao;

import dao.model.Card;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CardDAO {
    private Connection conn;

    public CardDAO(Connection conn) {
        this.conn = conn;
    }

    public void salvar(Card card, int colunaId) throws SQLException {
        String sql = "INSERT INTO card (titulo, descricao, bloqueado, motivo_bloqueio, motivo_desbloqueio, data_criacao, coluna_id) VALUES (?, ?, ?, ?, ?, NOW(), ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, card.getTitulo());
            stmt.setString(2, card.getDescricao());
            stmt.setBoolean(3, card.isBloqueado());
            stmt.setString(4, card.getMotivoBloqueio());
            stmt.setString(5, card.getMotivoDesbloqueio());
            stmt.setInt(6, colunaId);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    card.setId(rs.getInt(1));
                }
            }
        }
    }

    public void atualizarColuna(int cardId, int colunaId) throws SQLException {
        String sql = "UPDATE card SET coluna_id = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, colunaId);
            stmt.setInt(2, cardId);
            stmt.executeUpdate();
        }
    }

    public Card buscarPorTitulo(String titulo) throws SQLException {
        String sql = "SELECT * FROM card WHERE titulo = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, titulo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Card card = new Card(rs.getString("titulo"), rs.getString("descricao"));
                    card.setId(rs.getInt("id"));
                    card.setBloqueado(rs.getBoolean("bloqueado"));
                    card.setMotivoBloqueio(rs.getString("motivo_bloqueio"));
                    card.setMotivoDesbloqueio(rs.getString("motivo_desbloqueio"));
                    card.setDataEntrada(rs.getTimestamp("data_entrada") != null ? rs.getTimestamp("data_entrada").toLocalDateTime() : null);
                    card.setDataSaida(rs.getTimestamp("data_saida") != null ? rs.getTimestamp("data_saida").toLocalDateTime() : null);
                    return card;
                }
            }
        }
        return null;
    }

    public void atualizarBloqueio(int cardId, boolean bloqueado, String motivo) throws SQLException {
        String sql;
        if (bloqueado) {
            sql = "UPDATE card SET bloqueado = TRUE, motivo_bloqueio = ? WHERE id = ?";
        } else {
            sql = "UPDATE card SET bloqueado = FALSE, motivo_desbloqueio = ? WHERE id = ?";
        }
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, motivo);
            stmt.setInt(2, cardId);
            stmt.executeUpdate();
        }
    }

    public List<Card> listarPorColuna(int colunaId) throws SQLException {
        List<Card> cards = new ArrayList<>();
        String sql = "SELECT * FROM card WHERE coluna_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, colunaId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Card card = new Card(rs.getString("titulo"), rs.getString("descricao"));
                    card.setId(rs.getInt("id"));
                    card.setBloqueado(rs.getBoolean("bloqueado"));
                    card.setMotivoBloqueio(rs.getString("motivo_bloqueio"));
                    card.setMotivoDesbloqueio(rs.getString("motivo_desbloqueio"));
                    card.setDataEntrada(rs.getTimestamp("data_entrada") != null ? rs.getTimestamp("data_entrada").toLocalDateTime() : null);
                    card.setDataSaida(rs.getTimestamp("data_saida") != null ? rs.getTimestamp("data_saida").toLocalDateTime() : null);
                    cards.add(card);
                }
            }
        }
        return cards;
    }
}
