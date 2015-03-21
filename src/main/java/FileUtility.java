import javax.swing.*;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by haui on 20.03.15.
 */
public class FileUtility {


    public static void main(String... aArgs) throws IOException {


        String currentDir = System.getProperty("user.dir");


        String modifiedFileName;

        JFrame jFrame = new JFrame();

        JFileChooser j = new JFileChooser(currentDir);
        j.setDialogTitle("Arbeitsordner auswählen");
        j.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        j.showDialog(jFrame, "OK");

        if ( j.getSelectedFile() != null ){
            currentDir = j.getSelectedFile().getAbsolutePath();
        }

        log("Gegenwärtiger Ordner: " + currentDir);

        log( "Order \"1original\" für Originaldatei existiert: " + Boolean.toString( Files.exists( Paths.get ( currentDir + "/1original") ) ) );
        log( "Order \"2new\" für Originaldatei existiert: " + Boolean.toString( Files.exists( Paths.get ( currentDir + "/2new") ) ) );

        if ( Files.exists( Paths.get ( currentDir + "/1original") ) == false || Files.exists( Paths.get ( currentDir + "/2new") ) == false  ){
            log("\nOrdner müssen für Verarbeitung existieren.");
        }


        if ( new File(currentDir + "/1original").list().length != 1 || new File(currentDir + "/2new").list().length != 1 ){
            log("\nJeder der oben genannten Ordner muß eine Datei enthalten.");
            System.exit(0);
            return;
        }



        long currentMillis = System.currentTimeMillis();

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



        File[] newFileArray = new File( currentDir + "/2new" ).listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.isDirectory())
                    return false;

                return true;
            }
        });

        File[] originalFile = new File( currentDir + "/1original" ).listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.isDirectory())
                    return false;

                return true;
            }
        });


        for (File file : newFileArray) {
            String newFileName = file.getAbsolutePath();
            modifiedFileName =  file.getAbsoluteFile().getParentFile().getParentFile() + "/modified with As " + currentMillis + " - " + file.getName();

            doTheLimboOnFileBasis(currentDir, originalFile[0].getAbsolutePath(), newFileName , modifiedFileName );
            Files.move( Paths.get( newFileName ), Paths.get( currentDir + "/old_previous_runs/" + currentMillis + "/" + file.getName() ));
        }



        Files.move( Paths.get( originalFile[0].getAbsolutePath() ),Paths.get( currentDir + "/old_previous_runs/" + currentMillis + "/" + originalFile[0].getName() ));


    }

    private static void doTheLimboOnFileBasis(String currentDir, String originalFileName, String newFileName, String modifiedFileName)
            throws IOException {


        FileUtility fileUtility = new FileUtility();

        int newByteBuffersize = 0;


        byte[] originalBytes = fileUtility.readBinaryFile(originalFileName);
        byte[] newBytes = fileUtility.readBinaryFile(newFileName);

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


        //Unterschiede beider Dateien
        for (int i=0;i<originalBytes.length;i++) {

            //Wenn Stelle an Originaldatei nich an höherer Stelle als Gesamtanzahl der Bytes
            //der neuen Datei sowie Byte an Stelle zwischen beiden Dateien unterschiedlich sind.
            if ( i <= newBytes.length - 1 && originalBytes[i] != newBytes[i]){
                bbuf.put( (byte) 0x41 );
            }
            else {
                //System.out.println(originalBytes[i]);
                bbuf.put(originalBytes[i]);
            }

        }

        //Wenn neue Datei größer als alte übernimm ueberschuessige Bytes
        //neuer Datei in Generierente.
        if ( newBytes.length > originalBytes.length ){

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



        fileUtility.writeBinaryFile(bbuf.array(), modifiedFileName );


    }


    byte[] readBinaryFile(String aFileName) throws IOException {
        Path path = Paths.get(aFileName);
        return Files.readAllBytes(path);
    }

    void writeBinaryFile(byte[] aBytes, String aFileName) throws IOException {
        Path path = Paths.get(aFileName);
        Files.write(path, aBytes);
    }

    private static void log(Object aMsg){
        System.out.println(String.valueOf(aMsg));
    }

}
