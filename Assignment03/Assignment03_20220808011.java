import java.util.HashMap;


public class Assignment03_20220808011 {
    /**
     * @author Emre Topcu
     * @since 05.29.2023
     */
    public static void main(String[] args) {
        
    }
}

class Product {
    private Long Id;
    private String Name;
    private double Price;

    Product(Long Id, String Name, double Price) {
        setName(Name);
        setId(Id);
        setPrice(Price);
    }

    public double getPrice() {
        return Price;
    }

    public Long getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public void setId(Long id) {
        this.Id = id;
    }

    public void setPrice(double price) throws InvalidPriceException {
        if (price < 0) {
            throw new InvalidPriceException(price);
        } else {
            this.Price = price;
        }
    }

    @Override
    public String toString() {
        return Id + " - " + Name + " @ " + Price;
    }

    public boolean equals(Object o) {
        double otherProduct = getPrice();
        return o instanceof Product && Math.abs(otherProduct - ((Product) o).getPrice()) < 0.001;
    }
}

class FoodProduct extends Product {
    private int Calories;
    private boolean Dairy;
    private boolean Eggs;
    private boolean Peanuts;
    private boolean Gluten;


    FoodProduct(Long Id, String Name, double Price,
                int Calories, boolean Dairy, boolean Peanuts,
                boolean Eggs, boolean Gluten) {

        super(Id, Name, Price);
        setCalories(Calories);
        this.Dairy = Dairy;
        this.Eggs = Eggs;
        this.Peanuts = Peanuts;
        this.Gluten = Gluten;
    }

    public int getCalories() {
        return Calories;
    }

    public boolean containsDairy() {
        return Dairy;
    }

    public void setCalories(int calories) {
        if (calories < 0) {
            throw new InvalidAmountException(calories);
        } else {
            this.Calories = calories;
        }
    }

    public boolean containsEggs() {
        return Eggs;
    }

    public boolean containsGluten() {
        return Gluten;
    }

    public boolean containsPeanuts() {
        return Peanuts;
    }
}

class CleaningProduct extends Product {
    private boolean Liquid;
    private String WhereToUse;

    CleaningProduct(Long Id, String Name, double Price, boolean Liquid, String WhereToUse) {
        super(Id, Name, Price);
        this.WhereToUse = WhereToUse;
        this.Liquid = Liquid;
    }

    public boolean isLiquid() {
        return Liquid;
    }

    public String getWhereToUse() {
        return WhereToUse;
    }

    public void setWhereToUse(String size) {
        WhereToUse = size;
    }
}

class Customer {
    private String name;
    private HashMap<Store, HashMap<Product, Integer>> cart;

    Customer(String name) {
        cart = new HashMap<>();
        setName(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addToCart(Store store, Product product, int count) throws InvalidAmountException, ProductNotFoundException {
        try {
            if (count < 0 || count > store.getProductCount(product)) {
                throw new InvalidAmountException(count, store.getProductCount(product));
            }
            HashMap<Product, Integer> temp = cart.computeIfAbsent(store, k -> new HashMap<>());
            int currentCount = temp.getOrDefault(product, 0);
            temp.put(product, count + currentCount);
            cart.put(store, temp);
        } catch (InvalidAmountException | ProductNotFoundException ex) {
            System.out.println("ERROR: " + ex);
        }
    }

    public String receipt(Store store) throws StoreNotFoundException {
        if (!cart.containsKey(store)) {
            throw new StoreNotFoundException(store.getName());
        }

        StringBuilder returnVal = new StringBuilder("Customer receipt for "+store.getName() + " \n");
        HashMap<Product, Integer> products = cart.get(store);
        returnVal.append("\n");
        for (Product product : products.keySet()) {
            returnVal.append(product).append(" X ").append(products.get(product)).append("...").append(products.get(product) * product.getPrice()).append("\n");
        }
        returnVal.append("\n------------------------------------------------------------------------------");
        returnVal.append("\nTotal Due: ").append(getTotalDue(store)).append("\n");
        return returnVal.toString();
    }

    public double getTotalDue(Store store) throws StoreNotFoundException {
        if (!cart.containsKey(store)) {
            throw new StoreNotFoundException(store.getName());
        }
        HashMap<Product, Integer> products = cart.get(store);
        double total = 0;

        for (Product product : products.keySet()) {
            total += product.getPrice() * products.get(product);
        }

        return total;
    }

    public int getPoints(Store store) throws StoreNotFoundException {
        if (!cart.containsKey(store)) {
            throw new StoreNotFoundException(store.getName());
        }
        try {
            return store.getCustomerPoints(this);
        } catch (CustomerNotFoundException ex) {
            throw new StoreNotFoundException(store.getName());
        }
    }

    public double pay(Store store, double amount, boolean usePoints) throws InsufficientFundsException, StoreNotFoundException {
        if (!cart.containsKey(store)) {
            throw new StoreNotFoundException(store.getName());
        }
        HashMap<Product, Integer> products = cart.get(store);
        double totalDue = getTotalDue(store);
        if (usePoints) {
            int points;
            try {
                points = store.getCustomerPoints(this);
            } catch (CustomerNotFoundException ex) {
                points = 0;
            }
            double discount = points * 0.01;
            totalDue -= discount;
            if (amount >= totalDue) {
                System.out.println("Thank you for your business");
                for (Product product : products.keySet()) {
                    store.purchase(product, products.get(product));
                }
                if (totalDue > 0) {
                    store.addPoints(this,-points);
                    store.addPoints(this, (int) totalDue);
                    return amount - totalDue;
                } else {
                    store.addPoints(this,-points);
                    store.addPoints(this, -(int) totalDue);
                    return amount;
                }
            } else {
                throw new InsufficientFundsException(totalDue, amount);
            }
        } else {
            if (amount >= totalDue) {
                System.out.println("Thank you for your business");
                for (Product product : products.keySet()) {
                    store.purchase(product, products.get(product));
                }
                cart.remove(store);
                if (amount > 0) {
                    store.addPoints(this, (int) totalDue);
                    return amount - totalDue;
                } else {
                    store.addPoints(this, -(int) totalDue);
                    return amount;
                }


            } else {
                throw new InsufficientFundsException(totalDue, amount);
            }
        }
    }

    @Override
    public String toString() {
        return getName();
    }
}

class Store {
    private String Name;
    private String Website;

    private HashMap<Product, Integer> products_quantity;
    private HashMap<Customer, Integer> customers_points;

    Store(String Name, String Website) {
        setName(Name);
        setWebsite(Website);
        products_quantity = new HashMap<>();
        customers_points = new HashMap<>();
    }

    public String getName() {
        return Name;
    }

    public String getWebsite() {
        return Website;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public void setWebsite(String website) {
        this.Website = website;
    }

    public int getInventorySize() {
        return products_quantity.size();
    }

    public int getCount() {
        return products_quantity.size();
    }

    public void addCustomer(Customer customer) {
        customers_points.put(customer, 0);
    }

    public int getCustomerPoints(Customer customer) throws CustomerNotFoundException {
        if (customers_points.containsKey(customer)) {
            return customers_points.get(customer);
        } else {
            throw new CustomerNotFoundException(customer);
        }
    }

    public void addPoints(Customer customer, int point) {
        customers_points.computeIfPresent(customer, (key, val) -> val + point);
    }

    public int getProductCount(Product product) throws ProductNotFoundException {
        if (products_quantity.containsKey(product)) {
            return products_quantity.get(product);
        } else {
            throw new ProductNotFoundException(product);
        }
    }

    public void removeProduct(Product product) throws ProductNotFoundException {
        if (products_quantity.containsKey(product)) {
            products_quantity.remove(product);
        } else {
            throw new ProductNotFoundException(product);
        }
    }

    public void addToInventory(Product product, int amount) {
        if (amount < 0) {
            throw new InvalidAmountException(amount);
        } else {
            try {
                int total = getProductCount(product) + amount;
                products_quantity.put(product, total);
            } catch (ProductNotFoundException ex) {
                products_quantity.put(product, amount);
            }
        }

    }

    public double purchase(Product product, int amount) throws InvalidAmountException, ProductNotFoundException {
        if (products_quantity.containsKey(product)) {
            if (amount < 0) {
                throw new InvalidAmountException(amount);
            } else if (amount > products_quantity.get(product)) {
                throw new InvalidAmountException(amount, products_quantity.get(product));
            } else {
                products_quantity.put(product,products_quantity.get(product) - amount);
                return product.getPrice() * amount;
            }
        }
        throw new ProductNotFoundException(product);
    }
}

class CustomerNotFoundException extends IllegalArgumentException {
    private Customer customer;

    CustomerNotFoundException(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "CustomerNotFoundException: Name - " + customer.getName();
    }
}

class InsufficientFundsException extends RuntimeException {
    private double total;
    private double payment;

    InsufficientFundsException(double total, double payment) {
        this.payment = payment;
        this.total = total;
    }

    @Override
    public String toString() {
        return "InsufficientFundsException: " + total + " due, but only " + payment + " given";
    }
}

class InvalidAmountException extends RuntimeException {
    private int amount;
    private int quantity = -1;

    InvalidAmountException(int amount) {
        this.amount = amount;
    }

    InvalidAmountException(int amount, int quantity) {
        this.amount = amount;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        if (quantity == -1) {
            return "InvalidAmountException: " + amount;
        } else {
            return "InvalidAmountException: " + amount + " was requested, but only " + quantity + " remaining";
        }
    }
}

class InvalidPriceException extends RuntimeException {
    private double price;

    InvalidPriceException(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "InvalidPriceException: " + price;
    }
}

class ProductNotFoundException extends IllegalArgumentException {
    private Long ID;
    private String name;
    private Product product;

    ProductNotFoundException(Product product) {
        this.name = product.getName();
        this.ID = product.getId();
        this.product = product;
    }

    @Override
    public String toString() {
        return "ProductNotFoundException: ID - " + ID + " Name - " + name;
    }
}

class StoreNotFoundException extends IllegalArgumentException {
    private String name;

    StoreNotFoundException(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "StoreNotFoundException: " + name;
    }
}
