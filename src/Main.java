import dao.*;
import dao.model.*;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try (Connection conn = Conexao.conectar()) {
            System.out.println("✅ Conexão com o banco estabelecida com sucesso!");

            Scanner sc = new Scanner(System.in);

            BoardDAO boardDAO = new BoardDAO(conn);
            ColunaDAO colunaDAO = new ColunaDAO(conn);
            CardDAO cardDAO = new CardDAO(conn);
            HistoricoMovimentacaoDAO historicoDAO = new HistoricoMovimentacaoDAO(conn);

            while (true) {
                System.out.println("\n==== MENU PRINCIPAL ====");
                System.out.println("1 - Criar novo board");
                System.out.println("2 - Selecionar board");
                System.out.println("3 - Excluir board");
                System.out.println("4 - Sair");
                System.out.print("Escolha: ");
                int opcao = sc.nextInt();
                sc.nextLine();

                switch (opcao) {
                    case 1:
                        System.out.print("Nome do board: ");
                        String nome = sc.nextLine();
                        Board novoBoard = new Board(nome);
                        boardDAO.salvar(novoBoard);

                        colunaDAO.salvar(new Coluna("A Fazer", 1, TipoColuna.INICIAL), novoBoard.getId());
                        colunaDAO.salvar(new Coluna("Em Progresso", 2, TipoColuna.PENDENTE), novoBoard.getId());
                        colunaDAO.salvar(new Coluna("Concluído", 3, TipoColuna.FINAL), novoBoard.getId());

                        System.out.println("✅ Board criado com ID: " + novoBoard.getId());
                        break;

                    case 2:
                        List<Board> boards = boardDAO.listar();
                        if (boards.isEmpty()) {
                            System.out.println("⚠️ Nenhum board disponível.");
                            break;
                        }
                        System.out.println("Boards disponíveis:");
                        for (Board b : boards) {
                            System.out.println(b.getId() + " - " + b.getNome());
                        }
                        System.out.print("Digite o ID do board para selecionar: ");
                        int boardId = sc.nextInt();
                        sc.nextLine();

                        Board boardSelecionado = boards.stream()
                                .filter(b -> b.getId() == boardId)
                                .findFirst()
                                .orElse(null);

                        if (boardSelecionado == null) {
                            System.out.println("⚠️ Board não encontrado.");
                            break;
                        }

                        carregarColunasECards(boardSelecionado, colunaDAO, cardDAO);

                        menuBoard(sc, boardSelecionado, cardDAO, colunaDAO, historicoDAO);
                        break;

                    case 3:
                        System.out.print("ID do board a excluir: ");
                        int idExcluir = sc.nextInt();
                        sc.nextLine();
                        boardDAO.excluir(idExcluir);
                        System.out.println("🗑️ Board excluído.");
                        break;

                    case 4:
                        System.out.println("👋 Saindo...");
                        sc.close();
                        return;

                    default:
                        System.out.println("⚠️ Opção inválida.");
                }
            }

        } catch (Exception e) {
            System.out.println("❌ Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void menuBoard(Scanner sc, Board board, CardDAO cardDAO, ColunaDAO colunaDAO, HistoricoMovimentacaoDAO historicoDAO) throws Exception {
        while (true) {
            System.out.println("\n=== Board: " + board.getNome() + " ===");
            System.out.println("1 - Criar card");
            System.out.println("2 - Mover card");
            System.out.println("3 - Bloquear card");
            System.out.println("4 - Desbloquear card");
            System.out.println("5 - Voltar");
            System.out.print("Escolha: ");
            int opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1:
                    criarCard(sc, board, cardDAO, colunaDAO, historicoDAO);
                    break;
                case 2:
                    moverCard(sc, board, cardDAO, colunaDAO, historicoDAO);
                    break;
                case 3:
                    bloquearCard(sc, cardDAO, historicoDAO);
                    break;
                case 4:
                    desbloquearCard(sc, cardDAO, historicoDAO);
                    break;
                case 5:
                    System.out.println("Voltando ao menu principal...");
                    return;
                default:
                    System.out.println("⚠️ Opção inválida.");
            }
        }
    }

    private static void criarCard(Scanner sc, Board board, CardDAO cardDAO, ColunaDAO colunaDAO, HistoricoMovimentacaoDAO historicoDAO) throws Exception {
        System.out.print("Título do card: ");
        String titulo = sc.nextLine();
        System.out.print("Descrição do card: ");
        String descricao = sc.nextLine();

        Card card = new Card(titulo, descricao);

        Coluna colunaInicial = board.getColunas().stream()
                .filter(c -> c.getTipo() == TipoColuna.INICIAL)
                .findFirst()
                .orElse(null);

        if (colunaInicial == null) {
            System.out.println("⚠️ Coluna inicial não encontrada.");
            return;
        }

        cardDAO.salvar(card, colunaInicial.getId());
        colunaInicial.adicionarCard(card);

        HistoricoMovimentacao historico = new HistoricoMovimentacao(
                card.getId(),
                null,
                colunaInicial.getId(),
                LocalDateTime.now(),
                null,
                null,
                null // motivoDesbloqueio
        );
        historicoDAO.salvar(historico);

        System.out.println("✅ Card criado e adicionado à coluna '" + colunaInicial.getNome() + "'");
    }

    private static void moverCard(Scanner sc, Board board, CardDAO cardDAO, ColunaDAO colunaDAO, HistoricoMovimentacaoDAO historicoDAO) throws Exception {
        System.out.print("Título do card para mover: ");
        String titulo = sc.nextLine();

        Card card = null;
        Coluna colunaAtual = null;

        for (Coluna c : board.getColunas()) {
            for (Card cardIter : c.getCards()) {
                if (cardIter.getTitulo().equalsIgnoreCase(titulo)) {
                    card = cardIter;
                    colunaAtual = c;
                    break;
                }
            }
            if (card != null) break;
        }

        if (card == null) {
            System.out.println("⚠️ Card não encontrado.");
            return;
        }

        if (card.isBloqueado()) {
            System.out.println("⚠️ Card está bloqueado. Desbloqueie antes de mover.");
            return;
        }

        int ordemAtual = colunaAtual.getOrdem();
        Coluna colunaDestino = board.getColunas().stream()
                .filter(c -> c.getOrdem() == ordemAtual + 1)
                .findFirst()
                .orElse(null);

        // Se não existe próxima coluna normal, tenta a coluna CANCELAMENTO
        if (colunaDestino == null) {
            colunaDestino = board.getColunas().stream()
                    .filter(c -> c.getTipo() == TipoColuna.CANCELAMENTO)
                    .findFirst()
                    .orElse(null);
        }

        if (colunaDestino == null) {
            System.out.println("⚠️ Próxima coluna não encontrada.");
            return;
        }

        HistoricoMovimentacao ultimoHist = historicoDAO.buscarUltimoPorCard(card.getId());
        if (ultimoHist != null && ultimoHist.getDataSaida() == null) {
            historicoDAO.atualizarDataSaida(ultimoHist.getId(), LocalDateTime.now());
        }

        colunaAtual.removerCard(card);
        colunaDestino.adicionarCard(card);

        cardDAO.atualizarColuna(card.getId(), colunaDestino.getId());

        HistoricoMovimentacao novoHist = new HistoricoMovimentacao(
                card.getId(),
                colunaAtual.getId(),
                colunaDestino.getId(),
                LocalDateTime.now(),
                null,
                null,
                null // motivoDesbloqueio
        );
        historicoDAO.salvar(novoHist);

        System.out.println("✅ Card movido para '" + colunaDestino.getNome() + "'");
    }

    private static void bloquearCard(Scanner sc, CardDAO cardDAO, HistoricoMovimentacaoDAO historicoDAO) throws Exception {
        System.out.print("Título do card para bloquear: ");
        String titulo = sc.nextLine();

        Card card = cardDAO.buscarPorTitulo(titulo);
        if (card == null) {
            System.out.println("⚠️ Card não encontrado.");
            return;
        }
        if (card.isBloqueado()) {
            System.out.println("⚠️ Card já está bloqueado.");
            return;
        }

        System.out.print("Motivo do bloqueio: ");
        String motivo = sc.nextLine();

        card.bloquear(motivo);
        cardDAO.atualizarBloqueio(card.getId(), true, motivo);

        HistoricoMovimentacao hist = historicoDAO.buscarUltimoPorCard(card.getId());
        if (hist != null) {
            hist.setMotivoBloqueio(motivo);
            historicoDAO.atualizarMotivos(hist);
        }

        System.out.println("🔒 Card bloqueado.");
    }

    private static void desbloquearCard(Scanner sc, CardDAO cardDAO, HistoricoMovimentacaoDAO historicoDAO) throws Exception {
        System.out.print("Título do card para desbloquear: ");
        String titulo = sc.nextLine();

        Card card = cardDAO.buscarPorTitulo(titulo);
        if (card == null) {
            System.out.println("⚠️ Card não encontrado.");
            return;
        }
        if (!card.isBloqueado()) {
            System.out.println("⚠️ Card não está bloqueado.");
            return;
        }

        System.out.print("Motivo do desbloqueio: ");
        String motivo = sc.nextLine();

        card.desbloquear(motivo);
        cardDAO.atualizarBloqueio(card.getId(), false, motivo);

        HistoricoMovimentacao hist = historicoDAO.buscarUltimoPorCard(card.getId());
        if (hist != null) {
            hist.setMotivoDesbloqueio(motivo);
            historicoDAO.atualizarMotivos(hist);
        }

        System.out.println("🔓 Card desbloqueado.");
    }

    private static void carregarColunasECards(Board board, ColunaDAO colunaDAO, CardDAO cardDAO) throws Exception {
        List<Coluna> colunas = colunaDAO.listarPorBoard(board.getId());
        board.getColunas().clear();
        board.getColunas().addAll(colunas);

        for (Coluna c : colunas) {
            List<Card> cards = cardDAO.listarPorColuna(c.getId());
            c.getCards().clear();
            c.getCards().addAll(cards);
        }
    }
}
