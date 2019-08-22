package src.clipp.scrape;

import java.util.ArrayList;
import java.util.List;

public class ItemSearcher {

  private List<WeeklyItem> itemList;

  public ItemSearcher( List<WeeklyItem> itemList ) {
    this.itemList = itemList;
  }

  public void addToList( List<WeeklyItem> items ) {
    items.addAll( items );
  }

  public List<WeeklyItem> searchName( String searchParam ) {
    List<WeeklyItem> resultList = new ArrayList<WeeklyItem>();
    String[] searchWords = searchParam.split( " " );

    for ( WeeklyItem item : itemList ) {
      for ( String word : searchWords ) {
        if ( item.getNameList().contains( word ) ) {
          resultList.add(item);
          break;
        }
      }
    }

    return resultList;
  }

}
