package com.kalidore.herokumicro;

import com.kalidore.herokumicro.dao.*;
import com.kalidore.herokumicro.model.Kore;
import com.kalidore.herokumicro.model.Owner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
        resetHerd();
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
    public Kore getKoreById(@PathVariable int id) {
        return dao.getKoreById(id);
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
    
    @RequestMapping(value = "/kore/name/{name}", method = RequestMethod.GET)
    @ResponseBody
    public List<Kore> getKoreByName(@PathVariable String name) {
        return dao.getKoreByName(name);
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
    public Kore addNewKore(@RequestBody Kore kore){
        dao.addKore(kore);
        return kore;
    }
    
    @ResponseBody
    @RequestMapping(value = "/kore/id/{id}", method = RequestMethod.POST)
    public Kore updateKoreInfo(@PathVariable int id, @RequestBody Kore kore){
        kore.setId(id);
        dao.updateKoreInfo(kore);
        return kore;
    }
    
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/kore/id/{id}/owner/{ownerId}", method = RequestMethod.PUT)
    public void updateKoreOwner(@PathVariable int id, @PathVariable int ownerId){
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
    
    
    
    @ResponseBody @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler({NoSuchLlamaException.class, BadUserInputException.class, Exception.class})
    public Map<String, String> processException(Exception e){
        Map<String, String> error = new HashMap<>();
        error.put("message", e.getMessage());
        return error;
    }
    
    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void reset() {
        resetHerd();
    }
    
    private void resetHerd(){
        
    }

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
}
