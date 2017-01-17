package stackoverflow.groupby;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author aiet
 * original question was deleted
 */
public class MultikeyGroupbyTest {

  @Test
  public void test() throws Exception {
    List<TaxLine> list = Arrays.asList(
        new TaxLine(new BigDecimal(13.00), new BigDecimal(0.10), "CT"),
        new TaxLine(new BigDecimal(7.00), new BigDecimal(0.10), "BJ"),
        new TaxLine(new BigDecimal(20.00), new BigDecimal(0.20), "NYT"),
        new TaxLine(new BigDecimal(10.00), new BigDecimal(0.10), "CT"),
        new TaxLine(new BigDecimal(10.00), new BigDecimal(0.20), "NYT")
    );

    List<TaxLine> taxLines = list.stream().sorted((o1, o2) -> {
      if (o1.title.equals(o2.title)) {
        if (o1.rate.equals(o2.rate)) {
          return o1.rate.compareTo(o2.rate);
        } else return 0;
      } else return o1.title.compareTo(o2.title);
    }).collect((Supplier<List<TaxLine>>) ArrayList::new, (resultList, taxLine) -> {
      if (resultList.size() > 0) {
        TaxLine last = resultList.get(resultList.size() - 1);
        if (last.title.equals(taxLine.title) && last.rate.equals(taxLine.rate)) {
          last.price = last.price.add(taxLine.price);
          return;
        }
      }
      resultList.add(taxLine);
    }, List::addAll);
    System.out.println(Arrays.toString(taxLines.toArray()));
  }

  static class TaxLine {
    TaxLine(BigDecimal price, BigDecimal rate, String title) {
      this.price = price;
      this.rate = rate;
      this.title = title;
    }

    BigDecimal price;
    BigDecimal rate;
    String title;
    @Override
    public String toString(){
      return String.format("%s(%.2f): %s", title, rate, price);
    }
  }
}
