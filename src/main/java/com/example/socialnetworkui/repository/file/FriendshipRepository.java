package com.example.socialnetworkui.repository.file;
import com.example.socialnetworkui.domain.*;
import com.example.socialnetworkui.domain.validators.Validator;

public class FriendshipRepository extends AbstractFileRepository<Tuple<Long, Long>, Friendship> {
    public FriendshipRepository(Validator<Friendship> validator, String fileName) {
        super(validator, fileName);
    }

    /**
     *
     * @param line
     *          line must not be null
     * @return Friendship
     * transform String to Friendship
     */
    @Override
    public Friendship createEntity(String line) {
        String[] splited = line.split(",");
        Friendship friendship = new Friendship();
        friendship.setId(new Tuple<>(Long.parseLong(splited[0]), Long.parseLong(splited[1])));
        return friendship;
    }
    /**
     *
     * @param entity
     *          entity must not be null
     * @return String
     * transform Friendship to String
     */
    @Override
    public String saveEntity(Friendship entity) {
        return entity.getId().getLeft().toString() + "," + entity.getId().getRight().toString();
    }
}
