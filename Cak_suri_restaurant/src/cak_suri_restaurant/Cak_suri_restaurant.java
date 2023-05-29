package cak_suri_restaurant;

import java.io.*;
import javax.swing.JOptionPane;

public class Cak_suri_restaurant {
    private static final String MENU_FILE = "menu.txt";
    private static String vaNumber;
    private static String restaurantName = "Cak Suri Restaurant";
    private static String[] menuItems;
    private static double[] menuPrices;

    public static void main(String[] args) {
        loadMenu();

        // Login
        String username = JOptionPane.showInputDialog(null, "Masukkan username:");
        String password = JOptionPane.showInputDialog(null, "Masukkan password:");

        if (!isValidLogin(username, password)) {
            JOptionPane.showMessageDialog(null, "Username atau password salah!");
            return;
        }

        boolean isAdmin = username.equals("admin");

        if (isAdmin) {
            int adminChoice = JOptionPane.showOptionDialog(null, "Selamat datang, admin!\nPilih opsi:",
                    "Ordering System - Menu Admin", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                    new String[] { "Ubah Menu", "Tambah Menu" }, "Ubah Menu");

            if (adminChoice == 0) { // Ubah Menu
                editMenu();
                saveMenu();
            } else if (adminChoice == 1) { // Tambah Menu
                addMenu();
                saveMenu();
            }
        }

        if (!isAdmin) {
            // List pesanan
            StringBuilder orderList = new StringBuilder();

            boolean ordering = true;
            while (ordering) {
                // Menampilkan menu
                StringBuilder menuDisplay = new StringBuilder();
                for (int i = 0; i < menuItems.length; i++) {
                    menuDisplay.append(i + 1).append(". ").append(menuItems[i]).append(" - Rp").append(menuPrices[i])
                            .append("\n");
                }

                // Memilih menu
                int totalItems = menuItems.length;
                String menuItemInput = JOptionPane.showInputDialog(null, "Pilih menu (separated by comma):\n" + menuDisplay);
                String[] menuItemChoices = menuItemInput.split(",");

                for (String menuItemChoice : menuItemChoices) {
                    int menuItem = Integer.parseInt(menuItemChoice.trim());
                    if (menuItem < 1 || menuItem > totalItems) {
                        JOptionPane.showMessageDialog(null, "Pilihan tidak valid!");
                        continue;
                    }

                    int quantity = Integer
                            .parseInt(JOptionPane.showInputDialog(null, "Masukkan jumlah pesanan untuk " + menuItems[menuItem - 1] + ":"));

                    double subtotal = menuPrices[menuItem - 1] * quantity;
                    orderList.append(menuItems[menuItem - 1]).append(" - ").append(quantity).append(" - Rp").append(subtotal)
                            .append("\n");
                }

                // Meminta inputan apakah ingin menambah pesanan lagi atau selesai
                int continueChoice = JOptionPane.showOptionDialog(null, "Apakah ingin menambah pesanan lagi?",
                        "Ordering System", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                        new String[] { "Ya", "Tidak" }, "Ya");

                if (continueChoice == 0) {
                    ordering = true;
                } else {
                    ordering = false;
                }
            }

            // Menampilkan daftar pesanan
            JOptionPane.showMessageDialog(null, "Daftar Pesanan:\n" + orderList.toString());

            // Pembayaran
            String[] paymentOptions = { "Cash", "Non-tunai" };
            int paymentChoice = JOptionPane.showOptionDialog(null, "Pilih metode pembayaran:",
                    "Ordering System - Pembayaran", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                    paymentOptions, "Cash");

            String paymentMethod = paymentOptions[paymentChoice];
            String paymentMessage = "Pembayaran dilakukan dengan " + paymentMethod;

            if (paymentMethod.equals("Non-tunai")) {
                vaNumber = JOptionPane.showInputDialog(null, "Masukkan Virtual Account Number:");
                paymentMessage += "\nVirtual Account Number: " + vaNumber;
            }

            JOptionPane.showMessageDialog(null, paymentMessage + "\nTerima kasih telah melakukan pemesanan!");
        }
    }

    private static void loadMenu() {
        File file = new File(MENU_FILE);
        if (!file.exists()) {
            initializeMenu();
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            int menuSize = Integer.parseInt(reader.readLine());
            menuItems = new String[menuSize];
            menuPrices = new double[menuSize];

            for (int i = 0; i < menuSize; i++) {
                String line = reader.readLine();
                String[] parts = line.split(",");
                menuItems[i] = parts[0];
                menuPrices[i] = Double.parseDouble(parts[1]);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Gagal memuat menu. Menggunakan menu default.");
            initializeMenu();
        }
    }

    private static void saveMenu() {
        File file = new File(MENU_FILE);

        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println(menuItems.length);
            for (int i = 0; i < menuItems.length; i++) {
                writer.println(menuItems[i] + "," + menuPrices[i]);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Gagal menyimpan menu.");
        }
    }

    private static void initializeMenu() {
        menuItems = new String[] { "Nasi Goreng", "Ayam Goreng", "Mie Ayam", "Es Teh", "Es Jeruk" };
        menuPrices = new double[] { 15000, 18000, 12000, 5000, 6000 };
    }

    private static boolean isValidLogin(String username, String password) {
        // TODO: Implement your own login validation logic
        // This is just a placeholder
        return !username.isEmpty() && !password.isEmpty();
    }

    private static void editMenu() {
        StringBuilder menuDisplay = new StringBuilder();
        for (int i = 0; i < menuItems.length; i++) {
            menuDisplay.append(i + 1).append(". ").append(menuItems[i]).append(" - Rp").append(menuPrices[i]).append("\n");
        }

        String editMenuChoice = JOptionPane.showInputDialog(null,
                "Pilih menu yang ingin diubah (separated by comma):\n" + menuDisplay);
        String[] menuChoices = editMenuChoice.split(",");

        for (String menuChoice : menuChoices) {
            int menuItem = Integer.parseInt(menuChoice.trim());
            if (menuItem < 1 || menuItem > menuItems.length) {
                JOptionPane.showMessageDialog(null, "Pilihan tidak valid!");
                continue;
            }

            String newMenuItem = JOptionPane.showInputDialog(null, "Masukkan nama baru untuk " + menuItems[menuItem - 1] + ":");
            double newMenuPrice = Double.parseDouble(
                    JOptionPane.showInputDialog(null, "Masukkan harga baru untuk " + menuItems[menuItem - 1] + ":"));

            menuItems[menuItem - 1] = newMenuItem;
            menuPrices[menuItem - 1] = newMenuPrice;
        }
    }

    private static void addMenu() {
        String newMenuItem = JOptionPane.showInputDialog(null, "Masukkan nama menu baru:");
        double newMenuPrice = Double.parseDouble(JOptionPane.showInputDialog(null, "Masukkan harga menu baru:"));

        String[] newMenuItems = new String[menuItems.length + 1];
        double[] newMenuPrices = new double[menuPrices.length + 1];

        System.arraycopy(menuItems, 0, newMenuItems, 0, menuItems.length);
        System.arraycopy(menuPrices, 0, newMenuPrices, 0, menuPrices.length);

        newMenuItems[newMenuItems.length - 1] = newMenuItem;
        newMenuPrices[newMenuPrices.length - 1] = newMenuPrice;

        menuItems = newMenuItems;
        menuPrices = newMenuPrices;
    }
}
