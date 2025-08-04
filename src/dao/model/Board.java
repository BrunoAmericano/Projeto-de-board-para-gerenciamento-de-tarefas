package dao.model;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private int id;
    private String nome;
    private List<Coluna> colunas;

    // Construtor vazio para uso no DAO
    public Board() {
        this.colunas = new ArrayList<>();
    }

    public Board(String nome) {
        this.nome = nome;
        this.colunas = new ArrayList<>();
    }

    public Board(int id, String nome) {
        this(nome);
        this.id = id;
    }

    // getters e setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public List<Coluna> getColunas() { return colunas; }

    public void adicionarColuna(Coluna coluna) {
        this.colunas.add(coluna);
    }

    public void moverCard(Card card, Coluna origem, Coluna destino) {
        origem.removerCard(card);
        destino.adicionarCard(card);
    }
}
