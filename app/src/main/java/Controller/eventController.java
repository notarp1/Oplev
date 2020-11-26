package Controller;

import com.A4.oplev.Activity_Event;

public class eventController {


    private static eventController instance = null;


    public eventController(){

        this.instance = this;
    }

    public static eventController getInstance(){
        if (instance == null) instance = new eventController();
        return instance;
    }


/*
    public void iniEvents(Activity_Event ctx){
        String aboutText = user.getfName() + ", " + user.getAge();
        String cityText = "\uD83D\uDCCD " + user.getCity();
        String descText = user.getDescription();
        String aboutNameText = "Om "+ user.getfName();
        String eduText = "\uD83C\uDF93 " + user.getEducation();
        String jobText = "\uD83D\uDCBC " + user.getJob();

        ctx.about.setText(aboutText);
        ctx.city.setText(cityText);
        ctx.desc.setText(descText);
        ctx.aboutName.setText(aboutNameText);
        ctx.edu.setText(eduText);
        ctx.job.setText(jobText);

        if(user.getEducation() == null || user.getEducation().equals("")){
            eduText = "\uD83C\uDF93 " + "Ikke angivet";
            ctx.edu.setText(eduText);

        }
        if(user.getJob() == null || user.getJob().equals("")){
            jobText = "\uD83D\uDCBC " + "Ikke angivet";
            ctx.job.setText(jobText);
        }
    } */
}
