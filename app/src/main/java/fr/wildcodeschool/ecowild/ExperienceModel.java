package fr.wildcodeschool.ecowild;

public class ExperienceModel {

    int experience;
    int experienceGain;
    int level;

    public ExperienceModel(int experience, int experienceGain, int level) {
        this.experience = experience;
        this.experienceGain = experienceGain;
        this.level = level;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getExperienceGain() {
        return experienceGain;
    }

    public void setExperienceGain(int experienceGain) {
        this.experienceGain = experienceGain;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
