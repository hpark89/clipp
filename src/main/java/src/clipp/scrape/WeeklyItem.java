package src.clipp.scrape;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WeeklyItem {

  private String categoryName;
  private long amount;
  private List<String> nameList;
  private String brandName;
  private String productName;
  private String storeName;

  // set to DateTime?
  private String validDateRange;


  public WeeklyItem( String productName, String brandName, long amount, String categoryName, String validDateRange, String storeName ) {
    this.productName = productName;
    this.brandName = brandName;
    this.amount = amount;
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
    return this.productName + "::" + this.amount + "::" + this.categoryName + "::" + this.storeName;
  }


}
