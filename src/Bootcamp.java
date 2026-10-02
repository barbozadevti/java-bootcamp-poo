import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Encapsulamento: o bootcamp guarda seus conteúdos e inscritos em coleções privadas
 * e só entrega visões somente-leitura. Os conteúdos formam uma trilha em ordem.
 */
public class Bootcamp {

    private final String nome;
    private final String descricao;
    private final LocalDate dataInicial;
    private final LocalDate dataFinal;
    private final int vagas;
    private final Set<Conteudo> conteudos = new LinkedHashSet<>();
    private final Set<Dev> devsInscritos = new LinkedHashSet<>();

    public Bootcamp(String nome, String descricao, LocalDate dataInicial, int duracaoEmDias, int vagas) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O bootcamp precisa de um nome.");
        }
        if (dataInicial == null || duracaoEmDias <= 0) {
            throw new IllegalArgumentException("Informe a data inicial e uma duração maior que zero.");
        }
        if (vagas <= 0) {
            throw new IllegalArgumentException("O bootcamp precisa de ao menos uma vaga.");
        }
        this.nome = nome.trim();
        this.descricao = descricao == null ? "" : descricao.trim();
        this.dataInicial = dataInicial;
        this.dataFinal = dataInicial.plusDays(duracaoEmDias);
        this.vagas = vagas;
    }

    public Bootcamp adicionar(Conteudo conteudo) {
        if (!conteudos.add(conteudo)) {
            throw new IllegalStateException("O conteúdo \"" + conteudo.getTitulo() + "\" já está no bootcamp.");
        }
        return this;
    }

    /** Chamado por {@link Dev#inscrever(Bootcamp, LocalDate)}, que é quem valida a regra de inscrição. */
    void receber(Dev dev) {
        devsInscritos.add(dev);
    }

    public boolean encerrado(LocalDate hoje) {
        return hoje.isAfter(dataFinal);
    }

    public boolean temVaga() {
        return devsInscritos.size() < vagas;
    }

    public int calcularXpTotal() {
        return conteudos.stream().mapToInt(Conteudo::calcularXp).sum();
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public LocalDate getDataInicial() {
        return dataInicial;
    }

    public LocalDate getDataFinal() {
        return dataFinal;
    }

    public int getVagas() {
        return vagas;
    }

    public Set<Conteudo> getConteudos() {
        return Collections.unmodifiableSet(conteudos);
    }

    public Set<Dev> getDevsInscritos() {
        return Collections.unmodifiableSet(devsInscritos);
    }
}
