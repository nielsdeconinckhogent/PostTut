package posttut;

public enum Items {

    //Couple of items used in the script. Looting list & looting in general will hopefully be improved in another revision.
    BRONZE_SWORD(1277),
    WOODEN_SHIELD(1171),
    SHORTBOW(841),
    BRONZE_ARROW(882),
    AIR_RUNE(556),
    MIND_RUNE(558),
    COWHIDE(1739),
    RAW_BEEF(2132),
    RAW_CHICKEN(2138),
    FEATHER(314),
    COOKED_MEAT(2142),
    COOKED_CHICKEN(2140),
    BONES(526);

    private int id;

    Items(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
