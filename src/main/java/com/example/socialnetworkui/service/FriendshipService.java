package com.example.socialnetworkui.service;

import com.example.socialnetworkui.domain.Friendship;
import com.example.socialnetworkui.domain.Tuple;
import com.example.socialnetworkui.repository.Repository;
import com.example.socialnetworkui.repository.db.FriendshipDBRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class FriendshipService {
    private final Repository<Tuple<Long, Long>, Friendship> friendshipRepository;

    public FriendshipService(Repository<Tuple<Long, Long>, Friendship> friendshipRepository) {
        this.friendshipRepository = friendshipRepository;
    }

    public Friendship addFriendship(Long ID1, Long ID2) {
        Friendship friendship = new Friendship();
        friendship.setId(new Tuple<Long, Long>(ID1, ID2));
        friendshipRepository.save(friendship);
        return friendship;
    }

    public Friendship deleteFriendship(Long ID1, Long ID2) throws NoSuchElementException {
        Optional<Friendship> friendship = friendshipRepository.findOne(new Tuple<Long, Long>(ID1, ID2));
        if(friendship.isEmpty()) {
            throw new NoSuchElementException();
        }
        friendshipRepository.delete(friendship.get().getId());
        return friendship.get();
    }

    /**
     *
     * @param ID1 Long
     * removes every friendship associated with the given id
     */
    public void deleteAllFriendships(Long ID1) {
        for(Friendship friendship : friendshipRepository.findAll()) {
            Tuple<Long, Long> tuple = friendship.getId();
            if(tuple.getLeft().equals(ID1) || tuple.getRight().equals(ID1)) {
                friendshipRepository.delete(tuple);
            }
        }
    }

    public Iterable<Long> FriendsofanId(Long id) {
        return FriendshipDBRepository.findById(id);
    }

    /**
     *
     * @return Iterable<Friendship> - all Friendship relations
     */
    public Iterable<Friendship> findAll() {
        return friendshipRepository.findAll();
    }

    public int noComunity() {
        Graph g = new Graph(friendshipRepository.findAll(), FriendshipDBRepository.IdsofUsers());
        return g.getComponents();
    }

    public List<String> mostSocialComunity() {
        Graph g = new Graph(friendshipRepository.findAll(), FriendshipDBRepository.IdsofUsers());
        return FriendshipDBRepository.namesOfUsers(g.longestPath());
    }

    public void sendFriendRequest(Long id1, Long toId2) {
        FriendshipDBRepository.friendRequest(id1, toId2);
    }

    public Boolean inPending(Long id1, Long toId2) {
        Boolean ok = FriendshipDBRepository.inPending(id1, toId2);
        return ok;
    }

    public Iterable<Long> friendRequests(Long id1) {
        return FriendshipDBRepository.receivedFriendRequest(id1);
    }
}
