// HistoricoMovimentacao.java
package dao.model;

import java.time.LocalDateTime;

public class HistoricoMovimentacao {
    private int id;
    private int cardId;
    private Integer colunaOrigemId; // pode ser null
    private Integer colunaDestinoId; // pode ser null
    private LocalDateTime dataEntrada;
    private LocalDateTime dataSaida;
    private String motivoBloqueio;
    private String motivoDesbloqueio;

    public HistoricoMovimentacao(int cardId, Integer colunaOrigemId, Integer colunaDestinoId,
                                 LocalDateTime dataEntrada, LocalDateTime dataSaida,
                                 String motivoBloqueio, String motivoDesbloqueio) {
        this.cardId = cardId;
        this.colunaOrigemId = colunaOrigemId;
        this.colunaDestinoId = colunaDestinoId;
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataSaida;
        this.motivoBloqueio = motivoBloqueio;
        this.motivoDesbloqueio = motivoDesbloqueio;
    }

    // getters e setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCardId() { return cardId; }
    public void setCardId(int cardId) { this.cardId = cardId; }

    public Integer getColunaOrigemId() { return colunaOrigemId; }
    public void setColunaOrigemId(Integer colunaOrigemId) { this.colunaOrigemId = colunaOrigemId; }

    public Integer getColunaDestinoId() { return colunaDestinoId; }
    public void setColunaDestinoId(Integer colunaDestinoId) { this.colunaDestinoId = colunaDestinoId; }

    public LocalDateTime getDataEntrada() { return dataEntrada; }
    public void setDataEntrada(LocalDateTime dataEntrada) { this.dataEntrada = dataEntrada; }

    public LocalDateTime getDataSaida() { return dataSaida; }
    public void setDataSaida(LocalDateTime dataSaida) { this.dataSaida = dataSaida; }

    public String getMotivoBloqueio() { return motivoBloqueio; }
    public void setMotivoBloqueio(String motivoBloqueio) { this.motivoBloqueio = motivoBloqueio; }

    public String getMotivoDesbloqueio() { return motivoDesbloqueio; }
    public void setMotivoDesbloqueio(String motivoDesbloqueio) { this.motivoDesbloqueio = motivoDesbloqueio; }
}
