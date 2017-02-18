package com.test;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Test {
	public static void main(String[] args) throws IOException{
		Document doc = Jsoup.connect("http://www.emag.ro/telefoane-mobile/p1/c").get();
			Element a=doc.getElementsByClass("middle-container").first().getElementsByAttribute("href").first();
			System.out.println(a.attr("href"));
		}
}
