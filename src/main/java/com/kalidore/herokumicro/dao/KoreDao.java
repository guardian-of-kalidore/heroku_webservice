/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kalidore.herokumicro.dao;

import com.kalidore.herokumicro.model.Kore;
import com.kalidore.herokumicro.model.Owner;
import java.util.List;

/**
 *
 * @author ahill
 */
public interface KoreDao {
    
    // CREATE
    public Kore addKore(Kore kore);
    public Owner addOwner(Owner owner);
    
    // READ
    public List<Kore> getAllKore();
    public List<Kore> getKoreByName(String name);
    
    public Kore getKoreById(int id);
    public Kore getRandomKore();
    
    public List<Kore> getOwnerKore(Owner owner);
    public List<Kore> getOwnerKore(int ownerId);
    
    public List<Owner> getAllOwners();
    public Owner getOwnerById(int id);
    
    // UPDATE
    public void updateKoreInfo(Kore kore);
    public void updateOwnerInfo(Owner owner);
    
    public void assignNewOwner(int koreId, int ownerId);
    
    // DELETE
    public Kore deleteKore(int id);
    public Owner deleteOwner(int id);
    
}
