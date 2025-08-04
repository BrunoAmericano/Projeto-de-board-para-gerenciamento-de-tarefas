// Card.java
package dao.model;

import java.time.LocalDateTime;

public class Card {
    private int id; // novo campo
    private String titulo;
    private String descricao;
    private LocalDateTime criadoEm;
    private boolean bloqueado;
    private String motivoBloqueio;
    private String motivoDesbloqueio;
    private LocalDateTime dataEntrada;
    private LocalDateTime dataSaida;

    public Card(String titulo, String descricao) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.criadoEm = LocalDateTime.now();
        this.bloqueado = false;
    }

    // getters e setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }

    public boolean isBloqueado() { return bloqueado; }
    public void setBloqueado(boolean bloqueado) { this.bloqueado = bloqueado; }

    public String getMotivoBloqueio() { return motivoBloqueio; }
    public void setMotivoBloqueio(String motivoBloqueio) { this.motivoBloqueio = motivoBloqueio; }

    public String getMotivoDesbloqueio() { return motivoDesbloqueio; }
    public void setMotivoDesbloqueio(String motivoDesbloqueio) { this.motivoDesbloqueio = motivoDesbloqueio; }

    public LocalDateTime getDataEntrada() { return dataEntrada; }
    public void setDataEntrada(LocalDateTime dataEntrada) { this.dataEntrada = dataEntrada; }

    public LocalDateTime getDataSaida() { return dataSaida; }
    public void setDataSaida(LocalDateTime dataSaida) { this.dataSaida = dataSaida; }

    // Métodos para bloquear e desbloquear o card, usando os setters e flags

    public void bloquear(String motivo) {
        setBloqueado(true);
        setMotivoBloqueio(motivo);
        setMotivoDesbloqueio(null); // limpar motivo desbloqueio ao bloquear
    }

    public void desbloquear(String motivo) {
        setBloqueado(false);
        setMotivoDesbloqueio(motivo);
        setMotivoBloqueio(null); // limpar motivo bloqueio ao desbloquear
    }
}
