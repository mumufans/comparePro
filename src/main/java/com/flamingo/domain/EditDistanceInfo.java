package com.flamingo.domain;

public class EditDistanceInfo {

    private int editDistance;

    private boolean theSame;

    public int getEditDistance() {
        return editDistance;
    }

    public void setEditDistance(int editDistance) {
        this.editDistance = editDistance;
    }

    public EditDistanceInfo(boolean theSame) {
        this.theSame = theSame;
    }

    public boolean isTheSame() {
        return theSame;
    }

    public void setTheSame(boolean theSame) {
        this.theSame = theSame;
    }
}
