package CsvWordpressSQLConverter.CreateWP;


import CsvWordpressSQLConverter.CreateDatabaseImport;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static CsvWordpressSQLConverter.CreateDatabaseImport.post_id;
import static CsvWordpressSQLConverter.CreateDatabaseImport.readCSV;

public class CreateWP_Post {

    public static int ID = post_id;
    String post_date;
    public static String post_month;
    public static String post_content;
    public static String post_title;
    String image_title;
    public static  String post_name;
    String image_name;
    String guid;


    private String filename = "wp_posts";
    private FileWriter fw;
    private BufferedWriter writer;

    private FileWriter fw2;
    private BufferedWriter siteMapWriter;

    boolean printDateMonth = true;

    public static ArrayList<String> uploadedImages = new ArrayList<>();
    public static ArrayList<Integer> uploadedImagesID = new ArrayList<>();

    private CreateWP_PostMeta wp_postMeta = new CreateWP_PostMeta();
    public static int i;

    public static void main(String[]args) throws IOException {
        CreateWP_Post databaseImport = new CreateWP_Post();
        databaseImport.create();
    }

    public CreateWP_Post() {


    }

    public void create()throws IOException{
        readCSV.read("FishingToScooters.csv");
        createFile();
        wp_postMeta.createFile();

        for (i = 0; i < 200; i++) { //readCSV.i
            getDate();
            getMonth();

            writeProductToFile();
            wp_postMeta.writeProductToFile();
            writeThumbnailToFile();
        }
        saveFile();
        wp_postMeta.saveFile();
    }

    public void getDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        post_date = dateFormat.format(date);

        if(printDateMonth) {
            System.out.println("post date = " + post_date);
        }
    }

    public void getMonth(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/");
        Date date = new Date();
        post_month = dateFormat.format(date);

        if(printDateMonth) {
            System.out.println("post month = " + post_month);
            printDateMonth = false;
        }
    }

    public  void writeProductToFile() throws IOException{


        post_title = readCSV.post_title[i];
        getPost_Name();
        post_content = readCSV.post_content[i];

        System.out.println("postName : " + post_name);
        System.out.println("link : " + readCSV.link[i] + "\ntitle : " + post_title);

        siteMapWriter.append("\"" + CreateDatabaseImport.website + "/" + getPost_Name() + "/\"");


            writer.append("INSERT INTO `wp_posts` VALUES(");
            writer.append(String.valueOf(ID) + ", "); //count
            writer.append(String.valueOf(1) + ", "); //count
            writer.append("'" + post_date + "'" + ", "); //count
            writer.append("'" + post_date + "'" + ", "); //count
            writer.append("'" + post_content + "'" + ", "); //count
            writer.append("'" + post_title + "'" + ", "); //count
            writer.append("'" + post_content + "'" + ", "); //count
            writer.append("'" + "publish" + "'" + ", ");
            writer.append("'" + "open" + "'" + ", "); //count
            writer.append("'" + "open" + "'" + ", ");
            writer.append("'',"); //blank
            writer.append("'" + getPost_Name() + "'" + ", "); // partially count
            writer.append("'',"); //blank
            writer.append("'',"); //blank
            writer.append("'" + post_date + "'" + ", ");
            writer.append("'" + post_date + "'" + ", ");
            writer.append("'',"); //blank
            writer.append(String.valueOf(0) + ", "); //count
            writer.append("'" + CreateDatabaseImport.website + "/" + getPost_Name() + "/" + "'" + ", ");
            writer.append(String.valueOf(0) + ", ");
            writer.append("'" + "post" + "'" + ", ");
            writer.append("'',"); //blank
            writer.append(String.valueOf(0));//count
            writer.append(");");
            writer.append('\n');



            ID++;

    }

    public void writeThumbnailToFile() throws IOException{
        image_title = readCSV.featured_image[i].substring(49, readCSV.featured_image[i].length() - 4); //change post title to featured image

        guid = readCSV.featured_image[i].substring(49);

        siteMapWriter.append(",\"" + image_title + "\"," + "\"" + CreateDatabaseImport.website + "/wp-content/uploads/" + post_month + guid + "\"," + getPost_Name() + "\n");

        if(!uploadedImages.contains(image_title)) {
            uploadedImages.add(image_title);
            uploadedImagesID.add(ID);



            //System.out.println("\"" + CreateDatabaseImport.website + "/wp-content/uploads/" + post_month + guid + "\"" + "," );

            image_name = image_title;

            System.out.println("ImageID " + ID + " : " + image_title); //f + " : " +

            writer.append("INSERT INTO `wp_posts` VALUES(");
            writer.append(String.valueOf(ID) + ", " ); //count
            writer.append(String.valueOf(1) + ", " ); //count
            writer.append("'" + post_date + "'" + ", " ); //count
            writer.append("'" + post_date + "'" + ", " ); //count
            writer.append("''," ); //blank
            writer.append("'" + image_title + "'" + ", " ); //count
            writer.append("''," ); //blank
            writer.append("'" + "inherit" + "'" + ", " );
            writer.append("'" + "open" + "'" + ", " ); //count
            writer.append("'" + "closed" + "'" + ", " );
            writer.append("''," ); //blank
            writer.append("'" + image_name + "'" + ", " ); // partially count
            writer.append("''," ); //blank
            writer.append("''," ); //blank
            writer.append("'" + post_date + "'" + ", " ); //count
            writer.append("'" + post_date + "'" + ", " ); //count
            writer.append("''," ); //blank
            writer.append(String.valueOf(ID - 1) + ", " ); //count
            writer.append("'" + CreateDatabaseImport.website + "/wp-content/uploads/" + post_month + guid + "'" + ", " );//count
            writer.append(String.valueOf(0) + ", " );//count
            writer.append("'" + "attachment" + "'" + ", " );//count
            writer.append("'" + "image/jpeg" + "'" + ", " );//count
            writer.append(String.valueOf(0));//count
            writer.append(");");

            writer.append('\n');

            ID++;

        }
    }

    public void createFile() throws IOException {
        File file = new File(CreateDatabaseImport.exportFolder + filename + ".sql");
        fw = new FileWriter(file.getAbsolutePath());
        writer = new BufferedWriter(fw);
        initiateSQL();
    }

    public void createSiteMap() throws IOException {
        File file = new File(CreateDatabaseImport.exportFolder + "sitemap.csv");
        fw2 = new FileWriter(file.getAbsolutePath());
        siteMapWriter = new BufferedWriter(fw2);

    }

    public void saveFile() throws IOException {
        writer.flush();
        writer.close();
        System.out.println("wp_post CSV Saved" );
    }

    public void saveSiteMap() throws IOException {
        siteMapWriter.flush();
        siteMapWriter.close();
        System.out.println("sitemap Saved" );
    }

    public static String getPost_Name() throws IOException{

        post_name = post_title;
        post_name = post_name.replace("\\","").replace("/","").replace("\\|","");

        for(int i = 0; i < post_name.length();i++){
            int k = 0;
            for(int j = 0; j < deleteString().length; j++) {

                if (post_name.substring(i,i+1).toLowerCase().contains(deleteString()[j])) {
                    j = deleteString().length-1;
                    k = deleteString().length;
                }

                if (k == deleteString().length-1 ){ //deleteString().length
                    post_name = post_name.replace(post_name.substring(i,i+1),"-").toLowerCase();
                }

                //System.out.println(post_name.substring(i,i+1) + " : " + deleteString()[j] + " : " + j + " : " + k);
                k++;
            }
        }

        if(post_name.substring(post_name.length()-1).contains("-")){
            post_name = post_name.substring(0,post_name.length()-1);
        }

        post_name = post_name.replaceAll("-+","-");

        if (post_name.length() > 200){
            post_name = post_name.substring(0,200);
        }

        if(post_name.startsWith("-") || post_name.startsWith(" ") || post_name.startsWith(" -")){
            post_name = post_name.substring(1,post_name.length());
        }

        if(post_name.endsWith("-") || post_name.endsWith(" ") || post_name.endsWith(" -")){
            post_name = post_name.substring(0,post_name.length()-1);
        }

        if (post_name.length() < 2){
            post_name = "empty";
        }

        return post_name;
    }

    public static String[] deleteString(){

        String[] replaceString = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","0","1","2","3","4","5","6","7","8","9","-","_"};

        return replaceString;
    }

    public void initiateSQL() throws IOException{
        writer.append("CREATE TABLE IF NOT EXISTS `wp_posts`(");
        writer.append('\n');
        writer.append('\n');
        writer.append("`ID` bigint(20) UNSIGNED NOT NULL,\n" +
                "  `post_author` bigint(20) UNSIGNED NOT NULL DEFAULT '0',\n" +
                "  `post_date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',\n" +
                "  `post_date_gmt` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',\n" +
                "  `post_content` longtext COLLATE utf8mb4_unicode_520_ci NOT NULL,\n" +
                "  `post_title` text COLLATE utf8mb4_unicode_520_ci NOT NULL,\n" +
                "  `post_excerpt` text COLLATE utf8mb4_unicode_520_ci NOT NULL,\n" +
                "  `post_status` varchar(20) COLLATE utf8mb4_unicode_520_ci NOT NULL DEFAULT 'publish',\n" +
                "  `comment_status` varchar(20) COLLATE utf8mb4_unicode_520_ci NOT NULL DEFAULT 'open',\n" +
                "  `ping_status` varchar(20) COLLATE utf8mb4_unicode_520_ci NOT NULL DEFAULT 'open',\n" +
                "  `post_password` varchar(255) COLLATE utf8mb4_unicode_520_ci NOT NULL DEFAULT '',\n" +
                "  `post_name` varchar(200) COLLATE utf8mb4_unicode_520_ci NOT NULL DEFAULT '',\n" +
                "  `to_ping` text COLLATE utf8mb4_unicode_520_ci NOT NULL,\n" +
                "  `pinged` text COLLATE utf8mb4_unicode_520_ci NOT NULL,\n" +
                "  `post_modified` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',\n" +
                "  `post_modified_gmt` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',\n" +
                "  `post_content_filtered` longtext COLLATE utf8mb4_unicode_520_ci NOT NULL,\n" +
                "  `post_parent` bigint(20) UNSIGNED NOT NULL DEFAULT '0',\n" +
                "  `guid` varchar(255) COLLATE utf8mb4_unicode_520_ci NOT NULL DEFAULT '',\n" +
                "  `menu_order` int(11) NOT NULL DEFAULT '0',\n" +
                "  `post_type` varchar(20) COLLATE utf8mb4_unicode_520_ci NOT NULL DEFAULT 'post',\n" +
                "  `post_mime_type` varchar(100) COLLATE utf8mb4_unicode_520_ci NOT NULL DEFAULT '',\n" +
                "  `comment_count` bigint(20) NOT NULL DEFAULT '0'\n\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;");
        
        writer.append('\n');
        //writer.append('\n');
        //writer.append("INSERT INTO `wp_posts` (`ID`, `post_author`, `post_date`, `post_date_gmt`, `post_content`, `url`, `post_excerpt`, `post_status`, `comment_status`, `ping_status`, `post_password`, `post_name`, `to_ping`, `pinged`, `post_modified`, `post_modified_gmt`, `post_content_filtered`, `post_parent`, `guid`, `menu_order`, `post_type`, `post_mime_type`, `comment_count`) VALUES");
        writer.append('\n');

    }

}
