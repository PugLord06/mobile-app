package com.eduvos.initial.core;

public class GreetingService {
	public String greet(String name) {
		if (name == null || name.isBlank()) {
			return "Hello, World!";
		}
		return "Hello, " + name + "!";
	}
}
