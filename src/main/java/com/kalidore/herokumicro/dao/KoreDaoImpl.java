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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 *
 * @author ahill
 */
public class KoreDaoImpl implements KoreDao {

    private JdbcTemplate jdbcTemplate = null;
    private Map<String, Map<Integer, Tag>> tags;

    public KoreDaoImpl() {
        jdbcTemplate = new JdbcTemplate();
        tags = new HashMap<>();

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(System.getenv("DB_URL"));
        dataSource.setUsername(System.getenv("DB_USR"));
        dataSource.setPassword(System.getenv("DB_PW"));

        jdbcTemplate.setDataSource(dataSource);
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
    public static String SQL_SELECT_ALL_KORE = "SELECT * FROM public.\"kore\" AS k "
            + " LEFT JOIN public.\"owners\" AS o ON k.ownerid = o.id "
            + " ORDER BY k.name";

    @Override
    public List<Kore> getAllKore() {
        return jdbcTemplate.query(SQL_SELECT_ALL_KORE, new KoreMapper());
    }

    public static String SQL_SELECT_ALL_KORE_BY_KORE_ID = "SELECT * FROM public.\"kore\" AS k "
            + "LEFT JOIN public.\"owners\" AS o ON k.ownerid = o.id "
            + "WHERE k.id = ?";

    @Override
    public Kore getKoreById(int id) {
        try {
            return jdbcTemplate.queryForObject(SQL_SELECT_ALL_KORE_BY_KORE_ID, new KoreMapper(), id);
        } catch (Exception e) {
            this.logException("Tried to find a kore w/ this id " + id, e);
            return null;
        }
    }

    public static String SQL_SELECT_KORE_DETAILS_ID = "SELECT k.*, o.*, s.name as sirename, d.name as damname "
            + "FROM public.\"kore\" AS k "
            + " LEFT JOIN public.\"kore\" as s ON k.sire = s.id "
            + " LEFT JOIN public.\"kore\" as d ON k.dam = d.id "
            + " LEFT JOIN public.\"owners\" AS o ON k.ownerid = o.id "
            + " WHERE k.id = ?";

    @Override
    public Map getKoreDetails(int id) {
        try {
            Map<String, Object> kore = jdbcTemplate.queryForObject(SQL_SELECT_KORE_DETAILS_ID, new KoreDetailsMapper(), id);
            kore.put("tags", this.getTagsByKore(id));
            return kore;
        } catch (Exception e) {
            this.logException("Tried to find a kore details w/ this id " + id, e);
            return null;
        }
    }

    public static String SQL_SELECT_RANDOM_KORE = "SELECT * FROM public.\"kore\" AS k "
            + "LEFT JOIN public.\"owners\" AS o ON k.ownerid = o.id "
            + "ORDER BY RANDOM() LIMIT 1";

    @Override
    public Kore getRandomKore() {
        try {
            return jdbcTemplate.queryForObject(SQL_SELECT_RANDOM_KORE, new KoreMapper());
        } catch (Exception e) {
            this.logException("Tried to find a random kore", e);
            return null;
        }
    }

    public static String SQL_SELECT_ALL_KORE_BY_OWNER_NAME = "SELECT * FROM public.\"kore\" AS k "
            + " LEFT JOIN public.\"owners\" AS o ON k.ownerid = o.id "
            + " WHERE o.owner LIKE ? "
            + " ORDER BY k.name";

    @Override
    public List<Kore> getOwnerKore(String ownerName) {
        try {
            return jdbcTemplate.query(SQL_SELECT_ALL_KORE_BY_OWNER_NAME, new KoreMapper(), "%" + ownerName + "%");
        } catch (Exception e) {
            this.logException("Tried to find a bunch of kore w/ this owner " + ownerName, e);
            return null;
        }
    }

    public static String SQL_SELECT_ALL_KORE_BY_OWNER_ID = "SELECT * FROM public.\"kore\" AS k "
            + " LEFT JOIN public.\"owners\" AS o ON k.ownerid = o.id "
            + " WHERE o.id = ? "
            + " ORDER BY k.name ";

    @Override
    public List<Kore> getOwnerKore(Owner owner) {
        return this.getOwnerKore(owner.getId());
    }

    @Override
    public List<Kore> getOwnerKore(int ownerId) {
        return jdbcTemplate.query(SQL_SELECT_ALL_KORE_BY_OWNER_ID, new KoreMapper(), ownerId);
    }

    public static String SQL_SELECT_ALL_KORE_BY_NAME = "SELECT * FROM public.\"kore\" AS k "
            + " LEFT JOIN public.\"owners\" AS o ON k.ownerid = o.id "
            + " WHERE k.name LIKE ? "
            + " ORDER BY k.name";

    @Override
    public List<Kore> getKoreByName(String name) {
        try {
            return jdbcTemplate.query(SQL_SELECT_ALL_KORE_BY_NAME, new KoreMapper(), "%" + name + "%");
        } catch (Exception e) {
            this.logException("Tried to find a kore w/ this name " + name, e);
            return null;
        }
    }

    public static String SQL_SELECT_ALL_TAGS = "SELECT * FROM public.\"tags\" AS t ";

    public void populateAllTags() {
        try {
            List<Tag> allTags = jdbcTemplate.query(SQL_SELECT_ALL_TAGS, new TagMapper());
            for (Tag t : allTags) {

                if (!tags.containsKey(t.getTypeName())) {
                    tags.put(t.getTagName(), new HashMap<>());
                }

                tags.get(t.getTagName()).put(t.getTagId(), t);

            }
        } catch (Exception e) {
            this.logException("Tried to populate all the kore tags.", e);
        }

    }

    public static String SQL_SELECT_TAGS_KORE_ID = "SELECT * FROM public.\"koreTags\" AS kt "
            + " WHERE k.id = ? ";

    public Map<String, List<Tag>> getTagsByKore(int id) {

        if (tags.isEmpty()) {
            this.populateAllTags();
        }

        Map<String, List<Tag>> tagMap = null;

        try {
            tagMap = new HashMap<>();
            List<List<Tag>> tags = jdbcTemplate.query(SQL_SELECT_TAGS_KORE_ID, new SimpleTagMapper(), id);

            for (List<Tag> tagGroup : tags) {
                Tag x = tagGroup.get(0);
                tagMap.put(x.getTypeName(), new ArrayList<>());

            }

        } catch (Exception e) {
            this.logException("Tried to find a kore tags w/ this kore id " + id, e);
        }

        return tagMap;
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
    private static final String SQL_INSERT_NEW_KORE = "INSERT INTO public.\"kore\" "
            + " (name, pic, ownerId) VALUES (?, ?, ?)";

    @Override
    public void addKore(Kore kore) {
        jdbcTemplate.update(SQL_INSERT_NEW_KORE,
                kore.getName(),
                kore.getMainPic() == null ? null : kore.getMainPic(),
                kore.getOwner() == null ? null : kore.getOwner().getId());
    }

    private static final String SQL_UPDATE_KORE_NO_GENES = "UPDATE public.\"kore\" "
            + " SET name = ? , pic = ? , ownerid = ? , topcolor = ? "
            + " WHERE id = ?";

    @Override
    public void updateKoreBasicInfo(Kore kore) {
        jdbcTemplate.update(SQL_UPDATE_KORE_NO_GENES,
                kore.getName(),
                kore.getMainPic(),
                kore.getOwner() == null ? null : kore.getOwner().getId(),
                kore.getColor(),
                kore.getId());
    }

    private static final String SQL_UPDATE_KORE_HAS_GENES = "UPDATE public.\"kore\" "
            + " SET name = ? , pic = ? , ownerid = ? , topcolor = ? , dam = ? , sire = ?"
            + " WHERE id = ?";

    @Override
    public void updateKoreInfo(Kore kore, Geneology genes) {
        System.out.println("Updating kore# " + kore.getId()
                + "with owner# " + (kore.getOwner() == null ? null : kore.getOwner().getId())
                + "and dam # " + (genes.getDamId() > 0 ? genes.getDamId() : null)
                + "and sire # " + (genes.getSireId() > 0 ? genes.getSireId() : null)
        );

        jdbcTemplate.update(SQL_UPDATE_KORE_HAS_GENES,
                kore.getName(),
                kore.getMainPic(),
                kore.getOwner() == null ? null : kore.getOwner().getId(),
                kore.getColor(),
                genes.getDamId() > 0 ? genes.getDamId() : null,
                genes.getSireId() > 0 ? genes.getSireId() : null,
                kore.getId());
    }

    private static final String SQL_DELETE_KORE_BY_ID = "DELETE FROM public.\"kore\" "
            + " WHERE id = ?";

    @Override
    public void deleteKore(int id) {
        jdbcTemplate.update(SQL_DELETE_KORE_BY_ID, id);
    }

    public static String SQL_UPDATE_KORE_OWNER = "UPDATE public.\"kore\" AS k "
            + " SET k.ownerid = ? "
            + " WHERE k.id = ? ";

    @Override
    public void assignNewOwner(int koreId, int ownerId) {
        jdbcTemplate.update(SQL_UPDATE_KORE_OWNER, ownerId, koreId);
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

    public static String SQL_SELECT_ALL_OWNERS = "SELECT * FROM public.\"owners\" AS o ORDER BY o.owner";

    @Override
    public List<Owner> getAllOwners() {
        return jdbcTemplate.query(SQL_SELECT_ALL_OWNERS, new OwnerMapper());
    }

    public static String SQL_SELECT_OWNER_BY_ID = "SELECT * FROM public.\"owners\" AS o "
            + "WHERE o.id = ?";

    @Override
    public Owner getOwnerById(int id) {
        try {
            return jdbcTemplate.queryForObject(SQL_SELECT_OWNER_BY_ID, new OwnerMapper(), id);
        } catch (Exception e) {
            this.logException("Tried to find an owner w/ this id " + id, e);
            return null;
        }
    }

    public static String SQL_SELECT_OWNER_BY_NAME = "SELECT * FROM public.\"owners\" AS o "
            + "WHERE o.owner LIKE %?% "
            + "ORDER BY o.owner ";

    @Override
    public List<Owner> getOwnerByName(String name) {

        return jdbcTemplate.query(SQL_SELECT_OWNER_BY_NAME, new OwnerMapper(), name);

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
    public static String SQL_INSERT_NEW_OWNER = "INSERT INTO public.\"owners\" (owner) VALUES (?)";

    @Override
    public void addOwner(Owner owner) {
        jdbcTemplate.update(SQL_INSERT_NEW_OWNER, owner.getName());
    }

    @Override
    public void addOwner(String ownerName) {
        jdbcTemplate.update(SQL_INSERT_NEW_OWNER, ownerName);
    }

    @Override
    public void updateOwnerInfo(Owner owner) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteOwner(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /*
    *  __   __  _______  _______  _______  _______  ______    _______ 
    * |  |_|  ||   _   ||       ||       ||       ||    _ |  |       |
    * |       ||  |_|  ||    _  ||    _  ||    ___||   | ||  |  _____|
    * |       ||       ||   |_| ||   |_| ||   |___ |   |_||_ | |_____ 
    * |       ||       ||    ___||    ___||    ___||    __  ||_____  |
    * | ||_|| ||   _   ||   |    |   |    |   |___ |   |  | | _____| |
    * |_|   |_||__| |__||___|    |___|    |_______||___|  |_||_______|
     */
    private static final class KoreMapper implements RowMapper<Kore> {
//        CREATE TABLE public.kore
//        (
//            id bigint NOT NULL,
//            name text COLLATE pg_catalog."default" NOT NULL,
//            breed text COLLATE pg_catalog."default",
//            pic text COLLATE pg_catalog."default",
//            CONSTRAINT kore_pkey PRIMARY KEY (id)

        @Override
        public Kore mapRow(ResultSet rs, int rowNum) throws SQLException {
            Kore k = new Kore();
            k.setId(rs.getInt("id"));
            k.setName(rs.getString("name"));
            k.setMainPic(rs.getString("pic"));
            k.setThumbNail(rs.getString("thumbnail"));

            Owner o = new Owner();
            o.setId(rs.getInt("ownerid"));
            o.setName(rs.getString("owner"));
            k.setOwner(o);

            return k;
        }
    }

    private static final class KoreDetailsMapper implements RowMapper<Map> {

        @Override
        public Map mapRow(ResultSet rs, int rowNum) throws SQLException {

            Kore k = new Kore();
            k.setId(rs.getInt("id"));
            k.setName(rs.getString("name"));
            k.setMainPic(rs.getString("pic"));
            k.setThumbNail(rs.getString("thumbnail"));

            Owner o = new Owner();
            o.setId(rs.getInt("ownerid"));
            o.setName(rs.getString("owner"));
            k.setOwner(o);

            Geneology g = new Geneology();
            g.setKoreId(rs.getInt("id"));
            g.setDam(rs.getString("damname"));
            g.setSire(rs.getString("sirename"));
            g.setDamId(rs.getInt("dam"));
            g.setSireId(rs.getInt("sire"));

            Map m = new HashMap();
            m.put("kore", k);
            m.put("heritage", g);
            return m;
        }
    }

    private static final class OwnerMapper implements RowMapper<Owner> {
//        CREATE TABLE public.kore
//        (
//            id bigint NOT NULL,
//            name text COLLATE pg_catalog."default" NOT NULL,
//            breed text COLLATE pg_catalog."default",
//            pic text COLLATE pg_catalog."default",
//            CONSTRAINT kore_pkey PRIMARY KEY (id)

        @Override
        public Owner mapRow(ResultSet rs, int rowNum) throws SQLException {
            Owner o = new Owner();
            o.setId(rs.getInt("id"));
            o.setName(rs.getString("owner"));
            return o;
        }
    }

    private static final class TagMapper implements RowMapper<Tag> {

        @Override
        public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
            Tag t = new Tag();

            t.setTagId(rs.getInt("id"));
            t.setTypeId(rs.getInt("typeid"));

            t.setTagName(rs.getString("tagname"));
            t.setTypeName(rs.getString("typename"));

            return t;
        }
    }

    private static final class SimpleTagMapper implements RowMapper<List<Tag>> {

        @Override
        public List<Tag> mapRow(ResultSet rs, int rowNum) throws SQLException {
            List<Tag> tags = new ArrayList<>();

            String typeName = rs.getString("type");
            String[] tagList = rs.getString("tagids").split(",");

            for (String tagId : tagList) {
                Tag t = new Tag();
                t.setTagId(Integer.parseInt(tagId.trim()));
                t.setTypeName(typeName);
            }

            return tags;
        }
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
    private void logException(String header, Exception e) {
        System.out.println(" ====================== BGN DAO EXCEPTION LOG ====================== ");
        System.out.println(header);
        System.out.println(" -------- Message -------- ");
        System.out.println(e.getMessage());
        System.out.println(" -------- Stack Trace -------- ");
        System.out.println(e.getStackTrace());
        System.out.println(" ====================== END DAO EXCEPTION LOG ====================== ");

    }
}
