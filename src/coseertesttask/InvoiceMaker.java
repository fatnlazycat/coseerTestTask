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
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author dkosyakov
 */
public class InvoiceMaker {
    
    static final String RTF_LINE_DELIMITER = "\\\\par(\\\\\\w)*";
    static final String CHAT_LINE_DELIMITER = "\\d?\\d/\\d{2}/\\d{4}, \\d?\\d:\\d{2}\\s(a|p)m - ";
    static final String HOST="host";
    static final String JSON_INVOICE_START = "{\"Invoice\":[\n";
    private String lineDelimiter;
    
    ArrayList<Product> getProductsList(){
        /*read a list of goods from database or whatever else
        //we should obviously rewrite this since when using in real life application
        //here we just fill the resulting array without involving any external resources*/
        ArrayList<Product> result=new ArrayList<>();
        int length=10;
        String[] names={"tomatoes", "onions", "cucumber", "potatoes", "cauliflower", "carrot", "ladyfinger", "pumpkin", "capsicum", "frenchbeans"};
        double[] prices={0.5, 1, 3, 7.1, 0.2, 1.1, 1.2, 1.05, 2.5, 5.2, 2.0};
        for (int i=0; i<length; i++){
            Product g=new Product();
            g.setId(i);
            g.setName(names[i]);
            g.setPrice(prices[i]);
            result.add(g);
        }
        return result;
    }
    
    String[] getTranscriptsFromRTF(File file) throws Exception {
        String contentOfFile="";
        BufferedReader brRead=new BufferedReader(new FileReader(file));
        String newLine="";
        while (!(newLine==null)) {
            contentOfFile=contentOfFile+newLine.toLowerCase();
            newLine=brRead.readLine();
        }
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
            technicalList.add(contentOfFile.substring(begin, end));
            count++;
        }
        String[] result=technicalList.toArray(new String[count]);
        System.out.println(count);
        for (String s:result){
            System.out.println(s);
        }
        return result;
    };
    
    Invoice[] parseTranscripts(String[] transcripts){
        ArrayList<Product> products=getProductsList();
        int length=products.size();
        ArrayList<String> productNames = new ArrayList<>(); 
        
        String quantityRegex="\\d+(\\.\\d+)?";
        
        ArrayList<Invoice> resultingList = new ArrayList<>();
        
        for (int i=0; i<length; i++){
            productNames.add(i, products.get(i).getName());
        }
        for (String currentLine:transcripts){
            Matcher mStart = Pattern.compile(lineDelimiter).matcher(currentLine);
            mStart.find();
            int begin = mStart.end();
            int end = currentLine.indexOf(":", begin);
            String customerName = currentLine.substring(begin, end);
            if (!(customerName.equals(HOST))){
                Invoice invoice = new Invoice();
                invoice.setCustomer(new Customer()); //setting new customer because we haven't got the Customers base yet
                invoice.getCustomer().setName(customerName);
                
                Map<Product, Double> orders = new HashMap<>();
                
                String messageFromCustomer=currentLine.substring(end);
                String[] probablyOrders=messageFromCustomer.split(",");
                //TODO insert null pointer control
                for (String probablyOrder: probablyOrders){
                    Matcher m = Pattern.compile("[a-z]+").matcher(probablyOrder);
                    
                    while (m.find()){
                        String probablyProduct = m.group();
                        int numberOfProductInList = productNames.indexOf(probablyProduct);
                        if (numberOfProductInList>=0){
                            Matcher quantityMatcher = Pattern.compile(quantityRegex).matcher(probablyOrder);
                            if (quantityMatcher.find()){
                                String quantity=quantityMatcher.group();
                                orders.put(products.get(numberOfProductInList), Double.parseDouble(quantity));
                            }
                        }
                    }
                }
                if (!(orders.isEmpty())){
                    invoice.setOrder(orders);
                    resultingList.add(invoice);
                }
            }
        }
        return resultingList.toArray(new Invoice[0]);
    }
    
    public void makeJSONinvoice(Invoice[] invoices){
        for (Invoice invoice : invoices){
            StringBuilder sb=new StringBuilder(JSON_INVOICE_START);
            sb.append("{\"CustomerName\":\""+invoice.getCustomer().getName()+"\", \"Order\":[\n");
            invoice.getOrder().forEach((Product p, Double d) -> {
                sb.append("{\"product\":\""+p.getName()+"\", \"quantity\":\""+d+"\"},\n");
            });
            sb.delete(sb.length()-2, sb.length()-1);
            sb.append("]}\n]}");
            System.out.println(sb);
        }
    }
    
    public String getLineDelimiter() {
        return lineDelimiter;
    }

    public void setLineDelimiter(String lineDelimiter) {
        this.lineDelimiter = lineDelimiter;
    }
}
