public class Main {
     
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
        SPADE,CLUB,HEART,DIAMOND
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
    public static void main(String[] args){
        Card card = generateRandomCard();
        System.out.println(String.format("Random card is a %s of %s", card.rank.getDisplay(), card.suit));
    }
}