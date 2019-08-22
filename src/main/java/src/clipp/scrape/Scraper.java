package src.clipp.scrape;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class Scraper {

  public static void main( String[] args ) throws Exception {
    Scraper sc = new Scraper();
    List<WeeklyItem> list = sc.scrapeHMart();
    ItemSearcher itemSearcher = new ItemSearcher( list );

    System.out.println( "start search" );

    while ( true ) {
      BufferedReader br = new BufferedReader( new InputStreamReader( System.in ) );
      String input = br.readLine().toLowerCase();

      if ( input.equals( "exit" ) ) {
        break;
      }
      else {
        List<WeeklyItem> resultWeeklyList = itemSearcher.searchName( input );

        if ( resultWeeklyList.size() == 0 ) {
          System.out.println( "no results" );
        }
        else {
          for ( WeeklyItem resultItem : resultWeeklyList ) {
            System.out.println( resultItem.getFullInfo() );
          }
        }
      }
    }
  }


  public void shoppingQueryLanguage() throws Exception {
    System.out.println( "clipp the scrapper" );
    boolean exit = false;

    while ( !exit ) {
      BufferedReader br = new BufferedReader( new InputStreamReader( System.in ) );
      String input = br.readLine();

      if ( input.equals( "exit" ) ) {
        exit = true;
      }
      else {
        System.out.println( input );
      }
    }
  }


  public Scraper() {
  }



  public List<WeeklyItem> scrapeHMart() throws Exception {
    System.setProperty( "webdriver.chrome.driver", System.getProperty( "user.dir" ) + "/library/drivers/chromedriver" );
    WebDriver driver = new ChromeDriver();

    driver.navigate().to( "https://www.hmartus.com/product-category/weekly-sale-item-list-wa" );

    Thread.sleep( 6000 );

    boolean finishedLastPage = false;
    int pageNumber = 1;
    List<WeeklyItem> itemList = new ArrayList<WeeklyItem>();

    while ( !finishedLastPage ) {

      List<WebElement> elemItemList = driver.findElements( By.className( "product-title" ) );
      List<WebElement> elemPriceList = driver.findElements( By.className( "shop_price_lightbox_holder" ) );

      List<String> names = new ArrayList<String>();
      for ( WebElement elemItem : elemItemList ) {
        names.add( elemItem.getText().split( Pattern.quote("|" ) )[0].toLowerCase() );
      }

      List<Integer> amounts = new ArrayList<Integer>();
      for ( WebElement elemPrice : elemPriceList ) {
        amounts.add( Integer.parseInt( elemPrice.getText().replaceAll( "[^x0-9]", "" ) ) );
      }

      for ( int i=0; i < names.size(); i++ ) {
        itemList.add( new WeeklyItem( names.get( i ), amounts.get( i ), "HMART" ) );
      }


      WebElement itemCount = driver.findElement( By.className( "woocommerce-result-count" ) );

      String[] nums = itemCount.getText().split( " " );
      int totalItemCount = Integer.parseInt( nums[4] );
      int currentItemCount = Integer.parseInt( nums[2] );

      if ( totalItemCount == currentItemCount ) {
        finishedLastPage = true;
      }
      else {
        pageNumber++;
        driver.navigate().to( "https://www.hmartus.com/product-category/weekly-sale-item-list-wa/page/" + pageNumber );
        Thread.sleep( 6000 );
      }
    }

    return itemList;
  }


}
