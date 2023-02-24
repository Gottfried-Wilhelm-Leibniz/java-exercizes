package com.golov.springspace.ui;
public enum UiEnum {
    MENU,
    FLEETLIST,
    PROVISION,
    ISSUCOMMAND,
    QUIT;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
