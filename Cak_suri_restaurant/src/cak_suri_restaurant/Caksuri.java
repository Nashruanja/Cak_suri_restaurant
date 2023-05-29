/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cak_suri_restaurant;
import javax.swing.JOptionPane;
/**
 *
 * @author ACER
 */
public class Caksuri {
    private static String vaNumber;
    private static String restaurantName = "Cak Suri Restaurant";
    private static String[] menuItems = { "Nasi Goreng", "Ayam Goreng", "Mie Ayam", "Es Teh", "Es Jeruk" };
    private static double[] menuPrices = { 15000, 18000, 12000, 5000, 6000 };

    public static void main(String[] args) {
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
            } else if (adminChoice == 1) { // Tambah Menu
                addMenu();
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

            double total = calculateTotal(orderList.toString(), menuPrices);
            double vat = calculateVAT(total);

            StringBuilder paymentMessage = new StringBuilder();
            paymentMessage.append("Total: Rp").append(total).append("\n");
            paymentMessage.append("PPN 10%: Rp").append(vat).append("\n");
            paymentMessage.append("Grand Total: Rp").append(total + vat).append("\n");

            if (paymentChoice == 0) { // Pembayaran tunai
                paymentMessage.append("\nTerima kasih telah memesan di ").append(restaurantName).append("!\nSilakan bayar di kasir.");
            } else if (paymentChoice == 1) { // Pembayaran non-tunai
                String[] banks = { "BCA", "BNI", "BRI", "Mandiri" };
                String bankName = (String) JOptionPane.showInputDialog(null, "Pilih bank:", "Pilih Bank",
                        JOptionPane.PLAIN_MESSAGE, null, banks, banks[0]);

                vaNumber = generateVANumber(bankName);
                paymentMessage.append("\nSilakan transfer pembayaran sebesar Rp").append(total + vat)
                        .append(" ke Virtual Account ").append(vaNumber);
            }

            JOptionPane.showMessageDialog(null, paymentMessage.toString());
        }
    }

    private static boolean isValidLogin(String username, String password) {
        // Simulasi validasi login
        // Anda dapat mengganti ini dengan logika validasi yang sesuai
        return (username.equals("admin") && password.equals("admin123"))
                || (username.equals("user") && password.equals("user123"));
    }

    private static double calculateTotal(String orderList, double[] menuPrices) {
        double total = 0.0;
        String[] orders = orderList.split("\n");

        for (String order : orders) {
            String[] orderDetails = order.split(" - ");
            int menuItemIndex = getIndex(orderDetails[0]);
            int quantity = Integer.parseInt(orderDetails[1]);
            double price = menuPrices[menuItemIndex];

            total += price * quantity;
        }

        return total;
    }

    private static int getIndex(String menuItem) {
        for (int i = 0; i < menuItems.length; i++) {
            if (menuItems[i].equals(menuItem)) {
                return i;
            }
        }
        return -1;
    }

    private static double calculateVAT(double subtotal) {
        return subtotal * 0.1;
    }

    private static String generateVANumber(String bankName) {
        int randomNumber = (int) (Math.random() * 900000) + 100000;
        return randomNumber + " - " + bankName;
    }

    private static void printReceipt(String orderList, double total, String vaNumber) {
        StringBuilder receipt = new StringBuilder();
        receipt.append("\t\t").append(restaurantName).append("\n\n");
        receipt.append("Daftar Pesanan:\n").append(orderList).append("\n");
        receipt.append("Total Pembayaran: Rp").append(total).append("\n");

        if (vaNumber != null) {
            receipt.append("Virtual Account: ").append(vaNumber).append("\n");
        }

        JOptionPane.showMessageDialog(null, receipt.toString());
    }

    private static void editMenu() {
        String[] menuOptions = new String[menuItems.length];
        for (int i = 0; i < menuItems.length; i++) {
            menuOptions[i] = (i + 1) + ". " + menuItems[i] + " - Rp" + menuPrices[i];
        }

        int editChoice = JOptionPane.showOptionDialog(null, "Pilih menu yang ingin diubah:", "Edit Menu",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, menuOptions, menuOptions[0]);

        if (editChoice != -1) {
            String newMenuItem = JOptionPane.showInputDialog(null, "Masukkan nama menu baru:",
                    menuItems[editChoice].split(" - ")[0]);
            double newMenuPrice = Double.parseDouble(JOptionPane.showInputDialog(null, "Masukkan harga menu baru (dalam Rp):",
                    menuPrices[editChoice]));

            menuItems[editChoice] = newMenuItem;
            menuPrices[editChoice] = newMenuPrice;

            JOptionPane.showMessageDialog(null, "Menu berhasil diubah!");
        }
    }

    private static void addMenu() {
        String newMenuItem = JOptionPane.showInputDialog(null, "Masukkan nama menu baru:");
        double newMenuPrice = Double.parseDouble(JOptionPane.showInputDialog(null, "Masukkan harga menu baru (dalam Rp):"));

        String[] newMenuItems = new String[menuItems.length + 1];
        double[] newMenuPrices = new double[menuPrices.length + 1];

        for (int i = 0; i < menuItems.length; i++) {
            newMenuItems[i] = menuItems[i];
            newMenuPrices[i] = menuPrices[i];
        }

        newMenuItems[newMenuItems.length - 1] = newMenuItem;
        newMenuPrices[newMenuPrices.length - 1] = newMenuPrice;

        menuItems = newMenuItems;
        menuPrices = newMenuPrices;

        JOptionPane.showMessageDialog(null, "Menu berhasil ditambahkan!");
    }
    
}
