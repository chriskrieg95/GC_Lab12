package co.grandcircus;

public class UsedCar extends Car {

	private double mileage;

	
	//Constructors
	public UsedCar(String make, String model, int year, double price, double mileage) {
		super(make, model, year, price);
		this.mileage = mileage;
	}
	
	//Setters and getters
	public double getMileage() {
		return mileage;
	}
	
	public void setMileage(double mileage) {
		this.mileage = mileage;
	}
	
	
	//toString()
	@Override
	public String toString() {
		return  "UsedCar make=" + getMake() + ", model=" + getModel()
		+ ", year=" + getYear() + ", price=" + getPrice() + ", mileage=" + mileage;
	}

	
}
