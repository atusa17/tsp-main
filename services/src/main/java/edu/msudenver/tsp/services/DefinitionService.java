package edu.msudenver.tsp.services;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import edu.msudenver.tsp.services.dto.Definition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class DefinitionService {
    private final RestService restService;
    @Value("${persistence.api.connection.timeout.milliseconds}") private int connectionTimeoutMilliseconds;
    @Value("${persistence.api.socket.timeout.milliseconds}") private int socketTimeoutMilliseconds;
    @Value("${persistence.api.base.url}") private String persistenceApiBaseUrl;

    @Autowired
    public DefinitionService(final RestService restService) {
        this.restService = restService;
    }

    public Optional<List<Definition>> getAllDefinitions() {
        final Instant start = Instant.now();

        try {
            final TypeToken<List<Definition>> typeToken = new TypeToken<List<Definition>>(){};
            final Optional<List<Definition>> persistenceApiResponse =
                    restService.get(persistenceApiBaseUrl + "definitions/",
                            typeToken, connectionTimeoutMilliseconds, socketTimeoutMilliseconds, null);

            if (persistenceApiResponse.isPresent()) {
                LOG.info("Returning {}", persistenceApiResponse.get());
            } else {
                LOG.info("Unable to get list of definitions");
            }

            return persistenceApiResponse;
        } catch (final Exception e) {
            LOG.error("Error getting list of definitions!", e);
            return Optional.empty();
        } finally {
            LOG.info("Get all definitions request took {}ms", Duration.between(start, Instant.now()).toMillis());
        }
    }

    public Optional<Definition> findById(final int id) {
        if (id == 0) {
            LOG.error("Null id specified; returning {}");
            return Optional.empty();
        }

        LOG.info("Sending request to find definition by id {}", id);
        final Instant start = Instant.now();

        try {
            final TypeToken<Definition> typeToken = new TypeToken<Definition>(){};
            final Optional<Definition> persistenceApiResponse = restService.get(persistenceApiBaseUrl + "definitions/" + id,
                    typeToken, connectionTimeoutMilliseconds, socketTimeoutMilliseconds, null);

            if (persistenceApiResponse.isPresent()) {
                LOG.info("Returning {}", persistenceApiResponse.get());
            } else {
                LOG.info("Unable to find definition with id {}", id);
            }

            return persistenceApiResponse;
        } catch (final Exception e) {
            LOG.error("Error finding definition by id", e);
            return Optional.empty();
        } finally {
            LOG.info("Find by id request took {}ms", Duration.between(start, Instant.now()).toMillis());
        }
    }

    public Optional<Definition> createDefinition(final Definition definition) {
        if (definition == null) {
            LOG.error("Given null definition, returning {}");
            return Optional.empty();
        }

        LOG.info("Sending request to insert definition {}", definition);
        final Instant start = Instant.now();

        try {
            final TypeToken<Definition> typeToken = new TypeToken<Definition>() {};
            final Optional<Definition> persistenceApiResponse = restService.post(persistenceApiBaseUrl + "definitions/",
                    new GsonBuilder().create().toJson(definition),
                    typeToken,
                    connectionTimeoutMilliseconds,
                    socketTimeoutMilliseconds);

            if (persistenceApiResponse.isPresent()) {
                LOG.info("Creation successful. Returning {}", persistenceApiResponse.get());
            } else {
                LOG.info("Unable to create new definition {}", definition);
            }

            return persistenceApiResponse;
        } catch (final Exception e) {
            LOG.error("Error creating new definition", e);
            return Optional.empty();
        } finally {
            LOG.info("Create new definition request took {}ms", Duration.between(start, Instant.now()).toMillis());
        }
    }

    public Optional<Definition> updateDefinition(final Definition definition) {
        if (definition == null) {
            LOG.error("Given null definition, returning {}");
            return Optional.empty();
        }

        if (definition.getId() == 0) {
            LOG.error("Given invalid id 0, returning {}");
            return Optional.empty();
        }

        LOG.info("Sending request to update definition {}", definition);
        final Instant start = Instant.now();

        try {
            final TypeToken<Definition> typeToken = new TypeToken<Definition>(){};
            final Optional<Definition> persistenceApiResponse = restService.patch(persistenceApiBaseUrl + "definitions/" + definition.getId(),
                    new GsonBuilder().create().toJson(definition),
                    typeToken,
                    connectionTimeoutMilliseconds,
                    socketTimeoutMilliseconds);

            if (persistenceApiResponse.isPresent()) {
                LOG.info("Update successful. Returning {}", persistenceApiResponse.get());
            } else {
                LOG.info("Unable to update definition {}", definition);
            }

            return persistenceApiResponse;
        } catch (final Exception e) {
            LOG.error("Error updating definition", e);
            return Optional.empty();
        } finally {
            LOG.info("Update definition request took {}ms", Duration.between(start, Instant.now()).toMillis());
        }
    }

    public boolean deleteDefinition(final Definition definition) {
        if (definition == null) {
            LOG.error("Given null definition, returning false");
            return false;
        }

        if (definition.getId() == 0) {
            LOG.error("Given invalid id 0, returning false");
            return false;
        }

        LOG.info("Sending request to delete definition {}", definition);
        final Instant start = Instant.now();

        try {
            final boolean deleteIsSuccessful = restService.delete(persistenceApiBaseUrl + "definitions/" + definition.getId(),
                    connectionTimeoutMilliseconds,
                    socketTimeoutMilliseconds,
                    HttpStatus.NO_CONTENT);

            if (deleteIsSuccessful) {
                LOG.info("Deletion successful. Returning true");
            } else {
                LOG.info("Unable to delete definition {}", definition);
            }

            return deleteIsSuccessful;
        } catch (final Exception e) {
            LOG.error("Error when deleting definition", e);
            return false;
        } finally {
            LOG.info("Delete definition request took {}ms", Duration.between(start, Instant.now()).toMillis());
        }
    }
}
