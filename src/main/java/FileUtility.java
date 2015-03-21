import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by haui on 20.03.15.
 */
public class FileUtility {

    private final static String currentDir = System.getProperty("user.dir");

    private static String originalFileName;
    private static String newFileName;

    private String modifiedFileName;

    private static int newByteBuffersize = 0;


    public static void main(String... aArgs) throws IOException {

        log( "Gegenwärtiger Ordner: " + currentDir );

        log( "Order \"1original\" für Originaldatei existiert: " + Boolean.toString( Files.exists( Paths.get ( currentDir + "/1original") ) ) );
        log( "Order \"2new\" für Originaldatei existiert: " + Boolean.toString( Files.exists( Paths.get ( currentDir + "/2new") ) ) );

        if ( Files.exists( Paths.get ( currentDir + "/1original") ) == false || Files.exists( Paths.get ( currentDir + "/2new") ) == false  ){
            log("\nOrdner müssen für Verarbeitung existieren.");
        }


        if ( new File(currentDir + "/1original").list().length != 1 || new File(currentDir + "/2new").list().length != 1 ){
            log("\nJeder der oben genannten Ordner muß eine Datei enthalten.");
        }

        originalFileName =  new File(currentDir + "/1original").listFiles()[0].getAbsolutePath();
        newFileName =  new File(currentDir + "/2new").listFiles()[0].getAbsolutePath();


        FileUtility binary = new FileUtility();

        byte[] originalBytes = binary.readSmallBinaryFile(originalFileName);
        byte[] newBytes = binary.readSmallBinaryFile(newFileName);

        newByteBuffersize = originalBytes.length;

        if ( originalBytes.length == newBytes.length ){
            log("\nDateien sind gleich groß.\n");
        }
        else {

            if ( newBytes.length > originalBytes.length){
                newByteBuffersize = newBytes.length;
            }

            log("\nDateien sind  N I C H T  gleich groß.\n");
        }



        ByteBuffer bbuf = ByteBuffer.allocate(newByteBuffersize);

        // Get the buffer's capacity
        //int capacity = bbuf.capacity(); // 10
        //System.out.println(capacity);

        // Use the absolute put().
        // This method does not affect the position.
        //bbuf.put((byte)0xFF); // position=0


        for (int i=0;i<originalBytes.length;i++) {

            if ( i <= newBytes.length - 1 && originalBytes[i] != newBytes[i]){
                bbuf.put( (byte) 0x41 );
            }
            else {
                //System.out.println(originalBytes[i]);
                bbuf.put(originalBytes[i]);
            }

        }


        if ( newBytes.length > originalBytes.length ){
            System.out.println(11111);

            for (int i=originalBytes.length;i<newBytes.length;i++) {

                    bbuf.put( newBytes[i] );

            }

        }



        // Set the position
        //bbuf.position(7);

        // Use the relative put()
        //bbuf.put((byte)0xFF);

        // Get the new position
        //int pos = bbuf.position(); // 6

        // Get remaining byte count
        //int rem = bbuf.remaining(); // 4

        // Set the limit
        //bbuf.limit(7); // remaining=1

        // This convenience method sets the position to 0
        //bbuf.rewind(); // remaining=7


        long currentMillis = System.currentTimeMillis();

        binary.writeSmallBinaryFile(bbuf.array(), currentDir + "/" + "modified with As " + currentMillis + " - " + (new File(newFileName).getName().toString() ) );

        File dir = new File( currentDir + "/old_previous_runs/" + currentMillis );

        //Nachdem zwei Ordner angelegt werden sollen kann Java das NATUERLICH WIEDER NICHT!
        //Waere ansonsten ja NICHT DOGMATISCH!!
        //Die Methode heißt ja mkdir und NICHT mkdirs!!!!
        //Ich Vollidiot sehe den Fehler natuerich nicht!!!!
        //Selbst schuld!!!!
        //Wer braucht schon Fehlermeldung!!!!
        //Ha, das ist doch nur etwas für Idioten!!!!
        //Fehlermeldung pffffhhhhhh
        //Wer die reine Lehre auf seiner Seite hat braucht so etwas PROFANES WIE FEHLERMELDUNGEN NATUERLICH nicht!!!!!!!
        //HA!!!!
        dir.mkdirs();




        //Files.move( Paths.get( originalFileName ),Paths.get( currentDir + "/old_previous_runs/" + currentMillis + "/" + Paths.get( originalFileName ).getFileName()));
        //Files.move( Paths.get( newFileName ),Paths.get( currentDir + "/old_previous_runs/" + currentMillis + "/" + Paths.get( newFileName ).getFileName() ));



    }


    byte[] readSmallBinaryFile(String aFileName) throws IOException {
        Path path = Paths.get(aFileName);
        return Files.readAllBytes(path);
    }

    void writeSmallBinaryFile(byte[] aBytes, String aFileName) throws IOException {
        Path path = Paths.get(aFileName);
        Files.write(path, aBytes); //creates, overwrites
    }

    private static void log(Object aMsg){
        System.out.println(String.valueOf(aMsg));
    }

}
