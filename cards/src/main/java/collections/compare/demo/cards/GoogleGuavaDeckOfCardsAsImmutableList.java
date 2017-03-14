package collections.compare.demo.cards;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.common.base.Function;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Multiset;

public class GoogleGuavaDeckOfCardsAsImmutableList {
    private ImmutableList<Card> cards;
    private ImmutableListMultimap<Suit, Card> cardsBySuit;

    public GoogleGuavaDeckOfCardsAsImmutableList() {
        EnumSet<Suit> suits = EnumSet.allOf(Suit.class);
        EnumSet<Rank> ranks = EnumSet.allOf(Rank.class);
        this.cards = suits.stream()
                .flatMap(suit -> ranks.stream().map(rank -> new Card(rank, suit)))
                .sorted()
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        ImmutableList::copyOf));
        //noinspection RedundantCast
        this.cardsBySuit = Multimaps.index(this.cards, (Function<Card, Suit>) Card::getSuit);
    }

    public Deque<Card> shuffle(Random random) {
        List<Card> shuffled = new ArrayList<>(this.cards);
        Collections.shuffle(shuffled, random);
        Collections.shuffle(shuffled, random);
        Collections.shuffle(shuffled, random);
        ArrayDeque<Card> cards = new ArrayDeque<>();
        shuffled.forEach(cards::push);
        return cards;
    }

    public Set<Card> deal(Deque<Card> deque, int count) {
        Set<Card> hand = new HashSet<>();
        IntStream.range(0, count).forEach(i -> hand.add(deque.pop()));
        return hand;
    }

    public Card dealOneCard(Deque<Card> deque) {
        return deque.pop();
    }

    public ImmutableList<Set<Card>> shuffleAndDeal(Random random, int hands, int cardsPerHand) {
        Deque<Card> shuffle = this.shuffle(random);
        return IntStream.range(0, hands)
                .mapToObj(i -> this.deal(shuffle, cardsPerHand))
                .collect(ImmutableList.toImmutableList());
    }

    public ImmutableList<Set<Card>> dealHands(Deque<Card> shuffled, int hands, int cardsPerHand) {
        return IntStream.range(0, hands)
                .mapToObj(i -> this.deal(shuffled, cardsPerHand))
                .collect(ImmutableList.toImmutableList());
    }

    public ImmutableList<Card> diamonds() {
        return this.cardsBySuit.get(Suit.DIAMONDS);
    }

    public ImmutableList<Card> hearts() {
        return this.cardsBySuit.get(Suit.HEARTS);
    }

    public ImmutableList<Card> spades() {
        return this.cardsBySuit.get(Suit.SPADES);
    }

    public ImmutableList<Card> clubs() {
        return this.cardsBySuit.get(Suit.CLUBS);
    }

    public Multiset<Suit> countsBySuit() {
        return this.cards.stream().map(Card::getSuit).collect(Collectors.toCollection(HashMultiset::create));
    }

    public Multiset<Rank> countsByRank() {
        return this.cards.stream().map(Card::getRank).collect(Collectors.toCollection(HashMultiset::create));
    }

    public ImmutableList<Card> getCards() {
        return this.cards;
    }

    public ImmutableListMultimap<Suit, Card> getCardsBySuit() {
        return this.cardsBySuit;
    }
}
