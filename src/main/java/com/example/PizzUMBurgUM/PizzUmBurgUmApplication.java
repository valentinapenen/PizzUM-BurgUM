package com.example.PizzUMBurgUM;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.awt.Desktop;
import java.net.URI;

@SpringBootApplication
public class PizzUmBurgUmApplication {

	public static void main(String[] args) {
		SpringApplication.run(PizzUmBurgUmApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void openBrowser() {
		String url = "http://localhost:5432";

		try {
			if (Desktop.isDesktopSupported()) {
				Desktop desktop = Desktop.getDesktop();
				desktop.browse(new URI(url));
				System.out.println("Browser opened at: " + url);
			}
		} catch (Exception e) {
			System.out.println("Could not open browser automatically. Please navigate to: " + url);
		}
	}
}
