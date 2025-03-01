import java.util.*;

class Car {
    private String carId, brand, model;
    private double basePricePerDay;
    private boolean isAvailable;

    public Car(String carId, String brand, String model, double basePricePerDay) {
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.basePricePerDay = basePricePerDay;
        this.isAvailable = true;
    }

    public String getCarId() { return carId; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public boolean isAvailable() { return isAvailable; }
    
    public double calculatePrice(int rentalDays) {
        double price = basePricePerDay * rentalDays;
        if (rentalDays > 7) price *= 0.9; // 10% discount for rentals over a week
        return price;
    }
    public void rent() { isAvailable = false; }
    public void returnCar() { isAvailable = true; }
}

class Customer {
    private String customerId, name;

    public Customer(String customerId, String name) {
        this.customerId = customerId;
        this.name = name;
    }

    public String getCustomerId() { return customerId; }
    public String getName() { return name; }
}

class Rental {
    private Car car;
    private Customer customer;
    private int days;

    public Rental(Car car, Customer customer, int days) {
        this.car = car;
        this.customer = customer;
        this.days = days;
    }

    public Car getCar() { return car; }
    public Customer getCustomer() { return customer; }
    public int getDays() { return days; }
}

class DriveEase {
    private List<Car> cars = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private List<Rental> rentals = new ArrayList<>();

    public void addCar(Car car) { cars.add(car); }
    public void addCustomer(Customer customer) { customers.add(customer); }

    public void rentCar(String carId, String customerName, int days) {
        Car selectedCar = cars.stream().filter(car -> car.getCarId().equals(carId) && car.isAvailable()).findFirst().orElse(null);
        if (selectedCar == null) {
            System.out.println("Car not available!"); return;
        }
        Customer customer = new Customer("CUS" + (customers.size() + 1), customerName);
        addCustomer(customer);
        selectedCar.rent();
        rentals.add(new Rental(selectedCar, customer, days));
        System.out.printf("Car rented successfully! Total cost: $%.2f\n", selectedCar.calculatePrice(days));
    }

    public void returnCar(String carId) {
        Rental rental = rentals.stream().filter(r -> r.getCar().getCarId().equals(carId)).findFirst().orElse(null);
        if (rental == null) {
            System.out.println("Invalid return! Car was not rented."); return;
        }
        rental.getCar().returnCar();
        rentals.remove(rental);
        System.out.println("Car returned successfully!");
    }

    public void viewRentedCars() {
        if (rentals.isEmpty()) {
            System.out.println("No cars are currently rented."); return;
        }
        System.out.println("\n===== Rented Cars =====");
        rentals.forEach(r -> System.out.println(r.getCar().getCarId() + " - " + r.getCar().getBrand() + " " + r.getCar().getModel() + " | Rented by: " + r.getCustomer().getName()));
    }

    public void menu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n===== DriveEase Car Rental System =====");
            System.out.println("1. Rent a Car\n2. Return a Car\n3. View Rented Cars\n4. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt(); scanner.nextLine();

            if (choice == 1) {
                System.out.print("Enter your name: ");
                String customerName = scanner.nextLine();
                System.out.print("Enter car ID to rent: ");
                String carId = scanner.nextLine();
                System.out.print("Enter rental days: ");
                int days = scanner.nextInt();
                rentCar(carId, customerName, days);
            } else if (choice == 2) {
                System.out.print("Enter car ID to return: ");
                returnCar(scanner.nextLine());
            } else if (choice == 3) {
                viewRentedCars();
            } else if (choice == 4) {
                System.out.println("Thank you for using DriveEase!"); break;
            } else {
                System.out.println("Invalid choice!");
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        DriveEase rentalSystem = new DriveEase();
        rentalSystem.addCar(new Car("C001", "Toyota", "Camry", 60.0));
        rentalSystem.addCar(new Car("C002", "Honda", "Accord", 70.0));
        rentalSystem.addCar(new Car("C003", "Mahindra", "Thar", 150.0));
        rentalSystem.menu();
    }
}
