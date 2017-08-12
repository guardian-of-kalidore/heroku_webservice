/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kalidore.herokumicro.dao;

import com.kalidore.herokumicro.model.Kore;
import com.kalidore.herokumicro.model.Owner;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 *
 * @author ahill
 */
public class KoreDaoImpl implements KoreDao {

    private JdbcTemplate jdbcTemplate = null;

    public KoreDaoImpl() {
        jdbcTemplate = new JdbcTemplate();

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(System.getenv("DB_URL"));
        dataSource.setUsername(System.getenv("DB_USR"));
        dataSource.setPassword(System.getenv("DB_PW"));

        jdbcTemplate.setDataSource(dataSource);
    }

    @Override
    public Kore addKore(Kore kore) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static String SQL_SELECT_ALL_KORE = "SELECT * FROM public.\"kore\" AS k LEFT JOIN public.\"owners\" AS o ON k.ownerid = o.id";

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
            this.logException("Tried to find a kore w/ this id " + id,e);
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
            this.logException("Tried to find a random kore",e);
            return null;
        }
    }
    
    @Override
    public void updateKoreInfo(Kore kore) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Kore deleteKore(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static String SQL_SELECT_ALL_KORE_BY_OWNER_ID = "SELECT * FROM public.\"kore\" AS k "
            + "LEFT JOIN public.\"owners\" AS o ON k.ownerid = o.id "
            + "WHERE o.id = ?";

    @Override
    public List<Kore> getOwnerKore(Owner owner) {
        return this.getOwnerKore(owner.getId());
    }

    @Override
    public List<Kore> getOwnerKore(int ownerId) {
        return jdbcTemplate.query(SQL_SELECT_ALL_KORE_BY_OWNER_ID, new KoreMapper(), ownerId);
    }

    public static String SQL_SELECT_ALL_KORE_BY_NAME = "SELECT * FROM public.\"kore\" AS k "
            + "LEFT JOIN public.\"owners\" AS o ON k.ownerid = o.id "
            + "WHERE k.name LIKE '%?%'";

    @Override
    public List<Kore> getKoreByName(String name) {
        return jdbcTemplate.query(SQL_SELECT_ALL_KORE_BY_NAME, new KoreMapper(), name);
    }

    @Override
    public Owner addOwner(Owner owner) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static String SQL_SELECT_ALL_OWNERS = "SELECT * FROM public.\"owners\" AS o ";

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
            this.logException("Tried to find an owner w/ this id " + id,e);
            return null;
        }
    }

    @Override
    public void updateOwnerInfo(Owner owner) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    
    public static String SQL_UPDATE_KORE_OWNER = "UPDATE public.\"kore\" AS k "
            + " SET k.ownerid = ? "
            + " WHERE k.id = ? ";
    
    @Override
    public void assignNewOwner(int koreId, int ownerId) {
        jdbcTemplate.update(SQL_UPDATE_KORE_OWNER, ownerId, koreId);
    }

    @Override
    public Owner deleteOwner(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

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

            Owner o = new Owner();
            o.setId(rs.getInt("ownerid"));
            o.setName(rs.getString("owner"));
            k.setOwner(o);

            return k;
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
            o.setId(rs.getInt("ownerid"));
            o.setName(rs.getString("owner"));
            return o;
        }
    }

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
