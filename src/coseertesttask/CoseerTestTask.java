/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coseertesttask;

import java.io.File;

/**
 *
 * @author dkosyakov
 */
public class CoseerTestTask {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        InvoiceMaker i=new InvoiceMaker();
        i.setLineDelimiter(InvoiceMaker.CHAT_LINE_DELIMITER);
        try{
        i.makeJSONinvoice(i.parseTranscripts(i.getTranscriptsFromRTF(new File(
                //put your path here
                "C:\\Users\\dkosyakov\\Desktop\\IT\\TestTasks\\coseer\\Project\\Sanitized7.rtf"))));
        } catch (Exception e){e.printStackTrace();}
    }
    
}
