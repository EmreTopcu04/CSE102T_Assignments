
import java.util.ArrayList;

public class Assignment01_20220808011 {
    /*
    @Author: Emre Topcu
    @Date: 3/27/2023
     */

}
class Product{
    private String Id;
    private String Name;
    private int Quantity;
    private double Price;

    Product(){
    }
    Product(String Id,String Name, int Quantity, double Price){
        this.Name = Name;
        this.Id = Id;
        this.Quantity = Quantity;
        this.Price = Price;
    }

    public double getPrice() {
        return Price;
    }

    public String getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }
    public void setName(String name) {
        this.Name = name;
    }

    public void setId(String id) {
        this.Id = id;
    }

    public void setPrice(double price) {
        this.Price = price;
    }
    public int remaining(){
        return Quantity;
    }
    public int addToInventory(int amount){
        if (amount < 0){
            return Quantity;
        }
        else{
            Quantity = amount+Quantity;
            return Quantity;
        }
    }
    public double purchase(int amount){
        if (amount > Quantity || amount < 0){
            return 0;
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
    FoodProduct(String Id, String Name, int Quantity, double Price,
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
        this.Calories = calories;
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
    CleaningProduct(String Id, String Name, int Quantity, double Price, boolean Liquid, String WhereToUse) {
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

    Customer(){
    }
    Customer(String Name){
        this.Name = Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getName() {
        return Name;
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

    @Override
    public String toString() {
        return getName() + " has "+ getPoints()+ " points";
    }
}
class Store{
    private String Name;
    private     String Website;

    private ArrayList<Product> InventorySize;
    Store(){}
    Store(String Name, String Website){
        this.Name = Name;
        this.Website = Website;
        InventorySize = new ArrayList<Product>();
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

    public void addProduct(Product product, int index) {
        if (index < 0 || index > InventorySize.size()){
            addProduct(product);
        }else{
            InventorySize.add(index,product);
        }
    }
    public void addProduct(Product product){
        InventorySize.add(product);
    }

    public Product getProduct(int index) {
        if (index < 0 || index > InventorySize.size()){
            return null;
        }else {
            return InventorySize.get(index);
        }
    }
    public int getProductIndex(Product p) {
        for (int i = 0; i < InventorySize.size(); i++){
            if (p == InventorySize.get(i)){
                return i;
            }
        }
        return -1;
    }
}

