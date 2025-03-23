package testutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

public class AutoGrader {

	// Test if the code demonstrates proper access to interface methods
	public boolean testAccessingInterfaceMethods(String filePath) throws IOException {
		System.out.println("Starting testAccessingInterfaceMethods with file: " + filePath);

		File participantFile = new File(filePath); // Path to participant's file
		if (!participantFile.exists()) {
			System.out.println("File does not exist at path: " + filePath);
			return false;
		}

		FileInputStream fileInputStream = new FileInputStream(participantFile);
		JavaParser javaParser = new JavaParser();
		CompilationUnit cu;
		try {
			cu = javaParser.parse(fileInputStream).getResult()
					.orElseThrow(() -> new IOException("Failed to parse the Java file"));
		} catch (IOException e) {
			System.out.println("Error parsing the file: " + e.getMessage());
			throw e;
		}

		System.out.println("Parsed the Java file successfully.");

		// Use AtomicBoolean to allow modifications inside lambda expressions
		AtomicBoolean interfaceFound = new AtomicBoolean(false);
		AtomicBoolean dogClassFound = new AtomicBoolean(false);
		AtomicBoolean catClassFound = new AtomicBoolean(false);
		AtomicBoolean dogImplementsInterface = new AtomicBoolean(false);
		AtomicBoolean catImplementsInterface = new AtomicBoolean(false);
		AtomicBoolean dogMethodsImplemented = new AtomicBoolean(false);
		AtomicBoolean catMethodsImplemented = new AtomicBoolean(false);
		AtomicBoolean methodsExecutedInMain = new AtomicBoolean(false);

		// Check for interface and class method implementations
		System.out.println("------ Interface and Method Check ------");
		for (TypeDeclaration<?> typeDecl : cu.findAll(TypeDeclaration.class)) {
			if (typeDecl instanceof ClassOrInterfaceDeclaration) {
				ClassOrInterfaceDeclaration classDecl = (ClassOrInterfaceDeclaration) typeDecl;

				// Check for AnimalInterface
				if (classDecl.getNameAsString().equals("AnimalInterface")) {
					System.out.println("Interface 'AnimalInterface' found.");
					interfaceFound.set(true);
				}

				// Check for Dog class implementing AnimalInterface
				if (classDecl.getNameAsString().equals("Dog")) {
					System.out.println("Class 'Dog' found.");
					dogClassFound.set(true);
					if (classDecl.getImplementedTypes().stream()
							.anyMatch(impl -> impl.getNameAsString().equals("AnimalInterface"))) {
						dogImplementsInterface.set(true);
						System.out.println("'Dog' class implements 'AnimalInterface'.");
					} else {
						System.out.println("Error: 'Dog' class does not implement 'AnimalInterface'.");
					}

					// Check for sound() and eat() methods in Dog class
					classDecl.getMethods().forEach(method -> {
						if (method.getNameAsString().equals("sound") && method.isPublic()) {
							dogMethodsImplemented.set(true);
							System.out.println("Method 'sound' implemented in 'Dog' class.");
						}
						if (method.getNameAsString().equals("eat") && method.isPublic()) {
							dogMethodsImplemented.set(true);
							System.out.println("Method 'eat' implemented in 'Dog' class.");
						}
					});
				}

				// Check for Cat class implementing AnimalInterface
				if (classDecl.getNameAsString().equals("Cat")) {
					System.out.println("Class 'Cat' found.");
					catClassFound.set(true);
					if (classDecl.getImplementedTypes().stream()
							.anyMatch(impl -> impl.getNameAsString().equals("AnimalInterface"))) {
						catImplementsInterface.set(true);
						System.out.println("'Cat' class implements 'AnimalInterface'.");
					} else {
						System.out.println("Error: 'Cat' class does not implement 'AnimalInterface'.");
					}

					// Check for sound() and eat() methods in Cat class
					classDecl.getMethods().forEach(method -> {
						if (method.getNameAsString().equals("sound") && method.isPublic()) {
							catMethodsImplemented.set(true);
							System.out.println("Method 'sound' implemented in 'Cat' class.");
						}
						if (method.getNameAsString().equals("eat") && method.isPublic()) {
							catMethodsImplemented.set(true);
							System.out.println("Method 'eat' implemented in 'Cat' class.");
						}
					});
				}
			}
		}

		// Ensure interface and methods are correctly implemented
		if (!interfaceFound.get()) {
			System.out.println("Error: Interface 'AnimalInterface' not found.");
			return false;
		}

		if (!dogClassFound.get() || !catClassFound.get()) {
			System.out.println("Error: Class 'Dog' or 'Cat' not found.");
			return false;
		}

		if (!dogImplementsInterface.get()) {
			System.out.println("Error: 'Dog' class does not implement methods from 'AnimalInterface'.");
			return false;
		}

		if (!catImplementsInterface.get()) {
			System.out.println("Error: 'Cat' class does not implement methods from 'AnimalInterface'.");
			return false;
		}

		if (!dogMethodsImplemented.get()) {
			System.out
					.println("Error: 'Dog' class does not implement methods 'sound' or 'eat' from 'AnimalInterface'.");
			return false;
		}

		if (!catMethodsImplemented.get()) {
			System.out
					.println("Error: 'Cat' class does not implement methods 'sound' or 'eat' from 'AnimalInterface'.");
			return false;
		}

		// Check if methods are executed in the main method
		System.out.println("------ Method Execution Check in Main ------");

		for (MethodDeclaration method : cu.findAll(MethodDeclaration.class)) {
			if (method.getNameAsString().equals("main")) {
				if (method.getBody().isPresent()) {
					method.getBody().get().findAll(com.github.javaparser.ast.expr.MethodCallExpr.class)
							.forEach(callExpr -> {
								if (callExpr.getNameAsString().equals("sound")) {
									methodsExecutedInMain.set(true);
									System.out.println("Method 'sound' is executed in the main method.");
								}
								if (callExpr.getNameAsString().equals("eat")) {
									methodsExecutedInMain.set(true);
									System.out.println("Method 'eat' is executed in the main method.");
								}
							});
				}
			}
		}

		// Fail the test if methods weren't executed
		if (!methodsExecutedInMain.get()) {
			System.out.println("Error: 'sound' or 'eat' method not executed in the main method.");
			return false;
		}

		// If all checks pass
		System.out.println("Test passed: Interface methods are correctly implemented and accessed.");
		return true;
	}
}
