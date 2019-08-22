package src.clipp.scrape;

import java.util.Arrays;
import java.util.List;

public class WeeklyItem {

  private String category;
  private long amount;
  private List<String> nameList;
  private String name;
  private String storeName;

  // create a way to set the category automatically if we do not know the category;
  public WeeklyItem( String name, long amount, String storeName ) {
    this.name = name;
    this.amount = amount;
    this.storeName = storeName;
    this.nameList = Arrays.asList( name.split( " " ) );

    this.category = "N/A";
  }

  public WeeklyItem( String name, long amount, String category, String storeName ) {
    this.name = name;
    this.amount = amount;
    this.category = category;
    this.storeName = storeName;
    this.nameList = Arrays.asList( name.split( " " ) );
  }

  public List<String> getNameList() {
    return nameList;
  }

  public String getFullInfo() {
    return this.name + "::" + this.amount + "::" + this.category + "::" + this.storeName;
  }


}
