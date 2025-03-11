package p2_chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class UserThread extends Thread {

    /*
        Ovo je socket koji predstavlja vezu između servera i jednog klijenta.
        Kada klijent šalje poruku, ona stiže kroz ovaj Socket.
        Kada server šalje odgovor, koristi isti socket da odgovori tom klijentu.

        Bez Socket client, ne možemo slati i primati podatke.
        Klasa UserThread mora da zna sa kojim klijentom komunicira.
     */
    private final Socket client;

    /*
     Zašto nam treba?

        UserThread treba da komunicira sa serverom, jer:
        Mora da doda korisnika na listu aktivnih korisnika.
        Mora da obaveštava druge korisnike kada neko pošalje poruku.
        Kada se korisnik diskonektuje, mora da ga ukloni iz liste.
🔹   Kako znamo da nam treba?

        Pošto UserThread komunicira ne samo sa klijentom, već i sa svim ostalim klijentima, mora imati
         referencu na server koji ih sve drži zajedno.
     */
    private Server server;

    /*
    Ako želimo da primamo poruke od klijenta, moramo koristiti BufferedReader.
    Bez ovoga, server ne bi znao šta klijent šalje.
     */
    private final BufferedReader fromUser;

    /*
    Ako želimo da server odgovara klijentu, moramo imati način da mu šaljemo podatke.
    Bez ovoga, klijent ne bi mogao da vidi poruke od drugih korisnika.
     */
    private final PrintWriter toUser;
    private  String username;
    public UserThread(Socket client, Server server) throws IOException {
        this.client = client;
        this.server = server;
        this.fromUser = new BufferedReader(new InputStreamReader(client.getInputStream()));
        /*
        Kada koristimo PrintWriter, podaci se ne šalju odmah – oni se prvo čuvaju u baferu.
        Ako stavimo true, poruka će se odmah poslati, bez čekanja da se bafer napuni.
         */
        this.toUser = new PrintWriter(client.getOutputStream(), true);


        /* RAZLIKA IZMEDJU PrintWriter i BufferedWriter

                PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
                pw.println("Hello, client!");  // Automatski šalje i prelazi u novi red


                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                bw.write("Hello, client!\n"); // Moramo dodati \n ručno
                bw.flush(); // Moramo ručno poslati podatke


         */


    }

    @Override
    public void run() {
        try{
            // Za protokol prvo procitaj username
            this.username = this.fromUser.readLine();

            //saljemo spisak klijenata
            this.sendToClient("Connected users: " + this.server.getUserNames());

            //obavestavamo sve druge korisnike da se povezao novi kolijent
            //sender - koji posiljalac radi broadcast da bi server znao da ne posalje njemu

            //server obavestava sve klijente (osim ovo  novog) da se neko prikljucio
            this.server.broadcast(this, "New user connected: " + this.username);

            String msg;
            do{

                msg = fromUser.readLine();
                if(msg == null )
                    break;
                String toBroadcast = "[" + this.username + "]: " + msg;
                this.server.broadcast(this, toBroadcast);

            }while(!msg.equals("bye"));



            //obavestavamo druge korisnike da je klijent napustio chat
            this.server.broadcast(this, this.username + "has left the chat");

        } catch (IOException e) {
            System.err.println("Error in UserThread");
            e.printStackTrace();

            //uklanjamo korisnika iz liste aktivnih korisnika kada se klijent odjavi, brisemo ga
        } finally {
            //TODO REMOVE FROM SERVER.USERS
            this.server.remove(this);
            //zatvaramo soket
            try{
                //zatvaranje konekcije
                this.client.close();
            } catch (IOException e) {
                //ignored
            }
        }



    }

    public void sendToClient(String msg){
        this.toUser.println(msg);

    }

    public String getUsername() {
        return username;
    }
}
