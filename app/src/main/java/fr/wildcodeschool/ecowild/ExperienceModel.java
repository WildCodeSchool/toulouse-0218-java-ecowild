package fr.wildcodeschool.ecowild;

public class ExperienceModel {

    int experience;
    int triExperience;
    int level;

    public ExperienceModel(int experience, int triExperience, int level) {
        this.experience = experience;
        this.triExperience = triExperience;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
