package com.kalidore.herokumicro;

import com.kalidore.herokumicro.dao.*;
import com.kalidore.herokumicro.model.Geneology;
import com.kalidore.herokumicro.model.Kore;
import com.kalidore.herokumicro.model.Owner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ahill
 */
@CrossOrigin // Only enable if you want to use this to prototype local demos
@Controller
@EnableAutoConfiguration
public class WebserviceControllerProto {

    private KoreDao dao;

    public WebserviceControllerProto() {
        dao = new KoreDaoImpl();
    }

    /*
    *   ___   _  _______  ______    _______           ______    _______  _______  ______          
    *  |   | | ||       ||    _ |  |       |         |    _ |  |       ||   _   ||      |         
    *  |   |_| ||   _   ||   | ||  |    ___|   ____  |   | ||  |    ___||  |_|  ||  _    |        
    *  |      _||  | |  ||   |_||_ |   |___   |____| |   |_||_ |   |___ |       || | |   |        
    *  |     |_ |  |_|  ||    __  ||    ___|         |    __  ||    ___||       || |_|   |        
    *  |    _  ||       ||   |  | ||   |___          |   |  | ||   |___ |   _   ||       |        
    *  |___| |_||_______||___|  |_||_______|         |___|  |_||_______||__| |__||______|    
    * 
     */
    @RequestMapping(value = "/kore/id/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Map getKoreById(@PathVariable int id) {
        return dao.getKoreDetails(id);
    }

    @RequestMapping(value = "/kore/all", method = RequestMethod.GET)
    @ResponseBody
    public List<Kore> allTheKore() {
        return dao.getAllKore();
    }

    @RequestMapping(value = "/kore/owner/{id}", method = RequestMethod.GET)
    @ResponseBody
    public List<Kore> getKoreByOwnerId(@PathVariable int id) {
        return dao.getOwnerKore(id);
    }

    @RequestMapping(value = "/kore/owner/{name}", method = RequestMethod.GET)
    @ResponseBody
    public List<Kore> getKoreByOwnerName(@PathVariable String name) {
        return dao.getOwnerKore(name);
    }

    @RequestMapping(value = "/kore/name/{name}", method = RequestMethod.GET)
    @ResponseBody
    public List<Kore> getKoreByName(@PathVariable String name) {
        return dao.getKoreByName(name);
    }

    @ResponseBody
    @RequestMapping(value = "/kore/random", method = RequestMethod.GET)
    public Kore getRandomKore() {
        return dao.getRandomKore();
    }

    /*
    *  ___   _  _______  ______    _______           _     _  ______    ___  _______  _______    
    *  |   | | ||       ||    _ |  |       |         | | _ | ||    _ |  |   ||       ||       |   
    *  |   |_| ||   _   ||   | ||  |    ___|   ____  | || || ||   | ||  |   ||_     _||    ___|   
    *  |      _||  | |  ||   |_||_ |   |___   |____| |       ||   |_||_ |   |  |   |  |   |___    
    *  |     |_ |  |_|  ||    __  ||    ___|         |       ||    __  ||   |  |   |  |    ___|   
    *  |    _  ||       ||   |  | ||   |___          |   _   ||   |  | ||   |  |   |  |   |___    
    *  |___| |_||_______||___|  |_||_______|         |__| |__||___|  |_||___|  |___|  |_______|  
     */
    @ResponseBody
    @RequestMapping(value = "/kore/new", method = RequestMethod.POST)
    public Kore addNewKore(HttpServletRequest request) {
        System.out.println("Creating a new Kore: ");
        System.out.println(request.getParameterMap().entrySet());
        Kore kore = this.makeKoreFromMap(request);
        dao.addKore(kore);
        return kore;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/kore/id/{id}", method = RequestMethod.POST)
    public void updateKoreInfo(HttpServletRequest request, @PathVariable int id) {
        try {
            System.out.println("Logging incoming parameters...");
            for(String param : request.getParameterMap().keySet()){
                System.out.print(param + " : ");
                for( String val : request.getParameterMap().get(param)){
                    System.out.print(val + " ");
                }
                System.out.println("");
            }
            
            Kore kore = this.makeKoreFromMap(request);
            Geneology genes = this.getGenesFromMap(request);
            kore.setId(id);
            genes.setKoreId(id);

//            System.out.println(kore);
//            System.out.println(genes);
            
            if (kore == null) {
                System.out.println("Something went bad w/ the kore.");
                return;
            } else if (genes.getDamId() > 0 || genes.getSireId() > 0) {
                System.out.println("Found some information. Updating heritage too.");
                dao.updateKoreInfo(kore, genes);
            } else {
                System.out.println("Trying to do a basic update.");
                dao.updateKoreBasicInfo(kore);
            }
        } catch (Exception e) {
            System.out.println("Problem in the update.");
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/kore/id/{id}/owner/{ownerId}", method = RequestMethod.PUT)
    public void updateKoreOwner(@PathVariable int id, @PathVariable int ownerId) {
        dao.assignNewOwner(id, ownerId);
    }


    /*
    *  _______  _     _  __    _  _______  ______             ______    _______  _______  ______       
    * |       || | _ | ||  |  | ||       ||    _ |           |    _ |  |       ||   _   ||      |      
    * |   _   || || || ||   |_| ||    ___||   | ||     ____  |   | ||  |    ___||  |_|  ||  _    |     
    * |  | |  ||       ||       ||   |___ |   |_||_   |____| |   |_||_ |   |___ |       || | |   |     
    * |  |_|  ||       ||  _    ||    ___||    __  |         |    __  ||    ___||       || |_|   |     
    * |       ||   _   || | |   ||   |___ |   |  | |         |   |  | ||   |___ |   _   ||       |     
    * |_______||__| |__||_|  |__||_______||___|  |_|         |___|  |_||_______||__| |__||______|     
     */
    @RequestMapping(value = "/owner/all", method = RequestMethod.GET)
    @ResponseBody
    public List<Owner> getAllOwners() {
        return dao.getAllOwners();
    }

    @RequestMapping(value = "/owner/id/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Owner getOwnerById(@PathVariable int id) {
        return dao.getOwnerById(id);
    }

    /*
    *  _______  _     _  __    _  _______  ______             _     _  ______    ___  _______  _______ 
    * |       || | _ | ||  |  | ||       ||    _ |           | | _ | ||    _ |  |   ||       ||       |
    * |   _   || || || ||   |_| ||    ___||   | ||     ____  | || || ||   | ||  |   ||_     _||    ___|
    * |  | |  ||       ||       ||   |___ |   |_||_   |____| |       ||   |_||_ |   |  |   |  |   |___ 
    * |  |_|  ||       ||  _    ||    ___||    __  |         |       ||    __  ||   |  |   |  |    ___|
    * |       ||   _   || | |   ||   |___ |   |  | |         |   _   ||   |  | ||   |  |   |  |   |___ 
    * |_______||__| |__||_|  |__||_______||___|  |_|         |__| |__||___|  |_||___|  |___|  |_______|
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler({NoSuchLlamaException.class, BadUserInputException.class, Exception.class})
    public Map<String, String> processException(Exception e) {
        Map<String, String> error = new HashMap<>();
        error.put("message", e.getMessage());
        return error;
    }

    /*
    *  _______  _______  __    _  _______  ___  _______ 
    * |       ||       ||  |  | ||       ||   ||       |
    * |       ||   _   ||   |_| ||    ___||   ||    ___|
    * |       ||  | |  ||       ||   |___ |   ||   | __ 
    * |      _||  |_|  ||  _    ||    ___||   ||   ||  |
    * |     |_ |       || | |   ||   |    |   ||   |_| |
    * |_______||_______||_|  |__||___|    |___||_______|
     */
    public static void main(String[] args) throws Exception {
        SpringApplication.run(WebserviceControllerProto.class, args);
    }

    @RequestMapping("/testConnection")
    @ResponseBody
    String testConnection() {

        System.out.println("-------- PostgreSQL JDBC Connection Testing ------------");

        try {

            Class.forName("org.postgresql.Driver");

        } catch (ClassNotFoundException e) {

            System.out.println("Where is your PostgreSQL JDBC Driver? "
                    + "Include in your library path!");
            e.printStackTrace();
            return "Driver exploded";

        }

        System.out.println("PostgreSQL JDBC Driver Registered!");

        Connection connection = null;

        try {

            connection = DriverManager.getConnection(
                    System.getenv("DB_URL"),
                    System.getenv("DB_USR"),
                    System.getenv("DB_PW"));

        } catch (SQLException e) {

            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return "Connection failed";

        }

        if (connection != null) {
            System.out.println("You made it, take control your database now!");
            return "It worked!";
        } else {
            System.out.println("Failed to make connection!");
        }

        return "Try again...";
    }

    /*
    *  __   __  _______  ___      _______  _______  ______   
    * |  | |  ||       ||   |    |       ||       ||    _ |  
    * |  |_|  ||    ___||   |    |    _  ||    ___||   | ||  
    * |       ||   |___ |   |    |   |_| ||   |___ |   |_||_ 
    * |       ||    ___||   |___ |    ___||    ___||    __  |
    * |   _   ||   |___ |       ||   |    |   |___ |   |  | |
    * |__| |__||_______||_______||___|    |_______||___|  |_|
     */
    private Geneology getGenesFromMap(HttpServletRequest request) {
        Geneology genes = new Geneology();

        try {
            int damId = Integer.parseInt(request.getParameter("damId"));
            genes.setDamId(damId);
        } catch (Exception e) {
            System.out.println("No dam.");
            genes.setDamId(-1);
        }

        try {
            int sireId = Integer.parseInt(request.getParameter("sireId"));
            genes.setSireId(sireId);
        } catch (Exception e) {
            System.out.println("No sire.");
            genes.setSireId(-1);
        }

        return genes;
    }

    private Kore makeKoreFromMap(HttpServletRequest request) {
        Kore kore = new Kore();

        String name = request.getParameter("koreName");
        String newOwner = request.getParameter("newOwner");
        String ownerId = request.getParameter("ownerId");

        String picUrl = request.getParameter("picUrl");
        String breed = request.getParameter("breed");
        String color = request.getParameter("color");

        if (nullOrEmpty(name) || nullOrEmpty(picUrl)) {
            return null;
        }

        try {
            int id = Integer.parseInt(ownerId);
            Owner o = new Owner();
            o.setId(id);
            kore.setOwner(o);
        } catch (Exception e) {
            System.out.println("Bad ownerId.");
        }

        kore.setName(name);
        kore.setMainPic(picUrl);
        kore.setColor(color);

        return kore;
    }

    private boolean nullOrEmpty(String testMe) {
        return testMe == null || testMe.isEmpty();
    }

}
