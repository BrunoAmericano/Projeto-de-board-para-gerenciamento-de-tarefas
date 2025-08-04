package dao;

import dao.model.HistoricoMovimentacao;

import java.sql.*;
import java.time.LocalDateTime;

public class HistoricoMovimentacaoDAO {
    private Connection conn;

    public HistoricoMovimentacaoDAO(Connection conn) {
        this.conn = conn;
    }

    public HistoricoMovimentacao buscarUltimoPorCard(int cardId) throws SQLException {
        String sql = "SELECT * FROM historico_movimentacao WHERE card_id = ? ORDER BY data_entrada DESC LIMIT 1";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cardId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    HistoricoMovimentacao hist = new HistoricoMovimentacao(
                        rs.getInt("card_id"),
                        (Integer) rs.getObject("coluna_origem_id"),
                        (Integer) rs.getObject("coluna_destino_id"),
                        rs.getTimestamp("data_entrada").toLocalDateTime(),
                        rs.getTimestamp("data_saida") != null ? rs.getTimestamp("data_saida").toLocalDateTime() : null,
                        rs.getString("motivo_bloqueio"),
                        rs.getString("motivo_desbloqueio")
                    );
                    hist.setId(rs.getInt("id"));
                    return hist;
                }
            }
        }
        return null;
    }

    public void atualizarDataSaida(int id, LocalDateTime dataSaida) throws SQLException {
        String sql = "UPDATE historico_movimentacao SET data_saida = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(dataSaida));
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }

    public void salvar(HistoricoMovimentacao hist) throws SQLException {
        String sql = "INSERT INTO historico_movimentacao (card_id, coluna_origem_id, coluna_destino_id, data_entrada, data_saida, motivo_bloqueio, motivo_desbloqueio) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, hist.getCardId());
            if (hist.getColunaOrigemId() != null) {
                stmt.setInt(2, hist.getColunaOrigemId());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            if (hist.getColunaDestinoId() != null) {
                stmt.setInt(3, hist.getColunaDestinoId());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            stmt.setTimestamp(4, Timestamp.valueOf(hist.getDataEntrada()));
            if (hist.getDataSaida() != null) {
                stmt.setTimestamp(5, Timestamp.valueOf(hist.getDataSaida()));
            } else {
                stmt.setNull(5, Types.TIMESTAMP);
            }
            stmt.setString(6, hist.getMotivoBloqueio());
            stmt.setString(7, hist.getMotivoDesbloqueio());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    hist.setId(rs.getInt(1));
                }
            }
        }
    }

    public void atualizarMotivos(HistoricoMovimentacao hist) throws SQLException {
        String sql = "UPDATE historico_movimentacao SET motivo_bloqueio = ?, motivo_desbloqueio = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, hist.getMotivoBloqueio());
            stmt.setString(2, hist.getMotivoDesbloqueio());
            stmt.setInt(3, hist.getId());
            stmt.executeUpdate();
        }
    }
}
