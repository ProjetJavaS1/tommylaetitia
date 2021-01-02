
import components.MissingIcon;
import components.ImageCouleur;
import components.ImageNiveauGris;
import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.AbstractAction;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.media.jai.RenderedOp;
import javax.media.jai.JAI;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import javax.imageio.ImageIO;

public class IconDemoApp extends JFrame {

    private JLabel photographLabel = new JLabel();
    private JToolBar buttonBar = new JToolBar();
    private String currentImg ;
    private String imagedir = "images/";

    private MissingIcon placeholderIcon = new MissingIcon();


    private File[] imageFileNames = new File(imagedir).listFiles(new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            String name = pathname.getName().toLowerCase();
            return pathname.isFile() && (name.endsWith(".png")
                    || name.endsWith(".jpg")
                    || name.endsWith(".gif"));
        }
    });

     private File[] imageCaptions=imageFileNames;
    /**
     * Main entry point to the demo. Loads the Swing elements on the "Event
     * Dispatch Thread".
     *
     * @param args
     */
    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                IconDemoApp app = new IconDemoApp();
                app.setVisible(true);
            }
        });
    }


    /**
     * Default constructor for the demo.
     */
    public IconDemoApp() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Traitement Images");

        // A label for displaying the pictures
        photographLabel.setVerticalTextPosition(JLabel.BOTTOM);
        photographLabel.setHorizontalTextPosition(JLabel.CENTER);
        photographLabel.setHorizontalAlignment(JLabel.CENTER);
        photographLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // We add two glue components. Later in process() we will add thumbnail buttons
        // to the toolbar inbetween thease glue compoents. This will center the
        // buttons in the toolbar.
        JButton b1 = new JButton("Mettre en NB");
        JButton b2 = new JButton("Eclaircir");
        JButton b3 = new JButton("Assombrir");
        JButton b4 = new JButton("Flouter");

        b1.addActionListener(new b1Action(currentImg));
        b2.addActionListener(new b2Action(currentImg));
        b3.addActionListener(new b3Action(currentImg));
        b4.addActionListener(new b4Action(currentImg));

        buttonBar.add(b1);
        buttonBar.add(b2);
        buttonBar.add(b3);
        buttonBar.add(b4);

        buttonBar.add(Box.createGlue());
        buttonBar.add(Box.createGlue());

        add(buttonBar, BorderLayout.SOUTH);
        add(photographLabel, BorderLayout.CENTER);

        setSize(900, 700);

        // this centers the frame on the screen
        setLocationRelativeTo(null);

        // start the image loading SwingWorker in a background thread
        loadimages.execute();
    }

    /**
     * SwingWorker class that loads the images a background thread and calls publish
     * when a new one is ready to be displayed.
     *
     * We use Void as the first SwingWroker param as we do not need to return
     * anything from doInBackground().
     */
    private SwingWorker<Void, ThumbnailAction> loadimages = new SwingWorker<Void, ThumbnailAction>() {

        /**
         * Creates full size and thumbnail versions of the target image files.
         */
        @Override
        protected Void doInBackground() throws Exception {

            for (File file : imageFileNames ) {
                ImageIcon icon;
                ThumbnailAction thumbAction ;
                icon = createImageIcon(file.getAbsolutePath() , file.getName());
                if(icon != null){

                    ImageIcon thumbnailIcon = new ImageIcon(getScaledImage(icon.getImage(), 64, 64));

                    thumbAction = new ThumbnailAction(icon, thumbnailIcon,file.getName(),file.getAbsolutePath() );

                }else{
                    // the image failed to load for some reason
                    // so load a placeholder instead
                    thumbAction = new ThumbnailAction(placeholderIcon, placeholderIcon, file.getName(),file.getAbsolutePath());
                }
                publish(thumbAction);
            }
            // unfortunately we must return something, and only null is valid to
            // return when the return type is void.
            return null;
        }

        /**
         * Process all loaded images.
         */
        @Override
        protected void process(List<ThumbnailAction> chunks) {
            for (ThumbnailAction thumbAction : chunks) {
                JButton thumbButton = new JButton(thumbAction);
                // add the new button BEFORE the last glue
                // this centers the buttons in the toolbar
                buttonBar.add(thumbButton, buttonBar.getComponentCount() - 1);
            }
        }
    };

    /**
     * Creates an ImageIcon if the path is valid.
     * @param String - resource path
     * @param String - description of the file
     */
    protected ImageIcon createImageIcon(String path,
            String description) {
        //java.net.URL imgURL = getClass().getResource(path);
        if (path != null) {
            return new ImageIcon(path, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    protected ImageIcon createBufferIcon(BufferedImage buf,
            String description) {
        //java.net.URL imgURL = getClass().getResource(path);
        if (buf != null) {
            return new ImageIcon(buf, description);
        } else {
            System.err.println("Couldn't find file: " + description);
            return null;
        }
    }
    /**
     * Resizes an image using a Graphics2D object backed by a BufferedImage.
     * @param srcImg - source image to scale
     * @param w - desired width
     * @param h - desired height
     * @return - the new resized image
     */
    private Image getScaledImage(Image srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }

    /**
     * Action class that shows the image specified in it's constructor.
     */
    private class ThumbnailAction extends AbstractAction{

        /**
         *The icon if the full image we want to display.
         */
        private Icon displayPhoto;
        private String displayPath;

        /**
         * @param Icon - The full size photo to show in the button.
         * @param Icon - The thumbnail to show in the button.
         * @param String - The descriptioon of the icon.
         */
        public ThumbnailAction(Icon photo, Icon thumb, String desc, String path){
            displayPhoto = photo;
            displayPath = path;
            // The short description becomes the tooltip of a button.
            putValue(SHORT_DESCRIPTION, desc);

            // The LARGE_ICON_KEY is the key for setting the
            // icon when an Action is applied to a button.
            putValue(LARGE_ICON_KEY, thumb);
        }

        /**
         * Shows the full image in the main area and sets the application title.
         */
        public void actionPerformed(ActionEvent e) {
          currentImg = displayPath;

            photographLabel.setIcon(displayPhoto);
            setTitle("Image : " + getValue(SHORT_DESCRIPTION).toString());
        }
    }

    public static boolean isGreyscale(String imagePath)
    {
      try{
        BufferedImage image = ImageIO.read(new File(imagePath));
        RenderedOp ropimage = JAI.create("fileload", imagePath); // ouvre fichier
        int width = ropimage.getWidth();
        int height = ropimage.getHeight();

        int pixel,red, green, blue;
        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                pixel = image.getRGB(i, j);
                red = (pixel >> 16) & 0xff;
                green = (pixel >> 8) & 0xff;
                blue = (pixel) & 0xff;
                if (red != green || green != blue ) return false;
            }
        }
      } catch (IOException e) {
      }
        return true;
    }

    private class b1Action extends AbstractAction{

        public b1Action(String photo){
          System.out.println("ACT BOUTON 1 ");
        }
        public void actionPerformed(ActionEvent e) {
          //photographLabel.setIcon(currentImg);
          setTitle("Image N&B  "+currentImg );

            if(isGreyscale(currentImg)){
              System.out.println("IMAGE DEJA EN NB");
            }
            else{
              ImageCouleur i = new ImageCouleur(currentImg);
              BufferedImage buf=i.imageGrayScale();
              Icon newIcon = createBufferIcon( buf, "resultat");
              photographLabel.setIcon(newIcon);
            }

          System.out.println("BOUTON 1 ");
        }
    }

    private class b2Action extends AbstractAction{

        public b2Action(String photo){
          System.out.println("ACT BOUTON 2 ");
        }
        public void actionPerformed(ActionEvent e) {
          //photographLabel.setIcon(currentImg);
          BufferedImage buf;
          setTitle("Image Eclaircie "+currentImg );
          if(isGreyscale(currentImg)){
            System.out.println("IMAGE NB");
            ImageNiveauGris i = new ImageNiveauGris(currentImg);
            buf=i.eclairage();

          }
          else{
            ImageCouleur i = new ImageCouleur(currentImg);
            buf=i.eclairage();

          }
          Icon newIcon = createBufferIcon( buf, "resultat");
          photographLabel.setIcon(newIcon);
          System.out.println("BOUTON 2 "+newIcon);
        }
    }

    private class b3Action extends AbstractAction{

        public b3Action(String photo){
          System.out.println("ACT BOUTON 3 ");
        }
        public void actionPerformed(ActionEvent e) {
          //photographLabel.setIcon(currentImg);
          setTitle("Image Assombrie "+currentImg );
          ImageCouleur i = new ImageCouleur(currentImg);
          BufferedImage buf=i.assombrissement();
          Icon newIcon = createBufferIcon( buf, "resultat");
          photographLabel.setIcon(newIcon);

          System.out.println("BOUTON 3 "+newIcon);
        }
    }

    private class b4Action extends AbstractAction{

        public b4Action(String photo){
          System.out.println("ACT BOUTON 4 ");
        }
        public void actionPerformed(ActionEvent e) {
          //photographLabel.setIcon(currentImg);
          setTitle("Image Floue "+currentImg );
          ImageCouleur i = new ImageCouleur(currentImg);
          BufferedImage buf=i.matriceConv("convmatrix.csv");
          Icon newIcon = createBufferIcon( buf, "resultat");
          photographLabel.setIcon(newIcon);

          System.out.println("BOUTON 3 "+newIcon);
        }
    }
}
