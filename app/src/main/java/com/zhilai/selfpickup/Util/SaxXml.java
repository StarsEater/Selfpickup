package com.zhilai.selfpickup.Util;


import com.zhilai.selfpickup.Object.HardInfo;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class SaxXml {
     public static HardInfo sax2xml(InputStream is) throws ParserConfigurationException, SAXException, IOException {
         SAXParserFactory spf = SAXParserFactory.newInstance();
         //初始化Sax解析器
         SAXParser sp = spf.newSAXParser();
         //新建解析处理器
         MyHandler handler = new MyHandler();
         //把解析交给处理器
         sp.parse(is,handler);
         return handler.getData();
     }
     public static class MyHandler extends DefaultHandler{
         private HardInfo hardInfo;

         private String tempString;
         /**
          *  解析到文档开始调用，一般做初始化操作
          */
         @Override
         public void startDocument() throws SAXException {
             super.startDocument();
         }
         /**
          * 解析到文档末尾调用，一般做回收操作
          */
         @Override
         public  void endDocument() throws SAXException {
             super.endDocument();
         }
         /**
          * 每读到一个元素就调用改方法
          */
         @Override
         public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
             if("HardInfo".equals(qName)){
                 hardInfo = new HardInfo();
             }
             super.startElement(uri,localName,qName,attributes);
         }

         @Override
         public void endElement(String uri, String localName, String qName) throws SAXException {
             if("ztgID".equals(qName)){
                 hardInfo.setZtgID(tempString);
             }else if("cabinetTotalRow".equals(qName)){
                 hardInfo.setCabinetTotalRow(Integer.parseInt(tempString));
             }else if("cabinetTotalCol".equals(qName)){
                 hardInfo.setCabinetTotalCol(Integer.parseInt(tempString));
             }else if("cabinetTotalNum".equals(qName)){
                 hardInfo.setCabinetTotalNum(Integer.parseInt(tempString));
             }else if("HardInfo".equals(qName)){
             }
             super.endElement(uri,localName,qName);
         }
         @Override
         public void characters(char[] ch, int start, int length) throws SAXException {
             tempString = new String(ch,start,length);
             super.characters(ch,start,length);
         }
         public HardInfo getData(){
             return hardInfo;
         }
     }
    /**
     *
     * <ConfigInfo>
     *     <HardInfo>
     *         <ztgID>"100001"</ztgID>
     *         <cabinetTotalRow>4</cabinetTotalRow>
     *         <cabinetTotalCol>4</cabinetTotalCol>
     *         <cabinetTotalNum>116</cabinetTotalNum>
     *     </HardInfo>
     * </ConfigInfo>

     */
}
