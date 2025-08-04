// Coluna.java
package dao.model;

import java.util.ArrayList;
import java.util.List;

public class Coluna {
    private int id; // novo campo
    private String nome;
    private int ordem;
    private TipoColuna tipo;
    private int boardId;  // campo para guardar o ID do board, essencial para DAO
    private List<Card> cards;

    public Coluna(String nome, int ordem, TipoColuna tipo) {
        this.nome = nome;
        this.ordem = ordem;
        this.tipo = tipo;
        this.cards = new ArrayList<>();
    }

    // getters e setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public int getOrdem() { return ordem; }
    public void setOrdem(int ordem) { this.ordem = ordem; }

    public TipoColuna getTipo() { return tipo; }
    public void setTipo(TipoColuna tipo) { this.tipo = tipo; }

    public int getBoardId() { return boardId; }
    public void setBoardId(int boardId) { this.boardId = boardId; }

    public List<Card> getCards() { return cards; }
    public void adicionarCard(Card card) { cards.add(card); }
    public void removerCard(Card card) { cards.remove(card); }
}
