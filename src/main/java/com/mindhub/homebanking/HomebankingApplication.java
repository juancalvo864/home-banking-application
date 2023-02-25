package com.mindhub.homebanking;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import static com.mindhub.homebanking.models.CardColor.*;
import static com.mindhub.homebanking.models.CardType.CREDIT;
import static com.mindhub.homebanking.models.CardType.DEBIT;


@SpringBootApplication
public class HomebankingApplication {
	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}


	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository,CardRepository cardRepository) {
		return (args) -> {


			Client melba = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("melba123"));
			Client roberto = new Client("Roberto", "Gomez", "elchavo@mindhub.com", passwordEncoder.encode("    "));
			Client admin = new Client("admin","admin","admin@mindhub.com", passwordEncoder.encode("admin3439"));
			Account account1 = 	new Account("VIN001",LocalDateTime.now() , 5000.0);
			Account account2 = new Account("VIN002", LocalDateTime.now().plusDays(1), 7500.0);
			Account account3 = new Account("VIN003", LocalDateTime.now(), 4500.0);
			Transaction transaction1 = new Transaction(TransactionType.Debit,-1000,"rent",LocalDateTime.now().minusDays(3));
			Transaction transaction2 = new Transaction(TransactionType.Debit,-2500,"gasoline",LocalDateTime.now().minusDays(10));
			Transaction transaction3 = new Transaction(TransactionType.Credit,50000,"salary",LocalDateTime.now().minusDays(11));
			Transaction transaction4 = new Transaction(TransactionType.Debit,-1000,"supermarket",LocalDateTime.now().minusDays(14));
			Transaction transaction5= new Transaction(TransactionType.Credit,40000,"salary",LocalDateTime.now().minusDays(15));
			Transaction transaction6 = new Transaction(TransactionType.Debit,-1000,"ice cream",LocalDateTime.now().minusDays(20));
			Transaction transaction7 = new Transaction(TransactionType.Debit,-500,"beer",LocalDateTime.now().minusDays(25));
			Transaction transaction8 = new Transaction(TransactionType.Credit,4550,"gasoline",LocalDateTime.now().minusDays(26));
			Transaction transaction9 = new Transaction(TransactionType.Debit,-89.90,"home depot",LocalDateTime.now().minusDays(35));
			Transaction transaction10 = new Transaction(TransactionType.Credit,3245,"wallmart",LocalDateTime.now().minusDays(40));
			Transaction transaction11 = new Transaction(TransactionType.Debit,-123.54,"parts car",LocalDateTime.now().minusDays(42));
			Loan mortgage = new Loan("Mortgage", 500000, List.of(6,12,24,48));
			Loan personal = new Loan("Personal",100000,List.of(6,12,24));
			Loan automotive = new Loan("Automotive", 500000, List.of(6,12,24,36));
			ClientLoan clientLoan1 = new ClientLoan(400000, (byte) 60) ;
			ClientLoan clientLoan2 = new ClientLoan(50000, (byte) 12) ;
			ClientLoan clientLoan3 = new ClientLoan(100000, (byte) 24);
			ClientLoan clientLoan4 = new ClientLoan(200000, (byte) 36);
			Card cardVisa1 =new Card(melba, DEBIT, GOLD,addCardNumber(),getCvvNumber(), LocalDate.now(),LocalDate.now().plusYears(5)) ;
			Card cardMaster1 = new Card(melba, CREDIT, TITANIUM,addCardNumber(),getCvvNumber(),LocalDate.now(),LocalDate.now().plusYears(5));
			Card cardVisa2 = new Card(roberto, CREDIT, SILVER,addCardNumber(),getCvvNumber(),LocalDate.now(),LocalDate.now().plusYears(5));


			melba.addAccount(account2);
			melba.addAccount(account1);
			roberto.addAccount(account3);
			account1.addTransaction(transaction1);
			account2.addTransaction(transaction2);
			account3.addTransaction(transaction3);
			account3.addTransaction(transaction4);
			account1.addTransaction(transaction5);
			account1.addTransaction(transaction6);
			account1.addTransaction(transaction7);
			account2.addTransaction(transaction8);
			account2.addTransaction(transaction9);
			account2.addTransaction(transaction10);
			account2.addTransaction(transaction11);
			melba.addClientLoan(clientLoan1);
			mortgage.addClientLoan(clientLoan1);
			melba.addClientLoan(clientLoan2);
			personal.addClientLoan(clientLoan2);
			roberto.addClientLoan(clientLoan3);
			personal.addClientLoan(clientLoan3);
			melba.addCard(cardVisa1);
			melba.addCard(cardMaster1);
			roberto.addCard(cardVisa2);

			clientRepository.save(melba);
			clientRepository.save(roberto);
			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);
			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);
			transactionRepository.save(transaction5);
			transactionRepository.save(transaction6);
			transactionRepository.save(transaction7);
			transactionRepository.save(transaction8);
			transactionRepository.save(transaction9);
			transactionRepository.save(transaction10);
			transactionRepository.save(transaction11);
			loanRepository.save(mortgage);
			loanRepository.save(personal);
			loanRepository.save(automotive);
			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);
			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);
			cardRepository.save(cardVisa1);
			cardRepository.save(cardMaster1);
			cardRepository.save(cardVisa2);






		};


	}

	public static String getCardNumber() {
		Random rand = new Random();
		int number = rand.nextInt(10000);
		String stringNumber = String.format("%04d", number);
		return stringNumber;
	}

	public static String addCardNumber(){
		String cardNumber = getCardNumber() + "-" +getCardNumber() + "-"+getCardNumber() + "-"+getCardNumber();
		return cardNumber;
	}

	public static int getCvvNumber() {
		Random rand = new Random();
		int number = rand.nextInt(898)+100;
		return number;

	}

}