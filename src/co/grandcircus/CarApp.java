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
			ResultSet myRs = myStat.executeQuery("select * from cars");
			while (myRs.next()) {
				Car car = new Car(myRs.getString("Make"), myRs.getString("Model"), myRs.getInt("Year"), myRs.getDouble("Price"));
				cars.add(car);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Welcome to the Grand Circus Motors admin console!");
		int numCars = Validator.getInt(scnr, "How many cars are you entering: ");
		int counter = 1;
		for (int i = 0; i < numCars; i++) {
			Car car = new Car();
			car.setMake(Validator.getString(scnr, "Enter Car # " + counter + " Make: "));
			car.setModel(Validator.getString(scnr, "Enter Car # " + counter + " Model: "));
			car.setYear(Validator.getInt(scnr, "Enter Car # " + counter + " Year: "));
			car.setPrice(Validator.getDouble(scnr, "Enter Car # " + counter + " Price: "));
			cars.add(car);
			counter++;
		}
		System.out.println("Current Inventory:");
		for (Car car : cars) {
			System.out.printf("%-10s %-10s %-10s $%.2f\n", car.getMake(), car.getModel(), car.getYear(), car.getPrice());
		}

		scnr.close();
	}
	

}
