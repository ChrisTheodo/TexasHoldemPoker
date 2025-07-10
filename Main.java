import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    //////tHE CARDS////////
    enum Rank {
        ACE("A"), TWO("2"), THREE("3"), FOUR("4"), FIVE("5"), SIX("6"),
        SEVEN("7"), EIGHT("8"), NINE("9"), TEN("10"), JACK("J"), QUEEN("Q"), KING("K");

        private final String display;

        Rank(String display) {
            this.display = display;
        }

        public String getDisplay() {
            return display;
        }
    }

    enum Suit {
        SPADES,CLUBS,HEARTS,DIAMONDS
    }

    public static class Card{
        Rank rank;
        Suit suit;
    }

    static Card generateRandomCard(){
        Card card = new Card();
        
        Rank[] ranks = Rank.values();
        Suit[] suits = Suit.values();

        card.rank = ranks[(int)(Math.random() * ranks.length)];
        card.suit = suits[(int)(Math.random() * suits.length)];

        return card;
    }

    
    ////// THE DECK //////////

    static ArrayList<Card> deck = new ArrayList<>();
    
    public static void resetDeck(){
        deck.clear();
        for (Rank rank : Rank.values()){
            for (Suit suit : Suit.values()){
                Card card = new Card();
                card.rank = rank;
                card.suit = suit;
                deck.add(card);
            }
        }
    }
    
    public static Card getCardFromDeck(){
        int i = (int)((Math.random())*(deck.size()));
        Card card = deck.get(i);
        deck.remove(i);
        return card;
    }

    /////// PLAYERS ///////////
 
    public static class Player{
        private String name;
        private int balance;
        private Card card1;
        private Card card2;
        private int bet;
        private boolean isInGame;

        public Player(String name, int balance){
            this.name = name;
            this.balance = balance;
            this.isInGame = true;
        }

        public String getName() {
            return name;
        }

        public int getBalance(){
            return balance;
        }
        
        public int getBet(){
            return bet;
        }
    }
    
    
    ////NPC LOGIC///////////////
    
    public static int getSmallBlind(Player[] players, int dealer){
        if (dealer == 2){
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter small blind: ");
            while (!sc.hasNextInt()) {
                System.out.println("A number, please?");
                sc.next();
            }
            return sc.nextInt();
            
        }
        else{
            return (int)(players[dealer+1].getBalance()/100);
        }
    }



    ////// round ///////////////
    
    public static int choicePrompt(){
        
        Scanner sc = new Scanner(System.in);
        System.out.println("Your turn.. (0: Fold, 1: Call, 2: Raise): ");
        
        while (!sc.hasNextInt()) {
            System.out.println("Not a valid choice..");
            sc.next(); // discard invalid input
        }
        
        int choice = sc.nextInt();
        while (choice != 0 && choice != 1 && choice != 2) {
            System.out.println("Not a valid choice..");
            choice = sc.nextInt();
        }
        

        return choice;
    }

    public static int raisePrompt(Player player, int bet){
        
        Scanner sc = new Scanner(System.in);
        System.out.println("Raise to: ");
        
        while (!sc.hasNextInt()) {
            System.out.println("A number, please?");
            sc.next(); // discard invalid input
        }
        
        int newBet = sc.nextInt();
        if (newBet > player.balance || newBet <= bet){
            System.out.println("Invalid number");
            newBet = raisePrompt(player, bet);
        }
        
        return newBet;
    }

    public static void roundOver(Player winner, int pot){
        winner.balance += pot;
        System.out.printf("Winner: %s ($%d)",winner.getName(),pot);
        pot = 0;
        return;
    }

    public static void bettingRound(Player[] players, int pot, int startingPlayer, int currentBet){

        boolean bettingDone = false;
        int currentPlayer = startingPlayer-1;
        int playerCountInGame = players.length;
        int called = 0;

        while(!bettingDone){
            currentPlayer = (currentPlayer+1)%(players.length);
            
            if (players[currentPlayer].isInGame == false){called++; continue;}

            if (currentPlayer == 0){
                switch (choicePrompt()){   // 0: Fold 1: Call 2: Raise kodikas gia seira paixth
                    case 0:  //fold
                        players[currentPlayer].isInGame = false;
                        players[currentPlayer].balance -= currentBet; //////////// feugei apo to balance thelei ftiaksimo isos
                        playerCountInGame--;
                        break;
                    case 1: //call
                        called++;
                        break;
                    case 2: //raise
                        currentBet = raisePrompt(players[currentPlayer], currentBet);
                        called = 1;
                }
            }
            else {} //// KODIKAS GIA SEIRA NPC
            
            
            if (called == players.length){
                bettingDone = true;
            }                                  ///// telos
        }
    }
    
    public static void playRound(Player[] players, int dealer){

        int pot = 0;
        
        for(Player player : players){
            player.card1 = getCardFromDeck();
            player.card2 = getCardFromDeck();
            player.bet = 0;

        }

        System.out.printf("your cards are: %s of %s and %s of %s\n", players[0].card1.rank, players[0].card1.suit, players[0].card2.rank, players[0].card2.suit);


        int smallBlind = getSmallBlind(players, dealer);
        int bigBlind = smallBlind*2;
        System.out.printf("small blind: %d\nbig blind: %d\n",smallBlind,bigBlind);

        int smallPlayer = (dealer+1)%players.length;
        int bigPlayer = (dealer+2)%players.length;

        //players[smallPlayer].balance -= smallBlind;
        //players[bigPlayer].balance -= bigBlind;

        players[smallPlayer].bet += smallBlind;
        players[bigPlayer].bet += bigBlind;
        pot += smallBlind+bigBlind;

        bettingRound(players, pot, bigPlayer+1, bigBlind);

    }


    /////// some funcs ///////////
    
    public static int getStartingBalance(){
        
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter starting balance: ");
        while (!sc.hasNextInt()) {
            System.out.println("A number, please?");
            sc.next();
        }
        return sc.nextInt();
    }
 
    public static void main(String[] args){
        
        resetDeck();
        
        int startingBalance = getStartingBalance();

        //get player name
        Scanner playerName = new Scanner(System.in);
        System.out.println("Enter Player Name: ");
        
        Player user = new Player(playerName.nextLine(), startingBalance);

        System.out.printf("Player name: %s    balance: %s\n",user.getName(),user.getBalance());

        //npcs
        Player bot1 = new Player("Bot1", startingBalance);
        Player bot2 = new Player("Bot2", startingBalance);

        Player[] players = {user,bot1,bot2};

        Boolean newRound = true;
        int round = -1;
        while(newRound){
            round++;
            int dealer = (round+2)%3;
            playRound(players, dealer);
            
            if(round==1){newRound = false;}
        }

    }
}