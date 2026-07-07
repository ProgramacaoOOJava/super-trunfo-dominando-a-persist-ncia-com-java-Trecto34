/**
* Classe que representa um aluno no sistema Super Trunfo
* Nível 1 - Novato: Desafio de Código - "Cartas Clássicas - JDBC Puro"
*
* Cada aluno representa uma carta com atributos únicos:
* - Nome: valor do campo nome
* - Força: valor do campo entrada (quanto mais recente, mais forte)
* - Raridade: determinada pela primeira letra da matrícula (A-M = Comum, N-Z = Rara)
*/
public class Aluno {
    private String matricula;
    private String nome;
    private int entrada;

    // Construtor padrão
    public Aluno() {
    }

    // Construtor com parâmetros
    public Aluno(String matricula, String nome, int entrada) {
        this.matricula = matricula;
        this.nome = nome;
        this.entrada = entrada;
    }

    // Getters e Setters
    public String getMatricula() {
        return this.matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getEntrada() {
        return this.entrada;
    }

    public void setEntrada(int entrada) {
        this.entrada = entrada;
    }

    /**
    * Calcula a força da carta baseada no ano de entrada
    * Quanto mais recente, mais forte
    */
    public int getForca() {
        return getEntrada();
    }

    /**
    * Determina a raridade da carta baseada na primeira letra da matrícula
    * A-M = Comum, N-Z = Rara
    */
    public String getRaridade() {
        char primeiraLetra = Character.toUpperCase(matricula.charAt(0));

        if (primeiraLetra >= 'A' && primeiraLetra <= 'M') {
            return "Comum";
        } else {
            return "Rara";
        }
    }


    /**
    * Exibe a carta formatada no estilo Super Trunfo
    */
    public void exibirCarta() {
        System.out.println("--- SUPER TRUNFO ---");
        System.out.println("Nome: " + this.nome);
        System.out.println("Matrícula: " + this.matricula);
        System.out.println("Força: " + getForca());
        System.out.println("Raridade: " + getRaridade());
        System.out.println("--------------------");
    }

    /**
    * Compara duas cartas em uma batalha
    * A carta com ano de entrada mais recente vence
    */
    public boolean batalhar(Aluno oponente) {
        return this.entrada > oponente.entrada;
    }

    @Override
    public String toString() {
        return this.nome + " (Matricula: " + this.matricula +")";
    }
}
