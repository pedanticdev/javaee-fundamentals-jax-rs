package com.pedantic.entities;


import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class PawnedDomains {
    private List<String> pawnedDomains = new ArrayList<>();

    public List<String> getPawnedDomains() {
        return pawnedDomains;
    }

    public void setPawnedDomains(List<String> pawnedDomains) {
        this.pawnedDomains = pawnedDomains;
    }
}
