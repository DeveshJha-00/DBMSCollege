package gui.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

/**
 * Manages icons for the application
 * Creates colored icons programmatically since we don't have image files
 */
public class IconManager {

    private static final Map<String, ImageIcon> iconCache = new HashMap<>();
    private static final int ICON_SIZE = 12;

    /**
     * Get an icon by name, creating it if it doesn't exist
     */
    public static ImageIcon getIcon(String iconName) {
        return getIcon(iconName, ICON_SIZE, UIConstants.PRIMARY_COLOR);
    }

    /**
     * Get an icon with specific size and color
     */
    public static ImageIcon getIcon(String iconName, int size, Color color) {
        String key = iconName + "_" + size + "_" + color.getRGB();

        if (!iconCache.containsKey(key)) {
            ImageIcon icon = createIcon(iconName, size, color);
            iconCache.put(key, icon);
        }

        return iconCache.get(key);
    }

    /**
     * Create an icon programmatically
     */
    private static ImageIcon createIcon(String iconName, int size, Color color) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        // Enable anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(color);

        int padding = size / 8;
        int innerSize = size - (2 * padding);

        switch (iconName.toLowerCase()) {
            case "artist":
                drawArtistIcon(g2d, padding, innerSize);
                break;
            case "album":
                drawAlbumIcon(g2d, padding, innerSize);
                break;
            case "song":
                drawSongIcon(g2d, padding, innerSize);
                break;
            case "genre":
                drawGenreIcon(g2d, padding, innerSize);
                break;
            case "award":
                drawAwardIcon(g2d, padding, innerSize);
                break;
            case "relationship":
                drawRelationshipIcon(g2d, padding, innerSize);
                break;
            case "search":
                drawSearchIcon(g2d, padding, innerSize);
                break;
            case "add":
                drawAddIcon(g2d, padding, innerSize);
                break;
            case "edit":
                drawEditIcon(g2d, padding, innerSize);
                break;
            case "delete":
                drawDeleteIcon(g2d, padding, innerSize);
                break;
            case "refresh":
                drawRefreshIcon(g2d, padding, innerSize);
                break;
            case "save":
                drawSaveIcon(g2d, padding, innerSize);
                break;
            case "cancel":
                drawCancelIcon(g2d, padding, innerSize);
                break;
            case "view":
                drawViewIcon(g2d, padding, innerSize);
                break;
            case "settings":
                drawSettingsIcon(g2d, padding, innerSize);
                break;
            case "folder":
                drawFolderIcon(g2d, padding, innerSize);
                break;
            case "chart":
                drawChartIcon(g2d, padding, innerSize);
                break;
            case "music":
                drawMusicIcon(g2d, padding, innerSize);
                break;
            case "database":
                drawDatabaseIcon(g2d, padding, innerSize);
                break;
            case "filter":
                drawFilterIcon(g2d, padding, innerSize);
                break;
            case "star":
                drawStarIcon(g2d, padding, innerSize);
                break;
            case "heart":
                drawHeartIcon(g2d, padding, innerSize);
                break;
            case "play":
                drawPlayIcon(g2d, padding, innerSize);
                break;
            case "pause":
                drawPauseIcon(g2d, padding, innerSize);
                break;
            case "stop":
                drawStopIcon(g2d, padding, innerSize);
                break;
            default:
                drawDefaultIcon(g2d, padding, innerSize);
                break;
        }

        g2d.dispose();
        return new ImageIcon(image);
    }

    private static void drawArtistIcon(Graphics2D g2d, int padding, int size) {
        // Draw a microphone
        int micWidth = size / 3;
        int micHeight = size * 2 / 3;
        int x = padding + (size - micWidth) / 2;
        int y = padding;

        g2d.fillRoundRect(x, y, micWidth, micHeight, micWidth / 2, micWidth / 2);

        // Draw stand
        int standX = padding + size / 2 - 1;
        int standY = y + micHeight;
        g2d.fillRect(standX, standY, 2, size / 4);

        // Draw base
        g2d.fillRect(padding + size / 4, standY + size / 4 - 2, size / 2, 4);
    }

    private static void drawAlbumIcon(Graphics2D g2d, int padding, int size) {
        // Draw a CD/vinyl record
        g2d.fillOval(padding, padding, size, size);
        g2d.setColor(Color.WHITE);
        g2d.fillOval(padding + size / 3, padding + size / 3, size / 3, size / 3);
        g2d.setColor(g2d.getColor().darker());
        g2d.fillOval(padding + size * 2 / 5, padding + size * 2 / 5, size / 5, size / 5);
    }

    private static void drawSongIcon(Graphics2D g2d, int padding, int size) {
        // Draw a musical note
        int noteX = padding + size / 3;
        int noteY = padding + size / 4;
        int noteSize = size / 3;

        g2d.fillOval(noteX, noteY + noteSize, noteSize, noteSize / 2);
        g2d.fillRect(noteX + noteSize - 2, noteY, 2, noteSize);

        // Draw flag
        int[] flagX = {noteX + noteSize, noteX + noteSize, noteX + noteSize + size / 4};
        int[] flagY = {noteY, noteY + size / 3, noteY + size / 6};
        g2d.fillPolygon(flagX, flagY, 3);
    }

    private static void drawGenreIcon(Graphics2D g2d, int padding, int size) {
        // Draw theater masks
        g2d.fillOval(padding, padding + size / 4, size * 2 / 3, size * 2 / 3);
        g2d.fillOval(padding + size / 3, padding, size * 2 / 3, size * 2 / 3);
    }

    private static void drawAwardIcon(Graphics2D g2d, int padding, int size) {
        // Draw a trophy
        int cupWidth = size * 2 / 3;
        int cupHeight = size / 2;
        int cupX = padding + (size - cupWidth) / 2;
        int cupY = padding;

        g2d.fillRoundRect(cupX, cupY, cupWidth, cupHeight, cupWidth / 4, cupWidth / 4);

        // Draw handles
        g2d.setStroke(new BasicStroke(2));
        g2d.drawArc(cupX - size / 6, cupY + cupHeight / 4, size / 6, cupHeight / 2, -90, 180);
        g2d.drawArc(cupX + cupWidth, cupY + cupHeight / 4, size / 6, cupHeight / 2, 90, 180);

        // Draw base
        g2d.fillRect(cupX + cupWidth / 4, cupY + cupHeight, cupWidth / 2, size / 4);
        g2d.fillRect(cupX, cupY + cupHeight + size / 4 - 2, cupWidth, 4);
    }

    private static void drawRelationshipIcon(Graphics2D g2d, int padding, int size) {
        // Draw connected nodes
        int nodeSize = size / 4;
        g2d.fillOval(padding, padding + size / 2 - nodeSize / 2, nodeSize, nodeSize);
        g2d.fillOval(padding + size - nodeSize, padding + size / 2 - nodeSize / 2, nodeSize, nodeSize);
        g2d.fillOval(padding + size / 2 - nodeSize / 2, padding, nodeSize, nodeSize);

        // Draw connections
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(padding + nodeSize, padding + size / 2,
                    padding + size / 2 - nodeSize / 2, padding + nodeSize / 2);
        g2d.drawLine(padding + size / 2 + nodeSize / 2, padding + nodeSize / 2,
                    padding + size - nodeSize, padding + size / 2);
    }

    private static void drawSearchIcon(Graphics2D g2d, int padding, int size) {
        // Draw magnifying glass
        int circleSize = size * 2 / 3;
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval(padding, padding, circleSize, circleSize);

        // Draw handle
        int handleStartX = padding + circleSize - 4;
        int handleStartY = padding + circleSize - 4;
        g2d.drawLine(handleStartX, handleStartY,
                    padding + size - 2, padding + size - 2);
    }

    private static void drawAddIcon(Graphics2D g2d, int padding, int size) {
        // Draw plus sign
        int thickness = size / 6;
        g2d.fillRect(padding + size / 2 - thickness / 2, padding, thickness, size);
        g2d.fillRect(padding, padding + size / 2 - thickness / 2, size, thickness);
    }

    private static void drawEditIcon(Graphics2D g2d, int padding, int size) {
        // Draw pencil
        int[] pencilX = {padding, padding + size / 4, padding + size, padding + size * 3 / 4};
        int[] pencilY = {padding + size, padding + size * 3 / 4, padding, padding + size / 4};
        g2d.fillPolygon(pencilX, pencilY, 4);
    }

    private static void drawDeleteIcon(Graphics2D g2d, int padding, int size) {
        // Draw trash can
        g2d.fillRect(padding + size / 4, padding + size / 4, size / 2, size * 3 / 4);
        g2d.fillRect(padding + size / 6, padding + size / 6, size * 2 / 3, size / 8);
        g2d.fillRect(padding + size * 5 / 12, padding, size / 6, size / 4);
    }

    private static void drawRefreshIcon(Graphics2D g2d, int padding, int size) {
        // Draw circular arrow
        g2d.setStroke(new BasicStroke(2));
        g2d.drawArc(padding + 2, padding + 2, size - 4, size - 4, 45, 270);

        // Draw arrow head
        int arrowX = padding + size - 4;
        int arrowY = padding + size / 2;
        int[] arrowXPoints = {arrowX, arrowX - 4, arrowX - 4};
        int[] arrowYPoints = {arrowY, arrowY - 3, arrowY + 3};
        g2d.fillPolygon(arrowXPoints, arrowYPoints, 3);
    }

    private static void drawSaveIcon(Graphics2D g2d, int padding, int size) {
        // Draw floppy disk
        g2d.fillRect(padding, padding, size, size);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(padding + 2, padding + 2, size - 4, size / 3);
        g2d.fillRect(padding + size / 4, padding + size * 2 / 3, size / 2, size / 3 - 2);
    }

    private static void drawCancelIcon(Graphics2D g2d, int padding, int size) {
        // Draw X
        g2d.setStroke(new BasicStroke(3));
        g2d.drawLine(padding + 2, padding + 2, padding + size - 2, padding + size - 2);
        g2d.drawLine(padding + size - 2, padding + 2, padding + 2, padding + size - 2);
    }

    private static void drawViewIcon(Graphics2D g2d, int padding, int size) {
        // Draw eye
        g2d.fillOval(padding, padding + size / 3, size, size / 3);
        g2d.setColor(Color.WHITE);
        g2d.fillOval(padding + size / 3, padding + size / 3 + 2, size / 3, size / 3 - 4);
    }

    private static void drawSettingsIcon(Graphics2D g2d, int padding, int size) {
        // Draw a gear
        int centerX = padding + size / 2;
        int centerY = padding + size / 2;
        int outerRadius = size / 2 - 1;
        int innerRadius = size / 4;

        // Draw outer gear teeth
        for (int i = 0; i < 8; i++) {
            double angle = i * Math.PI / 4;
            int x1 = centerX + (int)(Math.cos(angle) * outerRadius);
            int y1 = centerY + (int)(Math.sin(angle) * outerRadius);
            int x2 = centerX + (int)(Math.cos(angle) * (outerRadius - 2));
            int y2 = centerY + (int)(Math.sin(angle) * (outerRadius - 2));
            g2d.drawLine(x1, y1, x2, y2);
        }

        // Draw main circle
        g2d.fillOval(centerX - innerRadius, centerY - innerRadius, innerRadius * 2, innerRadius * 2);
        g2d.setColor(Color.WHITE);
        g2d.fillOval(centerX - innerRadius/2, centerY - innerRadius/2, innerRadius, innerRadius);
    }

    private static void drawFolderIcon(Graphics2D g2d, int padding, int size) {
        // Draw folder
        g2d.fillRect(padding, padding + size / 3, size, size * 2 / 3);
        g2d.fillRect(padding, padding + size / 4, size / 2, size / 6);

        // Draw folder tab
        g2d.setColor(g2d.getColor().brighter());
        g2d.fillRect(padding + 2, padding + size / 3 + 2, size - 4, size * 2 / 3 - 4);
    }

    private static void drawChartIcon(Graphics2D g2d, int padding, int size) {
        // Draw bar chart
        int barWidth = size / 4;
        g2d.fillRect(padding, padding + size / 2, barWidth, size / 2);
        g2d.fillRect(padding + barWidth + 2, padding + size / 4, barWidth, size * 3 / 4);
        g2d.fillRect(padding + 2 * (barWidth + 2), padding + size / 3, barWidth, size * 2 / 3);

        // Draw axes
        g2d.setStroke(new BasicStroke(1));
        g2d.drawLine(padding, padding + size, padding + size, padding + size); // X-axis
        g2d.drawLine(padding, padding, padding, padding + size); // Y-axis
    }

    private static void drawMusicIcon(Graphics2D g2d, int padding, int size) {
        // Draw multiple musical notes
        int noteSize = size / 4;

        // First note
        g2d.fillOval(padding + size / 6, padding + size / 2, noteSize, noteSize / 2);
        g2d.fillRect(padding + size / 6 + noteSize - 1, padding + size / 6, 2, noteSize);

        // Second note
        g2d.fillOval(padding + size / 2, padding + size * 2 / 3, noteSize, noteSize / 2);
        g2d.fillRect(padding + size / 2 + noteSize - 1, padding + size / 3, 2, noteSize);

        // Connect with beam
        g2d.fillRect(padding + size / 6 + noteSize - 1, padding + size / 6,
                    padding + size / 2 - padding - size / 6, 3);
    }

    private static void drawDatabaseIcon(Graphics2D g2d, int padding, int size) {
        // Draw database cylinder
        int cylinderHeight = size / 4;

        // Draw top ellipse
        g2d.fillOval(padding, padding, size, cylinderHeight);

        // Draw middle section
        g2d.fillRect(padding, padding + cylinderHeight / 2, size, size - cylinderHeight);

        // Draw bottom ellipse
        g2d.fillOval(padding, padding + size - cylinderHeight, size, cylinderHeight);

        // Draw lines to show layers
        g2d.setColor(g2d.getColor().darker());
        g2d.drawOval(padding, padding + size / 3, size, cylinderHeight / 2);
        g2d.drawOval(padding, padding + size * 2 / 3, size, cylinderHeight / 2);
    }

    private static void drawFilterIcon(Graphics2D g2d, int padding, int size) {
        // Draw funnel
        int[] xPoints = {padding, padding + size, padding + size / 3, padding + size * 2 / 3};
        int[] yPoints = {padding, padding, padding + size, padding + size};
        g2d.fillPolygon(xPoints, yPoints, 4);

        // Draw filter lines
        g2d.setColor(Color.WHITE);
        for (int i = 1; i < 4; i++) {
            int y = padding + (size * i) / 5;
            int width = size - (size * i) / 5;
            g2d.drawLine(padding + (size - width) / 2, y, padding + (size + width) / 2, y);
        }
    }

    private static void drawStarIcon(Graphics2D g2d, int padding, int size) {
        // Draw 5-pointed star
        int centerX = padding + size / 2;
        int centerY = padding + size / 2;
        int outerRadius = size / 2;
        int innerRadius = size / 5;

        int[] xPoints = new int[10];
        int[] yPoints = new int[10];

        for (int i = 0; i < 10; i++) {
            double angle = i * Math.PI / 5 - Math.PI / 2;
            int radius = (i % 2 == 0) ? outerRadius : innerRadius;
            xPoints[i] = centerX + (int)(Math.cos(angle) * radius);
            yPoints[i] = centerY + (int)(Math.sin(angle) * radius);
        }

        g2d.fillPolygon(xPoints, yPoints, 10);
    }

    private static void drawHeartIcon(Graphics2D g2d, int padding, int size) {
        // Draw heart shape
        int heartSize = size / 3;

        // Draw two circles for top of heart
        g2d.fillOval(padding + size / 6, padding + size / 6, heartSize, heartSize);
        g2d.fillOval(padding + size / 2, padding + size / 6, heartSize, heartSize);

        // Draw triangle for bottom of heart
        int[] xPoints = {padding + size / 6, padding + size * 5 / 6, padding + size / 2};
        int[] yPoints = {padding + size / 2, padding + size / 2, padding + size * 5 / 6};
        g2d.fillPolygon(xPoints, yPoints, 3);
    }

    private static void drawPlayIcon(Graphics2D g2d, int padding, int size) {
        // Draw play triangle
        int[] xPoints = {padding, padding, padding + size};
        int[] yPoints = {padding, padding + size, padding + size / 2};
        g2d.fillPolygon(xPoints, yPoints, 3);
    }

    private static void drawPauseIcon(Graphics2D g2d, int padding, int size) {
        // Draw two pause bars
        int barWidth = size / 4;
        g2d.fillRect(padding + size / 4, padding, barWidth, size);
        g2d.fillRect(padding + size / 2 + size / 8, padding, barWidth, size);
    }

    private static void drawStopIcon(Graphics2D g2d, int padding, int size) {
        // Draw stop square
        g2d.fillRect(padding + size / 6, padding + size / 6, size * 2 / 3, size * 2 / 3);
    }

    private static void drawDefaultIcon(Graphics2D g2d, int padding, int size) {
        // Draw a simple square
        g2d.fillRect(padding, padding, size, size);
    }
}
