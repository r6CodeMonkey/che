package util.security;

/**
 * Created by timmytime on 28/12/15.
 */
public class User {

    private String country;
    private String company;
    private String password;

    public User(){

    }

    public User(String country, String company, String password){
        this.country = country;
        this.company = company;
        this.password = password;
    }

    public void setCountry(String country){
        this.country = country;
    }

    public void setCompany(String company){
        this.company = company;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getCountry(){
        return country;
    }

    public String getCompany(){
        return company;
    }

    public String getPassword(){
        return password;
    }
}
