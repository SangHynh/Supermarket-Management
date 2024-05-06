package app;

import jakarta.persistence.Persistence;

public class Test {

	public static void main(String[] args) {
		Persistence.createEntityManagerFactory("supermarket_server");
		System.out.println("connected");
	}

}
