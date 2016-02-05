/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coseertesttask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author dkosyakov
 */
public class InvoiceMaker {
    
    static final String RTF_LINE_DELIMITER = "\\\\par(\\\\\\w)*";
    static final String CHAT_LINE_DELIMITER = "\\d?\\d/\\d{2}/\\d{4}, \\d?\\d:\\d{2}\\s(A|P)M ";
    static final String HOST="Host";
    //static final String MESSAGE_AUTHOR_PATTERN="- [a-zA-Z_ 0-9]+:";
    private String lineDelimiter;
    
    ArrayList<Product> getProductsList(){
        /*read a list of goods from database or whatever else
        //we should obviously rewrite this since when using in real life application
        //here we just fill the resulting array without involving any external resources*/
        Product g=new Product();
        ArrayList<Product> result=new ArrayList<>();
        int length=10;
        String[] names={"tomato", "onion", "cucumber", "potato", "garlick", "carrot", "banana", "pineapple", "apple", "bhindi"};
        double[] prices={0.5, 1, 3, 7.1, 0.2, 1.1, 1.2, 1.05, 2.5, 5.2, 2.0};
        for (int i=0; i<length; i++){
            g.setId(i);
            g.setName(names[i]);
            g.setPrice(prices[i]);
            result.add(g);
        }
        return result;
    }
    
    String[] getTranscriptsFromRTF(File file) throws Exception {
        String contentOfFile;
        BufferedReader brRead=new BufferedReader(new FileReader(file));
        contentOfFile=brRead.readLine();
        brRead.close();
        
        int begin=0;
        int end;
        int count=0;
        ArrayList<String> technicalList=new ArrayList<>();
        Matcher m=Pattern.compile(lineDelimiter).matcher(contentOfFile);
        boolean searchOn=m.find();
        while (searchOn){
            begin = m.start();
            searchOn=m.find();
            end = searchOn ? m.start(): contentOfFile.length();
            technicalList.add(contentOfFile.substring(begin, end).toLowerCase());
            count++;
        }
        String[] result=technicalList.toArray(new String[count]);
        System.out.println(count);
        for (String s:result){
            System.out.println(s);
        }
        return result;
    };
    
    Invoice parseTranscripts(String[] transcripts){
        ArrayList<Product> products=getProductsList();
        int length=products.size();
        ArrayList<String> productNames = new ArrayList<>(); 
        for (int i=0; i<length; i++){
            productNames.set(i, products.get(i).getName().replace("",""));
        }
        for (String currentLine:transcripts){
            Matcher mStart = Pattern.compile(lineDelimiter).matcher(currentLine);
            mStart.find();
            int begin = mStart.end();
            //Matcher mEnd =  Pattern.compile(lineDelimiter+MESSAGE_AUTHOR_PATTERN).matcher(currentLine);
            int end = currentLine.indexOf(":", begin);
            String customerId = currentLine.substring(begin, end);
            if (!(customerId.equals(HOST))){
                String messageFromCustomer=currentLine.substring(end);
                String[] probablyOrders=messageFromCustomer.split(",");
                //TODO insert null pointer control
                for (String probablyOrder: probablyOrders){
                    Matcher m = Pattern.compile("[a-z ]+").matcher(probablyOrder);
                    while (m.find()){
                        String probablyProduct = m.group().replace(" ", "");
                        if (productNames.contains(probablyProduct)){
                            String quantityRegex="\\d+(\\.\\d+)?(kg|gm)";
                            
                        }
                    }
                }
            }

        }
        return new Invoice();
    }

    public String getLineDelimiter() {
        return lineDelimiter;
    }

    public void setLineDelimiter(String lineDelimiter) {
        this.lineDelimiter = lineDelimiter;
    }
}
