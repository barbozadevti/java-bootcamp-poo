import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {
        LocalDate hoje = LocalDate.now();

        Curso javaBasico = new Curso("Java básico", "Sintaxe, tipos e estruturas de controle", 8);
        Curso poo = new Curso("Orientação a objetos", "Abstração, encapsulamento, herança e polimorfismo", 6);
        Mentoria mentoria = new Mentoria("Mentoria de Java", "Revisão de código com um mentor", hoje);
        Desafio desafio = new Desafio("Abstraindo um bootcamp", "Modelar o próprio bootcamp com POO", 2);
        Mentoria futura = new Mentoria("Mentoria de carreira", "Portfólio e entrevistas", hoje.plusDays(30));

        Bootcamp bootcamp = new Bootcamp("Bootcamp Java Developer", "Do zero ao primeiro projeto em Java", hoje.minusDays(7), 90, 2);
        bootcamp.adicionar(javaBasico).adicionar(poo).adicionar(mentoria).adicionar(desafio).adicionar(futura);

        System.out.println("== " + bootcamp.getNome() + " (" + bootcamp.calcularXpTotal() + " XP no total) ==");
        for (Conteudo c : bootcamp.getConteudos()) {
            System.out.println("  " + c + " -> " + c.explicarXp());
        }

        Dev camila = new Dev("Camila");
        Dev joao = new Dev("João");
        camila.inscrever(bootcamp, hoje);
        joao.inscrever(bootcamp, hoje);

        System.out.println("\n== Camila progride ==");
        for (int i = 0; i < 4; i++) {
            camila.progredir(hoje).ifPresent(c -> System.out.println("Concluiu: " + c.getTitulo() + " | XP: " + camila.calcularTotalXp()));
        }
        try {
            camila.progredir(hoje);
        } catch (IllegalStateException e) {
            System.out.println("Bloqueado: " + e.getMessage());
        }

        System.out.println("\n== João progride ==");
        joao.progredir(hoje).ifPresent(c -> System.out.println("Concluiu: " + c.getTitulo() + " | XP: " + joao.calcularTotalXp()));

        System.out.println("\n== Regras ==");
        try {
            new Dev("Maria").inscrever(bootcamp, hoje);
        } catch (IllegalStateException e) {
            System.out.println("Inscrição recusada: " + e.getMessage());
        }
    }
}
