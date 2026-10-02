/** Evolução do desafio: um projeto prático, com dificuldade de 1 a 3 que multiplica o bônus. */
public class Desafio extends Conteudo {

    private static final int XP_POR_NIVEL = 15;

    private final int dificuldade;

    public Desafio(String titulo, String descricao, int dificuldade) {
        super(titulo, descricao);
        if (dificuldade < 1 || dificuldade > 3) {
            throw new IllegalArgumentException("A dificuldade vai de 1 a 3.");
        }
        this.dificuldade = dificuldade;
    }

    public int getDificuldade() {
        return dificuldade;
    }

    @Override
    public int calcularXp() {
        return XP_PADRAO + XP_POR_NIVEL * dificuldade;
    }

    @Override
    public String explicarXp() {
        return XP_PADRAO + " XP + " + XP_POR_NIVEL + " XP x dificuldade " + dificuldade;
    }
}
