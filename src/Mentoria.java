import java.time.LocalDate;

/** Herança: uma mentoria vale o XP-base mais um adicional fixo, e só pode ser concluída a partir do dia em que acontece. */
public class Mentoria extends Conteudo {

    private static final int XP_ADICIONAL = 20;

    private final LocalDate data;

    public Mentoria(String titulo, String descricao, LocalDate data) {
        super(titulo, descricao);
        if (data == null) {
            throw new IllegalArgumentException("A mentoria precisa de uma data.");
        }
        this.data = data;
    }

    public LocalDate getData() {
        return data;
    }

    public boolean jaAconteceu(LocalDate hoje) {
        return !hoje.isBefore(data);
    }

    @Override
    public int calcularXp() {
        return XP_PADRAO + XP_ADICIONAL;
    }

    @Override
    public String explicarXp() {
        return XP_PADRAO + " XP + " + XP_ADICIONAL + " XP de mentoria";
    }
}
