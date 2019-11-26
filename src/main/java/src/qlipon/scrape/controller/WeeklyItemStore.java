package src.qlipon.scrape.controller;

import src.qlipon.jdbc.model.JDBCWeeklyItem;
import src.qlipon.scrape.model.WeeklyItem;

public class WeeklyItemStore {

  private WeeklyItem[] weeklyItems;

  WeeklyItemStore( WeeklyItem[] weeklyItems) {
    this.weeklyItems = weeklyItems;
  }

  public void save() {
    if ( null == weeklyItems || weeklyItems.length <= 0 ) {
      return; // logger tell user that the weeklyItems were empty or null
    }

    for ( WeeklyItem item : weeklyItems ) {
      JDBCWeeklyItem jdbc = new JDBCWeeklyItem();
      jdbc.create( item );
    }
  }

  public void lookup( String arg[] ) {

  }

}
