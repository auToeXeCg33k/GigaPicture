package core;

import exceptions.GigaException;
import features.*;
import formats.Format;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;


public class Window extends JFrame {
    private final ImageLoader loader;
    private final ImageModifier modifier;
    private final JLabel imgArea;
    private BufferedImage img;
    private BufferedImage disp;


    public Window(ArrayList<Class<Feature>> features, HashSet<Class<Format>> formats) {
        //Visuals of the frame
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Giga Picture Image Viewer");
        setMinimumSize(new Dimension(400, 400));
        setSize(960, 540);
        setLocationRelativeTo(null);
        JScrollPane pane = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        Color gigaColor = new Color(34, 34, 34);
        pane.getViewport().setBackground(gigaColor);
        pane.setBorder(null);
        setContentPane(pane);


        //imagehandlers
        loader = new ImageLoader(formats);
        modifier = new ImageModifier();
        modifier.loadAddons(features);


        //image display area
        imgArea = new JLabel();
        imgArea.setHorizontalAlignment(JLabel.CENTER);
        pane.setViewportView(imgArea);


        //menubar
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        menuBar.setBackground(gigaColor);
        menuBar.setBorder(null);


        //file menu
        JMenu fileMenu = new JMenu("File");
        menuBar.setBackground(gigaColor);
        fileMenu.setForeground(Color.white);
        fileMenu.setBorder(null);
        fileMenu.getPopupMenu().setBorder(null);
        menuBar.add(fileMenu);


        //zoom menu
        JMenu zoomMenu = new JMenu("Resize");
        zoomMenu.setBackground(gigaColor);
        zoomMenu.setForeground(Color.white);
        zoomMenu.setBorder(null);
        zoomMenu.getPopupMenu().setBorder(null);
        menuBar.add(zoomMenu);


        //edit menu
        JMenu editMenu = new JMenu("Edit");
        editMenu.setBackground(gigaColor);
        editMenu.setForeground(Color.white);
        editMenu.setBorder(null);
        editMenu.getPopupMenu().setBorder(null);
        menuBar.add(editMenu);


        //open menuitem
        JMenuItem open = new JMenuItem("Open");
        open.setBackground(gigaColor);
        open.setForeground(Color.white);
        open.setBorder(null);
        fileMenu.add(open);


        //resize menuitem
        JSlider zoom = new JSlider();
        zoom.setMinimum(-99);
        zoom.setMaximum(100);
        zoom.setValue(0);
        zoom.setBackground(gigaColor);
        zoom.setForeground(Color.white);
        zoom.setSnapToTicks(true);
        zoomMenu.add(zoom);
        zoom.addChangeListener(e -> {
            try {
                imgArea.setIcon(new ImageIcon(modifier.modify("Resize", disp, zoom.getValue())));
            } catch (GigaException f) {
                JOptionPane.showMessageDialog(this, f.getMessage(), "Giga Warning", JOptionPane.WARNING_MESSAGE);
            }
        });


        //generate edit menu items
        for (Class<Feature> mod : features) {
            try {
                Feature temp = mod.getDeclaredConstructor().newInstance();
                JMenuItem menuItem = new JMenuItem(temp.getLabel());
                menuItem.addActionListener(e -> {
                    try {
                        zoom.setValue(0);
                        disp = modifier.modify(menuItem.getText(), disp, null);
                        imgArea.setIcon(new ImageIcon(disp));
                    } catch (GigaException f) {
                        JOptionPane.showMessageDialog(this, f.getMessage(), f.getType() ? "Giga Error" : "Giga Warning", f.getType() ? JOptionPane.ERROR_MESSAGE : JOptionPane.WARNING_MESSAGE);
                    }
                });
                menuItem.setBackground(gigaColor);
                menuItem.setForeground(Color.white);
                menuItem.setBorder(null);
                editMenu.add(menuItem);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException f) {
                JOptionPane.showMessageDialog(this, "Invalid plugin: " + mod.getName() + ". Giga Picture is going to ignore this addon.", "Giga Error", JOptionPane.ERROR_MESSAGE);
            }
        }


        //window resize
        getContentPane().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (img == null || disp == null)
                    return;
                zoom.setValue(0);
                if (img.getWidth() > getContentPane().getWidth() - 50 && img.getHeight() > getContentPane().getHeight() - 50) {
                    disp = scaleToFit();
                    imgArea.setIcon(new ImageIcon(disp));
                }
            }
        });


        //opening images
        open.addActionListener(e -> {
            FileDialog fileDialog = new FileDialog(this, "Open");
            fileDialog.setVisible(true);
            if (fileDialog.getFiles().length != 0)
                try {
                    img = loader.load(fileDialog.getFiles()[0]);
                    if (img.getWidth() > getContentPane().getWidth() - 50 && img.getHeight() > getContentPane().getHeight() - 50)
                        disp = scaleToFit();
                    else {
                        disp = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
                        disp.getGraphics().drawImage(img, 0, 0, null);
                    }
                    imgArea.setIcon(new ImageIcon(disp));
                    zoom.setValue(0);
                } catch (GigaException epic) {
                    JOptionPane.showMessageDialog(this, epic.getMessage(), "Giga Error", JOptionPane.ERROR_MESSAGE);
                }
        });


        setVisible(true);
    }


    //function rescale the original image to fit the window size
    private BufferedImage scaleToFit() {
        BufferedImage ret;
        if ((double) img.getWidth() / img.getHeight() > (double) (getContentPane().getWidth() - 50) / (getContentPane().getHeight() - 50)) {
            ret = new BufferedImage(getContentPane().getWidth() - 50, (int) (((double) getContentPane().getWidth() - 50) / img.getWidth() * img.getHeight()), BufferedImage.TYPE_INT_ARGB);
            ret.getGraphics().drawImage(img.getScaledInstance((getContentPane().getWidth() - 50), (int) (((double) getContentPane().getWidth() - 50) / img.getWidth() * img.getHeight()), Image.SCALE_FAST), 0, 0, null);
        } else {
            ret = new BufferedImage((int) (((double) getContentPane().getHeight() - 50) / img.getHeight() * img.getWidth()), getContentPane().getHeight() - 50, BufferedImage.TYPE_INT_ARGB);
            ret.getGraphics().drawImage(img.getScaledInstance((int) (((double) getContentPane().getHeight() - 50) / img.getHeight() * img.getWidth()), getContentPane().getHeight() - 50, Image.SCALE_FAST), 0, 0, null);
        }
        return ret;
    }
}
