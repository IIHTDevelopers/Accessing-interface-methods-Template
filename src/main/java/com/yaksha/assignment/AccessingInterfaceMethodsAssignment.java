package com.yaksha.assignment;

// Define an interface for animals to demonstrate accessing interface methods
interface AnimalInterface {
	void sound(); // Interface method to be implemented by classes

	void eat(); // Interface method to be implemented by classes
}

// Dog class implements the AnimalInterface
class Dog implements AnimalInterface {

	@Override
	public void sound() {
		System.out.println("The dog barks.");
	}

	@Override
	public void eat() {
		System.out.println("The dog eats food.");
	}
}

// Cat class implements the AnimalInterface
class Cat implements AnimalInterface {

	@Override
	public void sound() {
		System.out.println("The cat meows.");
	}

	@Override
	public void eat() {
		System.out.println("The cat eats food.");
	}
}

public class AccessingInterfaceMethodsAssignment {
	public static void main(String[] args) {
		// Create objects of Dog and Cat, both implement AnimalInterface
		AnimalInterface dog = new Dog();
		AnimalInterface cat = new Cat();

		// Accessing interface methods through the object
		dog.sound(); // Should print "The dog barks."
		dog.eat(); // Should print "The dog eats food."

		cat.sound(); // Should print "The cat meows."
		cat.eat(); // Should print "The cat eats food."
	}
}
