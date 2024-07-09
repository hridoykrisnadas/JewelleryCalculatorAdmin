package com.hridoykrisna.jewellerycalculatoradmin;

public class LivePrice {
    private String update_date;
    private String running_date;
    private float gold_gram_22;
    private float gold_gram_21;
    private float gold_gram_18;
    private float gold_gram_sonaton;
    private float silver_gram_22;
    private float silver_gram_21;
    private float silver_gram_18;
    private float silver_gram_sonaton;
    private String update_at;

    public LivePrice() {
    }

    public LivePrice(String update_date, String running_date, float gold_gram_22, float gold_gram_21, float gold_gram_18, float gold_gram_sonaton, float silver_gram_22, float silver_gram_21, float silver_gram_18, float silver_gram_sonaton, String update_at) {
        this.update_date = update_date;
        this.running_date = running_date;
        this.gold_gram_22 = gold_gram_22;
        this.gold_gram_21 = gold_gram_21;
        this.gold_gram_18 = gold_gram_18;
        this.gold_gram_sonaton = gold_gram_sonaton;
        this.silver_gram_22 = silver_gram_22;
        this.silver_gram_21 = silver_gram_21;
        this.silver_gram_18 = silver_gram_18;
        this.silver_gram_sonaton = silver_gram_sonaton;
        this.update_at = update_at;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }

    public String getRunning_date() {
        return running_date;
    }

    public void setRunning_date(String running_date) {
        this.running_date = running_date;
    }

    public float getGold_gram_22() {
        return gold_gram_22;
    }

    public void setGold_gram_22(float gold_gram_22) {
        this.gold_gram_22 = gold_gram_22;
    }

    public float getGold_gram_21() {
        return gold_gram_21;
    }

    public void setGold_gram_21(float gold_gram_21) {
        this.gold_gram_21 = gold_gram_21;
    }

    public float getGold_gram_18() {
        return gold_gram_18;
    }

    public void setGold_gram_18(float gold_gram_18) {
        this.gold_gram_18 = gold_gram_18;
    }

    public float getGold_gram_sonaton() {
        return gold_gram_sonaton;
    }

    public void setGold_gram_sonaton(float gold_gram_sonaton) {
        this.gold_gram_sonaton = gold_gram_sonaton;
    }

    public float getSilver_gram_22() {
        return silver_gram_22;
    }

    public void setSilver_gram_22(float silver_gram_22) {
        this.silver_gram_22 = silver_gram_22;
    }

    public float getSilver_gram_21() {
        return silver_gram_21;
    }

    public void setSilver_gram_21(float silver_gram_21) {
        this.silver_gram_21 = silver_gram_21;
    }

    public float getSilver_gram_18() {
        return silver_gram_18;
    }

    public void setSilver_gram_18(float silver_gram_18) {
        this.silver_gram_18 = silver_gram_18;
    }

    public float getSilver_gram_sonaton() {
        return silver_gram_sonaton;
    }

    public void setSilver_gram_sonaton(float silver_gram_sonaton) {
        this.silver_gram_sonaton = silver_gram_sonaton;
    }

    public String getUpdate_at() {
        return update_at;
    }

    public void setUpdate_at(String update_at) {
        this.update_at = update_at;
    }
}
