package fr.wildcodeschool.ecowild;

public class ExperienceModel {

    int level;
    int experience;
    int triExperience;

    public ExperienceModel(int level, int experience, int triExperience) {
        this.level = level;
        this.experience = experience;
        this.triExperience = triExperience;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getTriExperience() {
        return triExperience;
    }

    public void setTriExperience(int triExperience) {
        this.triExperience = triExperience;
    }
}


