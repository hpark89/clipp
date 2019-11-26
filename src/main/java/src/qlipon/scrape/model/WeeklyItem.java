package src.qlipon.scrape.model;

import java.util.Arrays;
import java.util.List;

public class WeeklyItem {

  public String productName;
  public String brandName;
  public String storeName;
  public String categoryName;
  public long salePrice;
  public long regPrice;
  public String salePriceNonDigit;
  public String startDate;
  public String endDate;
  public String description;


  public List<String> nameList;



  public WeeklyItem(String productName, String brandName, String storeName, String categoryName, long salePrice, long regPrice,
                    String salePriceNonDigit, String startDate, String endDate, String description ) {
    this.productName = productName;
    this.brandName = brandName;
    this.storeName = storeName;
    this.categoryName = categoryName;
    this.salePrice = salePrice;
    this.regPrice = regPrice;
    this.salePriceNonDigit = salePriceNonDigit;
    this.startDate = startDate;
    this.endDate = endDate;
    this.description = description;

    // probably dont need this.
    this.nameList = Arrays.asList( productName.split( " " ) );

    //TODO: fix this
    //nameList.addAll( new ArrayList<>( Arrays.asList( brandName.split( " " )  ) ) );
  }

  public List<String> getNameList() {
    return nameList;
  }

  public String getFullInfo() {
    return this.productName + "::" + this.salePrice + "::" + this.categoryName + "::" + this.storeName;
  }


}
