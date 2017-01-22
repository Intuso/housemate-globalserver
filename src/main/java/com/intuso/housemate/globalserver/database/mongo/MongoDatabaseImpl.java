package com.intuso.housemate.globalserver.database.mongo;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.intuso.housemate.globalserver.database.Database;
import com.intuso.housemate.globalserver.database.model.*;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;

/**
 * Created by tomc on 21/01/17.
 */
public class MongoDatabaseImpl implements Database {

    private final MongoCollection<Document> userCollection;
    private final MongoCollection<Document> clientCollection;
    private final MongoCollection<Document> authorisationCollection;
    private final MongoCollection<Document> tokenCollection;

    private final LoadingCache<String, Optional<User>> userCache;
    private final LoadingCache<String, Optional<Client>> clientCache;

    private final Function<Document, User> toUser;
    private final Function<User, Document> fromUser;
    private final Function<Document, Client> toClient;
    private final Function<Client, Document> fromClient;
    private final Function<Document, Authorisation> toAuthorisation;
    private final Function<Authorisation, Document> fromAuthorisation;
    private final Function<Document, Token> toToken;
    private final Function<Token, Document> fromToken;

    public MongoDatabaseImpl() {

        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

        MongoDatabase database = mongoClient.getDatabase("housemate");

        userCollection = database.getCollection("user");
        clientCollection = database.getCollection("client");
        authorisationCollection = database.getCollection("authorisation");
        tokenCollection = database.getCollection("token");

        userCache = CacheBuilder.newBuilder()
                .expireAfterAccess(7, TimeUnit.DAYS)
                .build(new CacheLoader<String, Optional<User>>() {
                    @Override
                    public Optional<User> load(String s) throws Exception {
                        return loadAll(Lists.newArrayList(s)).get(s);
                    }

                    @Override
                    public Map<String, Optional<User>> loadAll(Iterable<? extends String> keys) throws Exception {
                        Map<String, Optional<User>> result = StreamSupport.stream(userCollection.find(in("_id", keys)).spliterator(), false)
                                .map(toUser)
                                .collect(Collectors.toMap(User::getId, Optional::of));
                        for(String key : keys)
                            if(!result.containsKey(key))
                                result.put(key, Optional.empty());
                        return result;
                    }
                });

        clientCache = CacheBuilder.newBuilder()
                .expireAfterAccess(7, TimeUnit.DAYS)
                .build(new CacheLoader<String, Optional<Client>>() {
                    @Override
                    public Optional<Client> load(String s) throws Exception {
                        return loadAll(Lists.newArrayList(s)).get(s);
                    }

                    @Override
                    public Map<String, Optional<Client>> loadAll(Iterable<? extends String> keys) throws Exception {
                        Map<String, Optional<Client>> result = StreamSupport.stream(clientCollection.find(in("_id", keys)).spliterator(), false)
                                .map(toClient)
                                .collect(Collectors.toMap(Client::getId, Optional::of));
                        for(String key : keys)
                            if(!result.containsKey(key))
                                result.put(key, Optional.empty());
                        return result;
                    }
                });

        toUser = document -> document == null ? null : new User(document.getString("_id"));
        fromUser = user -> user == null ? null : new Document().append("_id", user.getId());
        toClient = document -> document == null ? null : new Client(userCache.getUnchecked(document.getString("owner")).orElse(null),
                document.getString("_id"),
                document.getString("secret"),
                document.getString("name"));
        fromClient = client -> client == null ? null : new Document().append("owner", client.getOwner().getId())
                .append("_id", client.getId())
                .append("secret", client.getSecret())
                .append("name", client.getName());
        toAuthorisation = document -> document == null ? null : new Authorisation(clientCache.getUnchecked(document.getString("client")).orElse(null),
                userCache.getUnchecked(document.getString("user")).orElse(null),
                document.getString("_id"));
        fromAuthorisation = authorisation -> authorisation == null ? null : new Document().append("client", authorisation.getClient().getId())
                .append("user", authorisation.getUser().getId())
                .append("_id", authorisation.getCode());
        toToken = document -> document == null ? null : new Token(clientCache.getUnchecked(document.getString("client")).orElse(null),
                userCache.getUnchecked(document.getString("user")).orElse(null),
                document.getString("_id"));
        fromToken = token -> token == null ? null : new Document().append("client", token.getClient().getId())
                .append("user", token.getUser().getId())
                .append("_id", token.getToken());
    }

    @Override
    public Page<User> listUsers(long offset, int limit) {
        return new Page<>(offset,
                userCollection.count(),
                StreamSupport.stream(userCollection.find().skip((int) offset).limit(limit).spliterator(), false)
                        .map(toUser)
                        .collect(Collectors.toList()));
    }

    @Override
    public void addUser(User user) {
        clientCache.invalidate(user.getId());
        userCollection.updateOne(eq("_id", user.getId()), new Document("$set", fromUser.apply(user)), new UpdateOptions().upsert(true));
    }

    @Override
    public User getUser(String id) {
        return userCache.getUnchecked(id).orElse(null);
    }

    @Override
    public void deleteUser(String id) {
        userCache.invalidate(id);
        userCollection.deleteOne(eq("_id", id));
    }

    @Override
    public Page<Client> listClients(long offset, int limit) {
        return new Page<>(offset,
                clientCollection.count(),
                StreamSupport.stream(clientCollection.find().skip((int) offset).limit(limit).spliterator(), false)
                        .map(toClient)
                        .collect(Collectors.toList()));
    }

    @Override
    public void addClient(Client client) {
        clientCache.invalidate(client.getId());
        clientCollection.updateOne(eq("_id", client.getId()), new Document("$set", fromClient.apply(client)), new UpdateOptions().upsert(true));
    }

    @Override
    public Client getClient(String id) {
        return clientCache.getUnchecked(id).orElse(null);
    }

    @Override
    public void deleteClient(String id) {
        clientCache.invalidate(id);
        clientCollection.deleteOne(eq("_id", id));
    }

    @Override
    public void addAuthorisation(Authorisation authorisation) {
        authorisationCollection.updateOne(eq("_id", authorisation.getCode()), new Document("$set", fromAuthorisation.apply(authorisation)), new UpdateOptions().upsert(true));
    }

    @Override
    public Authorisation getAuthorisation(String code) {
        return toAuthorisation.apply(authorisationCollection.find(eq("_id", code)).first());
    }

    @Override
    public void deleteAuthorisation(String code) {
        authorisationCollection.deleteOne(eq("_id", code));
    }

    @Override
    public void addToken(Token token) {
        tokenCollection.updateOne(eq("_id", token.getToken()), new Document("$set", fromToken.apply(token)), new UpdateOptions().upsert(true));
    }

    @Override
    public Token getToken(String token) {
        return toToken.apply(tokenCollection.find(eq("_id", token)).first());
    }

    @Override
    public void deleteToken(String token) {
        tokenCollection.deleteOne(eq("_id", token));
    }

    @Override
    public Page<Token> getUserTokens(String id, long offset, int limit) {
        return new Page<>(offset,
                tokenCollection.count(eq("user", id)),
                StreamSupport.stream(tokenCollection.find(eq("user", id)).skip((int) offset).limit(limit).spliterator(), false)
                        .map(toToken)
                        .collect(Collectors.toList()));
    }

    @Override
    public Page<Token> getClientTokens(String id, long offset, int limit) {
        return new Page<>(offset,
                tokenCollection.count(eq("client", id)),
                StreamSupport.stream(tokenCollection.find(eq("client", id)).skip((int) offset).limit(limit).spliterator(), false)
                        .map(toToken)
                        .collect(Collectors.toList()));
    }
}
