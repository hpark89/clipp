package src.qlipon.scrape;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import src.qlipon.scrape.model.WeeklyItem;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class Scraper {

  public static void main( String[] args ) throws Exception {
    Scraper sc = new Scraper();
    List<WeeklyItem> list = sc.scrapeWholeFoods();
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
    System.out.println( "qlipon the scrapper" );
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

  public List<WeeklyItem> scrapePCC() throws Exception {
    System.setProperty( "webdriver.chrome.driver", System.getProperty( "user.dir" ) + "/library/drivers/chromedriver" );
    ChromeOptions options = new ChromeOptions();
    options.addArguments( "--headless" );

    WebDriver driver = new ChromeDriver( options );
    driver.navigate().to( "https://www.pccmarkets.com/departments/weekly-specials/" );
    new WebDriverWait( driver, 10 ).until( ExpectedConditions
        .visibilityOfElementLocated( By.xpath( "//section[contains(@class, 'pcc-promo-section')]" ) ) );

    List<WeeklyItem> resultList = new ArrayList<WeeklyItem>();

    //scrape the date range first b/c its available at the top and applies to all items.
    // does not have location specific sale items. All WA stores. only has WA stores.
    String validDates = driver.findElement( By.xpath( "//section[contains(@class, 'pcc-promo-section')]" ) ).getText();

    //WebElement mainSection = driver.findElement( By.xpath( "//div[@class='weekly-specials-template' and @data-js='weekly-specials-template']" ) );

    System.out.println( "getting product info ( categories )" );
    List<String> productInfoList = driver.findElements( By.xpath( "//div[@data-js='weekly-special']" ) )
        .stream().map( x -> x.getAttribute("data-filterable") ).collect( Collectors.toList() );
    System.out.println( "product Info: " + productInfoList.size() );

    System.out.println( "getting brand Names");
    List<String> brandNameList =  driver.findElements( By.xpath( "//div[@class='pcc-weekly-special-label']" ) )
        .stream().map( x -> x.getAttribute( "innerHTML" ) ).collect( Collectors.toList() );
    System.out.println( "brand names: " + brandNameList.size() );

    System.out.println( "getting product name list" );
    List<String> productNameList = driver.findElements( By.xpath( "//h3[@class='h5 pcc-weekly-special-headline']" ) )
        .stream().map( x -> x.getAttribute( "innerHTML" ) ).collect( Collectors.toList() );
    System.out.println( "product name list: " + productNameList.size() );

    System.out.println( "getting sale price list" );
    List<String> salePriceList = driver.findElements( By.xpath( "//div[contains(@class, 'pcc-weekly-special-block-price')]" ) )
        .stream().map( x -> x.getAttribute( "innerHTML" ) ).collect( Collectors.toList() );
    System.out.println( "price list: " + salePriceList.size() );
//    TODO: having trouble getting all the footer where they do not have one. only getting <total amount. need = total amount.
//    System.out.println( "getting extra footer info list" );
//    List<String> footInfoList = driver.findElements( By.xpath( "//div[@class='card-footer pcc-weekly-special-footer']" ) )
//        .stream().map( x -> x.getAttribute( "innerHTML" ) ).collect( Collectors.toList() );
//    System.out.println( "footer info: " + footInfoList.size() );

    //do some sort of verification that all the sizes are the same. ( currently it will throw and out of bounds array
    // exception.
    for ( int i=0; i<productNameList.size(); i++ ) {
      //TODO: parse the string amount correctly there are "2/$5", "$1.69", "99Â¢", "no <strong>", "15% off"
      Long salePrice = 0l;
      String salePriceNonDigit = "";
      String priceRaw = salePriceList.get( i );
      if ( priceRaw.contains( "<strong>" ) ) {
        String salePriceWCarrots = priceRaw.split( "strong" )[1];
        String cleanPrice = salePriceWCarrots.substring( 1, salePriceWCarrots.length() - 2 );

        if ( cleanPrice.contains( "% off" ) || cleanPrice.contains( "\\$" ) ) {
          salePrice = -1l;
          salePriceNonDigit = cleanPrice;
        }
        else {
          salePrice = parseToLong( cleanPrice );
          salePriceNonDigit = salePrice.toString();
        }
      }
      resultList.add( new WeeklyItem( productNameList.get( i ), brandNameList.get( i ), brandNameList.get( i ),
          "pcc community markets", salePrice, -1, salePriceNonDigit , validDates, validDates, "description" ) );
    }

    return resultList;
  }


  public List<WeeklyItem> scrapeWholeFoods() throws Exception {

    final int ITEMS_ADDED = 20;
    final List<String> categoryList = Arrays.asList( "produce", "dairy-eggs", "cheese", "frozen-foods", "beverages", "snacks-chips-salsas-dips", "pantry-essentials",
        "breads-rolls-bakery", "breakfast", "beef-poultry-pork", "seafood", "prepared-foods", "wine-beer-spirits" );

    System.setProperty( "webdriver.chrome.driver", System.getProperty( "user.dir" ) + "/library/drivers/chromedriver" );
    ChromeOptions options = new ChromeOptions();
    options.addArguments( "--headless" );

    WebDriver driver = new ChromeDriver( options );

    List<WeeklyItem> resultList = new ArrayList<WeeklyItem>();
    for ( String categoryName : categoryList ) {

      // navigate
      System.out.println( "navigating to " + categoryName );
      driver.navigate().to( "https://products.wholefoodsmarket.com/search?sort=relevance&store=10153&onSale=sale&category=" + categoryName );
      // waiting for the "..."
      new WebDriverWait( driver, 10 ).until( ExpectedConditions.visibilityOfElementLocated( By.xpath( "//div[contains(@class, 'Icon-Root-')]" ) ) );
      Thread.sleep( 3000 );

      // scroll to very bottom until there is no more wait.
      Long numOfItems = parseToLong( driver.findElement( By.xpath( "//div[contains(@class, 'FilterTags-ResultsLabel-')]" ) )
          .getText() );

      JavascriptExecutor jse = ( JavascriptExecutor ) driver;

      // divide by 20 because that is the number of items added per scroll down
      for ( int i = 0; i <= numOfItems / ITEMS_ADDED; i++ ) {
        jse.executeScript( "window.scrollTo(0, document.body.scrollHeight)" );
        // waiting for the "..."
        new WebDriverWait( driver, 10 ).until(ExpectedConditions
            .visibilityOfElementLocated( By.xpath( "//div[contains(@class, 'Icon-Root-')]" ) ) );
        Thread.sleep( 1000 );
      }

      List<String> productInfoList = driver.findElements( By.xpath( "//div[contains(@class, 'Grid')]//span" ) )
          .stream().map( x -> x.getAttribute( "innerHTML" ) ).collect( Collectors.toList() );

      for ( String info : productInfoList ) {
        System.out.println( info );
        if ( !info.contains( "On Sale") || !info.contains( "Old Price" )  || !info.contains( "Valid" ) ) {
          continue;
        }
        boolean hasBrand = info.contains( "by" );

        String productName = info.split( hasBrand ? "by" : "On Sale" )[0].trim();
        String brandName = hasBrand ? info.split( "by" )[1].split( "On Sale" )[0] : "";
        Long salePrice = parseToLong( info.split( "On Sale" )[1].split( "Old Price" )[0] );
        Long regPrice = parseToLong( info.split( "Old Price" )[1].split( "Valid" )[0] );
        String validDates = info.split( "Valid" )[1];

        // they sometimes just have a single number to show the amount of dollars.
        if ( salePrice < 10 ) salePrice *= 100;
        resultList.add( new WeeklyItem( productName, brandName, categoryName, "whole foods market", salePrice,
            -1, salePrice.toString(), validDates, validDates, "description" ) );
      }

    }
    driver.close();

    return resultList;
  }
//      List<String> productLinkList = driver.findElements( By.xpath( "//div[contains(@class, 'Grid')]//a" ) ).stream()
//          .map( x -> x.getAttribute( "href" ).split( "/" )[x.getAttribute( "href" ).split( "/" ).length - 1] ).collect(Collectors.toList() );
//
//      for ( String productLink : productLinkList ) {
//        WebElement e = driver.findElement( By.xpath( "//div[contains(@class, 'Grid')]//a[contains(@href, '" + productLink + "')]" ) );
//
//        String validDateRange = e.findElement( By.xpath( "//div[contains(@class, 'ProductCard-Sale-')]" ) ).getText();
//        String imageUri = e.findElement( By.xpath( "//div[contains(@class, 'LazyImage-Image-')]" ) ).getCssValue( "background-image" );
//
//        String brandName = "";
//        List<WebElement> eBrandName = e.findElements( By.xpath( "//div[contains(@class, 'ProductCard-Brand-')]" ) );
//        if ( eBrandName.size() == 1 ) {
//          brandName = e.findElement( By.xpath( "//div[contains(@class, 'ProductCard-Brand-)]" ) ).getText();
//        }
//
//        String productName = e.findElement( By.xpath( "//div[contains(@class, 'ProductCard-Name-')]" ) ).getText();
//        int salePrice = Integer.parseInt( e.findElement( By.xpath( "//div[contains(@class, 'ProductCard-Price-')]" ) )
//            .getText().replaceAll("[^x0-9]", "" ) );
//        int regPrice = Integer.parseInt( e.findElement( By.xpath( "//div[contains(@class, 'ProductCard-Price-')]" ) )
//            .getText().replaceAll("[^x0-9]", "" ) );
//
//      }
//      List<WebElement> elItemList = driver.findElements( By.xpath( "//div[contains(@class, 'Grid')]//a" ) );
//      for ( WebElement e : elItemList ) {
//        String validDateRange = e.findElement( By.xpath( "//div[contains(@class, 'ProductCard-Sale-')]" ) ).getText();
//        String imageUri = e.findElement( By.xpath( "//div[contains(@class, 'LazyImage-Image-')]" ) ).getCssValue( "background-image" );
//
//        String brandName = "";
//        List<WebElement> eBrandName = e.findElements( By.xpath( "//div[contains(@class, 'ProductCard-Brand-')]" ) );
//        if ( eBrandName.size() == 1 ) {
//          brandName = e.findElement( By.xpath( "//div[contains(@class, 'ProductCard-Brand-)]" ) ).getText();
//        }
//
//        String productName = e.findElement( By.xpath( "/div[contains(@class, 'ProductCard-Name-')]" ) ).getText();
//        int salePrice = Integer.parseInt( e.findElement( By.xpath( "//div[contains(@class, 'ProductCard-Price-')]" ) )
//            .getText().replaceAll("[^x0-9]", "" ) );
//        int regPrice = Integer.parseInt( e.findElement( By.xpath( "//div[contains(@class, 'ProductCard-Price-')]" ) )
//            .getText().replaceAll("[^x0-9]", "" ) );
//
//      }
//    }
//    return resultList;
//  }

      /**
       * inconsistent number of brandNames so array out of bounds errors.
       */
//      List<String> validDates = driver.findElements( By.xpath( "//div[contains(@class, 'ProductCard-Sale-')]" ) )
//          .stream().map( x -> x.getText() ).collect(Collectors.toList() );
//      List<String> imageUris = driver.findElements( By.xpath( "//div[contains(@class, 'LazyImage-Image-')]" ) )
//          .stream().map( x -> x.getCssValue( "background-image") ).collect( Collectors.toList() );
//      List<String> productNames = driver.findElements( By.xpath( "//div[contains(@class, 'ProductCard-Name-')]" ) )
//          .stream().map( x -> x.getText() ).collect( Collectors.toList() );
//      List<Integer> salePrices = driver.findElements( By.xpath( "//div[contains(@class, 'ProductCard-Price-')]" ) )
//          .stream().map( x -> Integer.parseInt( x.getText().replaceAll( "[^x0-9]", "" ) ) ).collect( Collectors.toList() );
//      List<Integer> regPrices = driver.findElements( By.xpath( "//div[contains(@class, 'ProductCard-Price-')]" ) )
//          .stream().map( x -> Integer.parseInt( x.getText().replaceAll( "[^x0-9]", "" ) ) ).collect( Collectors.toList() );
//
//      List<String> brandNames = null;
//      List<WebElement> eBrandNames = driver.findElements( By.xpath( "//div[contains(@class, 'ProductCard-Brand-')]" ) );
//      if ( eBrandNames.size() > 0 ) {
//        brandNames = eBrandNames.stream().map( x -> x.getText() ).collect( Collectors.toList() );
//      }
//
//      System.out.println( "validDates:" + validDates.size() + "// imageUris:" + imageUris.size() + "// productNames:" + productNames.size() + "// salesPrice:" + salePrices.size()
//      + "// regPrice:" + regPrices.size() );
//      if ( brandNames != null ) {
//        System.out.print( "// brandNames:" + brandNames.size() );
//      }
//
//      // use one of the lists b/c the total that was scraped from the top is not always accurate.
//      for ( int i=0; i<productNames.size(); i++ ) {
//        resultList.add( new WeeklyItem( productNames.get( i ),  brandNames != null ? brandNames.get( i ) : "",
//            salePrices.get( i ), categoryName, validDates.get( i ), "WHOLEFOODS") );
//      }
//    }
//
//    return resultList;
//  }
        /**
         * this does not work because it gets the first "a href" and always gets the first one. the href is dynamic which makes it hard to get the second element.
         */
//        List<WebElement> elItemList = driver.findElements( By.xpath( "//a[contains(@href, '/product/')]" ) );
//        for ( WebElement e : elItemList ) {
//        WebElement e = driver.findElement( By.xpath( "//div[contains(@class, 'Grid')]//a[2]" ) );
//        String validDateRange = e.findElement( By.xpath( "//div[contains(@class, 'ProductCard-Sale-')]" ) ).getText();
//        String imageUri = e.findElement( By.xpath( "//div[contains(@class, 'LazyImage-Image-')]" ) ).getCssValue( "background-image" );
//
//        String brandName = "";
//        List<WebElement> eBrandName = e.findElements( By.xpath( "//div[contains(@class, 'ProductCard-Brand-')]" ) );
//        if ( eBrandName.size() == 1 ) {
//          brandName = e.findElement( By.xpath( "//div[contains(@class, 'ProductCard-Brand-)]" ) ).getText();
//        }
//
//        String productName = e.findElement( By.xpath( "//div[contains(@class, 'ProductCard-Name-')]" ) ).getText();
//        int salePrice = Integer.parseInt( e.findElement( By.xpath( "//div[contains(@class, 'ProductCard-Price-')]" ) )
//            .getText().replaceAll("[^x0-9]", "" ) );
//        int regPrice = Integer.parseInt( e.findElement( By.xpath( "//div[contains(@class, 'ProductCard-Price-')]" ) )
//            .getText().replaceAll("[^x0-9]", "" ) );


//      long regPriceNum = -1;
//
//      String brandName = elem.findElement( By.className( BRAND_NAME ) ).getText();
//      String itemName = elem.findElement( By.className( ITEM_NAME ) ).getText();
//      String itemSize = elem.findElement( By.className( ITEM_SIZE ) ).getText();
//
//      // this has to be a string b/c they use eg: "2 for $5"
//      String salePrice = elem.findElement( By.className( SALE_PRICE ) ).getText();
//
//      String regPriceName = elem.findElement( By.className( REG_PRICE_NAME ) ).getText();
//
//      // this elem does not always exist. also can be parsed into a long
//      List<WebElement> elemsRegPriceNum = elem.findElements( By.className( REG_PRICE_NUM ) );
//      String validDateRange = elem.findElement( By.className( VALID_DATE_RANGE ) ).getText();
//
//      if ( elemsRegPriceNum.size() == 1 ) {
//        regPriceNum = Integer.parseInt( elemsRegPriceNum.get( 0 ).getText().replaceAll(  "[^x0-9]", "" ) );
//      }
//
//      itemList.add( new WeeklyItem( brandName + " " + itemName, regPriceNum, "WHOLEFOODS" ) );
//
//    }
//
//    return itemList;
//  }



  public List<WeeklyItem> scrapeHMart() throws Exception {
    System.setProperty( "webdriver.chrome.driver", System.getProperty( "user.dir" ) + "/library/drivers/chromedriver" );
    ChromeOptions options = new ChromeOptions();
    options.addArguments( "--headless" );

    WebDriver driver = new ChromeDriver( options );

    System.out.println( "navigating to hmart main." );
    driver.navigate().to( "https://www.hmartus.com/product-category/weekly-sale-item-list-wa" );
    new WebDriverWait( driver, 10 ).until( ExpectedConditions
        .visibilityOfElementLocated( By.xpath( "//p[@class='woocommerce-result-count']" ) ) );

    boolean finishedLastPage = false;
    int pageNumber = 1;
    List<WeeklyItem> itemList = new ArrayList<>();

    while ( !finishedLastPage ) {

      List<WebElement> elemItemList = driver.findElements( By.className( "product-title" ) );
      List<WebElement> elemPriceList = driver.findElements( By.className( "shop_price_lightbox_holder" ) );

      List<String> names = new ArrayList<>();
      for ( WebElement elemItem : elemItemList ) {
        names.add( elemItem.getText().split( Pattern.quote("|" ) )[0].toLowerCase() );
      }

      System.out.println( "getting amounts" );
      List<Long> amounts = new ArrayList<>();
      for ( WebElement elemPrice : elemPriceList ) {
        amounts.add( parseToLong( elemPrice.getText() ) );
      }

      for ( int i=0; i < names.size(); i++ ) {
        //TODO: brandNames, categoryNames, dateRange
        itemList.add( new WeeklyItem( names.get( i ), "", "hmart", "category",
            amounts.get( i ), -1, amounts.get( i ).toString(), "startDate", "endDate", "description" ) );
      }


      WebElement itemCount = driver.findElement( By.xpath( "//p[@class='woocommerce-result-count']" ) );

      String[] nums = itemCount.getText().split( " " );
      int totalItemCount = Integer.parseInt( nums[4] );
      int currentItemCount = Integer.parseInt( nums[2] );

      if ( totalItemCount == currentItemCount ) {
        finishedLastPage = true;
      }
      else {
        pageNumber++;
        driver.navigate().to( "https://www.hmartus.com/product-category/weekly-sale-item-list-wa/page/" + pageNumber );
        new WebDriverWait( driver, 10 ).until( ExpectedConditions
            .visibilityOfElementLocated( By.xpath( "//p[@class='woocommerce-result-count']" ) ) );
      }
    }

    return itemList;
  }


  // Helpers

  /**
   * removes all characters except digits and then parses to a Long ( positive only ).
   */
  Long parseToLong( String priceAmount ) {
    return priceAmount.isEmpty() ? -1l : Long.parseLong( priceAmount.replaceAll( "[^x0-9]", "" ) );
  }

}
