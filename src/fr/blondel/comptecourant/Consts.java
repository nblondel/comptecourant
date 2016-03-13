package fr.blondel.comptecourant;

public class Consts {
  public enum FragmentSection {
    ACCUEIL(0), REVENUS(1), DEPENSES(2), CONSOMMATION(3), PREVISIONS(4), ECONOMIE(5), A_PROPOS(6);
    private static FragmentSection[] sections = null;
    private final int value;
    
    private FragmentSection(int value) {
      this.value = value;
    }

    public int getValue() {
      return value;
    }
    
    public static FragmentSection fromInt(int value) {
      if(sections == null)
        sections = FragmentSection.values();
      return sections[value];
    }
  }
  
  public static final String ARG_SECTION_NUMBER = "section_number";
}
