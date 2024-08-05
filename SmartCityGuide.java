import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * SmartCityGuide is a GUI-based application to manage and search for places in a city.
 */
public class SmartCityGuide {
    public static final String FILE_PATH = "places.txt";
    public static Map<String, Place> places;

    public SmartCityGuide() {
        places = new HashMap<>();
    }

    /**
     * Adds a new place to the guide.
     *
     * @param name        the name of the place
     * @param address_ph  the address or phone number of the place
     * @param category    the category of the place
     * @param description a brief description of the place
     */
    public void addPlace(String name, String address_ph, String category, String description) {
        places.put(name, new Place(name, address_ph, category, description));
    }

    /**
     * Finds places by category.
     *
     * @param category the category to search for
     * @return a list of places matching the category
     */
    public List<Place> findPlacesByCategory(String category) {
        List<Place> result = new ArrayList<>();
        for (Place place : places.values()) {
            if (place.getCategory().equalsIgnoreCase(category)) {
                result.add(place);
            }
        }
        return result;
    }

    /**
     * Finds a place by name.
     *
     * @param name the name of the place
     * @return the place if found, otherwise null
     */
    public Place findPlace(String name) {
        return places.get(name);
    }

    /**
     * Prints all places to the provided JTextArea.
     *
     * @param outputTextArea the JTextArea to print the places
     */
    public void printAllPlacesGUI(JTextArea outputTextArea) {
        for (Place place : places.values()) {
            outputTextArea.append(place.getName() + ": " +
                    place.getAddress_ph() + " : " + place.getCategory() + " : " + place.getDescription() + "\n");
        }
    }

    /**
     * Saves the places to a file.
     */
    public void savePlacesToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (Place place : places.values()) {
                writer.println(place.getName() + ";" + place.getAddress_ph() + ";" + place.getCategory() + ";" + place.getDescription());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the places from a file.
     */
    public void loadPlacesFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 4) {
                    String name = parts[0];
                    String address_ph = parts[1];
                    String category = parts[2];
                    String description = parts[3];
                    places.put(name, new Place(name, address_ph, category, description));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Main method to run the application.
     */
    public static void main(String[] args) {
        SmartCityGuide cityGuide = new SmartCityGuide();
        cityGuide.loadPlacesFromFile(); // Load places from the file
        SwingUtilities.invokeLater(() -> createAndShowGUI(cityGuide));
    }

    /**
     * Creates and shows the GUI for the Smart City Guide application.
     *
     * @param cityGuide the SmartCityGuide instance
     */
    private static void createAndShowGUI(SmartCityGuide cityGuide) {
        JFrame frame = new JFrame("Smart City Guide");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JTextArea outputTextArea = new JTextArea(20, 50);
        outputTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputTextArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel panel = new JPanel(new GridLayout(1, 3));
        JButton addButton = new JButton("Add Place");
        JButton findButton = new JButton("Find Place");
        JButton displayAllButton = new JButton("Display All Places");
        panel.add(addButton);
        panel.add(findButton);
        panel.add(displayAllButton);
        frame.add(panel, BorderLayout.SOUTH);

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JTextField nameField = new JTextField();
                JTextField address_phField = new JTextField();
                JTextField categoryField = new JTextField();
                JTextField descriptionField = new JTextField();

                Object[] fields = {
                        "Name:", nameField,
                        "Address/Phone:", address_phField,
                        "Category:", categoryField,
                        "Description:", descriptionField
                };

                int result = JOptionPane.showConfirmDialog(frame, fields, "Add Place", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    String name = nameField.getText();
                    String address_ph = address_phField.getText();
                    String category = categoryField.getText();
                    String description = descriptionField.getText();

                    if (name.isEmpty() || address_ph.isEmpty() || category.isEmpty() || description.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "One or more input fields are missing.\nPlease fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else if (cityGuide.findPlace(name) != null) {
                        JOptionPane.showMessageDialog(frame, "A place with the same name already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        cityGuide.addPlace(name, address_ph, category, description);
                        cityGuide.savePlacesToFile(); // Save places to the file
                        outputTextArea.append("Place added successfully!\n");
                    }
                }
            }
        });

        findButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String[] options = {"Search by Name", "Search by Category"};
                int choice = JOptionPane.showOptionDialog(frame, "Choose an option:", "Find Place",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

                if (choice == 0) {
                    String searchName = JOptionPane.showInputDialog(frame, "Enter the name of the place:");
                    Place foundPlace = cityGuide.findPlace(searchName);
                    if (foundPlace != null) {
                        outputTextArea.append("Place found:\n");
                        outputTextArea.append("Name: " + foundPlace.getName() + "\n");
                        outputTextArea.append("Address/Phone: " + foundPlace.getAddress_ph() + "\n");
                        outputTextArea.append("Category: " + foundPlace.getCategory() + "\n");
                        outputTextArea.append("Description: " + foundPlace.getDescription() + "\n");
                    } else {
                        outputTextArea.append("Place not found.\n");
                    }
                } else if (choice == 1) {
                    String category = JOptionPane.showInputDialog(frame, "Enter the category:");
                    List<Place> foundPlaces = cityGuide.findPlacesByCategory(category);
                    if (!foundPlaces.isEmpty()) {
                        outputTextArea.append("Places in the category '" + category + "':\n");
                        for (Place place : foundPlaces) {
                            outputTextArea.append("Name: " + place.getName() + "\n");
                            outputTextArea.append("Address/Phone: " + place.getAddress_ph() + "\n");
                            outputTextArea.append("Category: " + place.getCategory() + "\n");
                            outputTextArea.append("Description: " + place.getDescription() + "\n\n");
                        }
                    } else {
                        outputTextArea.append("No places found in the category '" + category + "'.\n");
                    }
                }
            }
        });

        displayAllButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                outputTextArea.setText("");
                cityGuide.printAllPlacesGUI(outputTextArea);
            }
        });

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
