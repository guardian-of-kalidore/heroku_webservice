/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kalidore.herokumicro.model;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author ahill
 */
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Kore {
    private int id;
    private String name;
    private String mainPic;
    private String thumbNail;
    private Owner owner;
    private String color;
    private Map<String, List<String>> tagMap;
    private List<Tag> tags;
}
