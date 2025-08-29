package com.eduvos.initial.cli;

import com.eduvos.initial.core.GreetingService;

public class Main {
	public static void main(String[] args) {
		GreetingService service = new GreetingService();
		String name = args.length > 0 ? args[0] : null;
		System.out.println(service.greet(name));
	}
}
