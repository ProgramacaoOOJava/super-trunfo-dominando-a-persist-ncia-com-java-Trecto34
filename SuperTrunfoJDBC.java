import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
* Sistema Super Trunfo usando JDBC puro
* Nível 1 - Novato: Desafio de Código - "Cartas Clássicas - JDBC Puro"
*
* Funcionalidades:
* - Gerenciamento de cartas (alunos) com CRUD completo
* - Sistema de batalhas entre cartas
* - Interface de console interativa
* - Persistência com Apache Derby
*/
public class SuperTrunfoJDBC {

    // Configurações de conexão com o banco Derby
    private static final String URL = "jdbc:derby:escola;create=true";
    private static final String USUARIO = "";
    private static final String SENHA = "";

    private static Scanner scanner = new Scanner(System.in);
    private static Random random = new Random();

    /**
    * Obtém uma conexão com o banco de dados Derby
    */
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }

    /**
    * Cria a tabela aluno se ela não existir
    */
    public static void criarTabela() {
        String sql = "CREATE TABLE aluno (matricula VARCHAR(50) PRIMARY KEY, nome VARCHAR(100), entrada INT)";

        try (Connection conn = getConnection();
            Statement stmt = conn.createStatement()) {

                stmt.executeUpdate(sql);

            } catch (SQLException e) {
                // Se a tabela já existir, apenas informa
                if (e.getSQLState().equals("X0Y32")) System.out.println("Tabela aluno já existe no banco de dados.");
            }
    }

    /**
    * Insere um aluno (carta) na base de dados usando PreparedStatement
    */
    public static boolean inserirAluno(Aluno aluno) {
        String sql = "INSERT INTO aluno (matricula, nome, entrada) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, aluno.getMatricula());
                ps.setString(2, aluno.getNome());
                ps.setInt(3, aluno.getEntrada());

                int linhasAfetadas = ps.executeUpdate();

                if (linhasAfetadas > 0) {
                    System.out.println("✅ Carta inserida: " + aluno.getNome());
                    return true;
                }

            } catch (SQLException e) {
                System.err.println("❌ Erro ao inserir aluno: " + e.getMessage());
            }

            return false;
    }

    /**
    * Consulta todos os alunos usando Statement e ResultSet
    */
    public static List<Aluno> consultarTodosAlunos() {
        List<Aluno> alunos = new ArrayList<>();
        String sql = "SELECT * FROM aluno ORDER BY nome";

        try (Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    Aluno aluno = new Aluno();
                    aluno.setMatricula(rs.getString("matricula"));
                    aluno.setNome(rs.getString("nome"));
                    aluno.setEntrada(rs.getInt("entrada"));
                    alunos.add(aluno);
                }

            } catch (SQLException e) {
                System.err.println("❌ Erro ao consultar alunos: " + e.getMessage());
            }

            return alunos;
    }

    /**
    * Exclui um aluno usando PreparedStatement
    */
    public static boolean excluirAluno(String matricula) {
        String sql = "DELETE FROM aluno WHERE matricula = ?";

        try (Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, matricula);
                int linhasAfetadas = ps.executeUpdate();

                if (linhasAfetadas > 0) {
                    System.out.println("✅ Carta removida com Sucesso");
                    return true;
                } else {
                    System.out.println("⚠️  Nenhuma carta encontrada com essa matrícula.");
                    return false;
                }

            } catch (SQLException e) {
                System.err.println("❌ Erro ao excluir aluno: " + e.getMessage());
                return false;
            }
    }

    /**
    * Busca um aluno específico por matrícula
    */
    public static Aluno buscarAluno(String matricula) {
        String sql = "SELECT * FROM aluno WHERE matricula = ?";

        try (Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, matricula);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    Aluno aluno = new Aluno();
                    aluno.setMatricula(rs.getString("matricula"));
                    aluno.setNome(rs.getString("nome"));
                    aluno.setEntrada(rs.getInt("entrada"));
                    return aluno;
                }

            } catch (SQLException e) {
                System.err.println("❌ Erro ao buscar aluno: " + e.getMessage());
            }

            return null;
    }

    /**
    * Exibe todas as cartas formatadas
    */
    public static void exibirTodasCartas() {
        List<Aluno> alunos = consultarTodosAlunos();

        if (alunos.isEmpty()) {
            System.out.println("📭 Nenhuma carta encontrada no baralho.");
            return;
        }

        System.out.println("\n🃏 === BARALHO SUPER TRUNFO ===");
        System.out.printf("Total de cartas: %d%n%n", alunos.size());

        for (Aluno aluno : alunos) {
            aluno.exibirCarta();
            System.out.println();
        }
    }

    /**
    * Insere dados de exemplo no sistema
    */
    public static void inserirDadosExemplo() {
        System.out.println("\n🎲 Inserindo cartas de exemplo...");

        Aluno[] exemplos = {
            new Aluno("A2020001", "Ana Silva", 2020),
            new Aluno("B2021002", "Bruno Costa", 2021),
            new Aluno("N2019003", "Natalia Souza", 2019), // Rara! (Começa com N)
            new Aluno("C2022004", "Carlos Dias", 2022),
            new Aluno("Z2023005", "Ziraldo Lima", 2023)   // Rara! (Começa com Z)
        };

        int inseridos = 0;
        for (Aluno aluno : exemplos) {
            if (inserirAluno(aluno)) {
                inseridos++;
            }
        }

        System.out.printf("✅ %d cartas inseridas com sucesso!%n", inseridos);
    }

    /**
    * Implementa a lógica de batalha entre duas cartas
    */
    public static void batalharCartas() {
        List<Aluno> alunos = consultarTodosAlunos();

        if (alunos.size() < 2) {
            System.out.println("⚠️  É necessário ter pelo menos 2 cartas para batalhar!");
            return;
        }

        System.out.println("\n⚔️  === BATALHA SUPER TRUNFO ===");

        // Sorteia duas cartas aleatórias
        Aluno carta1 = alunos.get(random.nextInt(alunos.size()));
        Aluno carta2;
        do {
            carta2 = alunos.get(random.nextInt(alunos.size()));
        } while (carta1.getMatricula().equals(carta2.getMatricula()));

        System.out.println(carta1.getNome() + " VS " + carta2.getNome());

        if (carta1.batalhar(carta2)) {
            System.out.println("Vencedor: " + carta1.getNome());
        } else if (carta2.batalhar(carta1)) {
            System.out.println("Vencedor: " + carta2.getNome());
        } else {
            System.out.println("Empate!");
        }
    }

    /**
    * Menu interativo do sistema
    */
    public static void exibirMenu() {
        System.out.println("\n🃏 === SUPER TRUNFO - MENU PRINCIPAL ===");
        System.out.println("1. Exibir todas as cartas");
        System.out.println("2. Inserir nova carta");
        System.out.println("3. Buscar carta");
        System.out.println("4. Remover carta");
        System.out.println("5. Batalhar cartas");
        System.out.println("6. Inserir dados de exemplo");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    /**
    * Processa a opção escolhida pelo usuário
    */
    public static void processarOpcao(int opcao) {
        switch (opcao) {
            case 1:
            exibirTodasCartas();
            break;

            case 2:
            System.out.println("\n➕ === INSERIR NOVA CARTA ===");
            System.out.print("Matrícula: ");
            String matricula = scanner.nextLine();
            System.out.print("Nome: ");
            String nome = scanner.nextLine();
            System.out.print("Ano de Entrada: ");
            int entrada = Integer.parseInt(scanner.nextLine());

            Aluno novoAluno = new Aluno(matricula, nome, entrada);
            inserirAluno(novoAluno);
            break;

            case 3:
            System.out.println("\n🔍 === BUSCAR CARTA ===");
            System.out.print("Digite a matrícula: ");
            String matriculaBusca = scanner.nextLine();
            Aluno encontrado = buscarAluno(matriculaBusca);

            if (encontrado != null) {
                System.out.println("\n✅ Carta encontrada:");
                encontrado.exibirCarta();
            } else {
                System.out.println("❌ Carta não encontrada!");
            }
            break;

            case 4:
            System.out.println("\n❌ === REMOVER CARTA ===");
            System.out.print("Digite a matrícula da carta a ser removida: ");
            String matriculaRemover = scanner.nextLine();
            excluirAluno(matriculaRemover);
            break;

            case 5:
            batalharCartas();
            break;

            case 6:
            inserirDadosExemplo();
            break;

            case 0:
            System.out.println("Por favor, digite um número válido!");
            opcao = -1;

            default:
            System.out.println("❌ Opção inválida! Tente novamente.");
        }
    }

    /**
    * Método principal que executa o programa
    */
    public static void main(String[] args) {
        System.out.println("🃏 ===================================");
        System.out.println("   SUPER TRUNFO - CARTAS CLÁSSICAS");
        System.out.println("   Módulo 1 - Novato (JDBC Puro)");
        System.out.println("🃏 ===================================");

        // Inicializar banco de dados
        criarTabela();

        int opcao;
        do {
            exibirMenu();
            try {
                opcao = Integer.parseInt(scanner.nextLine());
                processarOpcao(opcao);
            } catch (NumberFormatException e) {
                System.out.println("Por favor, digite um número válido!");
                opcao = -1;
            }

            if (opcao != 0) {
                System.out.println("\nPressione ENTER para continuar...");
                scanner.nextLine();
            }

        } while (opcao != 0);

        scanner.close();
    }
}
