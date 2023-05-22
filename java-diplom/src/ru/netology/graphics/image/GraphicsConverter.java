package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class GraphicsConverter implements TextGraphicsConverter {
    private TextColorSchema newSchema = null;
    private int maxWidth;
    private int maxHeight;
    private double maxRatio;


    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));

        int newWidth = img.getWidth();
        int newHeight = img.getHeight();

        TextColorSchema schema = new ColorConverter();

        if (newSchema != null) {
            schema = newSchema;
        }


        double ratio = (img.getWidth() > img.getHeight()) ? (double) img.getWidth() / img.getHeight() : (double) img.getHeight() / img.getWidth();

        if (maxRatio < ratio) {
            throw new BadImageSizeException(ratio, maxRatio);

        } else if (img.getWidth() > maxWidth && img.getHeight() > maxHeight) {

            if (img.getWidth() > img.getHeight()) {
                newWidth = maxWidth;
                newHeight = (int) (maxWidth / ratio);

            } else if (img.getWidth() < img.getHeight()) {
                newHeight = maxHeight;
                newWidth = (int) (newHeight / ratio);
            } else {
                newHeight = maxHeight;
                newWidth = maxWidth;
            }


        } else if (img.getWidth() > maxWidth || img.getHeight() > maxHeight) {

            if (img.getWidth() > maxWidth) {

                if (img.getWidth() > img.getHeight()) {
                    newWidth = maxWidth;
                    newHeight = (int) (newWidth / ratio);

                } else if (img.getWidth() < img.getHeight()) {
                    newWidth = maxWidth;
                    newHeight = (int) (newWidth * ratio);
                } else {
                    newHeight = maxHeight;
                    newWidth = maxWidth;
                }


            } else if (img.getHeight() > maxHeight) {

                if (img.getWidth() > img.getHeight()) {
                    newHeight = maxHeight;
                    newWidth = (int) (newHeight * ratio);

                } else if (img.getWidth() < img.getHeight()) {
                    newHeight = maxHeight;
                    newWidth = (int) (newHeight / ratio);
                } else {
                    newHeight = maxHeight;
                    newWidth = maxWidth;
                }
            }


        } else {
            newWidth = img.getWidth();
            newHeight = img.getHeight();
        }

        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);

        // сохраняет сконверртированное изображение
        // RenderedImage imageObject = bwImg;
        // ImageIO.write(imageObject, "png", new File("out.png"));




        WritableRaster bwRaster = bwImg.getRaster();

        StringBuilder image = new StringBuilder();


        int w = 0;
        int h = 0;

        for (int r = 0; r < newHeight; r++) {
            for (int e = 0; e < newWidth; e++) {

                int color = bwRaster.getPixel(w, h, new int[3])[0];
                char c = schema.convert(color);
                image.append(c)
                        .append(c);

                if (h != newHeight - 1 && w == newWidth - 1) {
                    h++;
                    w = -1;
                }

                if (w != newWidth - 1) {
                    w++;
                }
            }
            image.append("\n");
        }


        return String.valueOf(image);

    }

    @Override
    public void setMaxWidth(int width) {

        maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {
        maxHeight = height;

    }

    @Override
    public void setMaxRatio(double ratio) {

        maxRatio = ratio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        newSchema = schema;
    }


}

