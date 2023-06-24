import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

public class Assignment02_20220808011 {
    /*
    @Author: Emre Topcu
    @Date: 4/14/2023
     */
    public static void main(String[] args) {
    }
}

class Product{
    private Long Id;
    private String Name;
    private int Quantity;
    private double Price;

    Product(){
    }
    Product(Long Id,String Name, int Quantity, double Price){
        this.Name = Name;
        this.Id = Id;
        this.Quantity = Quantity;
        this.Price = Price;
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

    public void setPrice(double price) throws InvalidPriceException{
        if (price < 0){
            throw new InvalidPriceException(price);
        }else {
            this.Price = price;
        }
    }
    public int remaining(){
        return Quantity;
    }
    public int addToInventory(int amount) throws InvalidAmountException{
        if (amount < 0){
            throw new InvalidAmountException(amount);
        }
        else{
            Quantity = amount+Quantity;
            return Quantity;
        }
    }
    public double purchase(int amount) throws InvalidAmountException{
        if (amount < 0){
            throw new InvalidAmountException(amount);
        }
        else if (amount > Quantity){
            throw new InvalidAmountException(amount, Quantity);
        }
        else{
            Quantity = Quantity - amount;
            return amount * Price;
        }
    }

    @Override
    public String toString() {
        return "Product "+Name+" has "+Quantity+" remaining";
    }
    public boolean equals (Object o){
        double otherProduct = getPrice();
        if (o instanceof Product && Math.abs(otherProduct-((Product) o).getPrice()) < 0.001){
            return true;
        }else {
            return false;
        }
    }
}
class FoodProduct extends Product{
    private int Calories;
    private boolean Dairy;
    private boolean Eggs;
    private boolean Peanuts;
    private boolean Gluten;

    FoodProduct(){}
    FoodProduct(Long Id, String Name, int Quantity, double Price,
                int Calories, boolean Dairy, boolean Peanuts,
                boolean Eggs, boolean Gluten) {

        super(Id, Name, Quantity, Price);
        this.Calories=Calories;
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
        if (calories < 0){
            throw new InvalidAmountException(calories);
        }else {
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
class CleaningProduct extends Product{
    private boolean Liquid;
    private String WhereToUse;
    CleaningProduct(){  }
    CleaningProduct(Long Id, String Name, int Quantity, double Price, boolean Liquid, String WhereToUse) {
        super(Id, Name, Quantity, Price);
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
class Customer{
    private String Name;
    private ArrayList <Product> products;
    private ArrayList <Integer> amounts;

    Customer(){
        amounts = new ArrayList<>();
        products = new ArrayList<>();
    }

    Customer(String Name){
        this();
        this.Name = Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getName() {
        return Name;
    }
    public void addToCart(Product product, int count){
        try {
            product.purchase(count);
            products.add(product);
            amounts.add(Integer.valueOf(count));
        } catch (InvalidAmountException ex){
            System.out.println("ERROR: " + ex);
        }
    }

    public String receipt(){
        String returnVal = "";
        for (int i = 0; i < products.size(); i++){
            returnVal += (products.get(i).getName() +" - "+ products.get(i).getPrice()+" X " +amounts.get(i) + " = "
                    +amounts.get(i)*products.get(i).getPrice())+"\n";
        }
        returnVal += "------------------------------------------------------------------\n";
        returnVal += "Total due = "+ getTotalDue();
        return returnVal;
    }
    public double getTotalDue(){
        double total = 0;
        for (int i = 0; i < products.size(); i++){
            total += products.get(i).getPrice()*amounts.get(i);
        }
        return total;
    }
    public double pay(double amount)throws InsufficientFundsException{
        double totalDue = getTotalDue();
        if (amount >= totalDue){
            System.out.println("Thank you for shopping with us.");
            amount -= totalDue;
            products.clear();
            amounts.clear();
            return amount;
        }else{
            throw new InsufficientFundsException(totalDue,amount);
        }
    }


    @Override
    public String toString() {
        return getName();
    }
}
class ClubCustomer extends Customer{
    private String Phone;
    private int Points;
    ClubCustomer(){}
    ClubCustomer(String Name, String Phone) {
        super(Name);
        this.Phone = Phone;
        Points = 0;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public int getPoints(){
        return Points;
    }
    public void addPoints(int points){
        if (points < 0){

        }else {
            Points = getPoints() + points;

        }

    }
    public double pay(double amount, boolean usePoints)throws InsufficientFundsException {
        int TotalPoints = getPoints();
        double TotalDue = getTotalDue();
        double reduced = 0;
        if (usePoints && TotalPoints > 0){
            reduced = TotalPoints * 0.01;
           if(reduced < TotalDue){
            amount += reduced;
            Points = 0;
               addPoints((int) (TotalDue - reduced));
           } else if (reduced > TotalDue) {
            Points -= TotalDue * 100;
            amount += TotalDue;
           }
        }else if (!usePoints){
            addPoints((int) TotalDue);
        }
        if (reduced + amount < TotalDue) {
            throw new InsufficientFundsException(TotalDue,amount);
        }
        return super.pay(amount);


    }

    @Override
    public String toString() {
        return getName() + " has "+ getPoints()+ " points";
    }
}
class Store{
    private String Name;
    private String Website;

    private ArrayList<Product> InventorySize;
    private ArrayList<ClubCustomer> ClubCustomers;
    Store(){}
    Store(String Name, String Website){
        this.Name = Name;
        this.Website = Website;
        InventorySize = new ArrayList<Product>();
        ClubCustomers = new ArrayList<ClubCustomer>();
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
    public int getInventorySize(){
        return InventorySize.size();
    }

    public void addProduct(Product product){
        InventorySize.add(product);
    }

    public Product getProduct(Long ID)throws ProductNotFoundException {
        for (int i = 0; i < InventorySize.size(); i++) {
            if (Objects.equals(InventorySize.get(i).getId(), ID)){
                return InventorySize.get(i);
            }
        }
        throw new ProductNotFoundException(ID);
    }
    public Product getProduct(String name) throws ProductNotFoundException{
        for (int i = 0; i < InventorySize.size(); i++){
            if (name.equalsIgnoreCase(InventorySize.get(i).getName())){
                return InventorySize.get(i);
            }
        }
        throw new ProductNotFoundException(name);
    }
    public void addCustomer (ClubCustomer customer){
        ClubCustomers.add(customer);
    }
    public ClubCustomer getCustomer(String phone)throws CustomerNotFoundException{
        for (int i = 0; i < ClubCustomers.size() ; i++) {
            if (ClubCustomers.get(i).getPhone().equals(phone)){
                return ClubCustomers.get(i);
            }
        }
        throw new CustomerNotFoundException(phone);
    }
    public void removeProduct(Long ID)throws ProductNotFoundException{
        for (int i = 0; i < InventorySize.size() ; i++) {
            if (Objects.equals(InventorySize.get(i).getId(), ID)){
                InventorySize.remove(i);
            }
        }
        throw new ProductNotFoundException(ID);
    }
    public void removeProduct(String name) throws ProductNotFoundException {
        for (int i = 0; i < InventorySize.size(); i++) {
            if (InventorySize.get(i).getName().equals(name)) {
                InventorySize.remove(i);
            }
        }
        throw new ProductNotFoundException(name);
    }
    public void removeCustomer(String phone)throws CustomerNotFoundException {
        for (int i = 0; i < InventorySize.size(); i++) {
            if (ClubCustomers.get(i).getPhone().equals(phone)) {
                InventorySize.remove(i);
            }
        }
        throw new CustomerNotFoundException(phone);
    }


}
class CustomerNotFoundException extends IllegalArgumentException{
    private String phone;
    CustomerNotFoundException(String phone){
        this.phone = phone;
    }
    CustomerNotFoundException(){}
    @Override
    public String toString() {
        return "CustomerNotFoundException: "+ phone;
    }
}
class InsufficientFundsException extends RuntimeException{
    private double total;
    private double payment;

    InsufficientFundsException(double total, double payment){
        this.payment = payment;
        this.total = total;
    }
    InsufficientFundsException(){}

    @Override
    public String toString() {
        return "InsufficientFundsException: "+total+" due, but only "+ payment + " given";
    }
}
class InvalidAmountException extends RuntimeException{
    private int amount;
    private int quantity;
    InvalidAmountException(){}
    InvalidAmountException(int amount){
        this.amount = amount;
    }
    InvalidAmountException(int amount, int quantity){
        this.amount = amount;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        if (quantity == 0){
            return "InvalidAmountException: "+ amount;
        }else{
            return "InvalidAmountException: "+amount + " was requested, but only "+ quantity + " remaining";
        }
    }
}
class InvalidPriceException extends RuntimeException{
    private double price;
    InvalidPriceException(double price){
        this.price = price;
    }
    InvalidPriceException(){}

    @Override
    public String toString() {
        return "InvalidPriceException: "+ price;
    }
}
class ProductNotFoundException extends IllegalArgumentException{
    private Long ID;
    private String name;
    ProductNotFoundException(Long ID){
        this.ID = ID;
        name = null;
    }
    ProductNotFoundException(String name){
        this.name = name;
        ID = (long) (100*(Math.random()+Integer.MAX_VALUE));
    }
    ProductNotFoundException(){}

    @Override
    public String toString() {
        if (name == null){
            return "ProductNotFoundException: ID - "+ ID;
        }else{
            return "ProductNotFoundException: Name - "+ name;
        }
    }
}