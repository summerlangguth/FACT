package com.example.FACT.model;
import java.util.List;
public interface ICreateSetDAO {
    public boolean addKeySet(KeySets keysets);

    List<String> listApplications();
    boolean addApplication(String name);

    List<KeySets> listKeySetsByApplication(String application);
    boolean updateKeySet(KeySets ks);
    boolean deleteKeySet(int id);
}
