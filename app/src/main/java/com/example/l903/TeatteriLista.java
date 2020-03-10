package com.example.l903;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class TeatteriLista {
    private String nimi;
    private List<String> elokuva_aika;
    private List<String> teatterilista = null;
    private ArrayList<Teatteri> nimilista =null;
    private static TeatteriLista tl= new TeatteriLista();



    //instanssin saaminen
    public static TeatteriLista getInstance(){
        return tl;
    }


    //luetaan teatterit ja lisätään ne listaan
    public void lueXML() {

        try {
            //parsetetaan XML
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            String teatteritUrl = "https://www.finnkino.fi/xml/TheatreAreas/";
            Document doc = builder.parse(teatteritUrl);
            doc.getDocumentElement().normalize();

            //tehdään lista siitä
            NodeList nList = doc.getDocumentElement().getElementsByTagName("TheatreArea");
            nimilista = new ArrayList<>();

            //etsitään halutut tiedot ja tallennetaan ne listaan
            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                System.out.println("Element" + node.getNodeName());

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    System.out.print("ID on: ");
                    System.out.println(element.getElementsByTagName("ID").item(0).getTextContent());
                    System.out.print("Teatterin nimi on: ");

                    System.out.println(element.getElementsByTagName("Name").item(0).getTextContent());
                    Teatteri theater = new Teatteri(element.getElementsByTagName("ID").item(0).getTextContent(), element.getElementsByTagName("Name").item(0).getTextContent());
                    nimilista.add(theater);
                }
            }
            //tehdään listan joka tulostetaan spinneriin
            teatterilista = new ArrayList<>();
            for (int j = 0; j < nimilista.size(); j++) {
                nimi = nimilista.get(j).getName();
                teatterilista.add(nimi);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } finally {
            System.out.println("*********tehty*******");
        }
    }

    public List<String> getTeatterilista() {
        return teatterilista;
    }
    public List<String> getElokuvalista(){return elokuva_aika;}

    public  void luePvm(String teatteri, String pvm) {
        String id = null;
        elokuva_aika = new ArrayList<>();
        try {
            //etsitään annetun teatteri id
            for (int j = 0; j < nimilista.size(); j++) {
                if (nimilista.get(j).getName() == teatteri) {
                    id = nimilista.get(j).getId();
                    //System.out.println(id);
                }
            }
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            //saadaan kaikki teatterit
            if (id.contains("1029")) {

                for (int j = 1; j < 9; j++) {
                    String osoite = String.format("https://www.finnkino.fi/xml/Schedule/?area=%s&dt=%s", 1013 + j, pvm);

                    Document doc1 = builder.parse(osoite);
                    doc1.getDocumentElement().normalize();
                    NodeList lista = doc1.getDocumentElement().getElementsByTagName("Show");
                    for (int i = 0; i < lista.getLength(); i++) {
                        Node node = lista.item(i);
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            Element element = (Element) node;
                            elokuva_aika.add("Teatteri: " + element.getElementsByTagName("Theatre").item(0).getTextContent() + "\n" + "Elokuva: " + element.getElementsByTagName("Title").item(0).getTextContent() + "\nAlkamisajankohta: " + element.getElementsByTagName("dttmShowStart").item(0).getTextContent().substring(11, 16));
                        }
                    }
                }
                //lisätään lappenrannna teatteri
                String osoite = String.format("https://www.finnkino.fi/xml/Schedule/?area=%s&dt=%s", 1041, pvm);
                Document doc2 = builder.parse(osoite);
                doc2.getDocumentElement().normalize();
                NodeList lista2 = doc2.getDocumentElement().getElementsByTagName("Show");
                for (int i = 0; i < lista2.getLength(); i++) {
                    Node node = lista2.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;

                        elokuva_aika.add("Teatteri: " + element.getElementsByTagName("Theatre").item(0).getTextContent() + "\n" + "Elokuva: " + element.getElementsByTagName("Title").item(0).getTextContent() + "\nAlkamisajankohta: " + element.getElementsByTagName("dttmShowStart").item(0).getTextContent().substring(11, 16));
                    }
                }

            } else {
                System.out.println("id = " + id);


                //parsetetaan XML, jossa elokuvat pävittäin jokaisesta teatterista

                String teatteritUrl2 = String.format("https://www.finnkino.fi/xml/Schedule/?area=%s&dt=%s", id, pvm);
                Document doc = builder.parse(teatteritUrl2);
                doc.getDocumentElement().normalize();

                //löytyy show-merkin  alta
                NodeList nList = doc.getDocumentElement().getElementsByTagName("Show");


                //System.out.println("listan pituus on" + nList.getLength());
                //täytetään lista joka tulostetaan näytölle
                for (int i = 0; i < nList.getLength(); i++) {
                    Node node = nList.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;

                        //nämä printit on vain koodin kirjoittajalle
                        System.out.println(element.getElementsByTagName("dttmShowStart").item(0).getTextContent());
                        System.out.print("Leffa on: ");
                        System.out.println(element.getElementsByTagName("Title").item(0).getTextContent());
                        System.out.print("Teatterin ID on: ");
                        System.out.println(element.getElementsByTagName("TheatreID").item(0).getTextContent());

                        elokuva_aika.add("Teatteri: " + element.getElementsByTagName("Theatre").item(0).getTextContent() + "\n" + "Elokuva: " + element.getElementsByTagName("Title").item(0).getTextContent() + "\nAlkamisajankohta: " + element.getElementsByTagName("dttmShowStart").item(0).getTextContent().substring(11, 16));
                    }

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } finally {
            System.out.println("*********tehty*******");
        }
    }
}