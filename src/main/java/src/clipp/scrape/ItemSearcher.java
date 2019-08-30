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
      boolean isMatch = false;
      for ( String word : searchWords ) {
        for ( String itemWord : item.getNameList() ) {
          if ( itemWord.toUpperCase().contains( word.toUpperCase() ) ) {
            resultList.add(item);
            isMatch = true;
            break;
          }
        }
        if ( isMatch ) {
          break;
        }
      }
    }

    return resultList;
  }

}
