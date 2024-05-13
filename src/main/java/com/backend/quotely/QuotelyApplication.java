package com.backend.quotely;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

@SpringBootApplication
public class QuotelyApplication implements CommandLineRunner {
	private static final String URL = "http://api.forismatic.com/api/1.0/?method={method}&format={format}&lang={lang}&key={key}";
	private static final Logger log = LoggerFactory.getLogger(QuotelyApplication.class);
	private static final Map<String, String> languageMap = Map.of(
			"1", "en",
			"2", "ru"
	);
	private static final String one = "1";
	private static final String two = "2";

	public static void main(String[] args) {
		SpringApplication.run(QuotelyApplication.class, args);
	}


	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Select language for your quote:");
		System.out.println("[1] - English");
		System.out.println("[2] - Russian");
		Scanner scan = new Scanner(System.in);
		String input = scan.nextLine();

		if(input.isEmpty()) {
			//default to English if user doesn't select and hit enter
			input = "1";
		} else {
			//loop to get the right input
			while(!input.equals(one) && !input.equals(two)) {
				System.out.println("You did not enter a correct number, please enter 1 for English or 2 for Russian:");
				input = scan.nextLine();
			}
		}

		Map<String, String> vars = new HashMap<String, String>();
		vars.put("method", "getQuote");
		vars.put("format", "json");
		vars.put("lang", languageMap.get(input));
		vars.put("key", "457651"); //looks like value of key doesn't matter, each time a random quote will be generated

		RestTemplate restTemplate = new RestTemplate();
		Quote quote = restTemplate.getForObject(URL, Quote.class, vars);

		if(quote != null) {
			System.out.println(quote.quoteText());
			System.out.println(quote.quoteAuthor());
		}

		System.exit(0);
	}
}
