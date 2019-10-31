package co.grandcircus;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

public class CarApp {

	public static void main(String[] args) {
		Scanner scnr = new Scanner(System.in);
		ArrayList<Car> cars = new ArrayList<>();
		Properties login = new Properties();
		try (FileReader in = new FileReader("login.properties")) {
		    login.load(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String user = login.getProperty("username");
		String password = login.getProperty("password");
		String url = "jdbc:mysql://localhost:3306/test";
		
		/*
		 * MySQL attempt to pre-load data
		 */
		try {
			Connection myConn = DriverManager.getConnection(url, user, password);
			Statement myStat = myConn.createStatement();
			ResultSet myRs1 = myStat.executeQuery("SELECT * FROM test.cars limit 0,4");
			while (myRs1.next()) {
				Car car = new Car(myRs1.getString("Make"), myRs1.getString("Model"), myRs1.getInt("Year"), myRs1.getDouble("Price"));
				cars.add(car);
			}
			ResultSet myRs2 = myStat.executeQuery("SELECT * FROM test.cars limit 4,6");
			while (myRs2.next()) {
				Car usedCar = new UsedCar(myRs2.getString("Make"), myRs2.getString("Model"), myRs2.getInt("Year"), myRs2.getDouble("Price"),myRs2.getDouble("Mileage"));
				cars.add(usedCar);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		 * addCars and purchaseCars
		 */
		System.out.println("Welcome to the Grand Circus Motors admin console!");
		int numCars = Validator.getInt(scnr, "How many cars are you entering: ");
		addCars(numCars, scnr, cars);
		purchaseCar(scnr, cars);
		System.out.println("Have a great day!");
		scnr.close();
	}
	
	public static void displayMenu(ArrayList<Car> cars) {
		System.out.println("Current Inventory:");
		System.out.printf("%s   %-10s %-9s %-12s %-14s %-10s\n", "#", "Make", "Model", "Year", "Price", "Mileage");
		System.out.println("");
		int carCounter = 1;
		for (Car car : cars) {
			if (car instanceof UsedCar) {
				System.out.printf("%d. %-10s %-10s %-10s $%,-15.2f %,-10.1f miles (Used)\n", carCounter++, ((UsedCar) car).getMake(), ((UsedCar) car).getModel(), ((UsedCar) car).getYear(), ((UsedCar) car).getPrice(), ((UsedCar) car).getMileage());
			} else if (car instanceof Car) {
				System.out.printf("%d. %-10s %-10s %-10s $%,-15.2f\n", carCounter++, car.getMake(), car.getModel(), car.getYear(), car.getPrice());
			}
		}
		System.out.printf("%d. %-10s\n",carCounter++, "Quit\n");
	}
	
	public static void addCars(int n, Scanner scnr, ArrayList<Car> cars) {
		int counter = 1;
		for (int i = 0; i < n; i++) {
			Car car = new Car();
			car.setMake(Validator.getString(scnr, "Enter Car # " + counter + " Make: "));
			car.setModel(Validator.getString(scnr, "Enter Car # " + counter + " Model: "));
			car.setYear(Validator.getInt(scnr, "Enter Car # " + counter + " Year: "));
			car.setPrice(Validator.getDouble(scnr, "Enter Car # " + counter + " Price: "));
			cars.add(car);
			counter++;
		}
	}
	
	public static void purchaseCar(Scanner scnr, ArrayList<Car> cars) {
		int action = 0;
		String decision = "";
		do {
			displayMenu(cars);
			action = Validator.getInt(scnr, "Which car would you like? ", 1, cars.size()+1);
			if (action != cars.size()+1) {
				Car car = cars.get(action-1);
				if (car instanceof UsedCar) {
					System.out.printf("%d. %-10s %-10s %-10s $%,-15.2f %,-10.1f miles (Used)\n", action, ((UsedCar) car).getMake(), ((UsedCar) car).getModel(), ((UsedCar) car).getYear(), ((UsedCar) car).getPrice(), ((UsedCar) car).getMileage());
				} else if (car instanceof Car) {
					System.out.printf("%d. %-10s %-10s %-10s $%,.2f\n", action, car.getMake(), car.getModel(), car.getYear(), car.getPrice());
				}
				decision = Validator.getString(scnr, "Would you like to buy this car? (y/n) ");
				if (decision.toLowerCase().charAt(0) == 'y') {
					System.out.println("Excellent! Our finance department will be in touch shortly.");
					cars.remove(action-1);
					action = action -1;
				}
			}
		} while(action != cars.size()+1);
	}

}
