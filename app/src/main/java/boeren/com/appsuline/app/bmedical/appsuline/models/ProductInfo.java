package boeren.com.appsuline.app.bmedical.appsuline.models;

/**
 * Created by Jamil on 17-2-2015.
 */
public class ProductInfo {

    long productId;
    String name;
    String quantity;
    String carbohydrates;
    public ProductInfo(){}
    public ProductInfo(String name, String quantity, String carbohydrates){
        this.name =name;
        this.quantity = quantity;
        this.carbohydrates =carbohydrates;
    }
    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(String carbohydrates) {
        this.carbohydrates = carbohydrates;
    }



}
