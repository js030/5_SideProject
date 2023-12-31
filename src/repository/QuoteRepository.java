package repository;

import domain.Quote;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class QuoteRepository {
    static long id = 0;
    Scanner sc = new Scanner(System.in);
    public static List<Quote> quotes = new ArrayList<>();

    public void save() {
        System.out.print("명언 : ");
        String content = sc.nextLine();
        System.out.print("작가 : ");
        String author = sc.nextLine();
        Quote quote = new Quote(++id, content, author);
        quotes.add(quote);
        System.out.println(id + "번 명언이 등록되었습니다.");
    }

    public void findAll() {
        System.out.println("번호 / 작가 / 명언");
        System.out.println("---------------------");
        try {
            JSONParser parser = new JSONParser();
            Reader reader = new FileReader("data.json");
            JSONArray arr = (JSONArray) parser.parse(reader);
            for (int i = arr.size() - 1; i >= 0; i--) {
                JSONObject obj = (JSONObject) arr.get(i);
                System.out.println(obj.get("id") + " / " + obj.get("author") + " / " + obj.get("content"));
            }
        } catch (IOException | ParseException e) {
            for (int i = quotes.size() - 1; i >= 0; i--) {
                Quote quote = quotes.get(i);
                System.out.println(quote.getId() + " / " + quote.getAuthor() + " / " + quote.getContent());
            }
        }
    }

    public void checkDeleteAndDelete(String cmd) {

        String[] deleteCmd = cmd.split("[=]");
        boolean flag = false;
        if (Pattern.matches("[1-9]+", deleteCmd[1])) {
            int deleteId = Integer.parseInt(deleteCmd[1]);
            for (int i = 0; i < quotes.size(); i++) {
                if (quotes.get(i).getId() == deleteId) {
                    flag = true;
                    quotes.remove(i);
                    break;
                }
            }
            if (!flag)
                System.out.println(deleteId + "번 명언은 존재하지 않습니다.");
            else
                System.out.println(deleteId + "번 명언이 삭제되었습니다.");
        }
    }

    public void checkEditAndEdit(String cmd) {

        String[] editCmd = cmd.split("[=]");
        boolean flag = false;
        if (Pattern.matches("[1-9]+", editCmd[1])) {
            int editId = Integer.parseInt(editCmd[1]);
            for (int i = 0; i < quotes.size(); i++) {
                if (quotes.get(i).getId() == editId) {
                    edit(i);
                    flag = true;
                    break;
                }
            }
            if (!flag)
                System.out.println(editId + "번 명언은 존재하지 않습니다.");
        }
    }

    private void edit(int i) {
        Quote editQuote = quotes.get(i);
        System.out.println("명언(기존) : " + editQuote.getContent());
        System.out.print("명언 : ");
        String editContent = sc.nextLine();
        System.out.println("작가(기존) : " + editQuote.getAuthor());
        System.out.print("작가 : ");
        String editAuthor = sc.nextLine();
        editQuote.setContent(editContent);
        editQuote.setAuthor(editAuthor);
    }


    public void build() {
        JSONArray arr = new JSONArray();
        for (Quote quote : quotes) {
            JSONObject obj = new JSONObject();
            obj.put("id", quote.getId());
            obj.put("content", quote.getContent());
            obj.put("author", quote.getAuthor());
            arr.add(obj);
        }
        try {
            FileWriter file = new FileWriter("data.json", true);
            file.write(arr.toJSONString());
            file.close();
            System.out.println("data.json 파일의 내용이 갱신되었습니다.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

