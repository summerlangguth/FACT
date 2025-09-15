package com.example.FACT.model;

public class KeySets {
    private int id;
    private String application;
    private String category;
    private String difficulty;
    private String description;
    private String keyBind;

    public KeySets(int id, String application, String category,
                   String difficulty, String description, String keyBind) {
        this.id = id;
        this.application = application;
        this.category = category;
        this.difficulty = difficulty;
        this.description = description;
        this.keyBind = keyBind;
    }

    public KeySets(String application, String category,
                   String difficulty, String description, String keyBind) {
        this(-1, application, category, difficulty, description, keyBind);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getApplication() { return application; }
    public void setApplication(String application) { this.application = application; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getKeyBind() { return keyBind; }
    public void setKeyBind(String keyBind) { this.keyBind = keyBind; }
}
