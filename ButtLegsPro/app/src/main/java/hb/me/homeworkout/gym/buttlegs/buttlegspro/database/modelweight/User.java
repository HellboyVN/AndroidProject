package hb.me.homeworkout.gym.buttlegs.buttlegspro.database.modelweight;

import io.realm.RealmObject;

public class User extends RealmObject   {
    private String email;
    private String first_name;
    private int gender;
    private float height;
    private int height_unit;
    private long id;
    private String last_name;
    private String password;
    private int weight_unit;

    public String realmGet$email() {
        return this.email;
    }

    public String realmGet$first_name() {
        return this.first_name;
    }

    public int realmGet$gender() {
        return this.gender;
    }

    public float realmGet$height() {
        return this.height;
    }

    public int realmGet$height_unit() {
        return this.height_unit;
    }

    public long realmGet$id() {
        return this.id;
    }

    public String realmGet$last_name() {
        return this.last_name;
    }

    public String realmGet$password() {
        return this.password;
    }

    public int realmGet$weight_unit() {
        return this.weight_unit;
    }

    public void realmSet$email(String str) {
        this.email = str;
    }

    public void realmSet$first_name(String str) {
        this.first_name = str;
    }

    public void realmSet$gender(int i) {
        this.gender = i;
    }

    public void realmSet$height(float f) {
        this.height = f;
    }

    public void realmSet$height_unit(int i) {
        this.height_unit = i;
    }

    public void realmSet$id(long j) {
        this.id = j;
    }

    public void realmSet$last_name(String str) {
        this.last_name = str;
    }

    public void realmSet$password(String str) {
        this.password = str;
    }

    public void realmSet$weight_unit(int i) {
        this.weight_unit = i;
    }

    public User() {

        realmSet$gender(0);
        realmSet$weight_unit(1);
        realmSet$height_unit(1);
        realmSet$id(1);
    }

    public long getId() {
        return realmGet$id();
    }

    public void setId(long id) {
        realmSet$id(id);
    }

    public String getFirstName() {
        return realmGet$first_name();
    }

    public void setFirstName(String first_name) {
        realmSet$first_name(first_name);
    }

    public String getLastName() {
        return realmGet$last_name();
    }

    public void setLastName(String last_name) {
        realmSet$last_name(last_name);
    }

    public String getEmail() {
        return realmGet$email();
    }

    public void setEmail(String email) {
        realmSet$email(email);
    }

    public String getPassword() {
        return realmGet$password();
    }

    public void setPassword(String password) {
        realmSet$password(password);
    }

    public int getGender() {
        return realmGet$gender();
    }

    public void setGender(int gender) {
        realmSet$gender(gender);
    }

    public float getHeight() {
        return realmGet$height();
    }

    public void setHeight(float height) {
        realmSet$height(height);
    }

    public int getWeightUnit() {
        return realmGet$weight_unit();
    }

    public void setWeightUnit(int weight_unit) {
        realmSet$weight_unit(weight_unit);
    }

    public int getHeightUnit() {
        return realmGet$height_unit();
    }

    public void setHeightUnit(int height_unit) {
        realmSet$height_unit(height_unit);
    }
}
