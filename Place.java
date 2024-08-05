/**
 * Place represents a location with its details in the SmartCityGuide.
 */
class Place {
    private String name;
    private String address_ph;
    private String category;
    private String description;

    public Place(String name, String address_ph, String category, String description) {
        this.name = name;
        this.address_ph = address_ph;
        this.category = category;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getAddress_ph() {
        return address_ph;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }
}
