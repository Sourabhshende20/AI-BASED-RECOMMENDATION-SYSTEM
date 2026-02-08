import java.util.*;

public class RecommendationSystem {

    private static final Map<String, List<String>> USER_PURCHASES = new HashMap<>();
    private static final Map<String, Set<String>> PRODUCT_CATEGORIES = new HashMap<>();
    private static final Map<String, List<String>> PRODUCTS_BY_CATEGORY = new HashMap<>();

    public static void main(String[] args) {
        initializeData();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Recommendation System");

        while (true) {
            System.out.println("\n1. View Purchase History");
            System.out.println("2. Generate Recommendations");
            System.out.println("3. Add Purchase");
            System.out.println("4. User Summary");
            System.out.println("5. Exit");
            System.out.print("Select option: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1: viewPurchaseHistory(scanner); break;
                    case 2: generateRecommendations(scanner); break;
                    case 3: addPurchase(scanner); break;
                    case 4: displayUserSummary(); break;
                    case 5: scanner.close(); return;
                    default: System.out.println("Invalid option.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Enter a number.");
                scanner.nextLine();
            }
        }
    }

    private static void initializeData() {
        USER_PURCHASES.put("Sourabh", Arrays.asList("Laptop", "Mouse", "Keyboard"));
        USER_PURCHASES.put("Vaishanvi", Arrays.asList("Phone", "Charger", "Earphones"));
        USER_PURCHASES.put("Sakshi", Arrays.asList("Laptop", "Monitor", "Webcam"));

        PRODUCT_CATEGORIES.put("Laptop", new HashSet<>(Arrays.asList("Electronics", "Computers")));
        PRODUCT_CATEGORIES.put("Mouse", new HashSet<>(Arrays.asList("Electronics", "Accessories")));
        PRODUCT_CATEGORIES.put("Keyboard", new HashSet<>(Arrays.asList("Electronics", "Accessories")));
        PRODUCT_CATEGORIES.put("Phone", new HashSet<>(Arrays.asList("Electronics", "Mobile")));
        PRODUCT_CATEGORIES.put("Charger", new HashSet<>(Arrays.asList("Electronics", "Accessories")));
        PRODUCT_CATEGORIES.put("Earphones", new HashSet<>(Arrays.asList("Electronics", "Audio")));
        PRODUCT_CATEGORIES.put("Monitor", new HashSet<>(Arrays.asList("Electronics", "Display")));
        PRODUCT_CATEGORIES.put("Webcam", new HashSet<>(Arrays.asList("Electronics", "Accessories")));
        PRODUCT_CATEGORIES.put("Tablet", new HashSet<>(Arrays.asList("Electronics", "Mobile")));
        PRODUCT_CATEGORIES.put("Speakers", new HashSet<>(Arrays.asList("Electronics", "Audio")));
        PRODUCT_CATEGORIES.put("USB Cable", new HashSet<>(Arrays.asList("Electronics", "Accessories")));
        PRODUCT_CATEGORIES.put("Headphones", new HashSet<>(Arrays.asList("Electronics", "Audio")));
        PRODUCT_CATEGORIES.put("Desktop", new HashSet<>(Arrays.asList("Electronics", "Computers")));
        PRODUCT_CATEGORIES.put("Smartwatch", new HashSet<>(Arrays.asList("Electronics", "Mobile")));
        PRODUCT_CATEGORIES.put("TV", new HashSet<>(Arrays.asList("Electronics", "Display")));

        PRODUCTS_BY_CATEGORY.put("Computers", Arrays.asList("Laptop", "Desktop", "Tablet"));
        PRODUCTS_BY_CATEGORY.put("Accessories", Arrays.asList("Mouse", "Keyboard", "Webcam", "USB Cable", "Charger"));
        PRODUCTS_BY_CATEGORY.put("Mobile", Arrays.asList("Phone", "Tablet", "Smartwatch"));
        PRODUCTS_BY_CATEGORY.put("Audio", Arrays.asList("Earphones", "Speakers", "Headphones"));
        PRODUCTS_BY_CATEGORY.put("Display", Arrays.asList("Monitor", "TV", "Projector"));
    }

    private static void viewPurchaseHistory(Scanner scanner) {
        System.out.print("Username: ");
        String user = scanner.nextLine().trim();
        List<String> purchases = USER_PURCHASES.get(user);

        if (purchases == null || purchases.isEmpty()) {
            System.out.println("No history found.");
            return;
        }

        System.out.println(user + "'s Purchases:");
        for (int i = 0; i < purchases.size(); i++) {
            System.out.println((i + 1) + ". " + purchases.get(i));
        }
        System.out.println("Total: " + purchases.size());
    }

    private static void generateRecommendations(Scanner scanner) {
        System.out.print("Username: ");
        String user = scanner.nextLine().trim();
        List<String> purchases = USER_PURCHASES.get(user);

        if (purchases == null || purchases.isEmpty()) {
            System.out.println("No data available.");
            return;
        }

        Set<String> categories = new HashSet<>();
        for (String product : purchases) {
            if (PRODUCT_CATEGORIES.containsKey(product)) {
                categories.addAll(PRODUCT_CATEGORIES.get(product));
            }
        }

        Map<String, Integer> recs = new HashMap<>();
        for (String cat : categories) {
            if (PRODUCTS_BY_CATEGORY.containsKey(cat)) {
                for (String product : PRODUCTS_BY_CATEGORY.get(cat)) {
                    if (!purchases.contains(product)) {
                        recs.merge(product, 1, Integer::sum);
                    }
                }
            }
        }

        List<Map.Entry<String, Integer>> sorted = recs.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .toList();

        System.out.println("Recommendations for " + user + ":");
        if (sorted.isEmpty()) {
            System.out.println("None available.");
        } else {
            for (int i = 0; i < Math.min(5, sorted.size()); i++) {
                Map.Entry<String, Integer> r = sorted.get(i);
                System.out.println((i + 1) + ". " + r.getKey() + " (" + r.getValue() + ")");
            }
        }
    }

    private static void addPurchase(Scanner scanner) {
        System.out.print("Username: ");
        String user = scanner.nextLine().trim();
        System.out.print("Product: ");
        String product = scanner.nextLine().trim();

        if (!PRODUCT_CATEGORIES.containsKey(product)) {
            System.out.println("Product not found.");
            return;
        }

        USER_PURCHASES.putIfAbsent(user, new ArrayList<>());
        List<String> userList = USER_PURCHASES.get(user);

        if (userList.contains(product)) {
            System.out.println("Already purchased.");
            return;
        }

        userList.add(product);
        System.out.println("Purchase added.");
    }

    private static void displayUserSummary() {
        System.out.println("\nUser Summary:");
        USER_PURCHASES.entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue().size(), a.getValue().size()))
                .forEach(e -> System.out.println(e.getKey() + ": " + e.getValue().size()));
        System.out.println("Total users: " + USER_PURCHASES.size());
    }
}