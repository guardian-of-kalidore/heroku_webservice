/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kalidore.herokumicro.dao;

import com.kalidore.herokumicro.model.Geneology;
import com.kalidore.herokumicro.model.Kore;
import com.kalidore.herokumicro.model.Owner;
import com.kalidore.herokumicro.model.Tag;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ahill
 */
public interface KoreDao {
    
    // CREATE
    public void addKore(Kore kore);
    public void addOwner(Owner owner);
    public void addOwner(String ownerName);
    
    // READ
    public List<Kore> getAllKore();
    public List<Kore> getKoreByName(String name);
    
    public Kore getKoreById(int id);
    public Map getKoreDetails(int id);
    public Kore getRandomKore();
    
    public List<Kore> getOwnerKore(Owner owner);
    public List<Kore> getOwnerKore(int ownerId);
    public List<Kore> getOwnerKore(String name);
    
    public List<Owner> getAllOwners();
    public Owner getOwnerById(int id);
    public List<Owner> getOwnerByName(String name);
    
    public List<Tag> getAllTags();
    
    // UPDATE
    public void updateKoreBasicInfo(Kore kore);
    public void updateKoreInfo(Kore kore, Geneology genes);
    public void updateOwnerInfo(Owner owner);
    
    public void assignNewOwner(int koreId, int ownerId);
    
    // DELETE
    public void deleteKore(int id);
    public void deleteOwner(int id);
    
}
