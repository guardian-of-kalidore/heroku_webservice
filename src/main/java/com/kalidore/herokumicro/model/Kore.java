/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kalidore.herokumicro.model;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author ahill
 */
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Kore {
    private int id;
    private String name;
    private Picture mainPic;
    private Owner owner;
    private List<Picture> pics = new ArrayList<>();
    
    public Kore(int id, String name, String mainPicUrl, String ownerName){
        this.id = id;
        this.name = name;
        this.mainPic = new Picture();
        this.mainPic.setUrl(mainPicUrl);
        
        this.owner = new Owner();
        this.owner.setName(ownerName);
        
        this.pics.add(mainPic);
    }
    
    public void setMainPic(String picUrl){
        this.mainPic = new Picture();
        this.mainPic.setUrl(picUrl);
        this.pics.add(mainPic);
    }
}
