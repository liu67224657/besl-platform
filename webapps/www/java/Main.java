import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;

/**
 * Created by ericliu on 2017/7/18.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        Document document= Jsoup.parse(new File("/Users/ericliu/Desktop/page.text"),"UTF-8");


        Elements elements=document.getElementsByClass("entry-name");
        System.out.println("==========");
        for(Element ele:elements){
            System.out.println(ele.text()+".joyme.com");
        }
        System.out.println("==========");

    }
}
