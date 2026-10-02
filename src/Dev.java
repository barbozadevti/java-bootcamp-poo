import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Quem faz o bootcamp. Ao se inscrever, recebe os conteúdos da trilha;
 * ao progredir, conclui o próximo conteúdo na ordem e acumula o XP dele.
 */
public class Dev {

    private final String nome;
    private final Set<Conteudo> conteudosInscritos = new LinkedHashSet<>();
    private final Set<Conteudo> conteudosConcluidos = new LinkedHashSet<>();

    public Dev(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O dev precisa de um nome.");
        }
        this.nome = nome.trim();
    }

    public void inscrever(Bootcamp bootcamp, LocalDate hoje) {
        if (bootcamp.encerrado(hoje)) {
            throw new IllegalStateException("O bootcamp " + bootcamp.getNome() + " já foi encerrado.");
        }
        if (bootcamp.getDevsInscritos().contains(this)) {
            throw new IllegalStateException(nome + " já está inscrito no bootcamp " + bootcamp.getNome() + ".");
        }
        if (!bootcamp.temVaga()) {
            throw new IllegalStateException("O bootcamp " + bootcamp.getNome() + " não tem mais vagas.");
        }
        conteudosInscritos.addAll(bootcamp.getConteudos());
        bootcamp.receber(this);
    }

    /** Conclui o próximo conteúdo pendente da trilha. Devolve o conteúdo concluído, se havia algum. */
    public Optional<Conteudo> progredir(LocalDate hoje) {
        Optional<Conteudo> proximo = conteudosInscritos.stream().findFirst();
        if (proximo.isEmpty()) {
            return Optional.empty();
        }
        Conteudo conteudo = proximo.get();
        if (conteudo instanceof Mentoria mentoria && !mentoria.jaAconteceu(hoje)) {
            throw new IllegalStateException("A mentoria \"" + conteudo.getTitulo() + "\" só acontece em " + mentoria.getData() + ".");
        }
        conteudosInscritos.remove(conteudo);
        conteudosConcluidos.add(conteudo);
        return Optional.of(conteudo);
    }

    /** Soma o XP de tudo o que foi concluído; cada conteúdo usa a sua própria regra (polimorfismo). */
    public int calcularTotalXp() {
        return conteudosConcluidos.stream().mapToInt(Conteudo::calcularXp).sum();
    }

    public String getNome() {
        return nome;
    }

    public Set<Conteudo> getConteudosInscritos() {
        return Collections.unmodifiableSet(conteudosInscritos);
    }

    public Set<Conteudo> getConteudosConcluidos() {
        return Collections.unmodifiableSet(conteudosConcluidos);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Dev outro && nome.equalsIgnoreCase(outro.nome);
    }

    @Override
    public int hashCode() {
        return nome.toLowerCase().hashCode();
    }
}
