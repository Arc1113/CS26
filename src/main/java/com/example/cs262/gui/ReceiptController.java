package com.example.cs262.gui;

import com.example.cs262.model.CartItems;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.scene.SnapshotParameters;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class ReceiptController {


    @FXML
    private Button DownloadBtn;

    @FXML
    private VBox receiptBox;

    @FXML
    private Label Delivery;

    @FXML
    private Label PaymentMethod;

    @FXML
    private Label totalLabel;

    @FXML
    private Label Price;

    @FXML
    private Label Productnamelabel;

    @FXML
    private Label Qnty;

    @FXML
    private Label Reference;

    @FXML
    private Label TotalPrice;


    @FXML
    private Label PaymentTime;

    @FXML
    private VBox Paymentload;

    @FXML
    private void downloadReceipt() {
        // Capture the VBox (receiptBox) as a WritableImage
        WritableImage snapshot = receiptBox.snapshot(new SnapshotParameters(), null);

        // Convert WritableImage to BufferedImage
        BufferedImage bufferedImage = convertToBufferedImage(snapshot);

        // Save the image using FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Receipt");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Image", "*.png"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try {
                ImageIO.write(bufferedImage, "png", file);
                System.out.println("Receipt saved successfully at " + file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to convert WritableImage to BufferedImage
    private BufferedImage convertToBufferedImage(WritableImage image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        int[] pixels = new int[width * height];
        image.getPixelReader().getPixels(0, 0, width, height, javafx.scene.image.PixelFormat.getIntArgbInstance(), pixels, 0, width);
        bufferedImage.setRGB(0, 0, width, height, pixels, 0, width);

        return bufferedImage;
    }

    public void setReceiptdata(LinkedList<CartItems> items, String Total, String Payment, String delivery) {

        Delivery.setText(delivery);
        PaymentMethod.setText(Payment);
        TotalPrice.setText(Total);
        for (CartItems item : items) {
            try {
                FXMLLoader itemLoader = new FXMLLoader(getClass().getResource("/com/example/cs262/itemsbar.fxml"));
                Parent itemRoot = itemLoader.load();

                // Get the controller of the PaymentBar.fxml
                ReceiptController controller = itemLoader.getController();
                controller.setData(
                        item.getName(),
                        item.getPrice(),
                        String.valueOf(item.getQuantity())
                );

                // Add the item to the VBox
                Paymentload.getChildren().add(itemRoot);


            } catch (IOException e) {
                System.err.println("Error loading PaymentBar.fxml: " + e.getMessage());
            }

        }
    }

    private void setData(String name, double price, String Quantity) {

        this.Productnamelabel.setText(name);
        this.Qnty.setText(Quantity);
        this.Price.setText(String.valueOf(price));

    }


    @FXML Button BackButton;
    @FXML
    private void handleBackButtonAction() {
        // Get the current stage (CartFrame)
        Stage cartStage = (Stage) BackButton.getScene().getWindow();

        // Find the main stage
        Stage mainStage = (Stage) cartStage.getOwner(); // Assuming the CartFrame was opened from the MainFrame

        // Apply a slide-out animation to the CartFrame
        TranslateTransition slideOut = new TranslateTransition(Duration.millis(300), cartStage.getScene().getRoot());
        slideOut.setFromX(0); // Start at the current position
        slideOut.setToX(-cartStage.getScene().getWidth()); // Slide out to the left

        slideOut.setOnFinished(event -> {
            // After the slide-out animation, close the CartFrame stage
            cartStage.close();

            // Bring the MainFrame to the foreground
            if (mainStage != null) {
                mainStage.show();

                // Optional: Add a slide-in animation for the MainFrame
                Parent mainRoot = mainStage.getScene().getRoot();
                TranslateTransition slideIn = new TranslateTransition(Duration.millis(100), mainRoot);
                mainRoot.setTranslateX(-mainStage.getScene().getWidth()); // Start outside the view
                slideIn.setFromX(-mainStage.getScene().getWidth());
                slideIn.setToX(0); // Slide into view
                slideIn.play();
            }
        });

        // Play the slide-out animation
        slideOut.play();
    }
}

