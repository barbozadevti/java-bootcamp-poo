/** Herança: um curso vale {@code XP_PADRAO} por hora de carga horária. */
public class Curso extends Conteudo {

    private final int cargaHoraria;

    public Curso(String titulo, String descricao, int cargaHoraria) {
        super(titulo, descricao);
        if (cargaHoraria <= 0) {
            throw new IllegalArgumentException("A carga horária precisa ser maior que zero.");
        }
        this.cargaHoraria = cargaHoraria;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    @Override
    public int calcularXp() {
        return XP_PADRAO * cargaHoraria;
    }

    @Override
    public String explicarXp() {
        return XP_PADRAO + " XP x " + cargaHoraria + " h";
    }
}
