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
import static com.mindhub.homebanking.models.CardAndAccountStatus.ENABLED;
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

			// CLIENTS CREATION
			Client melba = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("melba123"));
			Client roberto = new Client("Roberto", "Gomez", "elchavo@mindhub.com", passwordEncoder.encode("chavo123"));
			Client admin = new Client("admin","admin","admin@mindhub.com", passwordEncoder.encode("admin3439"));
			// ACCOUNTS CREATION
			Account account1 = 	new Account("VIN-001",LocalDateTime.now() , 5000.0,ENABLED, AccountType.SAVING);
			Account account2 = new Account("VIN-002", LocalDateTime.now().plusDays(1), 7000.0,ENABLED, AccountType.CHECKING);
			Account account3 = new Account("VIN-003", LocalDateTime.now(), 45000.0,ENABLED, AccountType.SAVING);
			// TRANSACTIONS ACCOUNT CREATION  1
			account1.setBalance(account1.getBalance() - 1000);
			Transaction transaction1 = new Transaction(TransactionType.DEBIT,-1000.0,"rent",LocalDateTime.now(), account1.getBalance());
			account1.setBalance(account1.getBalance() - 2500);
			Transaction transaction2 = new Transaction(TransactionType.DEBIT,-2500.0,"gasoline",LocalDateTime.now().minusDays(1), account1.getBalance());
			account1.setBalance(account1.getBalance() + 50000);
			Transaction transaction3 = new Transaction(TransactionType.CREDIT,50000.0,"salary",LocalDateTime.now().minusDays(2), account1.getBalance());
			account1.setBalance(account1.getBalance() - 1000);
			Transaction transaction4 = new Transaction(TransactionType.DEBIT,-1000.0,"supermarket",LocalDateTime.now().minusDays(3),account1.getBalance());
			account1.setBalance(account1.getBalance() - 1000);
			Transaction transaction5 = new Transaction(TransactionType.DEBIT,-1000.0,"garage rent",LocalDateTime.now().minusDays(4), account1.getBalance());
			account1.setBalance(account1.getBalance() - 200);
			Transaction transaction6 = new Transaction(TransactionType.DEBIT,-200.0,"gym",LocalDateTime.now().minusDays(6), account1.getBalance());
			account1.setBalance(account1.getBalance() - 100);
			Transaction transaction7 = new Transaction(TransactionType.DEBIT,-100.0,"7eleven",LocalDateTime.now().minusDays(7), account1.getBalance());
			account1.setBalance(account1.getBalance() - 250);
			Transaction transaction8= new Transaction(TransactionType.DEBIT,-250.0,"wallmart",LocalDateTime.now().minusDays(8), account1.getBalance());
			account1.setBalance(account1.getBalance() - 350);
			Transaction transaction9 = new Transaction(TransactionType.DEBIT,-350.0,"homedepot",LocalDateTime.now().minusDays(9), account1.getBalance());
			account1.setBalance(account1.getBalance() - 150);
			Transaction transaction10 = new Transaction(TransactionType.DEBIT,-150.0,"harbor freigth",LocalDateTime.now().minusDays(10), account1.getBalance());

			//TRANSACTION ACCOUNT CREATION 2
			Transaction transaction11= new Transaction(TransactionType.CREDIT,40000.0,"salary",LocalDateTime.now(), account2.getBalance());
			account2.setBalance(account2.getBalance() + 40000);
			Transaction transaction12 = new Transaction(TransactionType.DEBIT,-50.0,"ice cream",LocalDateTime.now().minusDays(1), account2.getBalance());
			account2.setBalance(account2.getBalance() - 50);
			Transaction transaction13 = new Transaction(TransactionType.DEBIT,-60.0,"beer",LocalDateTime.now().minusDays(2), account2.getBalance());
			account2.setBalance(account2.getBalance() - 60);
			Transaction transaction14 = new Transaction(TransactionType.CREDIT,350.0,"plumber service",LocalDateTime.now().minusDays(4), account2.getBalance());
			account2.setBalance(account2.getBalance() + 350);
			Transaction transaction15 = new Transaction(TransactionType.DEBIT,-89.90,"home depot",LocalDateTime.now().minusDays(6), account2.getBalance());
			account2.setBalance(account2.getBalance() - 89.90);
			Transaction transaction16 = new Transaction(TransactionType.DEBIT,-80.0,"dunkyn donuts",LocalDateTime.now().minusDays(7), account2.getBalance());
			account2.setBalance(account2.getBalance() - 80);
			Transaction transaction17 = new Transaction(TransactionType.DEBIT,-90.0,"mc donalds",LocalDateTime.now().minusDays(8), account2.getBalance());
			account2.setBalance(account2.getBalance() - 90);
			Transaction transaction18 = new Transaction(TransactionType.DEBIT,-300.0,"race track",LocalDateTime.now().minusDays(10), account2.getBalance());
			account2.setBalance(account2.getBalance() - 300);
			Transaction transaction19 = new Transaction(TransactionType.DEBIT,-250.0,"gym",LocalDateTime.now().minusDays(11), account2.getBalance());
			account2.setBalance(account2.getBalance() - 250);
			Transaction transaction20 = new Transaction(TransactionType.DEBIT,-100.0,"zumba",LocalDateTime.now().minusDays(13), account2.getBalance());
			account2.setBalance(account2.getBalance() - 100);
			Transaction transaction21 = new Transaction(TransactionType.DEBIT,-350.0,"golf point",LocalDateTime.now().minusDays(14), account2.getBalance());
			account2.setBalance(account2.getBalance() - 350);

			//TRANSACTION ACCOUNT CREATION 3
			Transaction transaction22= new Transaction(TransactionType.CREDIT,3245.0,"furniture service",LocalDateTime.now().minusDays(1), account3.getBalance());
			account3.setBalance(account3.getBalance() + 3245);
			Transaction transaction23 = new Transaction(TransactionType.DEBIT,-123.54,"parts car",LocalDateTime.now().minusDays(3), account3.getBalance());
			account3.setBalance(account3.getBalance() - 123.54);
			//LOANS CREATION
			Loan mortgage = new Loan("Mortgage", 500000, List.of(6,12,24,48), (byte) 10);
			Loan personal = new Loan("Personal",100000,List.of(6,12,24), (byte) 20);
			Loan automotive = new Loan("Automotive", 400000, List.of(6,12,24,36), (byte) 15);
			//CLIENT LOANS CREATION
			ClientLoan clientLoan1 = new ClientLoan(400000, (byte) 60,mortgage) ;
			ClientLoan clientLoan2 = new ClientLoan(50000, (byte) 12,personal) ;
			ClientLoan clientLoan3 = new ClientLoan(100000, (byte) 24,personal);
			ClientLoan clientLoan4 = new ClientLoan(200000, (byte) 36,automotive);
			//CARDS CREATION
			Card cardVisa1 =new Card(melba, DEBIT, GOLD,addCardNumber(),getCvvNumber(), LocalDate.now().minusYears(5),LocalDate.now().minusYears(1) ,ENABLED) ;
			Card cardVisa2 = new Card(melba, CREDIT, TITANIUM,addCardNumber(),getCvvNumber(),LocalDate.now(),LocalDate.now().plusYears(5),ENABLED);
			Card cardVisa3 = new Card(roberto, CREDIT, SILVER,addCardNumber(),getCvvNumber(),LocalDate.now(),LocalDate.now().plusYears(5),ENABLED);


			//ADD CLIENT'S ACCOUNT
			melba.addAccount(account2);
			melba.addAccount(account1);
			roberto.addAccount(account3);
			//ADD CLIENT'S TRANSFERS
			account1.addTransaction(transaction1);
			account1.addTransaction(transaction2);
			account1.addTransaction(transaction3);
			account1.addTransaction(transaction4);
			account1.addTransaction(transaction5);
			account1.addTransaction(transaction6);
			account1.addTransaction(transaction7);
			account1.addTransaction(transaction8);
			account1.addTransaction(transaction9);
			account1.addTransaction(transaction10);
			account1.addTransaction(transaction12);
			account2.addTransaction(transaction13);
			account2.addTransaction(transaction14);
			account2.addTransaction(transaction15);
			account2.addTransaction(transaction16);
			account1.addTransaction(transaction17);
			account2.addTransaction(transaction18);
			account2.addTransaction(transaction19);
			account2.addTransaction(transaction20);
			account2.addTransaction(transaction21);
			account3.addTransaction(transaction22);
			account3.addTransaction(transaction23);

			//ADD CLIENT'S CLIENTLOAN
			melba.addClientLoan(clientLoan1);
			mortgage.addClientLoan(clientLoan1);
			melba.addClientLoan(clientLoan2);
			personal.addClientLoan(clientLoan2);
			roberto.addClientLoan(clientLoan3);
			personal.addClientLoan(clientLoan3);
			roberto.addClientLoan(clientLoan4);
			personal.addClientLoan(clientLoan4);

			//ADD CLIENT'S CARD
			melba.addCard(cardVisa1);
			melba.addCard(cardVisa2);
			roberto.addCard(cardVisa3);

			//SAVE EVERYTHING TO DATABASE
			clientRepository.save(melba);
			clientRepository.save(roberto);
			clientRepository.save(admin);
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
			transactionRepository.save(transaction12);
			transactionRepository.save(transaction13);
			transactionRepository.save(transaction14);
			transactionRepository.save(transaction15);
			transactionRepository.save(transaction16);
			transactionRepository.save(transaction17);
			transactionRepository.save(transaction18);
			transactionRepository.save(transaction19);
			transactionRepository.save(transaction20);
			transactionRepository.save(transaction21);
			transactionRepository.save(transaction22);
			transactionRepository.save(transaction23);
			loanRepository.save(mortgage);
			loanRepository.save(personal);
			loanRepository.save(automotive);
			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);
			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);
			cardRepository.save(cardVisa1);
			cardRepository.save(cardVisa2);
			cardRepository.save(cardVisa3);








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