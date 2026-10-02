import java.util.Objects;

/**
 * Abstração: tudo o que um bootcamp oferece (curso, mentoria, desafio) é um conteúdo.
 * Cada tipo sabe calcular o próprio XP, e quem usa só enxerga {@code Conteudo}.
 */
public abstract class Conteudo {

    /** XP-base de qualquer conteúdo concluído. */
    protected static final int XP_PADRAO = 10;

    private final String titulo;
    private final String descricao;

    protected Conteudo(String titulo, String descricao) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("O conteúdo precisa de um título.");
        }
        this.titulo = titulo.trim();
        this.descricao = descricao == null ? "" : descricao.trim();
    }

    /** Polimorfismo: cada subclasse tem a sua regra de XP. */
    public abstract int calcularXp();

    /** Explica de onde vem o XP, para exibir ao lado do número. */
    public abstract String explicarXp();

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Conteudo outro && getClass() == outro.getClass() && titulo.equalsIgnoreCase(outro.titulo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass(), titulo.toLowerCase());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " \"" + titulo + "\" (" + calcularXp() + " XP)";
    }
}
