package liquibase.ext.couchbase.types;

import liquibase.serializer.AbstractLiquibaseSerializable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * A fallback option for Couchbase keyspace. The idea has been taken from Couchbase Kotlin SDK. Will be replaced after the official
 * Couchbase Java SDK will support its own version.
 */

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Keyspace extends AbstractLiquibaseSerializable {

    private final String bucket;
    private final String scope;
    private final String collection;

    public static Keyspace keyspace(@NonNull String bucket,
                                    @NonNull String scope,
                                    @NonNull String collection) {
        return new Keyspace(bucket, scope, collection);
    }


    public String getKeyspace() {
        return String.format("`%s`.`%s`.`%s`", bucket, scope, collection);
    }

    @Override
    public String getSerializedObjectName() {
        return "keyspace";
    }

    @Override
    public String getSerializedObjectNamespace() {
        return STANDARD_CHANGELOG_NAMESPACE;
    }
}
