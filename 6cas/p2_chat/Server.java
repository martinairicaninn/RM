package p2_chat;

//komandan linija ( ncat -l 12345)

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Server {

    //br porta na kom server osluskuje dolazne konekcije
    public static final int PORT = 12345;

    public static void main(String[] args) {
        Server server = new Server(PORT);

        //pokrece server
        server.execute();
    }


    //port na kojem server sl;usa veze od klijenata
    private final int port;

    //skup svih aktivnih klijenata,
    private final Set<UserThread> users;

    public Server(int port){

        this.port = port;
        //HashSet - osiguravamo da ne moze biti duplikata korisnika
        this.users = new HashSet<>();    //TODO?

    }

    public void execute(){
        try(ServerSocket server = new ServerSocket(this.port)){
            System.err.println("Chat server is listening on port: " + this.port);

            //server je petlja koja prihvata klijente

            while(true){

                //blokiramo program dok se klijent ne poveze
                Socket client = server.accept();
                //kada se klijent povezao pravimo nit za svakog klijenta
                try {
                    UserThread user = new UserThread(client);
                    //KADA PRIHVATIMO USERA, MI GA DODAMO U SPISAK USERA
                    this.users.add(user);
                    //pokrece run() metod u UserThread klasi koji obradju klijentove poruke
                    user.start();
                } catch (IOException e){
                    //ignored
                    //TODO
                }



            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    //server ima metod broadcast pomocu kojeg poruku kjoju prihvati salje svima
    void broadcast(UserThread sender, String message){

        //svima osim tom klijentu koji se prijavio
        this.users
                .stream()   //prolazi kroz sve korisnije
                .filter(u -> u != sender) //filtrira posiljaoca (filter za izbacivanje odredjenog korisnika u ovom slucaju posiljaoca)
                .forEach(u -> u.sendToClient(message));   //posalje poruku ostalima (svakom korisniku u salje poruku preko metode sendToClient(message)



    }

    //metod remove koji uklanja odgovarajuci user thread
    //brise korisnika iz users skupa kada se on diskonektuje
    void remove(UserThread user){
        this.users.remove(user);
        System.err.println("Removed client: " + user.getUsername() + " | " + user.getId());

    }

    //metod koji vraca spisak svih username-ova koji su trenutno povezani
     List<String> getUserNames() {

        return this.users
                .stream()
                .map(UserThread::getUsername) //da iz users skupa uzme samo krisnicka imena
                .collect(Collectors.toList())  //da ih skupi u List<String>
                ;

    }
}
