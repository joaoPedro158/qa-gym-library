package br.edu.ifrn.qagym.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoanTest {

    // 1. As variáveis estão declaradas aqui com esses nomes:
    private Book book;
    private User user;
    private LocalDate loanDate;

    @BeforeEach
    void configurar() {
        book = new Book("12345", "senho dos anéis", "J.R.R. Tolkien", 1954);
        user = new User("1234", "João");

        loanDate = LocalDate.of(2026, 6, 1);
    }

    @Test
    void diasDeAtraso_QuandoNaoDevolvidoEAntesDaDataLimite_DeveRetornarZero() {

        Loan loan = new Loan(book, user, loanDate);

        // Data atual (10 de Junho) é antes do prazo (15 de Junho)
        LocalDate currentDate = LocalDate.of(2026, 6, 10);

        long diasAtrasados = loan.daysLate(currentDate);

        assertEquals(0L, diasAtrasados, "Não deveria haver dias de atraso antes da data limite.");
    }

    @Test
    void diasDeAtraso_QuandoNaoDevolvidoEDepoisDaDataLimite_DeveRetornarDiasExatos() {
        Loan loan = new Loan(book, user, loanDate);

        // Data atual (20 de Junho) é 5 dias após o prazo (15 de Junho)
        LocalDate currentDate = LocalDate.of(2026, 6, 20);

        long diasAtrasados = loan.daysLate(currentDate);

        assertEquals(5L, diasAtrasados, "Deveria calcular exatamente 5 dias de atraso.");
    }

    @Test
    void diasDeAtraso_QuandoDevolvidoNoPrazo_DeveRetornarZero() {
        Loan loan = new Loan(book, user, loanDate);

        // Devolvido no dia 12 de Junho (dentro do prazo que era dia 15)
        loan.setReturnDate(LocalDate.of(2026, 6, 12));

        LocalDate currentDate = LocalDate.of(2026, 6, 30);

        long diasAtrasados = loan.daysLate(currentDate);

        assertEquals(0L, diasAtrasados, "Livro devolvido no prazo não deve gerar dias de atraso.");
    }

    @Test
    void diasDeAtraso_QuandoDevolvidoComAtraso_DeveRetornarDiasComBaseNaDataDeDevolucao() {
        Loan loan = new Loan(book, user, loanDate);

        // Devolvido com atraso no dia 18 de Junho (3 dias após o dia 15)
        loan.setReturnDate(LocalDate.of(2026, 6, 18));

        LocalDate currentDate = LocalDate.of(2026, 6, 30);

        long diasAtrasados = loan.daysLate(currentDate);

        assertEquals(3L, diasAtrasados, "O cálculo deve ser baseado na data real de entrega, resultando em 3 dias.");
    }
}