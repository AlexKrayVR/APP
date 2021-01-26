package com.alex.vr_party_app.schedule;

import java.util.ArrayList;

public class PartyClass implements Comparable<PartyClass> {

    private int date;
    private int time_hours;
    private int time_minutes;

    private String name;
    private ArrayList<String> listOfDj;
    private ArrayList<Double> listOfDjHours;
    private ArrayList<String> finalList_even;
    private ArrayList<String> finalList_odd;
    private Double partyHours_even = 0D;
    private Double partyHours_odd = 0D;
    private Boolean even = true;

    public Boolean getEven() {
        return even;
    }

    public void setEven(Boolean even) {
        this.even = even;
    }

    public ArrayList<String> getFinalList_even() {
        return finalList_even;
    }

    public void setFinalList_even(ArrayList<String> finalList_even) {
        this.finalList_even = finalList_even;
    }

    public ArrayList<String> getFinalList_odd() {
        return finalList_odd;
    }

    public void setFinalList_odd(ArrayList<String> finalList_odd) {
        this.finalList_odd = finalList_odd;
    }

    public Double getPartyHours_even() {
        return partyHours_even;
    }

    public void setPartyHours_even(Double partyHours_even) {
        this.partyHours_even = partyHours_even;
    }

    public Double getPartyHours_odd() {
        return partyHours_odd;
    }

    public void setPartyHours_odd(Double partyHours_odd) {
        this.partyHours_odd = partyHours_odd;
    }

    public int getTime_hours() {
        return time_hours;
    }

    public void setTime_hours(int time_hours) {
        this.time_hours = time_hours;
    }

    public int getTime_minutes() {
        return time_minutes;
    }

    public void setTime_minutes(int time_minutes) {
        this.time_minutes = time_minutes;
    }


    public PartyClass(int date, int time_hours, int time_minutes, String name, ArrayList<String> listOfDj, ArrayList<Double> listOfDjHours) {
        this.date = date;
        this.time_hours = time_hours;
        this.time_minutes = time_minutes;
        this.name = name;
        this.listOfDj = listOfDj;
        this.listOfDjHours = listOfDjHours;

        if (time_minutes == 30) {
            even = false;
        }
        //if dj playing XX:30
        for (int i=0;i<listOfDjHours.size();i++){
            if((listOfDjHours.get(i)*10)%10==5){
                even = false;
                break;
            }
        }
            finalList_even=new ArrayList<>();
            for (int i = 0; i < listOfDjHours.size(); i++) {
                this.partyHours_even +=listOfDjHours.get(i);
            }
            //Log.d("AlexDebug", "Hours of party even: " + this.partyHours_even);
            for (int i = 0; i < listOfDj.size(); i++) {
                for (int j = 0; j < listOfDjHours.get(i); j++) {
                    finalList_even.add(listOfDj.get(i));
                }
            }
            //Log.d("AlexDebug", "Dj of party even: " + finalList_even.toString());

            finalList_odd=new ArrayList<>();
            for (int i = 0; i < listOfDjHours.size(); i++) {
                this.partyHours_odd +=listOfDjHours.get(i);
            }
            this.partyHours_odd*=2;
            //Log.d("AlexDebug", "Hours of party odd: " + this.partyHours_odd);

            for (int i = 0; i < listOfDj.size(); i++) {
                for (double j = 0; j < listOfDjHours.get(i); j+=0.5) {
                    finalList_odd.add(listOfDj.get(i));
                }
            }
            //Log.d("AlexDebug", "Dj of party odd: " + finalList_odd.toString());
    }

    public ArrayList<String> getListOfDj() {
        return listOfDj;
    }

    public void setListOfDj(ArrayList<String> listOfDj) {
        this.listOfDj = listOfDj;
    }

    public ArrayList<Double> getListOfDjHours() {
        return listOfDjHours;
    }

    public void setListOfDjHours(ArrayList<Double> listOfDjHours) {
        this.listOfDjHours = listOfDjHours;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }


    @Override
    public int compareTo(PartyClass dt) {
        if (this.getDate() < dt.getDate()) {
            return -1;
        } else if (this.getDate() > dt.getDate()) {
            return 1;
        } else {
            if (this.getTime_hours() < dt.getTime_hours()) {
                return -1;
            } else if (this.getTime_hours() > dt.getTime_hours()) {
                return 1;
            } else {
                if (this.getTime_minutes() < dt.getTime_minutes()) {
                    return -1;
                } else if (this.getTime_minutes() > dt.getTime_minutes()) {
                    return 1;
                }
            }
        }
        return 1;
    }

}
