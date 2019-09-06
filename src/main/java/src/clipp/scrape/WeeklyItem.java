package src.clipp.scrape;

import java.util.Arrays;
import java.util.List;

public class WeeklyItem {

  private String categoryName;
  private long salePrice;
  private List<String> nameList;
  private String brandName;
  private String productName;
  private String storeName;

  private long regPrice;
  // TODO: need to add productUnits & regPrice
  private String productUnits;

  // set to DateTime?
  private String validDateRange;


  public WeeklyI
  `tem(String productName, String brandName, String categoryName, String salePriceNonDigit, long salePrice, String storeName, String validDateRange) {
    this.productName = productName;
    this.brandName = brandName;
    this.salePrice = salePrice;
    this.categoryName = categoryName;
    this.validDateRange = validDateRange;
    this.storeName = storeName;

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
