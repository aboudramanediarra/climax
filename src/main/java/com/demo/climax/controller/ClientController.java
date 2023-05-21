package com.demo.climax.controller;

import com.demo.climax.bean.Moyenne;
import com.demo.climax.entity.Client;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.demo.climax.repository.ClientRepository;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@RestController
public class ClientController {

    private final ClientRepository clientRepository;
    Model model1;
    public ClientController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;

    }

    @GetMapping("/clients")
    public ResponseEntity<List<Client>> getAllClient(Model model) {
        List<Client> list = clientRepository.findAll();
        model.addAttribute("listClients", list);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/moyenne")
    public ResponseEntity<List<Moyenne>> getMoyenne(){

        HashMap<String, Long> list = new HashMap<String, Long>();
        List<Object[]> resultList = clientRepository.findByProfession();
        for (Object[] borderTypes: resultList) {
            list.put((String)borderTypes[0], Long.valueOf((Long)borderTypes[1]));
        }

        List<Moyenne> moyenneList = new ArrayList<>();
        for (String i : list.keySet()) {
            System.out.println("Job: " + i + " Moyenne des salaires: " + list.get(i));
            Moyenne moyenne = new Moyenne();
            moyenne.setProfession(i);
            moyenne.setSalaireMoy(Math.toIntExact(list.get(i)));
            moyenneList.add(moyenne);
        }
        return new ResponseEntity<>(moyenneList, HttpStatus.OK);
    }
    @PostMapping("/import")
    public String fileUpload(@RequestParam("file") MultipartFile file) throws IOException, InvalidFormatException, ParserConfigurationException, SAXException {
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        if (extension.equals("xlsx")) {
            readXLSXFile(file.getInputStream());
        } else if (extension.equals("csv")) {
            readCSVFile(file.getInputStream());
        } else if(extension.equals("xml")){
            readXMLFile(file.getInputStream());
        }else if (extension.equals("json")){
            readJSONFile(file.getInputStream());
        }else if(extension.equals("txt")){
            readTXTFile(file.getInputStream());
        }
        return "index";
    }

    public void readXLSXFile(InputStream inputStream) {
        try {
            //XLSX file must have a header as nom,prenom,age,profession,salaire
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet firstSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = firstSheet.iterator();
            while (iterator.hasNext()) {
                Row nextRow = iterator.next();
                Client client = new Client();
                try {
                    String nom = nextRow.getCell(0).getStringCellValue();
                    String prenom = nextRow.getCell(1).getStringCellValue();
                    int age = (int) nextRow.getCell(2).getNumericCellValue();
                    String profession = nextRow.getCell(3).getStringCellValue();
                    int salaire = (int) nextRow.getCell(4).getNumericCellValue();
                    client.setNom(nom);
                    client.setPrenom(prenom);
                    client.setAge(age);
                    client.setProfession(profession);
                    client.setSalaire(salaire);
                    clientRepository.save(client);
                } catch (Exception e) {
                }
            }
            workbook.close();
        } catch (Exception e) {
        }
    }

    public void readCSVFile(InputStream file) {
        String delimiter = ",";
        String line = "";
        try {
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(file, "UTF-8"));
            while ((line = fileReader.readLine()) != null) {
                Client client = new Client();
                String[] clients = line.split(delimiter);
                client.setNom(clients[0]);
                client.setPrenom(clients[1]);
                client.setAge(Integer.parseInt(clients[2]));
                client.setProfession(clients[3]);
                client.setSalaire(Integer.parseInt(clients[4].replace(";", "")));
                clientRepository.save(client);
            }
            file.close();
        } catch (IOException | UncheckedIOException e) {
        }
    }
    public void readXMLFile(InputStream inputStream) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(inputStream);
        doc.getDocumentElement().normalize();
        Client client = new Client();
        NodeList nodeList = doc.getElementsByTagName("client");
        for (int itr = 0; itr < nodeList.getLength(); itr++) {
            Node node = nodeList.item(itr);
            System.out.println("\nNode Name :" + node.getNodeName());
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                client.setNom(eElement.getElementsByTagName("nom").item(0).getTextContent());
                client.setPrenom(eElement.getElementsByTagName("prenom").item(0).getTextContent());
                client.setAge(Integer.parseInt(eElement.getElementsByTagName("age").item(0).getTextContent()));
                client.setProfession(eElement.getElementsByTagName("profession").item(0).getTextContent());
                client.setSalaire(Integer.parseInt(eElement.getElementsByTagName("salaire").item(0).getTextContent()));
                clientRepository.save(client);
            }
        }
    }

    public void readJSONFile(InputStream file) throws IOException {
        JSONParser jsonParser = new JSONParser();
        JSONObject clients;
        try {
            //Read JSON file
            Object obj = jsonParser.parse(new InputStreamReader(file));
            JSONArray clientList = (JSONArray) obj;

            Iterator iterator = clientList.iterator();
            while (iterator.hasNext()){
                JSONObject cltObject = (JSONObject) iterator.next();
                cltObject.get("client");
                String nom = (String) cltObject.get("nom");
                String prenom = (String) cltObject.get("prenom");
                String age = (String) cltObject.get("age");
                String profession = (String) cltObject.get("profession");
                String salaire = (String) cltObject.get("salaire");
                Client client = new Client();
                client.setNom(nom);
                client.setPrenom(prenom);
                client.setAge(Integer.parseInt(age));
                client.setProfession(profession);
                client.setSalaire(Integer.parseInt(salaire));
                clientRepository.save(client);
                System.out.println(nom +" "+ " "+ prenom+" "+ age+" "+ profession+" "+salaire);
            }
            file.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void readTXTFile(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while((line = br.readLine()) != null){
            //process the line
            try{
                String nom = line.substring(0,line.indexOf(","));
                line = line.substring(line.indexOf(",")+1);
                String prenom = line.substring(0,line.indexOf(","));
                line = line.substring(line.indexOf(",")+1);
                int age = Integer.parseInt(line.substring(0,line.indexOf(",")));
                line = line.substring(line.indexOf(",")+1);
                String profession = line.substring(0,line.indexOf(","));
                line = line.substring(line.indexOf(",")+1);
                int salaire = Integer.parseInt(line.substring(0,line.indexOf(";")));
                Client client = new Client();
                client.setNom(nom);
                client.setPrenom(prenom);
                client.setAge(age);
                client.setProfession(profession);
                client.setSalaire(salaire);
                clientRepository.save(client);
            }catch (Exception e){
            }
        }
        br.close();
    }
}