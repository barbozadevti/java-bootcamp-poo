import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Testes sem dependências: cada caso imprime "ok" ou "FALHA", e o código de saída diz se tudo passou. */
public class Testes {

    static final LocalDate HOJE = LocalDate.of(2026, 10, 2);
    static int falhas = 0;

    interface Caso { void rodar() throws Exception; }

    static void teste(String nome, Caso caso) {
        try {
            caso.rodar();
            System.out.println("ok    " + nome);
        } catch (Throwable e) {
            falhas++;
            System.out.println("FALHA " + nome + "\n  " + e);
        }
    }

    static void igual(Object esperado, Object atual) {
        if (!esperado.equals(atual)) throw new AssertionError("esperava " + esperado + " mas veio " + atual);
    }

    static void verdade(boolean condicao, String msg) {
        if (!condicao) throw new AssertionError(msg);
    }

    static void recusa(Class<? extends RuntimeException> tipo, String trecho, Runnable acao) {
        try {
            acao.run();
        } catch (RuntimeException e) {
            verdade(tipo.isInstance(e), "esperava " + tipo.getSimpleName() + " mas veio " + e);
            verdade(String.valueOf(e.getMessage()).contains(trecho), "mensagem \"" + e.getMessage() + "\" não contém \"" + trecho + "\"");
            return;
        }
        throw new AssertionError("não lançou " + tipo.getSimpleName());
    }

    static Bootcamp bootcamp(int vagas) {
        return new Bootcamp("Java", "", HOJE.minusDays(1), 30, vagas)
                .adicionar(new Curso("Java básico", "", 8))
                .adicionar(new Mentoria("Mentoria", "", HOJE))
                .adicionar(new Desafio("Projeto", "", 3));
    }

    public static void main(String[] args) {
        // Abstração e herança
        teste("Conteudo é abstrata e Curso, Mentoria e Desafio herdam dela", () -> {
            verdade(Modifier.isAbstract(Conteudo.class.getModifiers()), "Conteudo deveria ser abstrata");
            for (Class<?> c : List.of(Curso.class, Mentoria.class, Desafio.class)) {
                igual(Conteudo.class, c.getSuperclass());
            }
        });

        // Polimorfismo
        teste("curso vale 10 XP por hora", () -> igual(80, new Curso("A", "", 8).calcularXp()));
        teste("mentoria vale 30 XP", () -> igual(30, new Mentoria("A", "", HOJE).calcularXp()));
        teste("desafio vale 10 XP + 15 por nível", () -> {
            igual(25, new Desafio("A", "", 1).calcularXp());
            igual(55, new Desafio("A", "", 3).calcularXp());
        });
        teste("o total do bootcamp soma o XP de cada tipo", () -> igual(80 + 30 + 55, bootcamp(5).calcularXpTotal()));
        teste("o mesmo código trata qualquer Conteudo", () -> {
            List<Conteudo> todos = new ArrayList<>(bootcamp(5).getConteudos());
            igual(165, todos.stream().mapToInt(Conteudo::calcularXp).sum());
        });

        // Encapsulamento
        teste("as coleções expostas são somente leitura", () -> {
            Bootcamp b = bootcamp(5);
            recusa(UnsupportedOperationException.class, "", () -> b.getConteudos().clear());
            recusa(UnsupportedOperationException.class, "", () -> b.getDevsInscritos().add(new Dev("X")));
            recusa(UnsupportedOperationException.class, "", () -> new Dev("X").getConteudosConcluidos().clear());
        });
        teste("não há setters públicos", () -> {
            for (Class<?> c : List.of(Conteudo.class, Curso.class, Mentoria.class, Desafio.class, Bootcamp.class, Dev.class)) {
                for (var m : c.getDeclaredMethods()) {
                    verdade(!(Modifier.isPublic(m.getModifiers()) && m.getName().startsWith("set")), c.getSimpleName() + "." + m.getName());
                }
            }
        });
        teste("recusa dados inválidos", () -> {
            recusa(IllegalArgumentException.class, "título", () -> new Curso(" ", "", 4));
            recusa(IllegalArgumentException.class, "carga horária", () -> new Curso("A", "", 0));
            recusa(IllegalArgumentException.class, "1 a 3", () -> new Desafio("A", "", 4));
            recusa(IllegalArgumentException.class, "data", () -> new Mentoria("A", "", null));
            recusa(IllegalArgumentException.class, "vaga", () -> new Bootcamp("A", "", HOJE, 10, 0));
            recusa(IllegalArgumentException.class, "nome", () -> new Dev(" "));
        });
        teste("não aceita conteúdo repetido no bootcamp", () ->
                recusa(IllegalStateException.class, "já está", () -> bootcamp(5).adicionar(new Curso("java BÁSICO", "", 2))));

        // Regras do bootcamp
        teste("a inscrição traz os conteúdos na ordem da trilha", () -> {
            Dev dev = new Dev("Ana");
            dev.inscrever(bootcamp(5), HOJE);
            igual(List.of("Java básico", "Mentoria", "Projeto"), dev.getConteudosInscritos().stream().map(Conteudo::getTitulo).toList());
        });
        teste("progredir conclui o próximo conteúdo e soma o XP", () -> {
            Dev dev = new Dev("Ana");
            dev.inscrever(bootcamp(5), HOJE);
            igual("Java básico", dev.progredir(HOJE).orElseThrow().getTitulo());
            igual(80, dev.calcularTotalXp());
            dev.progredir(HOJE);
            dev.progredir(HOJE);
            igual(165, dev.calcularTotalXp());
            verdade(dev.progredir(HOJE).isEmpty(), "não deveria haver mais conteúdo");
        });
        teste("recusa inscrição repetida, sem vaga e em bootcamp encerrado", () -> {
            Bootcamp b = bootcamp(1);
            Dev ana = new Dev("Ana");
            ana.inscrever(b, HOJE);
            recusa(IllegalStateException.class, "já está inscrito", () -> ana.inscrever(b, HOJE));
            recusa(IllegalStateException.class, "não tem mais vagas", () -> new Dev("Bia").inscrever(b, HOJE));
            recusa(IllegalStateException.class, "encerrado", () -> new Dev("Caio").inscrever(bootcamp(5), HOJE.plusDays(60)));
        });
        teste("a mentoria só pode ser concluída a partir da sua data", () -> {
            Bootcamp b = new Bootcamp("B", "", HOJE, 30, 5).adicionar(new Mentoria("Futura", "", HOJE.plusDays(3)));
            Dev dev = new Dev("Ana");
            dev.inscrever(b, HOJE);
            recusa(IllegalStateException.class, "só acontece", () -> dev.progredir(HOJE));
            igual(0, dev.calcularTotalXp());
            dev.progredir(HOJE.plusDays(3));
            igual(30, dev.calcularTotalXp());
        });
        teste("o dev pode estar em mais de um bootcamp", () -> {
            Dev dev = new Dev("Ana");
            dev.inscrever(bootcamp(5), HOJE);
            dev.inscrever(new Bootcamp("Outro", "", HOJE, 30, 5).adicionar(new Curso("SQL", "", 4)), HOJE);
            igual(4, dev.getConteudosInscritos().size());
        });

        System.out.println();
        if (falhas == 0) {
            System.out.println("Todos os testes passaram.");
        } else {
            System.out.println(falhas + " teste(s) falharam.");
            System.exit(1);
        }
    }
}
