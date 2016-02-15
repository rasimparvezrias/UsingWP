import java.awt.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.intbit.WordPressPost;
import javax.imageio.ImageIO;


/**
 *
 * @author intbit
 */
public class MainClass {

    private static FileOutputStream osf;

	/**
     * @param args the command line arguments
     * @throws XmlRpcException 
     * @throws IOException 
     */
    public static void main(String[] args) throws Exception, IOException {

        try {
            WordPressPost wordPressPost1 = new WordPressPost("rasim", "rasim", "1", "http://localhost/rpc/xmlrpc.php");
            Map<String, Object> post1 = new HashMap<String, Object>();
            post1.put("title", "hello rasim");
            post1.put("description", "This is the content of a trivial post.");
            post1.put("mt_keywords", "tag1,tag2");
            post1.put("wp_password", "12345");
            //  post1.put("post_thumbnail", "2892");
            String category[] = new String[1];
            category[0] = "RPC";
            post1.put("categories", category);
          
            HashMap<String, Object> imageUploadMap = new HashMap<String, Object>();

            Object images = imageUploadMap.put("name", "rasim.jpg");
            imageUploadMap.put("type", "image/jpeg");

            BufferedImage imgs = ImageIO.read(new File("C:\\Users\\Public\\Pictures\\Sample Pictures\\Chrysanthemum.jpg"));
            BufferedImage newImg;
            String imgstr = encodeToString(imgs, "jpg");
            System.out.println(imgstr);
            newImg = decodeToImage(imgstr);
            boolean imagefsf = ImageIO.write(newImg, "jpg", new File("C:\\Users\\Public\\Pictures\\Sample Pictures\\Chrysanthemum.jpg"));
            System.out.println("imagess" + imagefsf);

            byte[] btDataFile = decodeToByte(imgstr);
            File of = new File("C:\\Users\\Public\\Pictures\\Sample Pictures\\Chrysanthemum.jpg");
            osf = new FileOutputStream(of);
            osf.write(btDataFile);
            imageUploadMap.put("bits", btDataFile);
            
            Object imageUploadResponse = wordPressPost1.addimage(imageUploadMap);
            Map<String, Object> imageUploadResponseMap = (Map<String, Object>) imageUploadResponse;
            Object id = (Object) imageUploadResponseMap.get("id");
            Object name = (Object) imageUploadResponseMap.get("file");
            Object gettype = (Object) imageUploadResponseMap.get("type");
            
            System.out.println(id);
            
         
            imageUploadMap.put("wp_post_thumbnail", id);
            
            
            ArrayList<Hashtable> customFieldsList = new ArrayList<Hashtable>();

            Hashtable customFields = new Hashtable();
            customFields.put("key", "test3");
            customFields.put("value", "rasim");
            customFieldsList.add(customFields);

            customFields = new Hashtable();
            customFields.put("key", "test");
            customFields.put("value", "man!!!");
            customFieldsList.add(customFields);

            customFields = new Hashtable();
            customFields.put("key", "test2");
            Object linkImage = id;
            customFields.put("value", linkImage);
            customFieldsList.add(customFields);
            post1.put("custom_fields", customFieldsList);

            Object value = post1.get("custom_fields");
           Object getvalues= wordPressPost1.addpost(post1);
           
           
           
         Object deletePost= wordPressPost1.deletepost(Integer.parseInt(id.toString()), post1);
         System.out.println(deletePost);
          
         
         Object updatePost= wordPressPost1.updatePost(Integer.parseInt(id.toString()), post1);
         System.out.println(updatePost);
           
        } catch (MalformedURLException ex) {
            Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
        }
      
     
    }
    public static BufferedImage decodeToImage(String imageString) {

        BufferedImage image = null;
        byte[] imageByte;
        try {
            imageByte = Base64.getDecoder().decode(imageString);
            try (ByteArrayInputStream bis = new ByteArrayInputStream(imageByte)) {
                image = ImageIO.read(bis);
            }
        } catch (Exception e) {
        }
        return image;
    }
    
    public static byte[] decodeToByte(String imageString) {

        byte[] imageByte = null;
        try {
            imageByte = Base64.getDecoder().decode(imageString);
        } catch (Exception e) {
        }
        return imageByte;
    }

    // Encode image to string
    public static String encodeToString(BufferedImage image, String format) {
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, format, bos);
            byte[] imageBytes = bos.toByteArray();

            imageString = Base64.getEncoder().encodeToString(imageBytes);

            bos.close();
        } catch (IOException e) {
        }
        return imageString;
    }
}

